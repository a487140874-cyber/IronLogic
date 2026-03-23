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
 * Default application service for Workout module.
 *
 * <p>This class is the orchestration layer of live training execution. It deliberately keeps
 * template reads, ownership checks, and mutation rules in one place so controllers stay thin
 * and the future progression module can hook into a stable completed-workout boundary later.
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

    /** {@inheritDoc} */
    @Override
    @Transactional
    public WorkoutDetailResponse createWorkoutFromTemplate(Long userId, Long templateId) {
        TemplateContext templateContext = requireAccessibleTemplate(userId, templateId);
        LocalDateTime now = LocalDateTime.now();

        // TEMPLATE is explicit because future progression and history features need to know
        // whether this workout came from a planned template or from ad-hoc manual training.
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
            // Even though the template already references an exercise, we validate visibility again
            // so workout execution never creates records for exercises the user cannot access.
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public WorkoutDetailResponse getWorkoutDetail(Long userId, Long workoutId) {
        WorkoutSession session = requireOwnedWorkoutSession(userId, workoutId);
        return buildWorkoutDetailResponse(session);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<WorkoutHistoryItemResponse> listWorkoutHistory(Long userId) {
        return workoutSessionRepository.findByUserId(userId).stream()
                .map(WorkoutApplicationServiceImpl::toWorkoutHistoryItemResponse)
                .toList();
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    @Transactional
    public List<WorkoutSetResponse> saveWorkoutSets(Long userId, Long workoutExerciseId, SaveWorkoutSetsRequest request) {
        WorkoutExercise workoutExercise = requireOwnedWorkoutExercise(userId, workoutExerciseId);
        requireModifiableWorkoutSession(userId, workoutExercise.workoutSessionId());

        validateDistinctSetNo(request.sets());

        // MVP uses full overwrite instead of incremental patching because it keeps persistence
        // logic simple and deterministic while the workout editor behavior is still evolving.
        workoutSetRepository.deleteByWorkoutExerciseId(workoutExerciseId);

        List<WorkoutSetResponse> responses = request.sets().stream()
                .map(input -> workoutSetRepository.save(toWorkoutSet(workoutExerciseId, input)))
                .map(WorkoutApplicationServiceImpl::toWorkoutSetResponse)
                .toList();

        // TODO: when the workout editor becomes more sophisticated, replace full overwrite
        // with incremental insert/update/delete to preserve set-level history and ids.
        return responses;
    }

    /** {@inheritDoc} */
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
            // Workout owns the act of completing one training session.
            // Progression owns the separate concern of updating the recommendation cursor afterwards.
            progressionApplicationService.advanceProgramProgressAfterWorkoutCompletion(
                    userId,
                    completed.id(),
                    completed.sourceProgramId(),
                    completed.sourceBlockId(),
                    completed.sourceTemplateId()
            );
        }

        if (completed.sourceType() == WorkoutSourceType.MANUAL) {
            // MANUAL workouts are intentionally excluded from progression.
            // They are free-form records and should not move the planned template sequence.
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
            // Completed workouts become immutable so later progression/statistics modules can
            // treat them as stable historical facts instead of mutable draft data.
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
