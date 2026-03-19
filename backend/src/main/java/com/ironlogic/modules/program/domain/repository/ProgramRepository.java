package com.ironlogic.modules.program.domain.repository;

import com.ironlogic.modules.program.domain.model.Program;
import java.util.List;
import java.util.Optional;

/**
 * Repository boundary for Program access.
 */
public interface ProgramRepository {

    /**
     * Persists a new program.
     *
     * @param program program to persist
     * @return persisted program
     */
    Program save(Program program);

    /**
     * Updates an existing program.
     *
     * @param program updated program
     * @return updated program
     */
    Program update(Program program);

    /**
     * Finds a program by id.
     *
     * @param id program id
     * @return optional program
     */
    Optional<Program> findById(Long id);

    /**
     * Finds a program by id and owner.
     *
     * @param id program id
     * @param userId owner id
     * @return optional owned program
     */
    Optional<Program> findByIdAndUserId(Long id, Long userId);

    /**
     * Lists programs of one user.
     *
     * @param userId owner id
     * @return owned programs
     */
    List<Program> findByUserId(Long userId);
}
