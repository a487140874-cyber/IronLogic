package com.ironlogic.modules.progression.domain.repository;

import com.ironlogic.modules.progression.domain.model.ProgramProgress;
import java.util.Optional;

/**
 * Repository boundary for ProgramProgress access.
 *
 * <p>The progression module depends on this interface so its business logic stays decoupled
 * from MyBatis-Plus details and can be unit-tested with simple mocks.
 */
public interface ProgramProgressRepository {

    /**
     * Persists a new ProgramProgress row.
     *
     * @param programProgress progress state to persist
     * @return persisted progress including generated id
     */
    ProgramProgress save(ProgramProgress programProgress);

    /**
     * Persists changes to an existing ProgramProgress row.
     *
     * @param programProgress updated progress state
     * @return updated progress state
     */
    ProgramProgress update(ProgramProgress programProgress);

    /**
     * Finds progression state by owner and program.
     *
     * @param userId owner user id
     * @param programId target program id
     * @return optional progression state
     */
    Optional<ProgramProgress> findByUserIdAndProgramId(Long userId, Long programId);
}
