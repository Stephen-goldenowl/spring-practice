package com.gscores.backend.controller;

import com.gscores.backend.common.ApiResponse;
import com.gscores.backend.dto.model.StudentDTO;
import com.gscores.backend.dto.model.TopStudentDTO;
import com.gscores.backend.repository.StudentRepository;
import com.gscores.backend.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{registrationNumber}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentByRegistrationNumber(@PathVariable("registrationNumber") String registrationNumber) {
        StudentDTO student = studentService.getStudentByRegistrationNumber(registrationNumber);
        ApiResponse<StudentDTO> apiResponse = ApiResponse.<StudentDTO>builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(student)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/topGroups/{group}")
    public ResponseEntity<ApiResponse<List<TopStudentDTO>>> getTopStudentsByGroup(@PathVariable("group") String group,
                                                                                  @RequestParam(required = false, value = "top", defaultValue = "10") int top) {
        List<TopStudentDTO> students = studentService.getTopStudentsByGroup(group, top);
        ApiResponse<List<TopStudentDTO>> apiResponse = ApiResponse.<List<TopStudentDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(students)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
