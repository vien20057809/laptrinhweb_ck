package com.example.scholl.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.scholl.dto.PageResponse;
import com.example.scholl.dto.TrainingProgramRequest;
import com.example.scholl.exception.BusinessException;
import com.example.scholl.model.Department;
import com.example.scholl.model.Major;
import com.example.scholl.model.TrainingProgram;
import com.example.scholl.repository.DepartmentRepository;
import com.example.scholl.repository.MajorRepository;
import com.example.scholl.repository.TrainingProgramRepository;

@Service
public class TrainingProgramService {

    @Autowired
    private TrainingProgramRepository repository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public PageResponse<TrainingProgram> search(
            String keyword,
            UUID majorId,
            UUID departmentId,
            boolean activeOnly,
            Pageable pageable) {
        String q = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        Page<TrainingProgram> page = repository.findFiltered(q, majorId, departmentId, activeOnly, pageable);
        return new PageResponse<>(page);
    }

    public TrainingProgram getById(UUID id) {
        return repository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy CTĐT"));
    }

    public TrainingProgram create(TrainingProgramRequest request) {
        validateBusinessRules(request);
        Major major = loadMajor(request.getMajorId());
        Department department = loadDepartment(request.getDepartmentId());
        ensureCodeUnique(request.getCode(), null);

        TrainingProgram entity = new TrainingProgram();
        entity.setId(UUID.randomUUID());
        applyRequest(entity, request, major, department);
        entity.setCreatedBy(normalizeActor(request.getActor()));
        return repository.save(entity);
    }

    public TrainingProgram update(UUID id, TrainingProgramRequest request) {
        validateBusinessRules(request);
        TrainingProgram entity = getById(id);
        Major major = loadMajor(request.getMajorId());
        Department department = loadDepartment(request.getDepartmentId());
        ensureCodeUnique(request.getCode(), id);

        applyRequest(entity, request, major, department);
        entity.setUpdatedBy(normalizeActor(request.getActor()));
        return repository.save(entity);
    }

    public TrainingProgram toggleActive(UUID id, boolean active, String actor) {
        TrainingProgram entity = getById(id);
        entity.setIsActive(active);
        entity.setUpdatedBy(normalizeActor(actor));
        return repository.save(entity);
    }

    public void softDelete(UUID id, String deletedBy) {
        TrainingProgram entity = getById(id);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(normalizeActor(deletedBy));
        entity.setIsActive(false);
        repository.save(entity);
    }

    private void ensureCodeUnique(String code, UUID excludeId) {
        String normalized = code.trim();
        boolean exists = excludeId == null
                ? repository.existsByCodeIgnoreCaseAndDeletedAtIsNull(normalized)
                : repository.existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(normalized, excludeId);
        if (exists) {
            throw new BusinessException(HttpStatus.CONFLICT, "Mã CTĐT \"" + normalized + "\" đã tồn tại");
        }
    }

    private void validateBusinessRules(TrainingProgramRequest request) {
        if (request.getEffectiveDate() != null
                && request.getExpiryDate() != null
                && request.getEffectiveDate().isAfter(request.getExpiryDate())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Ngày có hiệu lực phải trước hoặc bằng ngày hết hiệu lực");
        }
    }

    private Major loadMajor(UUID majorId) {
        return majorRepository.findActiveById(majorId)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Ngành không tồn tại"));
    }

    private Department loadDepartment(UUID departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Khoa không tồn tại"));
    }

    private String normalizeActor(String actor) {
        if (actor == null || actor.isBlank()) {
            return null;
        }
        return actor.trim();
    }

    private void applyRequest(
            TrainingProgram entity,
            TrainingProgramRequest request,
            Major major,
            Department department) {
        entity.setMajor(major);
        entity.setDepartment(department);
        entity.setCode(request.getCode().trim());
        entity.setName(request.getName().trim());
        entity.setNameEn(request.getNameEn());
        entity.setDegreeLevel(request.getDegreeLevel());
        entity.setEducationType(request.getEducationType());
        entity.setTotalCredits(request.getTotalCredits());
        entity.setRequiredCredits(request.getRequiredCredits());
        entity.setElectiveCredits(request.getElectiveCredits());
        entity.setInternshipCredits(request.getInternshipCredits());
        entity.setThesisCredits(request.getThesisCredits());
        entity.setAdmissionYear(request.getAdmissionYear());
        entity.setDurationYears(request.getDurationYears());
        entity.setMaxDurationYears(request.getMaxDurationYears());
        entity.setEffectiveDate(request.getEffectiveDate());
        entity.setExpiryDate(request.getExpiryDate());
        entity.setDescription(request.getDescription());
        entity.setObjectives(request.getObjectives());
        entity.setLearningOutcomes(request.getLearningOutcomes());
        entity.setVersion(request.getVersion());
        entity.setStatus(request.getStatus());
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
    }
}
