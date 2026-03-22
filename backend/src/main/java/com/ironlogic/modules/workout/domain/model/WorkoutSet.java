package com.ironlogic.modules.workout.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model for one set inside a WorkoutExercise.
 *
 * <p>Sets capture the smallest execution unit of a workout. MVP stores them as a complete
 * replacement list instead of granular patch operations to keep write logic easy to understand.
 *
 * @param id workout set id
 * @param workoutExerciseId parent workout exercise id
 * @param setNo set number inside one exercise
 * @param weight optional recorded weight
 * @param reps optional recorded reps
 * @param durationSeconds optional duration-based result
 * @param restSeconds optional rest duration
 * @param rpe optional rating of perceived exertion
 * @param rir optional reps in reserve
 * @param isWarmup whether the set is a warmup set
 * @param isCompleted whether the set was completed
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record WorkoutSet(
        Long id,
        Long workoutExerciseId,
        Integer setNo,
        BigDecimal weight,
        Integer reps,
        Integer durationSeconds,
        Integer restSeconds,
        BigDecimal rpe,
        Integer rir,
        Boolean isWarmup,
        Boolean isCompleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
