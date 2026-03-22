package com.ironlogic.modules.workout.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Input DTO for one WorkoutSet during overwrite-save.
 *
 * @param setNo set number inside the exercise
 * @param weight optional recorded weight
 * @param reps optional recorded reps
 * @param durationSeconds optional duration-based value
 * @param restSeconds optional rest seconds
 * @param rpe optional RPE
 * @param rir optional RIR
 * @param isWarmup whether the set is warmup
 * @param isCompleted whether the set is completed
 */
public record WorkoutSetInput(
        /** setNo must be unique inside the same workout exercise. */
        @NotNull
        Integer setNo,

        /** Optional recorded weight. */
        BigDecimal weight,

        /** Optional recorded reps. */
        Integer reps,

        /** Optional duration for timed movements. */
        Integer durationSeconds,

        /** Optional rest duration. */
        Integer restSeconds,

        /** Optional rating of perceived exertion. */
        BigDecimal rpe,

        /** Optional reps in reserve. */
        Integer rir,

        /** Whether the set is warmup; null is treated as false by service. */
        Boolean isWarmup,

        /** Whether the set is completed; null is treated as true by service. */
        Boolean isCompleted
) {
}
