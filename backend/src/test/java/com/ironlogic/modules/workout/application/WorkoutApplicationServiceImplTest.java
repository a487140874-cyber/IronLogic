package com.ironlogic.modules.workout.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ironlogic.common.exception.BusinessException;
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
import com.ironlogic.modules.workout.dto.AddWorkoutExerciseRequest;
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
 * {@link WorkoutApplicationServiceImpl} 的聚焦单元测试。
 *
 * <p>这些测试覆盖训练执行层中最关键的 MVP 行为，而不依赖数据库或 Web 层。
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

    /** 验证手动训练会以 MANUAL 和 IN_PROGRESS 状态创建。 */
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

    /** 验证模板训练完成后会推进 progression 状态。 */
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

    /** 验证手动训练完成后不会推动计划序列。 */
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

    /** 验证已完成训练不能再继续修改。 */
    @Test
    void shouldRejectModificationForCompletedWorkout() {
        WorkoutSession completed = new WorkoutSession(
                302L,
                1L,
                WorkoutSourceType.TEMPLATE,
                10L,
                20L,
                30L,
                WorkoutStatus.COMPLETED,
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().minusHours(1),
                null,
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().minusHours(1)
        );

        when(workoutSessionRepository.findByIdAndUserId(302L, 1L)).thenReturn(java.util.Optional.of(completed));

        assertThatThrownBy(() -> workoutApplicationService.addWorkoutExercise(
                1L,
                302L,
                new AddWorkoutExerciseRequest(99L, 1, null, null)
        )).isInstanceOf(BusinessException.class)
                .hasMessage("Completed workout cannot be modified");
    }
}
