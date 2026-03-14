package com.ironlogic.modules.exercise.domain.repository;

import com.ironlogic.modules.exercise.domain.model.Exercise;
import java.util.List;
import java.util.Optional;

/**
 * Repository boundary for Exercise aggregate access.
 *
 * <p>The application layer depends on this abstraction instead of directly on MyBatis-Plus.
 * That keeps use-case code independent from infrastructure details and makes unit testing
 * straightforward with simple mocks.
 */
public interface ExerciseRepository {

    /**
     * Persists a newly created exercise.
     *
     * @param exercise domain exercise to persist
     * @return persisted exercise including generated id
     */
    Exercise save(Exercise exercise);

    /**
     * Persists changes to an existing exercise.
     *
     * @param exercise updated exercise
     * @return updated exercise
     */
    Exercise update(Exercise exercise);

    /**
     * Finds exercise by id without visibility filtering.
     *
     * @param id exercise id
     * @return optional exercise
     */
    Optional<Exercise> findById(Long id);

    /**
     * Finds an exercise only if it is visible to the current user.
     *
     * @param userId current user id
     * @param id exercise id
     * @return optional visible exercise
     */
    Optional<Exercise> findVisibleById(Long userId, Long id);

    /**
     * Lists exercises visible to the current user.
     *
     * @param userId current user id
     * @param name optional name filter
     * @param category optional category filter
     * @param equipmentType optional equipment filter
     * @return visible exercise list
     */
    List<Exercise> findVisible(Long userId, String name, String category, String equipmentType);
}
