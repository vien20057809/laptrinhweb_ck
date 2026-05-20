package com.example.scholl.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.scholl.dto.MajorRequest;
import com.example.scholl.dto.PageResponse;
import com.example.scholl.model.Major;
import com.example.scholl.service.MajorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/majors")
@CrossOrigin
@Validated
public class MajorController {

    @Autowired
    private MajorService service;

    @GetMapping
    public PageResponse<Major> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "code") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        int safeSize = Math.min(Math.max(size, 1), 50);
        Sort sortOrder = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, sortOrder);
        return service.search(q, departmentId, activeOnly, pageable);
    }

    @GetMapping("/{id}")
    public Major getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public Major create(@Valid @RequestBody MajorRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Major update(@PathVariable UUID id, @Valid @RequestBody MajorRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/active")
    public Major toggleActive(
            @PathVariable UUID id,
            @RequestParam boolean active,
            @RequestParam(required = false) String actor) {
        return service.toggleActive(id, active, actor);
    }

    @DeleteMapping("/{id}")
    public void softDelete(
            @PathVariable UUID id,
            @RequestParam(required = false) String deletedBy) {
        service.softDelete(id, deletedBy);
    }
}
