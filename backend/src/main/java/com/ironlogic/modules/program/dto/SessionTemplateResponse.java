package com.ironlogic.modules.program.dto;

import java.time.LocalDateTime;

/**
 * Response DTO for SessionTemplate APIs.
 *
 * @param id session template id
 * @param blockId parent block id
 * @param name template name
 * @param sequenceNo order inside block
 * @param triggerMode trigger mode
 * @param notes notes
 * @param metadataJson metadata json
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record SessionTemplateResponse(
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
