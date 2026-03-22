package com.ironlogic.modules.workout.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for one WorkoutSet.
 *
 * @param id workout set id
 * @param workoutExerciseId parent workout exercise id
 * @param setNo set number
 * @param weight recorded weight
 * @param reps recorded reps
 * @param durationSeconds recorded duration
 * @param restSeconds recorded rest seconds
 * @param rpe recorded RPE
 * @param rir recorded RIR
 * @param isWarmup warmup flag
 * @param isCompleted completion flag
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record WorkoutSetResponse(
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
