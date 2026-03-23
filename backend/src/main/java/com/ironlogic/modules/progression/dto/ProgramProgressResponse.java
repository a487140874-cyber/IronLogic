package com.ironlogic.modules.progression.dto;

import java.time.LocalDateTime;

/**
 * DTO for exposing persisted ProgramProgress state.
 *
 * @param id progression row id
 * @param userId owner user id
 * @param programId target program id
 * @param currentBlockId current block where progression is parked
 * @param nextSessionTemplateId next recommended session template id
 * @param lastCompletedWorkoutId latest completed template workout that advanced progression
 * @param sequenceCursor simple count of sequence advances
 * @param progressSnapshotJson lightweight progression snapshot json
 * @param updatedAt last update time
 */
public record ProgramProgressResponse(
        Long id,
        Long userId,
        Long programId,
        Long currentBlockId,
        Long nextSessionTemplateId,
        Long lastCompletedWorkoutId,
        Integer sequenceCursor,
        String progressSnapshotJson,
        LocalDateTime updatedAt
) {
}
