package com.ironlogic.modules.program.domain.repository;

import com.ironlogic.modules.program.domain.model.SessionTemplate;
import java.util.List;
import java.util.Optional;

/**
 * Repository boundary for SessionTemplate access.
 */
public interface SessionTemplateRepository {

    /**
     * Persists a new session template.
     *
     * @param template template to persist
     * @return persisted template
     */
    SessionTemplate save(SessionTemplate template);

    /**
     * Updates an existing session template.
     *
     * @param template updated template
     * @return updated template
     */
    SessionTemplate update(SessionTemplate template);

    /**
     * Finds a session template by id.
     *
     * @param id template id
     * @return optional template
     */
    Optional<SessionTemplate> findById(Long id);

    /**
     * Lists session templates under one block.
     *
     * @param blockId parent block id
     * @return ordered template list
     */
    List<SessionTemplate> findByBlockId(Long blockId);

    /**
     * Checks whether sequence number already exists under one block.
     *
     * @param blockId parent block id
     * @param sequenceNo sequence number
     * @return true when duplicated
     */
    boolean existsByBlockIdAndSequenceNo(Long blockId, Integer sequenceNo);

    /**
     * Checks whether sequence number already exists under one block excluding current template.
     *
     * @param blockId parent block id
     * @param sequenceNo sequence number
     * @param excludeId current template id
     * @return true when duplicated
     */
    boolean existsByBlockIdAndSequenceNoAndIdNot(Long blockId, Integer sequenceNo, Long excludeId);
}
