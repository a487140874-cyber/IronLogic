package com.ironlogic.modules.program.domain.model;

import java.time.LocalDateTime;

/**
 * Domain model for a ProgramBlock.
 *
 * <p>A ProgramBlock belongs to one Program and orders larger phases such as hypertrophy or
 * strength blocks. Sequence number is important because IronLogic prefers sequence-driven
 * progression over calendar-only scheduling.
 *
 * @param id block id
 * @param programId parent program id
 * @param name block name
 * @param blockType block type string
 * @param sequenceNo sequence within one program
 * @param durationMode duration mode string
 * @param durationValue optional duration value
 * @param deloadEnabled whether the block enables deload behavior
 * @param metadataJson extensible metadata json
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record ProgramBlock(
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
