package com.example.scholl.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.scholl.model.TrainingProgram;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, UUID> {

    @Query("SELECT t FROM TrainingProgram t WHERE t.deletedAt IS NULL AND t.id = :id")
    Optional<TrainingProgram> findActiveById(@Param("id") UUID id);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNull(String code);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(String code, UUID id);

    @Query("""
            SELECT t FROM TrainingProgram t
            WHERE t.deletedAt IS NULL
              AND (:q IS NULL OR :q = ''
                   OR LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(t.code) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:majorId IS NULL OR t.major.id = :majorId)
              AND (:departmentId IS NULL OR t.department.id = :departmentId)
              AND (:activeOnly = false OR t.isActive = true)
            """)
    Page<TrainingProgram> findFiltered(
            @Param("q") String q,
            @Param("majorId") UUID majorId,
            @Param("departmentId") UUID departmentId,
            @Param("activeOnly") boolean activeOnly,
            Pageable pageable);
}
