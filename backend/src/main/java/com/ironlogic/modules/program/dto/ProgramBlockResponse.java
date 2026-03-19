package com.ironlogic.modules.program.dto;

import java.time.LocalDateTime;

/**
 * Response DTO for ProgramBlock APIs.
 *
 * @param id block id
 * @param programId parent program id
 * @param name block name
 * @param blockType block type
 * @param sequenceNo sequence number inside program
 * @param durationMode duration mode
 * @param durationValue duration value
 * @param deloadEnabled deload flag
 * @param metadataJson metadata json
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record ProgramBlockResponse(
        Long id,
        Long programId,
        String name,
        String blockType,
        Integer sequenceNo,
        String durationMode,
        Integer durationValue,
        Boolean deloadEnabled,
        String metadataJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
