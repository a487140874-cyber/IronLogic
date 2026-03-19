package com.ironlogic.modules.program.application;

import com.ironlogic.modules.program.dto.CreateProgramBlockRequest;
import com.ironlogic.modules.program.dto.CreateProgramRequest;
import com.ironlogic.modules.program.dto.CreateSessionExerciseTemplateRequest;
import com.ironlogic.modules.program.dto.CreateSessionTemplateRequest;
import com.ironlogic.modules.program.dto.ProgramBlockResponse;
import com.ironlogic.modules.program.dto.ProgramResponse;
import com.ironlogic.modules.program.dto.SessionExerciseTemplateResponse;
import com.ironlogic.modules.program.dto.SessionTemplateResponse;
import com.ironlogic.modules.program.dto.UpdateProgramBlockRequest;
import com.ironlogic.modules.program.dto.UpdateProgramRequest;
import com.ironlogic.modules.program.dto.UpdateSessionExerciseTemplateRequest;
import com.ironlogic.modules.program.dto.UpdateSessionTemplateRequest;
import java.util.List;

/**
 * Application service for Program definition use cases.
 *
 * <p>This service groups the plan definition workflow in one place because Program,
 * ProgramBlock, SessionTemplate, and SessionExerciseTemplate form one cohesive hierarchy in
 * MVP. It keeps all parent-child validation and sequence/order rules out of controllers.
 */
public interface ProgramApplicationService {

    /** Creates a Program owned by the current user. */
    ProgramResponse createProgram(Long userId, CreateProgramRequest request);

    /** Lists Programs of the current user. */
    List<ProgramResponse> listPrograms(Long userId);

    /** Loads one owned Program. */
    ProgramResponse getProgram(Long userId, Long programId);

    /** Updates one owned Program. */
    ProgramResponse updateProgram(Long userId, Long programId, UpdateProgramRequest request);

    /** Creates a ProgramBlock under one owned Program. */
    ProgramBlockResponse createProgramBlock(Long userId, Long programId, CreateProgramBlockRequest request);

    /** Lists ProgramBlocks under one owned Program. */
    List<ProgramBlockResponse> listProgramBlocks(Long userId, Long programId);

    /** Updates one ProgramBlock under an owned Program. */
    ProgramBlockResponse updateProgramBlock(Long userId, Long blockId, UpdateProgramBlockRequest request);

    /** Creates a SessionTemplate under one owned ProgramBlock. */
    SessionTemplateResponse createSessionTemplate(Long userId, Long blockId, CreateSessionTemplateRequest request);

    /** Lists SessionTemplates under one owned ProgramBlock. */
    List<SessionTemplateResponse> listSessionTemplates(Long userId, Long blockId);

    /** Updates one SessionTemplate under an owned Program hierarchy. */
    SessionTemplateResponse updateSessionTemplate(Long userId, Long templateId, UpdateSessionTemplateRequest request);

    /** Creates a SessionExerciseTemplate under one owned SessionTemplate. */
    SessionExerciseTemplateResponse createSessionExerciseTemplate(
            Long userId,
            Long sessionTemplateId,
            CreateSessionExerciseTemplateRequest request
    );

    /** Lists SessionExerciseTemplates under one owned SessionTemplate. */
    List<SessionExerciseTemplateResponse> listSessionExerciseTemplates(Long userId, Long sessionTemplateId);

    /** Updates one SessionExerciseTemplate under an owned Program hierarchy. */
    SessionExerciseTemplateResponse updateSessionExerciseTemplate(
            Long userId,
            Long sessionExerciseTemplateId,
            UpdateSessionExerciseTemplateRequest request
    );
}
