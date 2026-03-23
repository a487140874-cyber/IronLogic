package com.ironlogic.modules.program.domain.repository;

import com.ironlogic.modules.program.domain.model.SessionTemplate;
import java.util.List;
import java.util.Optional;

/**
 * SessionTemplate 的仓储边界。
 */
public interface SessionTemplateRepository {

    /**
     * 持久化一个新建的 SessionTemplate。
     *
     * @param template 待持久化的模板
     * @return 持久化后的模板
     */
    SessionTemplate save(SessionTemplate template);

    /**
     * 持久化一个已有 SessionTemplate 的更新。
     *
     * @param template 更新后的模板
     * @return 更新后的模板
     */
    SessionTemplate update(SessionTemplate template);

    /**
     * 按 id 查询 SessionTemplate。
     *
     * @param id SessionTemplate id
     * @return 查询结果
     */
    Optional<SessionTemplate> findById(Long id);

    /**
     * 列出某个 Block 下的 SessionTemplate。
     *
     * @param blockId 所属 Block id
     * @return 排序后的模板列表
     */
    List<SessionTemplate> findByBlockId(Long blockId);

    /**
     * 判断某个 Block 下是否已存在指定 sequenceNo。
     *
     * @param blockId 所属 Block id
     * @param sequenceNo 顺序号
     * @return 已存在时返回 true
     */
    boolean existsByBlockIdAndSequenceNo(Long blockId, Integer sequenceNo);

    /**
     * 判断某个 Block 下是否已存在指定 sequenceNo，但排除当前模板自身。
     *
     * @param blockId 所属 Block id
     * @param sequenceNo 顺序号
     * @param excludeId 更新时需要排除的当前模板 id
     * @return 已存在时返回 true
     */
    boolean existsByBlockIdAndSequenceNoAndIdNot(Long blockId, Integer sequenceNo, Long excludeId);
}
