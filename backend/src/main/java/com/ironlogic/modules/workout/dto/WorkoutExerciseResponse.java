package com.ironlogic.modules.workout.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for one WorkoutExercise including its set list.
 *
 * @param id workout exercise id
 * @param workoutSessionId parent workout session id
 * @param exerciseId referenced exercise id
 * @param sourceTemplateExerciseId optional source template exercise id
 * @param actualOrderNo order inside workout
 * @param replacementOfExerciseId reserved replacement field
 * @param notes notes
 * @param createdAt creation time
 * @param updatedAt update time
 * @param sets recorded workout sets
 */
public record WorkoutExerciseResponse(
        Long id,
        Long workoutSessionId,
        Long exerciseId,
        Long sourceTemplateExerciseId,
        Integer actualOrderNo,
        Long replacementOfExerciseId,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<WorkoutSetResponse> sets
) {
}
