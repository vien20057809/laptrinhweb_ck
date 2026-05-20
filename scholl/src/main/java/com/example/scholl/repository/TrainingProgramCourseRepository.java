package com.example.scholl.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.scholl.model.TrainingProgramCourse;

public interface TrainingProgramCourseRepository extends JpaRepository<TrainingProgramCourse, UUID> {

    @Query("SELECT t FROM TrainingProgramCourse t WHERE t.deletedAt IS NULL AND t.id = :id")
    Optional<TrainingProgramCourse> findActiveById(@Param("id") UUID id);

    boolean existsByTrainingProgramIdAndCourseIdAndDeletedAtIsNull(UUID trainingProgramId, UUID courseId);

    boolean existsByTrainingProgramIdAndCourseIdAndDeletedAtIsNullAndIdNot(
            UUID trainingProgramId, UUID courseId, UUID id);

    @Query("""
            SELECT t FROM TrainingProgramCourse t
            WHERE t.deletedAt IS NULL
              AND (:q IS NULL OR :q = ''
                   OR LOWER(t.courseName) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(t.courseCode) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:trainingProgramId IS NULL OR t.trainingProgram.id = :trainingProgramId)
              AND (:courseId IS NULL OR t.course.id = :courseId)
              AND (:semesterId IS NULL OR t.semester.id = :semesterId)
              AND (:activeOnly = false OR t.isActive = true)
            """)
    Page<TrainingProgramCourse> findFiltered(
            @Param("q") String q,
            @Param("trainingProgramId") UUID trainingProgramId,
            @Param("courseId") UUID courseId,
            @Param("semesterId") UUID semesterId,
            @Param("activeOnly") boolean activeOnly,
            Pageable pageable);
}
