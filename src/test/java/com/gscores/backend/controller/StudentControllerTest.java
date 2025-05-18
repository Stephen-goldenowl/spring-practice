package com.gscores.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gscores.backend.dto.model.StudentDTO;
import com.gscores.backend.dto.model.TopStudentDTO;
import com.gscores.backend.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStudentByRegistrationNumber_success() throws Exception {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setRegistrationNumber("123");
        Mockito.when(studentService.getStudentByRegistrationNumber("123")).thenReturn(studentDTO);

        mockMvc.perform(get("/api/v1/students/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.data.registrationNumber", is("123")));
    }

    @Test
    void getTopStudentsByGroup_success() throws Exception {
        TopStudentDTO topStudentDTO = TopStudentDTO.builder().student(new StudentDTO()).totalScore(99.0).build();
        Mockito.when(studentService.getTopStudentsByGroup(eq("A"), anyInt())).thenReturn(List.of(topStudentDTO));

        mockMvc.perform(get("/api/v1/students/topGroups/A?top=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void createStudent_success() throws Exception {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setRegistrationNumber("123");

        Mockito.when(studentService.createStudent(ArgumentMatchers.any(StudentDTO.class))).thenReturn(studentDTO);

        mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is(201)))
                .andExpect(jsonPath("$.data.registrationNumber", is("123")));
    }

    @Test
    void updateStudent_success() throws Exception {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setRegistrationNumber("123");

        Mockito.when(studentService.updateStudent(eq("123"), ArgumentMatchers.any(StudentDTO.class)))
                .thenReturn(studentDTO);

        mockMvc.perform(put("/api/v1/students/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.data.registrationNumber", is("123")));
    }

    @Test
    void deleteStudent_success() throws Exception {
        mockMvc.perform(delete("/api/v1/students/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("deleted")));
    }
}