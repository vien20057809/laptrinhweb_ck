package com.example.scholl.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.scholl.model.Course;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query("SELECT c FROM Course c WHERE c.deletedAt IS NULL AND c.id = :id")
    Optional<Course> findActiveById(@Param("id") UUID id);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNull(String code);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(String code, UUID id);

    @Query("""
            SELECT c FROM Course c
            WHERE c.deletedAt IS NULL
              AND (:q IS NULL OR :q = ''
                   OR LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(c.code) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:departmentId IS NULL OR c.department.id = :departmentId)
              AND (:courseType IS NULL OR :courseType = '' OR c.courseType = :courseType)
              AND (:activeOnly = false OR c.isActive = true)
            """)
    Page<Course> findFiltered(
            @Param("q") String q,
            @Param("departmentId") UUID departmentId,
            @Param("courseType") String courseType,
            @Param("activeOnly") boolean activeOnly,
            Pageable pageable);
}
