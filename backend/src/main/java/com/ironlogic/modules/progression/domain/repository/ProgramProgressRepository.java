package com.ironlogic.modules.progression.domain.repository;

import com.ironlogic.modules.progression.domain.model.ProgramProgress;
import java.util.Optional;

/**
 * ProgramProgress 的仓储边界。
 *
 * <p>progression 模块依赖这个接口，而不是直接依赖 MyBatis-Plus，
 * 这样业务逻辑可以和基础设施细节解耦，也更方便单元测试。
 */
public interface ProgramProgressRepository {

    /**
     * 持久化一条新的 ProgramProgress。
     *
     * @param programProgress 待持久化的推进状态
     * @return 持久化后的推进状态，包含生成 id
     */
    ProgramProgress save(ProgramProgress programProgress);

    /**
     * 持久化一条已有 ProgramProgress 的更新。
     *
     * @param programProgress 更新后的推进状态
     * @return 更新后的推进状态
     */
    ProgramProgress update(ProgramProgress programProgress);

    /**
     * 按用户和 Program 查询推进状态。
     *
     * @param userId 所有者用户 id
     * @param programId 目标 Program id
     * @return 查询结果
     */
    Optional<ProgramProgress> findByUserIdAndProgramId(Long userId, Long programId);
}
