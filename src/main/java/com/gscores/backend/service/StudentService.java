package com.gscores.backend.service;

import com.gscores.backend.dto.model.StudentDTO;
import com.gscores.backend.dto.model.TopStudentDTO;

import java.util.List;

public interface StudentService {
    StudentDTO getStudentByRegistrationNumber(String registrationNumber);

    List<TopStudentDTO> getTopStudentsByGroup(String group, int top);

    StudentDTO createStudent(StudentDTO studentDTO);

    StudentDTO updateStudent(String registrationNumber, StudentDTO studentDTO);

    void deleteStudent(String registrationNumber);
}
