package com.ironlogic.modules.progression.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for the currently recommended SessionTemplate.
 *
 * @param id session template id
 * @param blockId parent block id
 * @param name template name
 * @param sequenceNo sequence number inside the block
 * @param triggerMode trigger mode configured on the template
 * @param notes optional template notes
 * @param metadataJson extensible template metadata json
 * @param createdAt creation time
 * @param updatedAt last update time
 * @param exercises ordered recommended exercise list under this template
 */
public record RecommendedSessionTemplateResponse(
        Long id,
        Long blockId,
        String name,
        Integer sequenceNo,
        String triggerMode,
        String notes,
        String metadataJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<RecommendedExerciseResponse> exercises
) {
}
