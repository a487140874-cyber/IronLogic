package com.ironlogic.modules.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating a ProgramBlock.
 *
 * @param name updated block name
 * @param blockType updated block type
 * @param sequenceNo updated sequence number
 * @param durationMode updated duration mode
 * @param durationValue updated duration value
 * @param deloadEnabled updated deload flag
 * @param metadataJson updated metadata json
 */
public record UpdateProgramBlockRequest(
        /** Updated block name. */
        @NotBlank
        @Size(max = 128)
        String name,

        /** Updated block type. */
        @NotBlank
        @Size(max = 32)
        String blockType,

        /** Updated unique sequence inside the parent program. */
        @NotNull
        Integer sequenceNo,

        /** Updated duration mode. */
        @NotBlank
        @Size(max = 32)
        String durationMode,

        /** Updated duration value. */
        Integer durationValue,

        /** Updated deload flag. */
        Boolean deloadEnabled,

        /** Updated metadata json. */
        String metadataJson
) {
}
