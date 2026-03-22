package com.ironlogic.modules.workout.dto;

import java.time.LocalDateTime;

/**
 * Lightweight history list item for workout history API.
 *
 * @param id workout session id
 * @param sourceType source type string
 * @param status status string
 * @param startedAt start time
 * @param endedAt end time
 * @param notes notes
 */
public record WorkoutHistoryItemResponse(
        Long id,
        String sourceType,
        String status,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String notes
) {
}
