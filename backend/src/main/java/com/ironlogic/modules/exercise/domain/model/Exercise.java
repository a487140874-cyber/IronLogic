package com.ironlogic.modules.exercise.domain.model;

import java.time.LocalDateTime;

/**
 * Domain representation of an Exercise.
 *
 * <p>The project currently keeps the domain model intentionally light. We still separate it
 * from the persistence entity so the application layer can depend on a business-shaped object
 * rather than directly on MyBatis-Plus annotations. That keeps future rule changes localized.
 *
 * @param id exercise id
 * @param ownerUserId {@code null} means system exercise; non-null means user-owned
 * @param name exercise display name
 * @param category lightweight category string for MVP filtering
 * @param primaryMuscle main trained muscle
 * @param secondaryMusclesJson json string for secondary muscles
 * @param equipmentType equipment classification
 * @param movementPattern movement pattern classification
 * @param isCustom whether this exercise was created by a user
 * @param metadataJson extensible json field for future lightweight metadata
 * @param createdAt creation time
 * @param updatedAt last update time
 */
public record Exercise(
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
