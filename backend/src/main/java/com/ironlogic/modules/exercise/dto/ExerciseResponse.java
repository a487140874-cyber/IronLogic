package com.ironlogic.modules.exercise.dto;

import java.time.LocalDateTime;

/**
 * Response DTO returned by Exercise APIs.
 *
 * <p>The response mirrors the key exercise fields the frontend needs to render catalog and
 * detail views, while staying separate from both persistence annotations and domain behavior.
 *
 * @param id exercise id
 * @param ownerUserId owner id, where {@code null} means system exercise
 * @param name exercise name
 * @param category exercise category
 * @param primaryMuscle main trained muscle
 * @param secondaryMusclesJson json string describing secondary muscles
 * @param equipmentType equipment classification
 * @param movementPattern movement pattern classification
 * @param isCustom whether the exercise belongs to the user's custom catalog
 * @param metadataJson extensible json metadata
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record ExerciseResponse(
        Long id,
        Long ownerUserId,
        String name,
        String category,
        String primaryMuscle,
        String secondaryMusclesJson,
        String equipmentType,
        String movementPattern,
        Boolean isCustom,
        String metadataJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
