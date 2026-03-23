package com.ironlogic.modules.progression.application;

import com.ironlogic.common.exception.BusinessException;
import com.ironlogic.common.exception.NotFoundException;
import com.ironlogic.modules.exercise.domain.model.Exercise;
import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.program.domain.model.Program;
import com.ironlogic.modules.program.domain.model.ProgramBlock;
import com.ironlogic.modules.program.domain.model.SessionExerciseTemplate;
import com.ironlogic.modules.program.domain.model.SessionTemplate;
import com.ironlogic.modules.program.domain.repository.ProgramBlockRepository;
import com.ironlogic.modules.program.domain.repository.ProgramRepository;
import com.ironlogic.modules.program.domain.repository.SessionExerciseTemplateRepository;
import com.ironlogic.modules.program.domain.repository.SessionTemplateRepository;
import com.ironlogic.modules.progression.domain.model.ProgramProgress;
import com.ironlogic.modules.progression.domain.repository.ProgramProgressRepository;
import com.ironlogic.modules.progression.dto.CurrentRecommendationResponse;
import com.ironlogic.modules.progression.dto.ProgramProgressResponse;
import com.ironlogic.modules.progression.dto.RecommendedExerciseResponse;
import com.ironlogic.modules.progression.dto.RecommendedSessionTemplateResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * progression 模块 v1 的默认应用服务实现。
 *
 * <p>这一版实现刻意保持最小闭环：只跟踪当前 block 内的序列推荐，不计算负荷推进，
 * 不自动切 block，也不改写 workout 历史。这些都留给后续轮次处理。
 */
@Service
public class ProgressionApplicationServiceImpl implements ProgressionApplicationService {

    private final ProgramRepository programRepository;
    private final ProgramBlockRepository programBlockRepository;
    private final SessionTemplateRepository sessionTemplateRepository;
    private final SessionExerciseTemplateRepository sessionExerciseTemplateRepository;
    private final ExerciseRepository exerciseRepository;
    private final ProgramProgressRepository programProgressRepository;

    public ProgressionApplicationServiceImpl(
            ProgramRepository programRepository,
            ProgramBlockRepository programBlockRepository,
            SessionTemplateRepository sessionTemplateRepository,
            SessionExerciseTemplateRepository sessionExerciseTemplateRepository,
            ExerciseRepository exerciseRepository,
            ProgramProgressRepository programProgressRepository
    ) {
        this.programRepository = programRepository;
        this.programBlockRepository = programBlockRepository;
        this.sessionTemplateRepository = sessionTemplateRepository;
        this.sessionExerciseTemplateRepository = sessionExerciseTemplateRepository;
        this.exerciseRepository = exerciseRepository;
        this.programProgressRepository = programProgressRepository;
    }

    /**
     * 查询某个 Program 当前推荐的 SessionTemplate。
     */
    @Override
    @Transactional(readOnly = true)
    public CurrentRecommendationResponse getCurrentRecommendation(Long userId, Long programId) {
        requireOwnedProgram(userId, programId);

        RecommendationState recommendationState = resolveCurrentRecommendationState(userId, programId);
        RecommendedSessionTemplateResponse recommendedSessionTemplate =
                buildRecommendedSessionTemplateResponse(userId, recommendationState.template());

        return new CurrentRecommendationResponse(
                programId,
                recommendationState.block().id(),
                recommendationState.template().id(),
                recommendationState.sequenceCursor(),
                recommendedSessionTemplate
        );
    }

    /**
     * 查询某个 Program 当前已持久化的推进状态。
     */
    @Override
    @Transactional(readOnly = true)
    public ProgramProgressResponse getProgramProgress(Long userId, Long programId) {
        requireOwnedProgram(userId, programId);
        return programProgressRepository.findByUserIdAndProgramId(userId, programId)
                .map(ProgressionApplicationServiceImpl::toProgramProgressResponse)
                .orElse(null);
    }

    /**
     * 在模板训练完成后推进 ProgramProgress。
     */
    @Override
    @Transactional
    public void advanceProgramProgressAfterWorkoutCompletion(
            Long userId,
            Long workoutId,
            Long programId,
            Long blockId,
            Long templateId
    ) {
        requireOwnedProgram(userId, programId);
        ProgramBlock block = requireOwnedBlock(userId, blockId);
        SessionTemplate completedTemplate = requireTemplateInsideBlock(templateId, block.id());
        SessionTemplate nextTemplate = resolveNextTemplateInsideBlock(block.id(), completedTemplate.id());

        ProgramProgress existing = programProgressRepository.findByUserIdAndProgramId(userId, programId).orElse(null);
        int nextSequenceCursor = existing == null ? 1 : existing.sequenceCursor() + 1;
        LocalDateTime now = LocalDateTime.now();

        ProgramProgress nextProgress = new ProgramProgress(
                existing == null ? null : existing.id(),
                userId,
                programId,
                block.id(),
                nextTemplate.id(),
                workoutId,
                nextSequenceCursor,
                buildProgressSnapshotJson(block.id(), completedTemplate.id(), nextTemplate.id(), workoutId, nextSequenceCursor),
                now
        );

        if (existing == null) {
            programProgressRepository.save(nextProgress);
            return;
        }
        programProgressRepository.update(nextProgress);
    }

