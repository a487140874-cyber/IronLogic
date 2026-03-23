package com.ironlogic.modules.workout.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.program.domain.repository.ProgramBlockRepository;
import com.ironlogic.modules.program.domain.repository.ProgramRepository;
import com.ironlogic.modules.program.domain.repository.SessionExerciseTemplateRepository;
import com.ironlogic.modules.program.domain.repository.SessionTemplateRepository;
import com.ironlogic.modules.progression.application.ProgressionApplicationService;
import com.ironlogic.modules.workout.domain.model.WorkoutSession;
import com.ironlogic.modules.workout.domain.model.WorkoutSourceType;
import com.ironlogic.modules.workout.domain.model.WorkoutStatus;
import com.ironlogic.modules.workout.domain.repository.WorkoutExerciseRepository;
import com.ironlogic.modules.workout.domain.repository.WorkoutSessionRepository;
import com.ironlogic.modules.workout.domain.repository.WorkoutSetRepository;
import com.ironlogic.modules.workout.dto.CreateManualWorkoutRequest;
import com.ironlogic.modules.workout.dto.WorkoutDetailResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Focused unit test for WorkoutApplicationServiceImpl.
 *
 * <p>This test covers the happy path of creating a manual workout without requiring a database
 * or web layer.
 */
@ExtendWith(MockitoExtension.class)
class WorkoutApplicationServiceImplTest {

    @Mock
    private WorkoutSessionRepository workoutSessionRepository;

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Mock
    private WorkoutSetRepository workoutSetRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private SessionTemplateRepository sessionTemplateRepository;

    @Mock
    private SessionExerciseTemplateRepository sessionExerciseTemplateRepository;

    @Mock
    private ProgramBlockRepository programBlockRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgressionApplicationService progressionApplicationService;

    @InjectMocks
    private WorkoutApplicationServiceImpl workoutApplicationService;

    /** Verifies that a manual workout is created as MANUAL and IN_PROGRESS. */
    @Test
    void shouldCreateManualWorkout() {
        CreateManualWorkoutRequest request = new CreateManualWorkoutRequest("Evening session");

        when(workoutSessionRepository.save(any(WorkoutSession.class))).thenAnswer(invocation -> {
            WorkoutSession session = invocation.getArgument(0);
            return new WorkoutSession(
                    200L,
                    session.userId(),
                    session.sourceType(),
                    session.sourceProgramId(),
                    session.sourceBlockId(),
                    session.sourceTemplateId(),
                    session.status(),
                    session.startedAt(),
                    session.endedAt(),
                    session.notes(),
                    session.createdAt(),
                    session.updatedAt()
            );
        });
        when(workoutExerciseRepository.findByWorkoutSessionId(200L)).thenReturn(List.of());

        WorkoutDetailResponse response = workoutApplicationService.createManualWorkout(1L, request);

        assertThat(response.id()).isEqualTo(200L);
        assertThat(response.sourceType()).isEqualTo("MANUAL");
        assertThat(response.status()).isEqualTo("IN_PROGRESS");
        assertThat(response.notes()).isEqualTo("Evening session");

        ArgumentCaptor<WorkoutSession> captor = ArgumentCaptor.forClass(WorkoutSession.class);
        verify(workoutSessionRepository).save(captor.capture());
        assertThat(captor.getValue().sourceType()).isEqualTo(WorkoutSourceType.MANUAL);
        assertThat(captor.getValue().status()).isEqualTo(WorkoutStatus.IN_PROGRESS);
    }

    /** Verifies that completing a template workout advances progression state. */
    @Test
    void shouldAdvanceProgressionWhenTemplateWorkoutIsCompleted() {
        WorkoutSession existing = new WorkoutSession(
                300L,
                1L,
                WorkoutSourceType.TEMPLATE,
                10L,
                20L,
                30L,
                WorkoutStatus.IN_PROGRESS,
                LocalDateTime.now().minusHours(1),
                null,
                null,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().minusHours(1)
        );

        when(workoutSessionRepository.findByIdAndUserId(300L, 1L)).thenReturn(java.util.Optional.of(existing));
        when(workoutSessionRepository.update(any(WorkoutSession.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(workoutExerciseRepository.findByWorkoutSessionId(300L)).thenReturn(List.of());

        workoutApplicationService.finishWorkout(1L, 300L);

        verify(progressionApplicationService).advanceProgramProgressAfterWorkoutCompletion(1L, 300L, 10L, 20L, 30L);
    }

    /** Verifies that completing a manual workout does not move planned progression. */
    @Test
    void shouldNotAdvanceProgressionWhenManualWorkoutIsCompleted() {
        WorkoutSession existing = new WorkoutSession(
                301L,
                1L,
                WorkoutSourceType.MANUAL,
                null,
                null,
                null,
                WorkoutStatus.IN_PROGRESS,
                LocalDateTime.now().minusHours(1),
                null,
                "Free session",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().minusHours(1)
        );

        when(workoutSessionRepository.findByIdAndUserId(301L, 1L)).thenReturn(java.util.Optional.of(existing));
        when(workoutSessionRepository.update(any(WorkoutSession.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(workoutExerciseRepository.findByWorkoutSessionId(301L)).thenReturn(List.of());

        workoutApplicationService.finishWorkout(1L, 301L);

        verify(progressionApplicationService, org.mockito.Mockito.never())
                .advanceProgramProgressAfterWorkoutCompletion(any(), any(), any(), any(), any());
    }
}
