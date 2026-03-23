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
 * Default application service for progression module v1.
 *
 * <p>This implementation intentionally keeps the logic small: it only tracks sequence-based
 * recommendation inside the current block. It does not calculate load progression, does not
 * auto-switch blocks, and does not rewrite workout history. Those concerns belong to future
 * rounds after the minimal progression loop is stable.
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public ProgramProgressResponse getProgramProgress(Long userId, Long programId) {
        requireOwnedProgram(userId, programId);
        return programProgressRepository.findByUserIdAndProgramId(userId, programId)
                .map(ProgressionApplicationServiceImpl::toProgramProgressResponse)
                .orElse(null);
    }

    /** {@inheritDoc} */
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
            // When there is no ProgramProgress yet, recommendation must still work.
            // v1 chooses the first Block and its first SessionTemplate so a new Program can start
            // without requiring any extra initialization endpoint.
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

            // v1 only loops inside the current block. It intentionally does not switch to the
            // next block automatically because block transition rules are postponed to a later round.
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
