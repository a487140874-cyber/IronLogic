package com.ironlogic.modules.exercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a custom exercise.
 *
 * <p>This object belongs to the API boundary. It intentionally carries only user-input fields;
 * ownership, custom/system flags, and timestamps are assigned by the server.
 *
 * @param name user-facing exercise name
 * @param category simple category string for MVP filtering
 * @param primaryMuscle main trained muscle
 * @param secondaryMusclesJson json string describing secondary muscles
 * @param equipmentType equipment classification
 * @param movementPattern movement pattern classification
 * @param metadataJson extensible json string for lightweight custom metadata
 */
public record CreateExerciseRequest(
        /** Required display name of the custom exercise. */
        @NotBlank
        @Size(max = 128)
        String name,

        /** Optional MVP category, kept as a string to avoid over-design too early. */
        @Size(max = 32)
        String category,

        /** Optional primary muscle focus for basic catalog browsing. */
        @Size(max = 64)
        String primaryMuscle,

        /** Optional JSON string storing secondary muscles without forcing extra tables in MVP. */
        String secondaryMusclesJson,

        /** Optional equipment type for search/filter. */
        @Size(max = 32)
        String equipmentType,

        /** Optional movement pattern label such as push, pull, squat, hinge. */
        @Size(max = 64)
        String movementPattern,

        /** Optional free-form JSON metadata reserved for future lightweight extension. */
        String metadataJson
) {
}
