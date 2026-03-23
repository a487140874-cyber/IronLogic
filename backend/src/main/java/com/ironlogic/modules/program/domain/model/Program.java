package com.ironlogic.modules.program.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 训练 Program 的领域模型。
 *
 * <p>Program 是模板层的顶层对象，归属于某个用户，可以包含多个 ProgramBlock。
 * 它表示“训练计划定义”，而不是一次实际执行的训练记录。
 *
 * @param id Program id
 * @param userId 所有者用户 id
 * @param name Program 名称
 * @param goalType MVP 阶段使用的目标类型
 * @param status MVP 阶段使用的状态
 * @param description 可选描述
 * @param startDate 可选计划开始日期
 * @param endDate 可选计划结束日期
 * @param createdAt 创建时间
 * @param updatedAt 更新时间
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
