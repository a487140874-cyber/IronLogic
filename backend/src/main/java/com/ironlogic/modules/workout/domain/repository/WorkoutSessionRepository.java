package com.ironlogic.modules.workout.domain.repository;

import com.ironlogic.modules.workout.domain.model.WorkoutSession;
import java.util.List;
import java.util.Optional;

/**
 * WorkoutSession 的仓储边界。
 */
public interface WorkoutSessionRepository {

    /**
     * 持久化一个新建的 WorkoutSession。
     *
     * @param workoutSession 待持久化的 WorkoutSession
     * @return 持久化后的 WorkoutSession
     */
    WorkoutSession save(WorkoutSession workoutSession);

    /**
     * 持久化一个已有 WorkoutSession 的更新。
     *
     * @param workoutSession 更新后的 WorkoutSession
     * @return 更新后的 WorkoutSession
     */
    WorkoutSession update(WorkoutSession workoutSession);

    /**
     * 按 id 查询 WorkoutSession。
     *
     * @param id WorkoutSession id
     * @return 查询结果
     */
    Optional<WorkoutSession> findById(Long id);

    /**
     * 按 id 和所有者查询 WorkoutSession。
     *
     * @param id WorkoutSession id
     * @param userId 当前用户 id
     * @return 查询结果
     */
    Optional<WorkoutSession> findByIdAndUserId(Long id, Long userId);

    /**
     * 列出当前用户的训练历史。
     *
     * @param userId 当前用户 id
     * @return 排序后的 WorkoutSession 列表
     */
    List<WorkoutSession> findByUserId(Long userId);
}
