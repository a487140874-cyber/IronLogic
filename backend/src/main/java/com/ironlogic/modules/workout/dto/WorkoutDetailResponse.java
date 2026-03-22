package com.ironlogic.modules.workout.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Full workout detail response.
 *
 * @param id workout session id
 * @param userId owner user id
 * @param sourceType source type string
 * @param sourceProgramId source program id
 * @param sourceBlockId source block id
 * @param sourceTemplateId source template id
 * @param status workout status
 * @param startedAt start time
 * @param endedAt end time
 * @param notes workout notes
 * @param createdAt creation time
 * @param updatedAt update time
 * @param exercises workout exercises with sets
 */
public record WorkoutDetailResponse(
        Long id,
        Long userId,
        String sourceType,
        Long sourceProgramId,
        Long sourceBlockId,
        Long sourceTemplateId,
        String status,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<WorkoutExerciseResponse> exercises
) {
}
