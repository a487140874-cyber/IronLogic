package com.ironlogic.modules.progression.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ironlogic.common.persistence.typehandler.JsonbStringTypeHandler;
import java.time.LocalDateTime;

/**
 * Persistence entity mapped to the {@code program_progress} table.
 */
@TableName(value = "program_progress", autoResultMap = true)
public class ProgramProgressEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("program_id")
    private Long programId;

    @TableField("current_block_id")
    private Long currentBlockId;

    @TableField("next_session_template_id")
    private Long nextSessionTemplateId;

    @TableField("last_completed_workout_id")
    private Long lastCompletedWorkoutId;

    @TableField("sequence_cursor")
    private Integer sequenceCursor;

    @TableField(value = "progress_snapshot_json", typeHandler = JsonbStringTypeHandler.class)
    private String progressSnapshotJson;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }
    public Long getCurrentBlockId() { return currentBlockId; }
    public void setCurrentBlockId(Long currentBlockId) { this.currentBlockId = currentBlockId; }
    public Long getNextSessionTemplateId() { return nextSessionTemplateId; }
    public void setNextSessionTemplateId(Long nextSessionTemplateId) { this.nextSessionTemplateId = nextSessionTemplateId; }
    public Long getLastCompletedWorkoutId() { return lastCompletedWorkoutId; }
    public void setLastCompletedWorkoutId(Long lastCompletedWorkoutId) { this.lastCompletedWorkoutId = lastCompletedWorkoutId; }
    public Integer getSequenceCursor() { return sequenceCursor; }
    public void setSequenceCursor(Integer sequenceCursor) { this.sequenceCursor = sequenceCursor; }
    public String getProgressSnapshotJson() { return progressSnapshotJson; }
    public void setProgressSnapshotJson(String progressSnapshotJson) { this.progressSnapshotJson = progressSnapshotJson; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
