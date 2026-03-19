package com.ironlogic.modules.program.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * Persistence entity mapped to the {@code session_templates} table.
 */
@TableName("session_templates")
public class SessionTemplateEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("block_id")
    private Long blockId;

    private String name;

    @TableField("sequence_no")
    private Integer sequenceNo;

    @TableField("trigger_mode")
    private String triggerMode;

    private String notes;

    @TableField("metadata_json")
    private String metadataJson;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBlockId() { return blockId; }
    public void setBlockId(Long blockId) { this.blockId = blockId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSequenceNo() { return sequenceNo; }
    public void setSequenceNo(Integer sequenceNo) { this.sequenceNo = sequenceNo; }
    public String getTriggerMode() { return triggerMode; }
    public void setTriggerMode(String triggerMode) { this.triggerMode = triggerMode; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getMetadataJson() { return metadataJson; }
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
