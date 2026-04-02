package com.ironlogic.modules.program.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ironlogic.common.persistence.typehandler.JsonbStringTypeHandler;
import java.time.LocalDateTime;

/**
 * Persistence entity mapped to the {@code program_blocks} table.
 */
@TableName(value = "program_blocks", autoResultMap = true)
public class ProgramBlockEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("program_id")
    private Long programId;

    private String name;

    @TableField("block_type")
    private String blockType;

    @TableField("sequence_no")
    private Integer sequenceNo;

    @TableField("duration_mode")
    private String durationMode;

    @TableField("duration_value")
    private Integer durationValue;

    @TableField("deload_enabled")
    private Boolean deloadEnabled;

    @TableField(value = "metadata_json", typeHandler = JsonbStringTypeHandler.class)
    private String metadataJson;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBlockType() { return blockType; }
    public void setBlockType(String blockType) { this.blockType = blockType; }
    public Integer getSequenceNo() { return sequenceNo; }
    public void setSequenceNo(Integer sequenceNo) { this.sequenceNo = sequenceNo; }
    public String getDurationMode() { return durationMode; }
    public void setDurationMode(String durationMode) { this.durationMode = durationMode; }
    public Integer getDurationValue() { return durationValue; }
    public void setDurationValue(Integer durationValue) { this.durationValue = durationValue; }
    public Boolean getDeloadEnabled() { return deloadEnabled; }
    public void setDeloadEnabled(Boolean deloadEnabled) { this.deloadEnabled = deloadEnabled; }
    public String getMetadataJson() { return metadataJson; }
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
