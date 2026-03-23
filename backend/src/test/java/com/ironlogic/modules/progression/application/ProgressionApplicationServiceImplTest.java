package com.ironlogic.modules.progression.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Focused unit tests for ProgressionApplicationServiceImpl.
 *
 * <p>These tests cover the core v1 behaviors: default recommendation when no progress exists
 * and sequence advancement after a completed template workout.
 */
@ExtendWith(MockitoExtension.class)
class ProgressionApplicationServiceImplTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramBlockRepository programBlockRepository;

    @Mock
    private SessionTemplateRepository sessionTemplateRepository;

    @Mock
    private SessionExerciseTemplateRepository sessionExerciseTemplateRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ProgramProgressRepository programProgressRepository;

    @InjectMocks
    private ProgressionApplicationServiceImpl progressionApplicationService;

    /** Verifies that a Program without progress defaults to the first block and first template. */
    @Test
    void shouldReturnFirstBlockFirstTemplateWhenProgressDoesNotExist() {
        Program program = program();
        ProgramBlock firstBlock = block(10L, 1);
        SessionTemplate firstTemplate = template(100L, 10L, 1, "Push A");
        SessionExerciseTemplate templateExercise = templateExercise(1000L, 100L, 900L, 1);
        Exercise exercise = exercise(900L, "Bench Press");

        when(programRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(program));
        when(programProgressRepository.findByUserIdAndProgramId(1L, 1L)).thenReturn(Optional.empty());
        when(programBlockRepository.findByProgramId(1L)).thenReturn(List.of(firstBlock));
        when(sessionTemplateRepository.findByBlockId(10L)).thenReturn(List.of(firstTemplate));
        when(sessionExerciseTemplateRepository.findBySessionTemplateId(100L)).thenReturn(List.of(templateExercise));
        when(exerciseRepository.findVisibleById(1L, 900L)).thenReturn(Optional.of(exercise));

        CurrentRecommendationResponse response = progressionApplicationService.getCurrentRecommendation(1L, 1L);

        assertThat(response.programId()).isEqualTo(1L);
        assertThat(response.currentBlockId()).isEqualTo(10L);
        assertThat(response.nextSessionTemplateId()).isEqualTo(100L);
        assertThat(response.sequenceCursor()).isEqualTo(0);
        assertThat(response.recommendedSessionTemplate().name()).isEqualTo("Push A");
        assertThat(response.recommendedSessionTemplate().exercises()).hasSize(1);
        assertThat(response.recommendedSessionTemplate().exercises().get(0).exerciseName()).isEqualTo("Bench Press");
    }

    /** Verifies that template completion advances recommendation to the next template in the same block. */
    @Test
    void shouldAdvanceToNextTemplateInsideSameBlock() {
        Program program = program();
        ProgramBlock block = block(10L, 1);
        SessionTemplate firstTemplate = template(100L, 10L, 1, "Push A");
        SessionTemplate secondTemplate = template(101L, 10L, 2, "Push B");

        when(programRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(program));
        when(programBlockRepository.findById(10L)).thenReturn(Optional.of(block));
        when(sessionTemplateRepository.findById(100L)).thenReturn(Optional.of(firstTemplate));
        when(sessionTemplateRepository.findByBlockId(10L)).thenReturn(List.of(firstTemplate, secondTemplate));
        when(programProgressRepository.findByUserIdAndProgramId(1L, 1L)).thenReturn(Optional.empty());

        progressionApplicationService.advanceProgramProgressAfterWorkoutCompletion(1L, 500L, 1L, 10L, 100L);

        ArgumentCaptor<ProgramProgress> captor = ArgumentCaptor.forClass(ProgramProgress.class);
        verify(programProgressRepository).save(captor.capture());

        ProgramProgress saved = captor.getValue();
        assertThat(saved.userId()).isEqualTo(1L);
        assertThat(saved.programId()).isEqualTo(1L);
        assertThat(saved.currentBlockId()).isEqualTo(10L);
        assertThat(saved.nextSessionTemplateId()).isEqualTo(101L);
        assertThat(saved.lastCompletedWorkoutId()).isEqualTo(500L);
        assertThat(saved.sequenceCursor()).isEqualTo(1);
    }

    private static Program program() {
        return new Program(
                1L,
                1L,
                "Upper/Lower",
                "HYPERTROPHY",
                "ACTIVE",
                null,
                LocalDate.now(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static ProgramBlock block(Long id, Integer sequenceNo) {
        return new ProgramBlock(
                id,
                1L,
                "Accumulation",
                "ACCUMULATION",
                sequenceNo,
                "SEQUENCE",
                null,
                false,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static SessionTemplate template(Long id, Long blockId, Integer sequenceNo, String name) {
        return new SessionTemplate(
                id,
                blockId,
                name,
                sequenceNo,
                "SEQUENCE",
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static SessionExerciseTemplate templateExercise(Long id, Long sessionTemplateId, Long exerciseId, Integer orderNo) {
        return new SessionExerciseTemplate(
                id,
                sessionTemplateId,
                exerciseId,
                orderNo,
                3,
                8,
                new BigDecimal("100.00"),
                "kg",
                120,
                "WEIGHT",
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static Exercise exercise(Long id, String name) {
        return new Exercise(
                id,
                null,
                name,
                "CHEST",
                "CHEST",
                null,
                "BARBELL",
                "PRESS",
                false,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
