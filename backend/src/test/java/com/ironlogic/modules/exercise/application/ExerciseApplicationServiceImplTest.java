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
 * {@link ExerciseApplicationServiceImpl} 的聚焦单元测试。
 *
 * <p>这个测试不依赖数据库，主要验证创建自定义 Exercise 的主流程：
 * service 会在落库前正确补齐 owner 和 custom 标记。
 */
@ExtendWith(MockitoExtension.class)
class ExerciseApplicationServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseApplicationServiceImpl exerciseApplicationService;

    /**
     * 验证创建自定义 Exercise 时，会正确写入 owner 和 custom 标记。
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
