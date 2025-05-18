package com.gscores.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gscores.backend.common.RestResponse;
import com.gscores.backend.dto.model.SubjectDTO;
import com.gscores.backend.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getSubjects_success() throws Exception {
        SubjectDTO subjectDTO = new SubjectDTO();
        Mockito.when(subjectService.getAllSubjects()).thenReturn(List.of(subjectDTO));

        mockMvc.perform(get("/api/v1/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void createSubject_success() throws Exception {
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setName("Math"); // ✅ Required field

        Mockito.when(subjectService.createSubject(any(SubjectDTO.class))).thenReturn(subjectDTO);

        mockMvc.perform(post("/api/v1/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subjectDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is(201)))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void updateSubject_success() throws Exception {
        Long id = 1L;
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setName("Physics"); // ✅ Required field

        Mockito.when(subjectService.updateSubject(eq(id), any(SubjectDTO.class))).thenReturn(subjectDTO);

        mockMvc.perform(put("/api/v1/subjects/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subjectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void deleteSubject_success() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v1/subjects/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("deleted")));
    }
}