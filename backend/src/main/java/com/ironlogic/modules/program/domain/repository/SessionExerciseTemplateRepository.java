package com.ironlogic.modules.program.domain.repository;

import com.ironlogic.modules.program.domain.model.SessionExerciseTemplate;
import java.util.List;
import java.util.Optional;

/**
 * Repository boundary for SessionExerciseTemplate access.
 */
public interface SessionExerciseTemplateRepository {

    /**
     * Persists a new template exercise.
     *
     * @param templateExercise template exercise to persist
     * @return persisted template exercise
     */
    SessionExerciseTemplate save(SessionExerciseTemplate templateExercise);

    /**
     * Updates an existing template exercise.
     *
     * @param templateExercise updated template exercise
     * @return updated template exercise
     */
    SessionExerciseTemplate update(SessionExerciseTemplate templateExercise);

    /**
     * Finds a template exercise by id.
     *
     * @param id template exercise id
     * @return optional template exercise
     */
    Optional<SessionExerciseTemplate> findById(Long id);

    /**
     * Lists template exercises under one session template.
     *
     * @param sessionTemplateId parent session template id
     * @return ordered template exercise list
     */
    List<SessionExerciseTemplate> findBySessionTemplateId(Long sessionTemplateId);

    /**
     * Checks whether order number already exists under one session template.
     *
     * @param sessionTemplateId parent session template id
     * @param orderNo order number
     * @return true when duplicated
     */
    boolean existsBySessionTemplateIdAndOrderNo(Long sessionTemplateId, Integer orderNo);

    /**
     * Checks whether order number already exists excluding current record.
     *
     * @param sessionTemplateId parent session template id
     * @param orderNo order number
     * @param excludeId current template exercise id
     * @return true when duplicated
     */
    boolean existsBySessionTemplateIdAndOrderNoAndIdNot(Long sessionTemplateId, Integer orderNo, Long excludeId);
}
