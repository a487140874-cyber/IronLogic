package com.ironlogic.modules.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a ProgramBlock.
 *
 * @param name block name
 * @param blockType block type
 * @param sequenceNo block order inside one program
 * @param durationMode duration mode
 * @param durationValue optional duration value
 * @param deloadEnabled whether deload is enabled
 * @param metadataJson extensible metadata json
 */
public record CreateProgramBlockRequest(
        /** Block name shown in program editor. */
        @NotBlank
        @Size(max = 128)
        String name,

        /** Lightweight block type string. */
        @NotBlank
        @Size(max = 32)
        String blockType,

        /** Sequence number must be unique inside one program. */
        @NotNull
        Integer sequenceNo,

        /** Duration mode such as weeks or sessions. */
        @NotBlank
        @Size(max = 32)
        String durationMode,

        /** Optional duration value corresponding to duration mode. */
        Integer durationValue,

        /** Whether the block enables deload. */
        Boolean deloadEnabled,

        /** Optional metadata json for future extension. */
        String metadataJson
) {
}
