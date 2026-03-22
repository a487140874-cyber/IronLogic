package com.ironlogic.modules.workout.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model for one exercise entry inside a WorkoutSession.
 *
 * <p>A WorkoutExercise is created either by copying a SessionExerciseTemplate when the workout
 * starts from template, or by manual insertion during a live workout.
 *
 * @param id workout exercise id
 * @param workoutSessionId parent workout session id
 * @param exerciseId referenced exercise id
 * @param sourceTemplateExerciseId optional template exercise id that produced this record
 * @param actualOrderNo display order inside the workout
 * @param replacementOfExerciseId optional reserved field for future replacement logic
 * @param notes optional exercise notes
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record WorkoutExercise(
        Long id,
        Long workoutSessionId,
        Long exerciseId,
        Long sourceTemplateExerciseId,
        Integer actualOrderNo,
        Long replacementOfExerciseId,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
