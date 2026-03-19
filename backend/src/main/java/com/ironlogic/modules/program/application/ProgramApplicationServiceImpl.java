package com.ironlogic.modules.program.application;

import com.ironlogic.common.exception.BusinessException;
import com.ironlogic.common.exception.NotFoundException;
import com.ironlogic.modules.exercise.domain.repository.ExerciseRepository;
import com.ironlogic.modules.program.domain.model.Program;
import com.ironlogic.modules.program.domain.model.ProgramBlock;
import com.ironlogic.modules.program.domain.model.SessionExerciseTemplate;
import com.ironlogic.modules.program.domain.model.SessionTemplate;
import com.ironlogic.modules.program.domain.repository.ProgramBlockRepository;
import com.ironlogic.modules.program.domain.repository.ProgramRepository;
import com.ironlogic.modules.program.domain.repository.SessionExerciseTemplateRepository;
import com.ironlogic.modules.program.domain.repository.SessionTemplateRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default application service for Program module.
 *
 * <p>This class orchestrates the full template hierarchy. It validates ownership by walking
 * the parent chain, enforces sequence/order uniqueness within the correct scope, and checks
 * that referenced exercises already exist before template exercises are created.
 */
@Service
public class ProgramApplicationServiceImpl implements ProgramApplicationService {

    private final ProgramRepository programRepository;
    private final ProgramBlockRepository programBlockRepository;
    private final SessionTemplateRepository sessionTemplateRepository;
    private final SessionExerciseTemplateRepository sessionExerciseTemplateRepository;
    private final ExerciseRepository exerciseRepository;

