package com.ironlogic.modules.workout.controller;

import com.ironlogic.common.api.ApiResponse;
import com.ironlogic.modules.workout.application.WorkoutApplicationService;
import com.ironlogic.modules.workout.dto.AddWorkoutExerciseRequest;
import com.ironlogic.modules.workout.dto.SaveWorkoutSetsRequest;
import com.ironlogic.modules.workout.dto.WorkoutExerciseResponse;
import com.ironlogic.modules.workout.dto.WorkoutSetResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP controller for workout exercise mutation APIs.
 *
 * <p>This controller is split from {@link WorkoutController} because adding exercises and
 * replacing set lists are exercise-level write operations rather than session-lifecycle actions.
 */
@RestController
public class WorkoutExerciseController {

    private final WorkoutApplicationService workoutApplicationService;

    public WorkoutExerciseController(WorkoutApplicationService workoutApplicationService) {
        this.workoutApplicationService = workoutApplicationService;
    }

    /** Adds one exercise into an in-progress workout. */
    @PostMapping("/api/workouts/{id}/exercises")
    public ApiResponse<WorkoutExerciseResponse> addWorkoutExercise(
            @PathVariable Long id,
            @Valid @RequestBody AddWorkoutExerciseRequest request
    ) {
        return ApiResponse.success(workoutApplicationService.addWorkoutExercise(currentUserId(), id, request));
    }

    /** Overwrites all sets of one workout exercise. */
    @PutMapping("/api/workout-exercises/{id}/sets")
    public ApiResponse<List<WorkoutSetResponse>> saveWorkoutSets(
            @PathVariable Long id,
            @Valid @RequestBody SaveWorkoutSetsRequest request
    ) {
        return ApiResponse.success(workoutApplicationService.saveWorkoutSets(currentUserId(), id, request));
    }

    private Long currentUserId() {
        // TODO: replace fixed user id after auth module provides authenticated user context.
        return 1L;
    }
}
