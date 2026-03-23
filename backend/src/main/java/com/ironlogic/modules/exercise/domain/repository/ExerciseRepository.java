package com.ironlogic.modules.exercise.domain.repository;

import com.ironlogic.modules.exercise.domain.model.Exercise;
import java.util.List;
import java.util.Optional;

/**
 * Exercise 聚合的仓储边界。
 *
 * <p>application 层依赖这个抽象，而不是直接依赖 MyBatis-Plus。这样可以让用例代码不和
 * 基础设施实现强耦合，单测时也能直接使用 mock。
 */
public interface ExerciseRepository {

    /**
     * 持久化一个新建的 Exercise。
     *
     * @param exercise 待持久化的领域对象
     * @return 持久化后的 Exercise，包含生成 id
     */
    Exercise save(Exercise exercise);

    /**
     * 持久化一个已有 Exercise 的更新。
     *
     * @param exercise 更新后的 Exercise
     * @return 更新后的 Exercise
     */
    Exercise update(Exercise exercise);

    /**
     * 按 id 查询 Exercise，不附带可见性过滤。
     *
     * @param id Exercise id
     * @return 查询结果
     */
    Optional<Exercise> findById(Long id);

    /**
     * 仅在当前用户可见时查询 Exercise。
     *
     * @param userId 当前用户 id
     * @param id Exercise id
     * @return 可见时返回结果
     */
    Optional<Exercise> findVisibleById(Long userId, Long id);

    /**
     * 列出当前用户可见的 Exercise。
     *
     * @param userId 当前用户 id
     * @param name 可选名称过滤
     * @param category 可选分类过滤
     * @param equipmentType 可选器械过滤
     * @return 可见 Exercise 列表
     */
    List<Exercise> findVisible(Long userId, String name, String category, String equipmentType);
}
