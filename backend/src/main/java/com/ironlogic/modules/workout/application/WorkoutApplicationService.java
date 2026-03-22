package com.ironlogic.modules.workout.application;

import com.ironlogic.modules.workout.dto.AddWorkoutExerciseRequest;
import com.ironlogic.modules.workout.dto.CreateManualWorkoutRequest;
import com.ironlogic.modules.workout.dto.SaveWorkoutSetsRequest;
import com.ironlogic.modules.workout.dto.WorkoutDetailResponse;
import com.ironlogic.modules.workout.dto.WorkoutExerciseResponse;
import com.ironlogic.modules.workout.dto.WorkoutHistoryItemResponse;
import java.util.List;

/**
 * Application service for Workout execution use cases.
 *
 * <p>This service coordinates template reads from the program module, exercise visibility
 * checks from the exercise module, and write operations into the workout execution tables.
 */
public interface WorkoutApplicationService {

    /** Starts a workout by copying exercises from one visible SessionTemplate. */
    WorkoutDetailResponse createWorkoutFromTemplate(Long userId, Long templateId);

    /** Starts a manual free workout. */
    WorkoutDetailResponse createManualWorkout(Long userId, CreateManualWorkoutRequest request);

    /** Loads full workout detail including exercises and sets. */
    WorkoutDetailResponse getWorkoutDetail(Long userId, Long workoutId);

    /** Lists workout history for the current user. */
    List<WorkoutHistoryItemResponse> listWorkoutHistory(Long userId);

    /** Adds one exercise into an in-progress workout. */
    WorkoutExerciseResponse addWorkoutExercise(Long userId, Long workoutId, AddWorkoutExerciseRequest request);

    /** Overwrites all sets of one workout exercise. */
    List<com.ironlogic.modules.workout.dto.WorkoutSetResponse> saveWorkoutSets(
            Long userId,
            Long workoutExerciseId,
            SaveWorkoutSetsRequest request
    );

    /** Completes one in-progress workout without triggering progression logic. */
    WorkoutDetailResponse finishWorkout(Long userId, Long workoutId);
}
