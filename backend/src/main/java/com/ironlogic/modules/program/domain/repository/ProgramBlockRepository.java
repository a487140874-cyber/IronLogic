package com.ironlogic.modules.program.domain.repository;

import com.ironlogic.modules.program.domain.model.ProgramBlock;
import java.util.List;
import java.util.Optional;

/**
 * Repository boundary for ProgramBlock access.
 */
public interface ProgramBlockRepository {

    /**
     * Persists a new block.
     *
     * @param block block to persist
     * @return persisted block
     */
    ProgramBlock save(ProgramBlock block);

    /**
     * Updates an existing block.
     *
     * @param block updated block
     * @return updated block
     */
    ProgramBlock update(ProgramBlock block);

    /**
     * Finds a block by id.
     *
     * @param id block id
     * @return optional block
     */
    Optional<ProgramBlock> findById(Long id);

    /**
     * Lists blocks under one program.
     *
     * @param programId parent program id
     * @return ordered block list
     */
    List<ProgramBlock> findByProgramId(Long programId);

    /**
     * Checks whether a sequence number already exists inside one program.
     *
     * @param programId parent program id
     * @param sequenceNo sequence number
     * @return true when duplicated
     */
    boolean existsByProgramIdAndSequenceNo(Long programId, Integer sequenceNo);

    /**
     * Checks whether a sequence number exists in the same program excluding one block.
     *
     * @param programId parent program id
     * @param sequenceNo sequence number
     * @param excludeId current block id to exclude during update
     * @return true when duplicated
     */
    boolean existsByProgramIdAndSequenceNoAndIdNot(Long programId, Integer sequenceNo, Long excludeId);
}
