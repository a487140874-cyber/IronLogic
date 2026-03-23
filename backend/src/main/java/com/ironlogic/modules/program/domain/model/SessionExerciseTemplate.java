package com.ironlogic.modules.program.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SessionExerciseTemplate 的领域模型。
 *
 * <p>这个对象把 SessionTemplate 和 Exercise 关联起来，并保存该位置上的模板处方，
 * 例如目标组数、次数、重量和休息时间。
 *
 * @param id 模板动作 id
 * @param sessionTemplateId 所属 SessionTemplate id
 * @param exerciseId 引用的 Exercise id
 * @param orderNo 在 SessionTemplate 内的展示顺序
 * @param targetSets 计划组数
 * @param targetReps 可选目标次数
 * @param targetWeight 可选目标重量
 * @param targetWeightUnit 可选重量单位
 * @param restSeconds 可选休息秒数
 * @param intensityMode 可选强度模式
 * @param progressionRuleId 预留给未来 progression rule 的 id
 * @param prescriptionJson 预留处方 JSON
 * @param createdAt 创建时间
 * @param updatedAt 更新时间
 */
public record SessionExerciseTemplate(
        Long id,
        Long sessionTemplateId,
        Long exerciseId,
        Integer orderNo,
        Integer targetSets,
        Integer targetReps,
        BigDecimal targetWeight,
        String targetWeightUnit,
        Integer restSeconds,
        String intensityMode,
        Long progressionRuleId,
        String prescriptionJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
