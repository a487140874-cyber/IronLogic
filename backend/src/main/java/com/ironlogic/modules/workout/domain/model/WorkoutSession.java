package com.ironlogic.modules.workout.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model for an actual WorkoutSession.
 *
 * <p>This is the execution-layer counterpart of plan definitions. It records one concrete
 * training occurrence for a user, optionally linked back to the template hierarchy that
 * created it.
 *
 * @param id workout session id
 * @param userId owner user id
 * @param sourceType whether the workout comes from template or manual creation
 * @param sourceProgramId optional source program id
 * @param sourceBlockId optional source block id
 * @param sourceTemplateId optional source session template id
 * @param status current workout status
 * @param startedAt actual start time
 * @param endedAt actual finish time
 * @param notes optional workout notes
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record WorkoutSession(
        Long id,
        Long userId,
        WorkoutSourceType sourceType,
        Long sourceProgramId,
        Long sourceBlockId,
        Long sourceTemplateId,
        WorkoutStatus status,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
