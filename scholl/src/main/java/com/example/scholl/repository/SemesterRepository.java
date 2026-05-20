package com.example.scholl.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scholl.model.Semester;

public interface SemesterRepository extends JpaRepository<Semester, UUID> {
}
