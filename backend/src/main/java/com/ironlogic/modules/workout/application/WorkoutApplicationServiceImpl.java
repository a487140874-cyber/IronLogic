package com.ironlogic.modules.workout.application;

import com.ironlogic.common.exception.BusinessException;
import com.ironlogic.common.exception.NotFoundException;
import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.program.domain.model.Program;
import com.ironlogic.modules.program.domain.model.ProgramBlock;
import com.ironlogic.modules.program.domain.model.SessionExerciseTemplate;
import com.ironlogic.modules.program.domain.model.SessionTemplate;
import com.ironlogic.modules.program.domain.repository.ProgramBlockRepository;
import com.ironlogic.modules.program.domain.repository.ProgramRepository;
import com.ironlogic.modules.program.domain.repository.SessionExerciseTemplateRepository;
import com.ironlogic.modules.program.domain.repository.SessionTemplateRepository;
import com.ironlogic.modules.progression.application.ProgressionApplicationService;
import com.ironlogic.modules.workout.domain.model.WorkoutExercise;
import com.ironlogic.modules.workout.domain.model.WorkoutSession;
import com.ironlogic.modules.workout.domain.model.WorkoutSet;
import com.ironlogic.modules.workout.domain.model.WorkoutSourceType;
import com.ironlogic.modules.workout.domain.model.WorkoutStatus;
import com.ironlogic.modules.workout.domain.repository.WorkoutExerciseRepository;
import com.ironlogic.modules.workout.domain.repository.WorkoutSessionRepository;
import com.ironlogic.modules.workout.domain.repository.WorkoutSetRepository;
import com.ironlogic.modules.workout.dto.AddWorkoutExerciseRequest;
import com.ironlogic.modules.workout.dto.CreateManualWorkoutRequest;
import com.ironlogic.modules.workout.dto.SaveWorkoutSetsRequest;
import com.ironlogic.modules.workout.dto.WorkoutDetailResponse;
import com.ironlogic.modules.workout.dto.WorkoutExerciseResponse;
import com.ironlogic.modules.workout.dto.WorkoutHistoryItemResponse;
import com.ironlogic.modules.workout.dto.WorkoutSetInput;
import com.ironlogic.modules.workout.dto.WorkoutSetResponse;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Workout 模块应用服务的默认实现。
 *
 * <p>这个类是训练执行流程的编排层，把模板读取、归属校验和写入规则集中放在一起，
 * 让 Controller 保持轻量，也为后续 progression 在“训练完成”这个稳定边界上接入提供位置。
 */
