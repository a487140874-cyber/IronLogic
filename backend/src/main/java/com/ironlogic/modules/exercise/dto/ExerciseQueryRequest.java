package com.ironlogic.modules.exercise.dto;

import jakarta.validation.constraints.Size;

/**
 * Query DTO for exercise list API.
 *
 * <p>This DTO is a mutable bean rather than a record because Spring MVC binds query-string
 * parameters to {@code @ModelAttribute} objects most predictably with standard setters.
 */
public class ExerciseQueryRequest {

    /** Optional fuzzy name filter used by GET /api/exercises. */
    @Size(max = 128)
    private String name;

    /** Optional exact category filter. */
    @Size(max = 32)
    private String category;

    /** Optional exact equipment type filter. */
    @Size(max = 32)
    private String equipmentType;

    /**
     * Returns optional name filter.
     *
     * @return requested name filter
     */
    public String getName() {
        return name;
    }

    /**
     * Sets optional name filter.
     *
     * @param name requested name filter
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns optional category filter.
     *
     * @return requested category filter
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets optional category filter.
     *
     * @param category requested category filter
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns optional equipment type filter.
     *
     * @return requested equipment type filter
     */
    public String getEquipmentType() {
        return equipmentType;
    }

    /**
     * Sets optional equipment type filter.
     *
     * @param equipmentType requested equipment type filter
     */
    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }
}
