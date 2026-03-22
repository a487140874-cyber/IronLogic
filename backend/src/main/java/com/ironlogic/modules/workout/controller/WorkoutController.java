package com.ironlogic.modules.workout.controller;

import com.ironlogic.common.api.ApiResponse;
import com.ironlogic.modules.workout.application.WorkoutApplicationService;
import com.ironlogic.modules.workout.dto.CreateManualWorkoutRequest;
import com.ironlogic.modules.workout.dto.WorkoutDetailResponse;
import com.ironlogic.modules.workout.dto.WorkoutHistoryItemResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP controller for WorkoutSession-oriented APIs.
 *
 * <p>This controller keeps session-level endpoints together because they all operate on the
 * lifecycle of one workout session: creation, detail, history, and finishing.
 */
@RestController
public class WorkoutController {

    private final WorkoutApplicationService workoutApplicationService;

    public WorkoutController(WorkoutApplicationService workoutApplicationService) {
        this.workoutApplicationService = workoutApplicationService;
    }

    /** Starts a workout from one accessible SessionTemplate. */
    @PostMapping("/api/workouts/from-template/{templateId}")
    public ApiResponse<WorkoutDetailResponse> createWorkoutFromTemplate(@PathVariable Long templateId) {
        return ApiResponse.success(workoutApplicationService.createWorkoutFromTemplate(currentUserId(), templateId));
    }

    /** Starts a manual free workout. */
    @PostMapping("/api/workouts/manual")
    public ApiResponse<WorkoutDetailResponse> createManualWorkout(
            @Valid @RequestBody(required = false) CreateManualWorkoutRequest request
    ) {
        CreateManualWorkoutRequest actualRequest = request == null ? new CreateManualWorkoutRequest(null) : request;
        return ApiResponse.success(workoutApplicationService.createManualWorkout(currentUserId(), actualRequest));
    }

    /** Loads full workout detail. */
    @GetMapping("/api/workouts/{id}")
    public ApiResponse<WorkoutDetailResponse> getWorkoutDetail(@PathVariable Long id) {
        return ApiResponse.success(workoutApplicationService.getWorkoutDetail(currentUserId(), id));
    }

    /** Lists workout history for the current user. */
    @GetMapping("/api/workouts/history")
    public ApiResponse<List<WorkoutHistoryItemResponse>> listWorkoutHistory() {
        return ApiResponse.success(workoutApplicationService.listWorkoutHistory(currentUserId()));
    }

    /** Finishes one in-progress workout. */
    @PostMapping("/api/workouts/{id}/finish")
    public ApiResponse<WorkoutDetailResponse> finishWorkout(@PathVariable Long id) {
        return ApiResponse.success(workoutApplicationService.finishWorkout(currentUserId(), id));
    }

    private Long currentUserId() {
        // TODO: replace fixed user id after auth module provides authenticated user context.
        return 1L;
    }
}