@Service
public class WorkoutApplicationServiceImpl implements WorkoutApplicationService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutSetRepository workoutSetRepository;
    private final ExerciseRepository exerciseRepository;
    private final SessionTemplateRepository sessionTemplateRepository;
    private final SessionExerciseTemplateRepository sessionExerciseTemplateRepository;
    private final ProgramBlockRepository programBlockRepository;
    private final ProgramRepository programRepository;
    private final ProgressionApplicationService progressionApplicationService;

    public WorkoutApplicationServiceImpl(
            WorkoutSessionRepository workoutSessionRepository,
            WorkoutExerciseRepository workoutExerciseRepository,
            WorkoutSetRepository workoutSetRepository,
            ExerciseRepository exerciseRepository,
            SessionTemplateRepository sessionTemplateRepository,
            SessionExerciseTemplateRepository sessionExerciseTemplateRepository,
            ProgramBlockRepository programBlockRepository,
            ProgramRepository programRepository,
            ProgressionApplicationService progressionApplicationService
    ) {
        this.workoutSessionRepository = workoutSessionRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutSetRepository = workoutSetRepository;
        this.exerciseRepository = exerciseRepository;
        this.sessionTemplateRepository = sessionTemplateRepository;
        this.sessionExerciseTemplateRepository = sessionExerciseTemplateRepository;
        this.programBlockRepository = programBlockRepository;
        this.programRepository = programRepository;
        this.progressionApplicationService = progressionApplicationService;
    }

    /**
     * 基于模板开始一次训练。
     */
    @Override
    @Transactional
    public WorkoutDetailResponse createWorkoutFromTemplate(Long userId, Long templateId) {
        TemplateContext templateContext = requireAccessibleTemplate(userId, templateId);
        LocalDateTime now = LocalDateTime.now();

        // 这里必须显式记录 TEMPLATE，因为后续 progression 和历史分析都需要知道
        // 这次训练是来自计划模板，还是临时手动创建的。
        WorkoutSession session = workoutSessionRepository.save(new WorkoutSession(
                null,
                userId,
                WorkoutSourceType.TEMPLATE,
                templateContext.program().id(),
                templateContext.block().id(),
                templateContext.template().id(),
                WorkoutStatus.IN_PROGRESS,
                now,
                null,
                null,
                now,
                now
        ));

        for (SessionExerciseTemplate templateExercise : templateContext.templateExercises()) {
            // 即使模板里已经引用了 Exercise，这里仍然再次校验可见性，
            // 以确保训练执行层不会为当前用户不可访问的动作创建记录。
            requireVisibleExercise(userId, templateExercise.exerciseId());

            workoutExerciseRepository.save(new WorkoutExercise(
                    null,
                    session.id(),
                    templateExercise.exerciseId(),
                    templateExercise.id(),
                    templateExercise.orderNo(),
                    null,
                    null,
                    now,
                    now
            ));
        }

        return buildWorkoutDetailResponse(session);
    }

    /**
     * 开始一次手动自由训练。
     */
    @Override
    @Transactional
    public WorkoutDetailResponse createManualWorkout(Long userId, CreateManualWorkoutRequest request) {
        LocalDateTime now = LocalDateTime.now();
        WorkoutSession session = workoutSessionRepository.save(new WorkoutSession(
                null,
                userId,
                WorkoutSourceType.MANUAL,
                null,
                null,
                null,
                WorkoutStatus.IN_PROGRESS,
                now,
                null,
                normalize(request.notes()),
                now,
                now
        ));
        return buildWorkoutDetailResponse(session);
    }

    /**
     * 查询完整训练详情。
     */
    @Override
    @Transactional(readOnly = true)
    public WorkoutDetailResponse getWorkoutDetail(Long userId, Long workoutId) {
        WorkoutSession session = requireOwnedWorkoutSession(userId, workoutId);
        return buildWorkoutDetailResponse(session);
    }

    /**
     * 列出当前用户的训练历史。
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkoutHistoryItemResponse> listWorkoutHistory(Long userId) {
        return workoutSessionRepository.findByUserId(userId).stream()
                .map(WorkoutApplicationServiceImpl::toWorkoutHistoryItemResponse)
                .toList();
    }

    /**
     * 向进行中的训练中新增一个动作。
     */
    @Override
    @Transactional
    public WorkoutExerciseResponse addWorkoutExercise(Long userId, Long workoutId, AddWorkoutExerciseRequest request) {
        WorkoutSession session = requireModifiableWorkoutSession(userId, workoutId);

        if (workoutExerciseRepository.existsByWorkoutSessionIdAndActualOrderNo(
                session.id(),
                request.actualOrderNo()
        )) {
            throw new BusinessException("WorkoutExercise actualOrderNo already exists in this workout");
        }

        requireVisibleExercise(userId, request.exerciseId());

        LocalDateTime now = LocalDateTime.now();
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(new WorkoutExercise(
                null,
                session.id(),
                request.exerciseId(),
                null,
                request.actualOrderNo(),
                request.replacementOfExerciseId(),
                normalize(request.notes()),
                now,
                now
        ));
        return buildWorkoutExerciseResponse(workoutExercise);
    }

    /**
     * 覆盖保存某个训练动作下的所有组。
     */
    @Override
    @Transactional
    public List<WorkoutSetResponse> saveWorkoutSets(Long userId, Long workoutExerciseId, SaveWorkoutSetsRequest request) {
        WorkoutExercise workoutExercise = requireOwnedWorkoutExercise(userId, workoutExerciseId);
        requireModifiableWorkoutSession(userId, workoutExercise.workoutSessionId());

        validateDistinctSetNo(request.sets());

        // MVP 先采用整列表覆盖而不是增量 patch，
        // 这样持久化逻辑更简单、结果也更确定，适合训练编辑器还在演进的阶段。
        workoutSetRepository.deleteByWorkoutExerciseId(workoutExerciseId);

        List<WorkoutSetResponse> responses = request.sets().stream()
                .map(input -> workoutSetRepository.save(toWorkoutSet(workoutExerciseId, input)))
                .map(WorkoutApplicationServiceImpl::toWorkoutSetResponse)
                .toList();

        // TODO: 当训练编辑器变得更复杂后，改成增量 insert/update/delete，
        // 以保留组级别历史和稳定 id。
        return responses;
    }

    /**
     * 完成一次进行中的训练。
     */
    @Override
    @Transactional
    public WorkoutDetailResponse finishWorkout(Long userId, Long workoutId) {
        WorkoutSession existing = requireOwnedWorkoutSession(userId, workoutId);

        if (existing.status() == WorkoutStatus.COMPLETED) {
            throw new BusinessException("Workout already completed");
        }

        WorkoutSession completed = workoutSessionRepository.update(new WorkoutSession(
                existing.id(),
                existing.userId(),
                existing.sourceType(),
                existing.sourceProgramId(),
                existing.sourceBlockId(),
                existing.sourceTemplateId(),
                WorkoutStatus.COMPLETED,
                existing.startedAt(),
                LocalDateTime.now(),
                existing.notes(),
                existing.createdAt(),
                LocalDateTime.now()
        ));

        if (completed.sourceType() == WorkoutSourceType.TEMPLATE) {
            // workout 模块负责“完成训练”这件事本身。
            // progression 模块负责“训练完成后如何推进推荐状态”这一独立关注点。
            progressionApplicationService.advanceProgramProgressAfterWorkoutCompletion(
                    userId,
                    completed.id(),
                    completed.sourceProgramId(),
                    completed.sourceBlockId(),
                    completed.sourceTemplateId()
            );
        }

        if (completed.sourceType() == WorkoutSourceType.MANUAL) {
            // MANUAL 训练故意不参与 progression。
            // 它属于自由记录，不应该推动计划模板序列向前。
        }

        return buildWorkoutDetailResponse(completed);
    }

    private WorkoutSession requireOwnedWorkoutSession(Long userId, Long workoutId) {
        return workoutSessionRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new NotFoundException("WorkoutSession not found"));
    }

    private WorkoutSession requireModifiableWorkoutSession(Long userId, Long workoutId) {
        WorkoutSession session = requireOwnedWorkoutSession(userId, workoutId);
        if (session.status() == WorkoutStatus.COMPLETED) {
            // 已完成训练必须保持不可变，
            // 这样后续 progression 和 stats 才能把它当成稳定历史事实，而不是可编辑草稿。
            throw new BusinessException("Completed workout cannot be modified");
        }
        return session;
    }

    private WorkoutExercise requireOwnedWorkoutExercise(Long userId, Long workoutExerciseId) {
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(workoutExerciseId)
                .orElseThrow(() -> new NotFoundException("WorkoutExercise not found"));
        requireOwnedWorkoutSession(userId, workoutExercise.workoutSessionId());
        return workoutExercise;
    }

    private void requireVisibleExercise(Long userId, Long exerciseId) {
        if (exerciseRepository.findVisibleById(userId, exerciseId).isEmpty()) {
            throw new NotFoundException("Exercise not found");
        }
    }

    private TemplateContext requireAccessibleTemplate(Long userId, Long templateId) {
        SessionTemplate template = sessionTemplateRepository.findById(templateId)
                .orElseThrow(() -> new NotFoundException("SessionTemplate not found"));
        ProgramBlock block = programBlockRepository.findById(template.blockId())
                .orElseThrow(() -> new NotFoundException("ProgramBlock not found"));
        Program program = programRepository.findByIdAndUserId(block.programId(), userId)
                .orElseThrow(() -> new NotFoundException("Program not found"));
        List<SessionExerciseTemplate> templateExercises =
                sessionExerciseTemplateRepository.findBySessionTemplateId(template.id());
        return new TemplateContext(program, block, template, templateExercises);
    }

    private void validateDistinctSetNo(List<WorkoutSetInput> sets) {
        Set<Integer> seen = new HashSet<>();
        for (WorkoutSetInput set : sets) {
            if (!seen.add(set.setNo())) {
                throw new BusinessException("WorkoutSet setNo must be unique within one workout exercise");
            }
        }
    }

    private WorkoutSet toWorkoutSet(Long workoutExerciseId, WorkoutSetInput input) {
        LocalDateTime now = LocalDateTime.now();
        return new WorkoutSet(
                null,
                workoutExerciseId,
                input.setNo(),
                input.weight(),
                input.reps(),
                input.durationSeconds(),
                input.restSeconds(),
                input.rpe(),
                input.rir(),
                Boolean.TRUE.equals(input.isWarmup()),
                input.isCompleted() == null || Boolean.TRUE.equals(input.isCompleted()),
                now,
                now
        );
    }

    private WorkoutDetailResponse buildWorkoutDetailResponse(WorkoutSession session) {
        List<WorkoutExerciseResponse> exerciseResponses = workoutExerciseRepository.findByWorkoutSessionId(session.id()).stream()
                .map(this::buildWorkoutExerciseResponse)
                .toList();
        return new WorkoutDetailResponse(
                session.id(),
                session.userId(),
                session.sourceType().name(),
                session.sourceProgramId(),
                session.sourceBlockId(),
                session.sourceTemplateId(),
                session.status().name(),
                session.startedAt(),
                session.endedAt(),
                session.notes(),
                session.createdAt(),
                session.updatedAt(),
                exerciseResponses
        );
    }

    private WorkoutExerciseResponse buildWorkoutExerciseResponse(WorkoutExercise workoutExercise) {
        List<WorkoutSetResponse> setResponses = workoutSetRepository.findByWorkoutExerciseId(workoutExercise.id()).stream()
                .map(WorkoutApplicationServiceImpl::toWorkoutSetResponse)
                .toList();
        return new WorkoutExerciseResponse(
                workoutExercise.id(),
                workoutExercise.workoutSessionId(),
                workoutExercise.exerciseId(),
                workoutExercise.sourceTemplateExerciseId(),
                workoutExercise.actualOrderNo(),
                workoutExercise.replacementOfExerciseId(),
                workoutExercise.notes(),
                workoutExercise.createdAt(),
                workoutExercise.updatedAt(),
                setResponses
        );
    }

    private static WorkoutSetResponse toWorkoutSetResponse(WorkoutSet workoutSet) {
        return new WorkoutSetResponse(
                workoutSet.id(),
                workoutSet.workoutExerciseId(),
                workoutSet.setNo(),
                workoutSet.weight(),
                workoutSet.reps(),
                workoutSet.durationSeconds(),
                workoutSet.restSeconds(),
                workoutSet.rpe(),
                workoutSet.rir(),
                workoutSet.isWarmup(),
                workoutSet.isCompleted(),
                workoutSet.createdAt(),
                workoutSet.updatedAt()
        );
    }

    private static WorkoutHistoryItemResponse toWorkoutHistoryItemResponse(WorkoutSession session) {
        return new WorkoutHistoryItemResponse(
                session.id(),
                session.sourceType().name(),
                session.status().name(),
                session.startedAt(),
                session.endedAt(),
                session.notes()
        );
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private record TemplateContext(
            Program program,
            ProgramBlock block,
            SessionTemplate template,
            List<SessionExerciseTemplate> templateExercises
    ) {
    }
}
