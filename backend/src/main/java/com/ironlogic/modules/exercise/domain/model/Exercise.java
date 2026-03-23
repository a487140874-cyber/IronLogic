package com.ironlogic.modules.exercise.domain.model;

import java.time.LocalDateTime;

/**
 * Exercise 的领域对象。
 *
 * <p>当前项目刻意保持领域模型轻量，但仍然把它和持久化实体分开。这样 application 层依赖的是
 * 业务意义上的对象，而不是直接依赖 MyBatis-Plus 注解，未来规则变化也更容易局部调整。
 *
 * @param id Exercise id
 * @param ownerUserId {@code null} 表示系统 Exercise；非空表示属于某个用户
 * @param name Exercise 展示名称
 * @param category MVP 阶段使用的轻量分类
 * @param primaryMuscle 主要训练肌群
 * @param secondaryMusclesJson 次要肌群 JSON
 * @param equipmentType 器械类型
 * @param movementPattern 动作模式
 * @param isCustom 是否为用户创建的自定义 Exercise
 * @param metadataJson 预留给后续扩展的元数据 JSON
 * @param createdAt 创建时间
 * @param updatedAt 更新时间
 */
public record Exercise(
        Long id,
        Long ownerUserId,
        String name,
        String category,
        String primaryMuscle,
        String secondaryMusclesJson,
        String equipmentType,
        String movementPattern,
        Boolean isCustom,
        String metadataJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
