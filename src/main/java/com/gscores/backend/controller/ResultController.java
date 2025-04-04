package com.gscores.backend.controller;

import com.gscores.backend.common.ApiResponse;
import com.gscores.backend.dto.model.ResultStat;
import com.gscores.backend.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/results")
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/stats/{subjectName}")
    public ResponseEntity<ApiResponse<List<ResultStat>>> getResultStatsBySubjectName(@PathVariable("subjectName") String subjectName) {
        List<ResultStat> resultStats = resultService.getResultStatsBySubjectName(subjectName);
        ApiResponse<List<ResultStat>> apiResponse = ApiResponse.<List<ResultStat>>builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(resultStats)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
