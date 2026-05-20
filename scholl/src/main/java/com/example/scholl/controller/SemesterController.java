package com.example.scholl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scholl.model.Semester;
import com.example.scholl.service.SemesterService;

@RestController
@RequestMapping("/api/semesters")
@CrossOrigin
public class SemesterController {

    @Autowired
    private SemesterService service;

    @GetMapping
    public List<Semester> getAll() {
        return service.getAll();
    }
}
