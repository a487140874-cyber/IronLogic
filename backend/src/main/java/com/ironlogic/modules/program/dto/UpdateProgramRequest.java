package com.ironlogic.modules.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Request DTO for updating a Program.
 *
 * @param name updated program name
 * @param goalType updated goal type
 * @param status updated status
 * @param description updated description
 * @param startDate updated start date
 * @param endDate updated end date
 */
public record UpdateProgramRequest(
        /** Required updated program name. */
        @NotBlank
        @Size(max = 128)
        String name,

        /** Updated goal type. */
        @NotBlank
        @Size(max = 32)
        String goalType,

        /** Updated status. */
        @NotBlank
        @Size(max = 32)
        String status,

        /** Updated description. */
        String description,

        /** Updated planned start date. */
        LocalDate startDate,

        /** Updated planned end date. */
        LocalDate endDate
) {
}
