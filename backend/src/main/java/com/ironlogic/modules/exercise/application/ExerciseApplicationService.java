package com.ironlogic.modules.exercise.application;

import com.ironlogic.modules.exercise.dto.CreateExerciseRequest;
import com.ironlogic.modules.exercise.dto.ExerciseQueryRequest;
import com.ironlogic.modules.exercise.dto.ExerciseResponse;
import com.ironlogic.modules.exercise.dto.UpdateExerciseRequest;
import java.util.List;

/**
 * Application service for Exercise module use cases.
 *
 * <p>This interface sits between the thin HTTP controller layer and the persistence layer.
 * Controllers delegate user intent here, and this service coordinates validation that belongs
 * to the use case itself, ownership rules, and DTO/domain conversion.
 */
public interface ExerciseApplicationService {

    /**
     * Creates a custom exercise for the given user.
     *
     * @param userId the current user id; in MVP this is temporarily fixed in controller
     * @param request request payload carrying custom exercise fields
     * @return the created exercise rendered as API response DTO
     */
    ExerciseResponse createCustomExercise(Long userId, CreateExerciseRequest request);

    /**
     * Updates an existing custom exercise.
     *
     * @param userId the current user id
     * @param exerciseId target exercise id
     * @param request request payload with the new exercise data
     * @return updated exercise response
     * @throws com.ironlogic.common.exception.NotFoundException when the exercise does not exist
     * @throws com.ironlogic.common.exception.ForbiddenException when the exercise is a system
     *         exercise or belongs to another user
     */
    ExerciseResponse updateCustomExercise(Long userId, Long exerciseId, UpdateExerciseRequest request);

    /**
     * Loads a single exercise visible to the current user.
     *
     * @param userId the current user id
     * @param exerciseId target exercise id
     * @return exercise detail
     * @throws com.ironlogic.common.exception.NotFoundException when the exercise does not exist
     *         or is not visible to the current user
     */
    ExerciseResponse getExercise(Long userId, Long exerciseId);

    /**
     * Lists exercises visible to the current user.
     *
     * <p>The result must include both system exercises and the current user's custom exercises.
     *
     * @param userId the current user id
     * @param request optional query filters
     * @return a list of visible exercises
     */
    List<ExerciseResponse> listExercises(Long userId, ExerciseQueryRequest request);
}
