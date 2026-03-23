package com.ironlogic.modules.exercise.application;

import com.ironlogic.modules.exercise.dto.CreateExerciseRequest;
import com.ironlogic.modules.exercise.dto.ExerciseQueryRequest;
import com.ironlogic.modules.exercise.dto.ExerciseResponse;
import com.ironlogic.modules.exercise.dto.UpdateExerciseRequest;
import java.util.List;

/**
 * Exercise 模块的应用服务接口。
 *
 * <p>这一层位于轻量 Controller 和持久化层之间，负责承接用户意图，并组织属于用例本身的
 * 校验逻辑，例如归属权、可见性以及 DTO 与领域对象之间的转换。
 */
public interface ExerciseApplicationService {

    /**
     * 为当前用户创建自定义 Exercise。
     *
     * @param userId 当前用户 id；MVP 阶段暂时由 Controller 提供固定值
     * @param request 创建请求
     * @return 创建后的响应 DTO
     */
    ExerciseResponse createCustomExercise(Long userId, CreateExerciseRequest request);

    /**
     * 更新一个已有的自定义 Exercise。
     *
     * @param userId 当前用户 id
     * @param exerciseId 目标 Exercise id
     * @param request 更新请求
     * @return 更新后的响应 DTO
     * @throws com.ironlogic.common.exception.NotFoundException 目标不存在时抛出
     * @throws com.ironlogic.common.exception.ForbiddenException 目标是系统 Exercise
     *         或不属于当前用户时抛出
     */
    ExerciseResponse updateCustomExercise(Long userId, Long exerciseId, UpdateExerciseRequest request);

    /**
     * 查询当前用户可见的单个 Exercise。
     *
     * @param userId 当前用户 id
     * @param exerciseId 目标 Exercise id
     * @return Exercise 详情
     * @throws com.ironlogic.common.exception.NotFoundException 目标不存在或不可见时抛出
     */
    ExerciseResponse getExercise(Long userId, Long exerciseId);

    /**
     * 列出当前用户可见的 Exercise。
     *
     * <p>结果需要同时包含系统 Exercise 和当前用户自己的自定义 Exercise。
     *
     * @param userId 当前用户 id
     * @param request 可选查询条件
     * @return 可见 Exercise 列表
     */
    List<ExerciseResponse> listExercises(Long userId, ExerciseQueryRequest request);
}
