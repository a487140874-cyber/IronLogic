package com.ironlogic.modules.program.domain.repository;

import com.ironlogic.modules.program.domain.model.Program;
import java.util.List;
import java.util.Optional;

/**
 * Program 的仓储边界。
 */
public interface ProgramRepository {

    /**
     * 持久化一个新建的 Program。
     *
     * @param program 待持久化的 Program
     * @return 持久化后的 Program
     */
    Program save(Program program);

    /**
     * 持久化一个已有 Program 的更新。
     *
     * @param program 更新后的 Program
     * @return 更新后的 Program
     */
    Program update(Program program);

    /**
     * 按 id 查询 Program。
     *
     * @param id Program id
     * @return 查询结果
     */
    Optional<Program> findById(Long id);

    /**
     * 按 id 和所有者查询 Program。
     *
     * @param id Program id
     * @param userId 所有者用户 id
     * @return 查询结果
     */
    Optional<Program> findByIdAndUserId(Long id, Long userId);

    /**
     * 列出某个用户的 Program。
     *
     * @param userId 所有者用户 id
     * @return Program 列表
     */
    List<Program> findByUserId(Long userId);
}
