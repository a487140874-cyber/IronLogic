package com.ironlogic.modules.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a SessionTemplate.
 *
 * @param name template name
 * @param sequenceNo order inside one block
 * @param triggerMode trigger mode
 * @param notes optional notes
 * @param metadataJson optional metadata json
 */
public record CreateSessionTemplateRequest(
        /** Required template name. */
        @NotBlank
        @Size(max = 128)
        String name,

        /** Sequence number must be unique inside one block. */
        @NotNull
        Integer sequenceNo,

        /** Trigger mode such as SEQUENCE. */
        @NotBlank
        @Size(max = 32)
        String triggerMode,

        /** Optional notes shown in the template editor. */
        String notes,

        /** Optional metadata json. */
        String metadataJson
) {
}
