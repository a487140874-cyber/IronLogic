package com.ironlogic.modules.program.domain.repository;

import com.ironlogic.modules.program.domain.model.ProgramBlock;
import java.util.List;
import java.util.Optional;

/**
 * ProgramBlock 的仓储边界。
 */
public interface ProgramBlockRepository {

    /**
     * 持久化一个新建的 ProgramBlock。
     *
     * @param block 待持久化的 Block
     * @return 持久化后的 Block
     */
    ProgramBlock save(ProgramBlock block);

    /**
     * 持久化一个已有 ProgramBlock 的更新。
     *
     * @param block 更新后的 Block
     * @return 更新后的 Block
     */
    ProgramBlock update(ProgramBlock block);

    /**
     * 按 id 查询 ProgramBlock。
     *
     * @param id Block id
     * @return 查询结果
     */
    Optional<ProgramBlock> findById(Long id);

    /**
     * 列出某个 Program 下的 ProgramBlock。
     *
     * @param programId 所属 Program id
     * @return 排序后的 Block 列表
     */
    List<ProgramBlock> findByProgramId(Long programId);

    /**
     * 判断某个 Program 内是否已存在指定 sequenceNo。
     *
     * @param programId 所属 Program id
     * @param sequenceNo 顺序号
     * @return 已存在时返回 true
     */
    boolean existsByProgramIdAndSequenceNo(Long programId, Integer sequenceNo);

    /**
     * 判断同一个 Program 内是否存在指定 sequenceNo，但排除当前 Block 自身。
     *
     * @param programId 所属 Program id
     * @param sequenceNo 顺序号
     * @param excludeId 更新时需要排除的当前 Block id
     * @return 已存在时返回 true
     */
    boolean existsByProgramIdAndSequenceNoAndIdNot(Long programId, Integer sequenceNo, Long excludeId);
}
