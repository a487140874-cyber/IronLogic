package com.ironlogic.modules.workout.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for adding a WorkoutExercise into an existing workout.
 *
 * @param exerciseId referenced exercise id
 * @param actualOrderNo position inside the workout
 * @param replacementOfExerciseId reserved field for future replacement support
 * @param notes optional notes for this workout exercise
 */
public record AddWorkoutExerciseRequest(
        /** Exercise must exist and be visible to the current user. */
        @NotNull
        Long exerciseId,

        /** actualOrderNo must be unique inside one workout session. */
        @NotNull
        Integer actualOrderNo,

        /** Reserved field for future replacement logic. */
        Long replacementOfExerciseId,

        /** Optional notes for the exercise entry. */
        String notes
) {
}
