package com.ironlogic.modules.workout.dto;

/**
 * Request DTO for creating a manual workout.
 *
 * @param notes optional notes entered when starting a free workout
 */
public record CreateManualWorkoutRequest(
        /** Optional notes for a manual workout. */
        String notes
) {
}