    public ProgramApplicationServiceImpl(
            ProgramRepository programRepository,
            ProgramBlockRepository programBlockRepository,
            SessionTemplateRepository sessionTemplateRepository,
            SessionExerciseTemplateRepository sessionExerciseTemplateRepository,
            ExerciseRepository exerciseRepository
    ) {
        this.programRepository = programRepository;
        this.programBlockRepository = programBlockRepository;
        this.sessionTemplateRepository = sessionTemplateRepository;
        this.sessionExerciseTemplateRepository = sessionExerciseTemplateRepository;
        this.exerciseRepository = exerciseRepository;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ProgramResponse createProgram(Long userId, CreateProgramRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Program program = new Program(
                null,
                userId,
                normalize(request.name()),
                normalize(request.goalType()),
                normalize(request.status()),
                normalize(request.description()),
                request.startDate(),
                request.endDate(),
                now,
                now
        );
        return toProgramResponse(programRepository.save(program));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<ProgramResponse> listPrograms(Long userId) {
        return programRepository.findByUserId(userId).stream()
                .map(ProgramApplicationServiceImpl::toProgramResponse)
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public ProgramResponse getProgram(Long userId, Long programId) {
        return toProgramResponse(requireOwnedProgram(userId, programId));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ProgramResponse updateProgram(Long userId, Long programId, UpdateProgramRequest request) {
        Program existing = requireOwnedProgram(userId, programId);
        Program updated = new Program(
                existing.id(),
                existing.userId(),
                normalize(request.name()),
                normalize(request.goalType()),
                normalize(request.status()),
                normalize(request.description()),
                request.startDate(),
                request.endDate(),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toProgramResponse(programRepository.update(updated));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ProgramBlockResponse createProgramBlock(Long userId, Long programId, CreateProgramBlockRequest request) {
        requireOwnedProgram(userId, programId);

        // sequenceNo is unique only inside one Program, so the validation must stay scoped to programId.
        if (programBlockRepository.existsByProgramIdAndSequenceNo(programId, request.sequenceNo())) {
            throw new BusinessException("Block sequenceNo already exists in this program");
        }

        LocalDateTime now = LocalDateTime.now();
        ProgramBlock block = new ProgramBlock(
                null,
                programId,
                normalize(request.name()),
                normalize(request.blockType()),
                request.sequenceNo(),
                normalize(request.durationMode()),
                request.durationValue(),
                defaultFalse(request.deloadEnabled()),
                normalize(request.metadataJson()),
                now,
                now
        );
        return toProgramBlockResponse(programBlockRepository.save(block));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<ProgramBlockResponse> listProgramBlocks(Long userId, Long programId) {
        requireOwnedProgram(userId, programId);
        return programBlockRepository.findByProgramId(programId).stream()
                .map(ProgramApplicationServiceImpl::toProgramBlockResponse)
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ProgramBlockResponse updateProgramBlock(Long userId, Long blockId, UpdateProgramBlockRequest request) {
        ProgramBlock existing = requireOwnedBlock(userId, blockId);

        if (programBlockRepository.existsByProgramIdAndSequenceNoAndIdNot(
                existing.programId(),
                request.sequenceNo(),
                existing.id()
        )) {
            throw new BusinessException("Block sequenceNo already exists in this program");
        }

        ProgramBlock updated = new ProgramBlock(
                existing.id(),
                existing.programId(),
                normalize(request.name()),
                normalize(request.blockType()),
                request.sequenceNo(),
                normalize(request.durationMode()),
                request.durationValue(),
                defaultFalse(request.deloadEnabled()),
                normalize(request.metadataJson()),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toProgramBlockResponse(programBlockRepository.update(updated));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public SessionTemplateResponse createSessionTemplate(Long userId, Long blockId, CreateSessionTemplateRequest request) {
        requireOwnedBlock(userId, blockId);

        // sequenceNo is unique inside one Block because template order is defined locally per block.
        if (sessionTemplateRepository.existsByBlockIdAndSequenceNo(blockId, request.sequenceNo())) {
            throw new BusinessException("SessionTemplate sequenceNo already exists in this block");
        }

        LocalDateTime now = LocalDateTime.now();
        SessionTemplate template = new SessionTemplate(
                null,
                blockId,
                normalize(request.name()),
                request.sequenceNo(),
                normalize(request.triggerMode()),
                normalize(request.notes()),
                normalize(request.metadataJson()),
                now,
                now
        );
        return toSessionTemplateResponse(sessionTemplateRepository.save(template));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<SessionTemplateResponse> listSessionTemplates(Long userId, Long blockId) {
        requireOwnedBlock(userId, blockId);
        return sessionTemplateRepository.findByBlockId(blockId).stream()
                .map(ProgramApplicationServiceImpl::toSessionTemplateResponse)
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public SessionTemplateResponse updateSessionTemplate(Long userId, Long templateId, UpdateSessionTemplateRequest request) {
        SessionTemplate existing = requireOwnedSessionTemplate(userId, templateId);

        if (sessionTemplateRepository.existsByBlockIdAndSequenceNoAndIdNot(
                existing.blockId(),
                request.sequenceNo(),
                existing.id()
        )) {
            throw new BusinessException("SessionTemplate sequenceNo already exists in this block");
        }

        SessionTemplate updated = new SessionTemplate(
                existing.id(),
                existing.blockId(),
                normalize(request.name()),
                request.sequenceNo(),
                normalize(request.triggerMode()),
                normalize(request.notes()),
                normalize(request.metadataJson()),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toSessionTemplateResponse(sessionTemplateRepository.update(updated));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public SessionExerciseTemplateResponse createSessionExerciseTemplate(
            Long userId,
            Long sessionTemplateId,
            CreateSessionExerciseTemplateRequest request
    ) {
        requireOwnedSessionTemplate(userId, sessionTemplateId);

        if (sessionExerciseTemplateRepository.existsBySessionTemplateIdAndOrderNo(
                sessionTemplateId,
                request.orderNo()
        )) {
            throw new BusinessException("SessionExerciseTemplate orderNo already exists in this session template");
        }

        // Template definition may only reference an exercise the current user can actually see.
        requireVisibleExercise(userId, request.exerciseId());

        LocalDateTime now = LocalDateTime.now();
        SessionExerciseTemplate templateExercise = new SessionExerciseTemplate(
                null,
                sessionTemplateId,
                request.exerciseId(),
                request.orderNo(),
                request.targetSets(),
                request.targetReps(),
                request.targetWeight(),
                normalize(request.targetWeightUnit()),
                request.restSeconds(),
                normalize(request.intensityMode()),
                request.progressionRuleId(),
                normalize(request.prescriptionJson()),
                now,
                now
        );
        return toSessionExerciseTemplateResponse(sessionExerciseTemplateRepository.save(templateExercise));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<SessionExerciseTemplateResponse> listSessionExerciseTemplates(Long userId, Long sessionTemplateId) {
        requireOwnedSessionTemplate(userId, sessionTemplateId);
        return sessionExerciseTemplateRepository.findBySessionTemplateId(sessionTemplateId).stream()
                .map(ProgramApplicationServiceImpl::toSessionExerciseTemplateResponse)
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public SessionExerciseTemplateResponse updateSessionExerciseTemplate(
            Long userId,
            Long sessionExerciseTemplateId,
            UpdateSessionExerciseTemplateRequest request
    ) {
        SessionExerciseTemplate existing = requireOwnedSessionExerciseTemplate(userId, sessionExerciseTemplateId);

        if (sessionExerciseTemplateRepository.existsBySessionTemplateIdAndOrderNoAndIdNot(
                existing.sessionTemplateId(),
                request.orderNo(),
                existing.id()
        )) {
            throw new BusinessException("SessionExerciseTemplate orderNo already exists in this session template");
        }

        requireVisibleExercise(userId, request.exerciseId());

        SessionExerciseTemplate updated = new SessionExerciseTemplate(
                existing.id(),
                existing.sessionTemplateId(),
                request.exerciseId(),
                request.orderNo(),
                request.targetSets(),
                request.targetReps(),
                request.targetWeight(),
                normalize(request.targetWeightUnit()),
                request.restSeconds(),
                normalize(request.intensityMode()),
                request.progressionRuleId(),
                normalize(request.prescriptionJson()),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return toSessionExerciseTemplateResponse(sessionExerciseTemplateRepository.update(updated));
    }

    private Program requireOwnedProgram(Long userId, Long programId) {
        return programRepository.findByIdAndUserId(programId, userId)
                .orElseThrow(() -> new NotFoundException("Program not found"));
    }

    private ProgramBlock requireOwnedBlock(Long userId, Long blockId) {
        ProgramBlock block = programBlockRepository.findById(blockId)
                .orElseThrow(() -> new NotFoundException("ProgramBlock not found"));
        requireOwnedProgram(userId, block.programId());
        return block;
    }

    private SessionTemplate requireOwnedSessionTemplate(Long userId, Long templateId) {
        SessionTemplate template = sessionTemplateRepository.findById(templateId)
                .orElseThrow(() -> new NotFoundException("SessionTemplate not found"));
        requireOwnedBlock(userId, template.blockId());
        return template;
    }

    private SessionExerciseTemplate requireOwnedSessionExerciseTemplate(Long userId, Long templateExerciseId) {
        SessionExerciseTemplate templateExercise = sessionExerciseTemplateRepository.findById(templateExerciseId)
                .orElseThrow(() -> new NotFoundException("SessionExerciseTemplate not found"));
        requireOwnedSessionTemplate(userId, templateExercise.sessionTemplateId());
        return templateExercise;
    }

    private void requireVisibleExercise(Long userId, Long exerciseId) {
        if (exerciseRepository.findVisibleById(userId, exerciseId).isEmpty()) {
            throw new NotFoundException("Exercise not found");
        }
    }

    private static Boolean defaultFalse(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static ProgramResponse toProgramResponse(Program program) {
        return new ProgramResponse(
                program.id(),
                program.userId(),
                program.name(),
                program.goalType(),
                program.status(),
                program.description(),
                program.startDate(),
                program.endDate(),
                program.createdAt(),
                program.updatedAt()
        );
    }

    private static ProgramBlockResponse toProgramBlockResponse(ProgramBlock block) {
        return new ProgramBlockResponse(
                block.id(),
                block.programId(),
                block.name(),
                block.blockType(),
                block.sequenceNo(),
                block.durationMode(),
                block.durationValue(),
                block.deloadEnabled(),
                block.metadataJson(),
                block.createdAt(),
                block.updatedAt()
        );
    }

    private static SessionTemplateResponse toSessionTemplateResponse(SessionTemplate template) {
        return new SessionTemplateResponse(
                template.id(),
                template.blockId(),
                template.name(),
                template.sequenceNo(),
                template.triggerMode(),
                template.notes(),
                template.metadataJson(),
                template.createdAt(),
                template.updatedAt()
        );
    }

    private static SessionExerciseTemplateResponse toSessionExerciseTemplateResponse(
            SessionExerciseTemplate templateExercise
    ) {
        return new SessionExerciseTemplateResponse(
                templateExercise.id(),
                templateExercise.sessionTemplateId(),
                templateExercise.exerciseId(),
                templateExercise.orderNo(),
                templateExercise.targetSets(),
                templateExercise.targetReps(),
                templateExercise.targetWeight(),
                templateExercise.targetWeightUnit(),
                templateExercise.restSeconds(),
                templateExercise.intensityMode(),
                templateExercise.progressionRuleId(),
                templateExercise.prescriptionJson(),
                templateExercise.createdAt(),
                templateExercise.updatedAt()
        );
    }
}
