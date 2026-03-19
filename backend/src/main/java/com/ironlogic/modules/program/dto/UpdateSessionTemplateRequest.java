package com.ironlogic.modules.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating a SessionTemplate.
 *
 * @param name updated template name
 * @param sequenceNo updated order inside one block
 * @param triggerMode updated trigger mode
 * @param notes updated notes
 * @param metadataJson updated metadata json
 */
public record UpdateSessionTemplateRequest(
        /** Updated template name. */
        @NotBlank
        @Size(max = 128)
        String name,

        /** Updated unique sequence inside one block. */
        @NotNull
        Integer sequenceNo,

        /** Updated trigger mode. */
        @NotBlank
        @Size(max = 32)
        String triggerMode,

        /** Updated notes. */
        String notes,

        /** Updated metadata json. */
        String metadataJson
) {
}
