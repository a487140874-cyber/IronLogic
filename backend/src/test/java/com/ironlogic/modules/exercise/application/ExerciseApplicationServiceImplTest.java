package com.ironlogic.modules.exercise.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ironlogic.modules.exercise.domain.model.Exercise;
import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.exercise.dto.CreateExerciseRequest;
import com.ironlogic.modules.exercise.dto.ExerciseResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Focused unit test for {@link ExerciseApplicationServiceImpl}.
 *
 * <p>The goal of this test is to verify the main happy path without requiring a database:
 * when a user creates a custom exercise, the service should assign ownership and the custom
 * flag correctly before delegating persistence to the repository.
 */
@ExtendWith(MockitoExtension.class)
class ExerciseApplicationServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseApplicationServiceImpl exerciseApplicationService;

    /**
     * Verifies that creating a custom exercise produces the expected ownership and custom flags.
     */
    @Test
    void shouldCreateCustomExercise() {
        CreateExerciseRequest request = new CreateExerciseRequest(
                "Incline Dumbbell Press",
                "strength",
                "chest",
                "[\"shoulders\",\"triceps\"]",
                "dumbbell",
                "horizontal_push",
                "{\"source\":\"user\"}"
        );

        when(exerciseRepository.save(any(Exercise.class))).thenAnswer(invocation -> {
            Exercise exercise = invocation.getArgument(0);
            return new Exercise(
                    10L,
                    exercise.ownerUserId(),
                    exercise.name(),
                    exercise.category(),
                    exercise.primaryMuscle(),
                    exercise.secondaryMusclesJson(),
                    exercise.equipmentType(),
                    exercise.movementPattern(),
                    exercise.isCustom(),
                    exercise.metadataJson(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        });

        ExerciseResponse response = exerciseApplicationService.createCustomExercise(1L, request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.ownerUserId()).isEqualTo(1L);
        assertThat(response.isCustom()).isTrue();
        assertThat(response.name()).isEqualTo("Incline Dumbbell Press");

        ArgumentCaptor<Exercise> captor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(captor.capture());
        Exercise savedExercise = captor.getValue();
        assertThat(savedExercise.ownerUserId()).isEqualTo(1L);
        assertThat(savedExercise.isCustom()).isTrue();
        assertThat(savedExercise.id()).isNull();
    }
}
