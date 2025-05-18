package com.gscores.backend.controller;

import com.gscores.backend.common.RestResponse;
import com.gscores.backend.dto.model.StudentDTO;
import com.gscores.backend.dto.model.TopStudentDTO;
import com.gscores.backend.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Students", description = "APIs for retrieving student data and top students by group")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/students")
public class StudentController {

        private final StudentService studentService;

        @Operation(summary = "Get student by registration number", description = "Retrieve a student's full information using their registration number", parameters = {
                        @Parameter(name = "registrationNumber", in = ParameterIn.PATH, description = "Student registration number", required = true, example = "01000010")
        })
        @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
        @GetMapping("/{registrationNumber}")
        public ResponseEntity<RestResponse<StudentDTO>> getStudentByRegistrationNumber(
                        @PathVariable("registrationNumber") String registrationNumber) {
                StudentDTO student = studentService.getStudentByRegistrationNumber(registrationNumber);
                RestResponse<StudentDTO> restResponse = RestResponse.<StudentDTO>builder()
                                .status(HttpStatus.OK.value())
                                .message("OK")
                                .data(student)
                                .build();
                return new ResponseEntity<>(restResponse, HttpStatus.OK);
        }

        @Operation(summary = "Get top students by group", description = "Retrieve top students in a specific group (e.g., A, B, C). Default top is 10.", parameters = {
                        @Parameter(name = "group", in = ParameterIn.PATH, description = "Student group name (e.g., A, B, C)", required = true, example = "A"),
                        @Parameter(name = "top", in = ParameterIn.QUERY, description = "Number of top students to retrieve", required = false, example = "5")
        }, responses = {
                        @ApiResponse(responseCode = "200", description = "List of top students", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TopStudentDTO.class))))
        })
        @GetMapping("/topGroups/{group}")
        public ResponseEntity<RestResponse<List<TopStudentDTO>>> getTopStudentsByGroup(
                        @PathVariable("group") String group,
                        @RequestParam(required = false, value = "top", defaultValue = "10") int top) {
                List<TopStudentDTO> students = studentService.getTopStudentsByGroup(group, top);
                RestResponse<List<TopStudentDTO>> restResponse = RestResponse.<List<TopStudentDTO>>builder()
                                .status(HttpStatus.OK.value())
                                .message("OK")
                                .data(students)
                                .build();
                return new ResponseEntity<>(restResponse, HttpStatus.OK);
        }

        @Operation(summary = "Create a new student", description = "Create a new student with the provided information", responses = {
                        @ApiResponse(responseCode = "201", description = "Student created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class)))
        })
        @PostMapping
        public ResponseEntity<RestResponse<StudentDTO>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
                StudentDTO createdStudent = studentService.createStudent(studentDTO);
                RestResponse<StudentDTO> restResponse = RestResponse.<StudentDTO>builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Student created successfully")
                                .data(createdStudent)
                                .build();
                return new ResponseEntity<>(restResponse, HttpStatus.CREATED);
        }

        @Operation(summary = "Update a student", description = "Update an existing student's information using their registration number", parameters = {
                        @Parameter(name = "registrationNumber", in = ParameterIn.PATH, description = "Student registration number", required = true, example = "01000010")
        })
        @PutMapping("/{registrationNumber}")
        public ResponseEntity<RestResponse<StudentDTO>> updateStudent(
                        @PathVariable String registrationNumber,
                        @Valid @RequestBody StudentDTO studentDTO) {
                StudentDTO updatedStudent = studentService.updateStudent(registrationNumber, studentDTO);
                RestResponse<StudentDTO> restResponse = RestResponse.<StudentDTO>builder()
                                .status(HttpStatus.OK.value())
                                .message("Student updated successfully")
                                .data(updatedStudent)
                                .build();
                return new ResponseEntity<>(restResponse, HttpStatus.OK);
        }

        @Operation(summary = "Delete a student", description = "Delete a student using their registration number", parameters = {
                        @Parameter(name = "registrationNumber", in = ParameterIn.PATH, description = "Student registration number", required = true, example = "01000010")
        })
        @DeleteMapping("/{registrationNumber}")
        public ResponseEntity<RestResponse<Void>> deleteStudent(@PathVariable String registrationNumber) {
                studentService.deleteStudent(registrationNumber);
                RestResponse<Void> restResponse = RestResponse.<Void>builder()
                                .status(HttpStatus.OK.value())
                                .message("Student deleted successfully")
                                .build();
                return new ResponseEntity<>(restResponse, HttpStatus.OK);
        }
}
