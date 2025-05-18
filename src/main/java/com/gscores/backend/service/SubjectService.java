package com.gscores.backend.service;

import com.gscores.backend.dto.model.SubjectDTO;

import java.util.List;

public interface SubjectService {
    List<SubjectDTO> getAllSubjects();

    SubjectDTO createSubject(SubjectDTO subjectDTO);

    SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO);

    void deleteSubject(Long id);
}
