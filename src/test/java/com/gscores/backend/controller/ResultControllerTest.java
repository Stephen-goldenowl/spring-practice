package com.gscores.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gscores.backend.dto.model.ResultDTO;
import com.gscores.backend.dto.model.ResultStat;
import com.gscores.backend.service.ResultService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResultController.class)
class ResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResultService resultService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getResultStatsBySubjectName_success() throws Exception {
        ResultStat stat = ResultStat.builder().range("8-10").totalStudents(5L).build();
        Mockito.when(resultService.getResultStatsBySubjectName("Math")).thenReturn(List.of(stat));

        mockMvc.perform(get("/api/v1/results/stats/Math"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].range", is("8-10")));
    }

    @Test
    void createResult_success() throws Exception {
        ResultDTO resultDTO = new ResultDTO("Math", 9.0);
        Mockito.when(resultService.createResult(eq("123"), ArgumentMatchers.any(ResultDTO.class)))
                .thenReturn(resultDTO);

        mockMvc.perform(post("/api/v1/results/students/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resultDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is(201)))
                .andExpect(jsonPath("$.data.subject", is("Math")))
                .andExpect(jsonPath("$.data.score", is(9.0)));
    }

    @Test
    void updateResult_success() throws Exception {
        ResultDTO resultDTO = new ResultDTO("Math", 8.5);
        Mockito.when(resultService.updateResult(eq("123"), eq("Math"), ArgumentMatchers.any(ResultDTO.class)))
                .thenReturn(resultDTO);

        mockMvc.perform(put("/api/v1/results/students/123/subjects/Math")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resultDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.data.subject", is("Math")))
                .andExpect(jsonPath("$.data.score", is(8.5)));
    }

    @Test
    void deleteResult_success() throws Exception {
        mockMvc.perform(delete("/api/v1/results/students/123/subjects/Math"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("deleted")));
    }
}