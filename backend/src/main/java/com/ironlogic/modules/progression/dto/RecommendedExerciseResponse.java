package com.ironlogic.modules.progression.dto;

import java.math.BigDecimal;

/**
 * 推荐模板中单个 SessionExerciseTemplate 的 DTO。
 *
 * <p>这个响应只暴露模板目标值，不计算真实训练负荷，因为 progression v1 只解决序列推荐，
 * 不解决重量、次数或 RPE 建议。
 *
 * @param id 模板动作 id
 * @param exerciseId 引用的 Exercise id
 * @param exerciseName 引用 Exercise 的展示名称
 * @param orderNo 在推荐模板中的顺序
 * @param targetSets 计划组数
 * @param targetReps 模板目标次数
 * @param targetWeight 模板目标重量
 * @param targetWeightUnit 模板重量单位
 * @param restSeconds 模板休息秒数
 * @param intensityMode 模板强度模式
 * @param prescriptionJson 预留处方 JSON
 */
public record RecommendedExerciseResponse(
        Long id,
        Long exerciseId,
        String exerciseName,
        Integer orderNo,
        Integer targetSets,
        Integer targetReps,
        BigDecimal targetWeight,
        String targetWeightUnit,
        Integer restSeconds,
        String intensityMode,
        String prescriptionJson
) {
}
