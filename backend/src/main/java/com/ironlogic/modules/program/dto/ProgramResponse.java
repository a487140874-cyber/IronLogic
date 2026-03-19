package com.ironlogic.modules.program.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for Program APIs.
 *
 * @param id program id
 * @param userId owner user id
 * @param name program name
 * @param goalType goal type
 * @param status status
 * @param description description
 * @param startDate planned start date
 * @param endDate planned end date
 * @param createdAt creation time
 * @param updatedAt update time
 */
public record ProgramResponse(
        Long id,
        Long userId,
        String name,
        String goalType,
        String status,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
