package com.ironlogic.modules.workout.domain.repository;

import com.ironlogic.modules.workout.domain.model.WorkoutSession;
import java.util.List;
import java.util.Optional;

/**
 * Repository boundary for WorkoutSession access.
 */
public interface WorkoutSessionRepository {

    /**
     * Persists a new workout session.
     *
     * @param workoutSession workout session to persist
     * @return persisted workout session
     */
    WorkoutSession save(WorkoutSession workoutSession);

    /**
     * Updates an existing workout session.
     *
     * @param workoutSession updated workout session
     * @return updated workout session
     */
    WorkoutSession update(WorkoutSession workoutSession);

    /**
     * Finds a workout session by id.
     *
     * @param id session id
     * @return optional session
     */
    Optional<WorkoutSession> findById(Long id);

    /**
     * Finds an owned workout session by id.
     *
     * @param id session id
     * @param userId current user id
     * @return optional owned session
     */
    Optional<WorkoutSession> findByIdAndUserId(Long id, Long userId);

    /**
     * Lists current user's workout history.
     *
     * @param userId current user id
     * @return ordered workout sessions
     */
    List<WorkoutSession> findByUserId(Long userId);
}
