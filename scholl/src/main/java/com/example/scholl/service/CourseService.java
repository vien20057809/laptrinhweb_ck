package com.example.scholl.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.scholl.dto.CourseRequest;
import com.example.scholl.dto.PageResponse;
import com.example.scholl.exception.BusinessException;
import com.example.scholl.model.Course;
import com.example.scholl.model.Department;
import com.example.scholl.repository.CourseRepository;
import com.example.scholl.repository.DepartmentRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public PageResponse<Course> search(
            String keyword,
            UUID departmentId,
            String courseType,
            boolean activeOnly,
            Pageable pageable) {
        String q = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        String type = (courseType == null || courseType.isBlank()) ? null : courseType.trim();
        Page<Course> page = repository.findFiltered(q, departmentId, type, activeOnly, pageable);
        return new PageResponse<>(page);
    }

    public Course getById(UUID id) {
        return repository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy học phần"));
    }

    public Course create(CourseRequest request) {
        Department department = loadDepartment(request.getDepartmentId());
        ensureCodeUnique(request.getCode(), null);

        Course entity = new Course();
        entity.setId(UUID.randomUUID());
        applyRequest(entity, request, department);
        entity.setCreatedBy(normalizeActor(request.getActor()));
        return repository.save(entity);
    }

    public Course update(UUID id, CourseRequest request) {
        Course entity = getById(id);
        Department department = loadDepartment(request.getDepartmentId());
        ensureCodeUnique(request.getCode(), id);

        applyRequest(entity, request, department);
        entity.setUpdatedBy(normalizeActor(request.getActor()));
        return repository.save(entity);
    }

    public Course toggleActive(UUID id, boolean active, String actor) {
        Course entity = getById(id);
        entity.setIsActive(active);
        entity.setUpdatedBy(normalizeActor(actor));
        return repository.save(entity);
    }

    public void softDelete(UUID id, String deletedBy) {
        Course entity = getById(id);
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
            throw new BusinessException(HttpStatus.CONFLICT, "Mã học phần \"" + normalized + "\" đã tồn tại");
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

    private void applyRequest(Course entity, CourseRequest request, Department department) {
        entity.setDepartment(department);
        entity.setCode(request.getCode().trim());
        entity.setName(request.getName().trim());
        entity.setNameEn(request.getNameEn());
        entity.setCredits(request.getCredits());
        entity.setCourseType(request.getCourseType());
        entity.setTheoryHours(request.getTheoryHours());
        entity.setPracticeHours(request.getPracticeHours());
        entity.setSelfStudyHours(request.getSelfStudyHours());
        entity.setInternshipCredits(request.getInternshipCredits());
        entity.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
    }
}
