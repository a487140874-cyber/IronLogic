package com.ironlogic.modules.program.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain model for a training Program.
 *
 * <p>A Program is the top-level template object owned by one user. It groups multiple
 * ProgramBlocks and represents a defined training plan rather than a workout execution record.
 *
 * @param id program id
 * @param userId owner user id
 * @param name program name
 * @param goalType simple goal type string for MVP
 * @param status simple status string for MVP
 * @param description optional description
 * @param startDate optional planned start date
 * @param endDate optional planned end date
 * @param createdAt creation time
 * @param updatedAt last update time
 */
public record Program(
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
