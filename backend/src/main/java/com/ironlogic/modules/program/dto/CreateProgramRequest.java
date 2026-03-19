package com.ironlogic.modules.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Request DTO for creating a Program.
 *
 * @param name program name shown to the user
 * @param goalType simple goal type string for MVP
 * @param status simple program status string
 * @param description optional description
 * @param startDate optional planned start date
 * @param endDate optional planned end date
 */
public record CreateProgramRequest(
        /** Required program name. */
        @NotBlank
        @Size(max = 128)
        String name,

        /** Goal type such as hypertrophy or strength. */
        @NotBlank
        @Size(max = 32)
        String goalType,

        /** Lightweight status such as draft or active. */
        @NotBlank
        @Size(max = 32)
        String status,

        /** Optional free-form description. */
        String description,

        /** Optional planned start date. */
        LocalDate startDate,

        /** Optional planned end date. */
        LocalDate endDate
) {
}
