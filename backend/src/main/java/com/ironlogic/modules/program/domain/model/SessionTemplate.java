package com.ironlogic.modules.program.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model for a SessionTemplate.
 *
 * <p>A SessionTemplate belongs to one ProgramBlock and defines a reusable training day
 * template, such as Push Day or Legs Day.
 *
 * @param id session template id
 * @param blockId parent block id
 * @param name template name
 * @param sequenceNo order within a block
 * @param triggerMode trigger mode string
 * @param notes optional notes
 * @param metadataJson extensible metadata json
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record SessionTemplate(
        Long id,
        Long blockId,
        String name,
        Integer sequenceNo,
        String triggerMode,
        String notes,
        String metadataJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
