package com.ironlogic.modules.workout.domain.repository;

import com.ironlogic.modules.workout.domain.model.WorkoutSet;
import java.util.List;

/**
 * Repository boundary for WorkoutSet access.
 */
public interface WorkoutSetRepository {

    /**
     * Persists one workout set.
     *
     * @param workoutSet workout set to persist
     * @return persisted workout set
     */
    WorkoutSet save(WorkoutSet workoutSet);

    /**
     * Lists sets under one workout exercise.
     *
     * @param workoutExerciseId parent workout exercise id
     * @return ordered set list
     */
    List<WorkoutSet> findByWorkoutExerciseId(Long workoutExerciseId);

    /**
     * Deletes all sets of one workout exercise.
     *
     * @param workoutExerciseId parent workout exercise id
     */
    void deleteByWorkoutExerciseId(Long workoutExerciseId);
}
