package com.example.scholl.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.scholl.model.Major;

public interface MajorRepository extends JpaRepository<Major, UUID> {

    @Query("SELECT m FROM Major m WHERE m.deletedAt IS NULL AND m.id = :id")
    Optional<Major> findActiveById(@Param("id") UUID id);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNull(String code);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(String code, UUID id);

    @Query("""
            SELECT m FROM Major m
            WHERE m.deletedAt IS NULL
              AND (:q IS NULL OR :q = ''
                   OR LOWER(m.name) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(m.code) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:departmentId IS NULL OR m.department.id = :departmentId)
              AND (:activeOnly = false OR m.isActive = true)
            """)
    Page<Major> findFiltered(
            @Param("q") String q,
            @Param("departmentId") UUID departmentId,
            @Param("activeOnly") boolean activeOnly,
            Pageable pageable);
}
