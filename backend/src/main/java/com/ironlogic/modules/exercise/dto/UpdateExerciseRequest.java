package com.ironlogic.modules.exercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating a custom exercise.
 *
 * <p>The server intentionally does not expose ownership or system/custom flags here. Those
 * values are protected by backend rules and cannot be modified from the client side.
 *
 * @param name updated exercise name
 * @param category updated category
 * @param primaryMuscle updated primary muscle
 * @param secondaryMusclesJson updated secondary muscles json
 * @param equipmentType updated equipment type
 * @param movementPattern updated movement pattern
 * @param metadataJson updated metadata json
 */
public record UpdateExerciseRequest(
        /** Required display name after update. */
        @NotBlank
        @Size(max = 128)
        String name,

        /** Optional category after update. */
        @Size(max = 32)
        String category,

        /** Optional primary muscle after update. */
        @Size(max = 64)
        String primaryMuscle,

        /** Optional secondary muscles JSON after update. */
        String secondaryMusclesJson,

        /** Optional equipment type after update. */
        @Size(max = 32)
        String equipmentType,

        /** Optional movement pattern after update. */
        @Size(max = 64)
        String movementPattern,

        /** Optional metadata JSON after update. */
        String metadataJson
) {
}
