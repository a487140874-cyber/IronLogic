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
 * WorkoutExercise 写操作的 HTTP Controller。
 *
 * <p>它和 {@link WorkoutController} 分开，是因为“加动作”和“改组列表”属于动作级写入，
 * 而不是一次训练生命周期本身的状态流转。
 */
@RestController
public class WorkoutExerciseController {

    private final WorkoutApplicationService workoutApplicationService;

    public WorkoutExerciseController(WorkoutApplicationService workoutApplicationService) {
        this.workoutApplicationService = workoutApplicationService;
    }

    /** 向进行中的训练中新增一个动作。 */
    @PostMapping("/api/workouts/{id}/exercises")
    public ApiResponse<WorkoutExerciseResponse> addWorkoutExercise(
            @PathVariable Long id,
            @Valid @RequestBody AddWorkoutExerciseRequest request
    ) {
        return ApiResponse.success(workoutApplicationService.addWorkoutExercise(currentUserId(), id, request));
    }

    /** 覆盖保存某个训练动作下的所有组。 */
    @PutMapping("/api/workout-exercises/{id}/sets")
    public ApiResponse<List<WorkoutSetResponse>> saveWorkoutSets(
            @PathVariable Long id,
            @Valid @RequestBody SaveWorkoutSetsRequest request
    ) {
        return ApiResponse.success(workoutApplicationService.saveWorkoutSets(currentUserId(), id, request));
    }

    private Long currentUserId() {
        // TODO: 等 auth 模块提供真实登录上下文后，替换这里的固定用户 id。
        return 1L;
    }
}
