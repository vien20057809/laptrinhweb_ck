package com.example.scholl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.scholl.model.Semester;
import com.example.scholl.repository.SemesterRepository;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    public List<Semester> getAll() {
        return semesterRepository.findAll();
    }
}
