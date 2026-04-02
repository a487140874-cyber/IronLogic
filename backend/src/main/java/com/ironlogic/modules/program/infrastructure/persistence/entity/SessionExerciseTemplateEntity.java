package com.ironlogic.modules.program.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ironlogic.common.persistence.typehandler.JsonbStringTypeHandler;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Persistence entity mapped to the {@code session_exercise_templates} table.
 */
@TableName(value = "session_exercise_templates", autoResultMap = true)
public class SessionExerciseTemplateEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("session_template_id")
    private Long sessionTemplateId;

    @TableField("exercise_id")
    private Long exerciseId;

    @TableField("order_no")
    private Integer orderNo;

    @TableField("target_sets")
    private Integer targetSets;

    @TableField("target_reps")
    private Integer targetReps;

    @TableField("target_weight")
    private BigDecimal targetWeight;

    @TableField("target_weight_unit")
    private String targetWeightUnit;

    @TableField("rest_seconds")
    private Integer restSeconds;

    @TableField("intensity_mode")
    private String intensityMode;

    @TableField("progression_rule_id")
    private Long progressionRuleId;

    @TableField(value = "prescription_json", typeHandler = JsonbStringTypeHandler.class)
    private String prescriptionJson;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSessionTemplateId() { return sessionTemplateId; }
    public void setSessionTemplateId(Long sessionTemplateId) { this.sessionTemplateId = sessionTemplateId; }
    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public Integer getOrderNo() { return orderNo; }
    public void setOrderNo(Integer orderNo) { this.orderNo = orderNo; }
    public Integer getTargetSets() { return targetSets; }
    public void setTargetSets(Integer targetSets) { this.targetSets = targetSets; }
    public Integer getTargetReps() { return targetReps; }
    public void setTargetReps(Integer targetReps) { this.targetReps = targetReps; }
    public BigDecimal getTargetWeight() { return targetWeight; }
    public void setTargetWeight(BigDecimal targetWeight) { this.targetWeight = targetWeight; }
    public String getTargetWeightUnit() { return targetWeightUnit; }
    public void setTargetWeightUnit(String targetWeightUnit) { this.targetWeightUnit = targetWeightUnit; }
    public Integer getRestSeconds() { return restSeconds; }
    public void setRestSeconds(Integer restSeconds) { this.restSeconds = restSeconds; }
    public String getIntensityMode() { return intensityMode; }
    public void setIntensityMode(String intensityMode) { this.intensityMode = intensityMode; }
    public Long getProgressionRuleId() { return progressionRuleId; }
    public void setProgressionRuleId(Long progressionRuleId) { this.progressionRuleId = progressionRuleId; }
    public String getPrescriptionJson() { return prescriptionJson; }
    public void setPrescriptionJson(String prescriptionJson) { this.prescriptionJson = prescriptionJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
