package com.ironlogic.modules.progression.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model for ProgramProgress.
 *
 * <p>This model stores the lightweight progression state of one user's one Program.
 * It intentionally does not replace workout history. Workout history remains the source
 * of truth for what actually happened, while ProgramProgress only stores the current
 * recommendation cursor that the progression module needs.
 *
 * @param id progression row id
 * @param userId owner user id
 * @param programId target program id
 * @param currentBlockId current block where progression is parked
 * @param nextSessionTemplateId next recommended session template id
 * @param lastCompletedWorkoutId latest completed template workout that advanced progression
 * @param sequenceCursor simple count of sequence advances inside the program
 * @param progressSnapshotJson lightweight json snapshot for debugging and future extension
 * @param updatedAt last update time
 */
public record ProgramProgress(
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
