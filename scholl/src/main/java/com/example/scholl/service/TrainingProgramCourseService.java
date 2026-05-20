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
import com.example.scholl.dto.TrainingProgramCourseRequest;
import com.example.scholl.exception.BusinessException;
import com.example.scholl.model.Course;
import com.example.scholl.model.Semester;
import com.example.scholl.model.TrainingProgram;
import com.example.scholl.model.TrainingProgramCourse;
import com.example.scholl.repository.CourseRepository;
import com.example.scholl.repository.SemesterRepository;
import com.example.scholl.repository.TrainingProgramCourseRepository;
import com.example.scholl.repository.TrainingProgramRepository;

@Service
public class TrainingProgramCourseService {

    @Autowired
    private TrainingProgramCourseRepository repository;

    @Autowired
    private TrainingProgramRepository trainingProgramRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    public PageResponse<TrainingProgramCourse> search(
            String keyword,
            UUID trainingProgramId,
            UUID courseId,
            UUID semesterId,
            boolean activeOnly,
            Pageable pageable) {
        String q = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        Page<TrainingProgramCourse> page = repository.findFiltered(
                q, trainingProgramId, courseId, semesterId, activeOnly, pageable);
        return new PageResponse<>(page);
    }

    public TrainingProgramCourse getById(UUID id) {
        return repository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy học phần trong CTĐT"));
    }

    public TrainingProgramCourse create(TrainingProgramCourseRequest request) {
        TrainingProgram program = loadTrainingProgram(request.getTrainingProgramId());
        Course course = loadCourse(request.getCourseId());
        ensurePairUnique(program.getId(), course.getId(), null);

        TrainingProgramCourse entity = new TrainingProgramCourse();
        entity.setId(UUID.randomUUID());
        applyRequest(entity, request, program, course);
        entity.setCreatedBy(normalizeActor(request.getActor()));
        return repository.save(entity);
    }

    public TrainingProgramCourse update(UUID id, TrainingProgramCourseRequest request) {
        TrainingProgramCourse entity = getById(id);
        TrainingProgram program = loadTrainingProgram(request.getTrainingProgramId());
        Course course = loadCourse(request.getCourseId());
        ensurePairUnique(program.getId(), course.getId(), id);

        applyRequest(entity, request, program, course);
        entity.setUpdatedBy(normalizeActor(request.getActor()));
        return repository.save(entity);
    }

    public TrainingProgramCourse toggleActive(UUID id, boolean active, String actor) {
        TrainingProgramCourse entity = getById(id);
        entity.setIsActive(active);
        entity.setUpdatedBy(normalizeActor(actor));
        return repository.save(entity);
    }

    public void softDelete(UUID id, String deletedBy) {
        TrainingProgramCourse entity = getById(id);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(normalizeActor(deletedBy));
        entity.setIsActive(false);
        repository.save(entity);
    }

    private void ensurePairUnique(UUID programId, UUID courseId, UUID excludeId) {
        boolean exists = excludeId == null
                ? repository.existsByTrainingProgramIdAndCourseIdAndDeletedAtIsNull(programId, courseId)
                : repository.existsByTrainingProgramIdAndCourseIdAndDeletedAtIsNullAndIdNot(
                        programId, courseId, excludeId);
        if (exists) {
            throw new BusinessException(HttpStatus.CONFLICT,
                    "Học phần đã tồn tại trong chương trình đào tạo này");
        }
    }

    private TrainingProgram loadTrainingProgram(UUID id) {
        return trainingProgramRepository.findActiveById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "CTĐT không tồn tại"));
    }

    private Course loadCourse(UUID id) {
        return courseRepository.findActiveById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Học phần không tồn tại"));
    }

    private Semester loadSemester(UUID id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Học kỳ không tồn tại"));
    }

    private Course loadPrerequisite(UUID id) {
        return courseRepository.findActiveById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Học phần tiên quyết không tồn tại"));
    }

    private String normalizeActor(String actor) {
        if (actor == null || actor.isBlank()) {
            return null;
        }
        return actor.trim();
    }

    private void applyRequest(
            TrainingProgramCourse entity,
            TrainingProgramCourseRequest request,
            TrainingProgram program,
            Course course) {
        entity.setTrainingProgram(program);
        entity.setCourse(course);
        entity.setCourseCode(
                request.getCourseCode() != null ? request.getCourseCode().trim() : course.getCode());
        entity.setCourseName(
                request.getCourseName() != null ? request.getCourseName().trim() : course.getName());

        if (request.getSemesterId() != null) {
            entity.setSemester(loadSemester(request.getSemesterId()));
        } else {
            entity.setSemester(null);
        }
        entity.setSemesterCode(request.getSemesterCode());
        entity.setAcademicYear(request.getAcademicYear());
        entity.setIsRequired(request.getIsRequired() != null ? request.getIsRequired() : true);
        entity.setGroupCode(request.getGroupCode());
        entity.setCredits(request.getCredits() != null ? request.getCredits() : course.getCredits());

        if (request.getPrerequisiteCourseId() != null) {
            entity.setPrerequisiteCourse(loadPrerequisite(request.getPrerequisiteCourseId()));
        } else {
            entity.setPrerequisiteCourse(null);
        }
        entity.setIsPrerequisiteRequired(
                request.getIsPrerequisiteRequired() != null ? request.getIsPrerequisiteRequired() : false);
        entity.setNote(request.getNote());
        entity.setSortOrder(request.getSortOrder());
        entity.setStatus(request.getStatus());
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
    }
}
