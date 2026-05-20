package com.example.scholl.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scholl.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
}
