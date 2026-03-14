package com.ironlogic.modules.exercise.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * Persistence entity mapped to the {@code exercises} table.
 *
 * <p>This class lives in infrastructure because it is shaped for database mapping rather than
 * for expressing business intent. The application layer works with the domain model instead,
 * and repository code translates between the two forms.
 */
@TableName("exercises")
public class ExerciseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("owner_user_id")
    private Long ownerUserId;

    private String name;

    private String category;

    @TableField("primary_muscle")
    private String primaryMuscle;

    @TableField("secondary_muscles_json")
    private String secondaryMusclesJson;

    @TableField("equipment_type")
    private String equipmentType;

    @TableField("movement_pattern")
    private String movementPattern;

    @TableField("is_custom")
    private Boolean isCustom;

    @TableField("metadata_json")
    private String metadataJson;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * Returns primary key.
     *
     * @return exercise id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets primary key.
     *
     * @param id exercise id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrimaryMuscle() {
        return primaryMuscle;
    }

    public void setPrimaryMuscle(String primaryMuscle) {
        this.primaryMuscle = primaryMuscle;
    }

    public String getSecondaryMusclesJson() {
        return secondaryMusclesJson;
    }

    public void setSecondaryMusclesJson(String secondaryMusclesJson) {
        this.secondaryMusclesJson = secondaryMusclesJson;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getMovementPattern() {
        return movementPattern;
    }

    public void setMovementPattern(String movementPattern) {
        this.movementPattern = movementPattern;
    }

    public Boolean getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Boolean custom) {
        isCustom = custom;
    }

    public String getMetadataJson() {
        return metadataJson;
    }

    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