    private RecommendationState resolveCurrentRecommendationState(Long userId, Long programId) {
        ProgramProgress programProgress = programProgressRepository.findByUserIdAndProgramId(userId, programId).orElse(null);
        if (programProgress == null) {
            // 即使还没有 ProgramProgress，推荐接口也必须能工作。
            // v1 直接回到第一个 Block 的第一个 SessionTemplate，
            // 这样新 Program 不需要额外的初始化接口就能开始训练。
            ProgramBlock firstBlock = requireFirstBlock(programId);
            SessionTemplate firstTemplate = requireFirstTemplate(firstBlock.id());
            return new RecommendationState(firstBlock, firstTemplate, 0);
        }

        ProgramBlock block = requireOwnedBlock(userId, programProgress.currentBlockId());
        SessionTemplate template = requireTemplateInsideBlock(programProgress.nextSessionTemplateId(), block.id());
        return new RecommendationState(block, template, programProgress.sequenceCursor());
    }

    private Program requireOwnedProgram(Long userId, Long programId) {
        return programRepository.findByIdAndUserId(programId, userId)
                .orElseThrow(() -> new NotFoundException("Program not found"));
    }

    private ProgramBlock requireOwnedBlock(Long userId, Long blockId) {
        ProgramBlock block = programBlockRepository.findById(blockId)
                .orElseThrow(() -> new NotFoundException("ProgramBlock not found"));
        requireOwnedProgram(userId, block.programId());
        return block;
    }

    private ProgramBlock requireFirstBlock(Long programId) {
        return programBlockRepository.findByProgramId(programId).stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException("Program has no ProgramBlock"));
    }

    private SessionTemplate requireFirstTemplate(Long blockId) {
        return sessionTemplateRepository.findByBlockId(blockId).stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException("ProgramBlock has no SessionTemplate"));
    }

    private SessionTemplate requireTemplateInsideBlock(Long templateId, Long blockId) {
        SessionTemplate template = sessionTemplateRepository.findById(templateId)
                .orElseThrow(() -> new NotFoundException("SessionTemplate not found"));
        if (!blockId.equals(template.blockId())) {
            throw new BusinessException("SessionTemplate does not belong to the expected ProgramBlock");
        }
        return template;
    }

    private SessionTemplate resolveNextTemplateInsideBlock(Long blockId, Long currentTemplateId) {
        List<SessionTemplate> templates = sessionTemplateRepository.findByBlockId(blockId);
        if (templates.isEmpty()) {
            throw new BusinessException("ProgramBlock has no SessionTemplate");
        }

        for (int i = 0; i < templates.size(); i++) {
            SessionTemplate template = templates.get(i);
            if (!template.id().equals(currentTemplateId)) {
                continue;
            }

            // v1 只在当前 block 内循环。
            // 故意不自动切到下一个 block，因为 block 切换规则留到后续轮次再做。
            int nextIndex = i + 1 < templates.size() ? i + 1 : 0;
            return templates.get(nextIndex);
        }
        throw new BusinessException("Current SessionTemplate not found inside the ProgramBlock sequence");
    }

    private RecommendedSessionTemplateResponse buildRecommendedSessionTemplateResponse(
            Long userId,
            SessionTemplate template
    ) {
        List<RecommendedExerciseResponse> exercises = sessionExerciseTemplateRepository
                .findBySessionTemplateId(template.id())
                .stream()
                .map(templateExercise -> toRecommendedExerciseResponse(userId, templateExercise))
                .toList();

        return new RecommendedSessionTemplateResponse(
                template.id(),
                template.blockId(),
                template.name(),
                template.sequenceNo(),
                template.triggerMode(),
                template.notes(),
                template.metadataJson(),
                template.createdAt(),
                template.updatedAt(),
                exercises
        );
    }

    private RecommendedExerciseResponse toRecommendedExerciseResponse(
            Long userId,
            SessionExerciseTemplate templateExercise
    ) {
        Exercise exercise = exerciseRepository.findVisibleById(userId, templateExercise.exerciseId())
                .orElseThrow(() -> new NotFoundException("Exercise not found"));
        return new RecommendedExerciseResponse(
                templateExercise.id(),
                templateExercise.exerciseId(),
                exercise.name(),
                templateExercise.orderNo(),
                templateExercise.targetSets(),
                templateExercise.targetReps(),
                templateExercise.targetWeight(),
                templateExercise.targetWeightUnit(),
                templateExercise.restSeconds(),
                templateExercise.intensityMode(),
                templateExercise.prescriptionJson()
        );
    }

    private static ProgramProgressResponse toProgramProgressResponse(ProgramProgress progress) {
        return new ProgramProgressResponse(
                progress.id(),
                progress.userId(),
                progress.programId(),
                progress.currentBlockId(),
                progress.nextSessionTemplateId(),
                progress.lastCompletedWorkoutId(),
                progress.sequenceCursor(),
                progress.progressSnapshotJson(),
                progress.updatedAt()
        );
    }

    private static String buildProgressSnapshotJson(
            Long currentBlockId,
            Long completedTemplateId,
            Long nextTemplateId,
            Long workoutId,
            Integer sequenceCursor
    ) {
        return """
                {"currentBlockId":%d,"completedTemplateId":%d,"nextSessionTemplateId":%d,"lastCompletedWorkoutId":%d,"sequenceCursor":%d}
                """.formatted(currentBlockId, completedTemplateId, nextTemplateId, workoutId, sequenceCursor);
    }

    private record RecommendationState(
            ProgramBlock block,
            SessionTemplate template,
            Integer sequenceCursor
    ) {
    }
}
