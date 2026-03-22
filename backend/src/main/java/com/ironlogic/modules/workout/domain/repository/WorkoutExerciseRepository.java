package com.ironlogic.modules.workout.domain.repository;

import com.ironlogic.modules.workout.domain.model.WorkoutExercise;
import java.util.List;
import java.util.Optional;

/**
 * Repository boundary for WorkoutExercise access.
 */
public interface WorkoutExerciseRepository {

    /**
     * Persists a new workout exercise.
     *
     * @param workoutExercise workout exercise to persist
     * @return persisted workout exercise
     */
    WorkoutExercise save(WorkoutExercise workoutExercise);

    /**
     * Finds a workout exercise by id.
     *
     * @param id workout exercise id
     * @return optional workout exercise
     */
    Optional<WorkoutExercise> findById(Long id);

    /**
     * Lists workout exercises under one workout session.
     *
     * @param workoutSessionId parent session id
     * @return ordered workout exercises
     */
    List<WorkoutExercise> findByWorkoutSessionId(Long workoutSessionId);

    /**
     * Checks whether actualOrderNo already exists inside one workout session.
     *
     * @param workoutSessionId parent session id
     * @param actualOrderNo actual order number
     * @return true when duplicated
     */
    boolean existsByWorkoutSessionIdAndActualOrderNo(Long workoutSessionId, Integer actualOrderNo);
}
