package com.ironlogic.modules.exercise.controller;

import com.ironlogic.common.api.ApiResponse;
import com.ironlogic.modules.exercise.application.ExerciseApplicationService;
import com.ironlogic.modules.exercise.dto.CreateExerciseRequest;
import com.ironlogic.modules.exercise.dto.ExerciseQueryRequest;
import com.ironlogic.modules.exercise.dto.ExerciseResponse;
import com.ironlogic.modules.exercise.dto.UpdateExerciseRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exercise 模块的 HTTP 入口。
 *
 * <p>Controller 故意保持轻量，只负责接收 HTTP 参数、触发参数校验、提供临时用户 id，
 * 再把真正的业务编排委托给 {@link ExerciseApplicationService}，从而让 application 层更易测试。
 */
@Validated
@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseApplicationService exerciseApplicationService;

    public ExerciseController(ExerciseApplicationService exerciseApplicationService) {
        this.exerciseApplicationService = exerciseApplicationService;
    }

    /**
     * 列出当前用户可见的 Exercise。
     *
     * @param request 可选过滤条件，例如名称
     * @return 系统 Exercise 与当前用户自定义 Exercise 列表
     */
    @GetMapping
    public ApiResponse<List<ExerciseResponse>> listExercises(@Valid @ModelAttribute ExerciseQueryRequest request) {
        return ApiResponse.success(exerciseApplicationService.listExercises(currentUserId(), request));
    }

    /**
     * 查询单个 Exercise 详情。
     *
     * @param id Exercise id
     * @return 当前用户可见的 Exercise 详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ExerciseResponse> getExercise(@PathVariable Long id) {
        return ApiResponse.success(exerciseApplicationService.getExercise(currentUserId(), id));
    }

    /**
     * 为当前用户创建一个自定义 Exercise。
     *
     * @param request 创建请求
     * @return 创建后的 Exercise
     */
    @PostMapping
    public ApiResponse<ExerciseResponse> createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        return ApiResponse.success(exerciseApplicationService.createCustomExercise(currentUserId(), request));
    }

    /**
     * 更新一个已有的自定义 Exercise。
     *
     * @param id Exercise id
     * @param request 更新请求
     * @return 更新后的 Exercise
     */
    @PutMapping("/{id}")
    public ApiResponse<ExerciseResponse> updateExercise(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExerciseRequest request
    ) {
        return ApiResponse.success(exerciseApplicationService.updateCustomExercise(currentUserId(), id, request));
    }

    /**
     * 提供 MVP 阶段的当前用户 id。
     *
     * <p>认证模块暂未实现，因此这里先使用固定 id，让 Exercise 模块能够先完整闭环。
     *
     * @return 临时当前用户 id
     */
    private Long currentUserId() {
        // TODO: 等 auth 模块提供真实登录上下文后，替换这里的固定用户 id。
        return 1L;
    }
}
