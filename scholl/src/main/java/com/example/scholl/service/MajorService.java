package com.example.scholl.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.scholl.dto.MajorRequest;
import com.example.scholl.dto.PageResponse;
import com.example.scholl.exception.BusinessException;
import com.example.scholl.model.Department;
import com.example.scholl.model.Major;
import com.example.scholl.repository.DepartmentRepository;
import com.example.scholl.repository.MajorRepository;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public PageResponse<Major> search(
            String keyword,
            UUID departmentId,
            boolean activeOnly,
            Pageable pageable) {
        String q = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        Page<Major> page = majorRepository.findFiltered(q, departmentId, activeOnly, pageable);
        return new PageResponse<>(page);
    }

    public Major getById(UUID id) {
        return majorRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ngành"));
    }

    public Major create(MajorRequest request) {
        validateBusinessRules(request);
        Department department = loadDepartment(request.getDepartmentId());
        ensureCodeUnique(request.getCode(), null);

        Major major = new Major();
        major.setId(UUID.randomUUID());
        applyRequest(major, request, department);
        major.setCreatedBy(normalizeActor(request.getActor()));
        return majorRepository.save(major);
    }

    public Major update(UUID id, MajorRequest request) {
        validateBusinessRules(request);
        Major major = getById(id);
        Department department = loadDepartment(request.getDepartmentId());
        ensureCodeUnique(request.getCode(), id);

        applyRequest(major, request, department);
        major.setUpdatedBy(normalizeActor(request.getActor()));
        return majorRepository.save(major);
    }

    public Major toggleActive(UUID id, boolean active, String actor) {
        Major major = getById(id);
        major.setIsActive(active);
        major.setUpdatedBy(normalizeActor(actor));
        return majorRepository.save(major);
    }

    public void softDelete(UUID id, String deletedBy) {
        Major major = getById(id);
        major.setDeletedAt(LocalDateTime.now());
        major.setDeletedBy(normalizeActor(deletedBy));
        major.setIsActive(false);
        majorRepository.save(major);
    }

    private void ensureCodeUnique(String code, UUID excludeId) {
        String normalized = code.trim();
        boolean exists = excludeId == null
                ? majorRepository.existsByCodeIgnoreCaseAndDeletedAtIsNull(normalized)
                : majorRepository.existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(normalized, excludeId);
        if (exists) {
            throw new BusinessException(HttpStatus.CONFLICT, "Mã ngành \"" + normalized + "\" đã tồn tại");
        }
    }

    private void validateBusinessRules(MajorRequest request) {
        if (request.getEffectiveDate() != null
                && request.getExpiryDate() != null
                && request.getEffectiveDate().isAfter(request.getExpiryDate())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Ngày có hiệu lực phải trước hoặc bằng ngày hết hiệu lực");
        }
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

    private void applyRequest(Major major, MajorRequest request, Department department) {
        major.setDepartment(department);
        major.setCode(request.getCode().trim());
        major.setName(request.getName().trim());
        major.setDescription(request.getDescription());
        major.setEffectiveDate(request.getEffectiveDate());
        major.setExpiryDate(request.getExpiryDate());
        if (request.getIsActive() != null) {
            major.setIsActive(request.getIsActive());
        }
    }
}
