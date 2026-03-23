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
 * WorkoutSession 维度的 HTTP Controller。
 *
 * <p>这里集中处理一次训练生命周期相关的接口：开始、详情、历史、完成。
 */
@RestController
public class WorkoutController {

    private final WorkoutApplicationService workoutApplicationService;

    public WorkoutController(WorkoutApplicationService workoutApplicationService) {
        this.workoutApplicationService = workoutApplicationService;
    }

    /** 基于可访问的 SessionTemplate 开始训练。 */
    @PostMapping("/api/workouts/from-template/{templateId}")
    public ApiResponse<WorkoutDetailResponse> createWorkoutFromTemplate(@PathVariable Long templateId) {
        return ApiResponse.success(workoutApplicationService.createWorkoutFromTemplate(currentUserId(), templateId));
    }

    /** 开始一次手动自由训练。 */
    @PostMapping("/api/workouts/manual")
    public ApiResponse<WorkoutDetailResponse> createManualWorkout(
            @Valid @RequestBody(required = false) CreateManualWorkoutRequest request
    ) {
        CreateManualWorkoutRequest actualRequest = request == null ? new CreateManualWorkoutRequest(null) : request;
        return ApiResponse.success(workoutApplicationService.createManualWorkout(currentUserId(), actualRequest));
    }

    /** 查询完整训练详情。 */
    @GetMapping("/api/workouts/{id}")
    public ApiResponse<WorkoutDetailResponse> getWorkoutDetail(@PathVariable Long id) {
        return ApiResponse.success(workoutApplicationService.getWorkoutDetail(currentUserId(), id));
    }

    /** 列出当前用户的训练历史。 */
    @GetMapping("/api/workouts/history")
    public ApiResponse<List<WorkoutHistoryItemResponse>> listWorkoutHistory() {
        return ApiResponse.success(workoutApplicationService.listWorkoutHistory(currentUserId()));
    }

    /** 完成一次进行中的训练。 */
    @PostMapping("/api/workouts/{id}/finish")
    public ApiResponse<WorkoutDetailResponse> finishWorkout(@PathVariable Long id) {
        return ApiResponse.success(workoutApplicationService.finishWorkout(currentUserId(), id));
    }

    private Long currentUserId() {
        // TODO: 等 auth 模块提供真实登录上下文后，替换这里的固定用户 id。
        return 1L;
    }
}
