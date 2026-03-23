package com.ironlogic.modules.progression.application;

import com.ironlogic.modules.progression.dto.CurrentRecommendationResponse;
import com.ironlogic.modules.progression.dto.ProgramProgressResponse;

/**
 * progression 模块的应用服务接口。
 *
 * <p>这一层负责“基于序列的推荐状态”。训练执行仍属于 workout 模块，
 * progression 只在训练已经完成之后响应并更新推进状态。
 */
public interface ProgressionApplicationService {

    /**
     * 返回某个 Program 当前推荐的 SessionTemplate。
     *
     * @param userId 当前用户 id
     * @param programId 目标 Program id
     * @return 当前推荐结果
     */
    CurrentRecommendationResponse getCurrentRecommendation(Long userId, Long programId);

    /**
     * 返回某个 Program 已持久化的 ProgramProgress。
     *
     * @param userId 当前用户 id
     * @param programId 目标 Program id
     * @return 已持久化的推进状态；如果尚未初始化则返回 {@code null}
     */
    ProgramProgressResponse getProgramProgress(Long userId, Long programId);

    /**
     * 在模板训练完成后推进 ProgramProgress。
     *
     * @param userId 当前用户 id
     * @param workoutId 已完成训练 id
     * @param programId 已完成训练的来源 Program id
     * @param blockId 已完成训练的来源 Block id
     * @param templateId 已完成训练的来源 SessionTemplate id
     */
    void advanceProgramProgressAfterWorkoutCompletion(
            Long userId,
            Long workoutId,
            Long programId,
            Long blockId,
            Long templateId
    );
}
