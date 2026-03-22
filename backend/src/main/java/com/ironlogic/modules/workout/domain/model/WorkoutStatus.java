package com.ironlogic.modules.workout.domain.model;

/**
 * Execution status of a WorkoutSession.
 *
 * <p>MVP keeps the lifecycle intentionally small: a session is either in progress or completed.
 * Once completed, mutation APIs must reject further changes so the execution history remains
 * stable for future progression and statistics modules.
 */
public enum WorkoutStatus {
    IN_PROGRESS,
    COMPLETED
}
