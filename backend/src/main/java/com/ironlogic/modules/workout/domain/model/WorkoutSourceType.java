package com.ironlogic.modules.workout.domain.model;

/**
 * Source type of a WorkoutSession.
 *
 * <p>The type exists because template-based workouts and manual workouts have different source
 * semantics. Keeping the source explicit makes future progression integration easier without
 * mixing manual and template sessions heuristically.
 */
public enum WorkoutSourceType {
    TEMPLATE,
    MANUAL
}
