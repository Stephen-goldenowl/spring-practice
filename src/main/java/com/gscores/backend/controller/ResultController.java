package com.gscores.backend.controller;

import com.gscores.backend.common.RestResponse;
import com.gscores.backend.dto.model.ResultDTO;
import com.gscores.backend.dto.model.ResultStat;
import com.gscores.backend.service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Result", description = "APIs for result statistics and management")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/results")
public class ResultController {

        private final ResultService resultService;

        @Operation(summary = "Get statistics by subject", description = "Returns the statistics of students' scores for a given subject")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successful retrieval of result statistics"),
                        @ApiResponse(responseCode = "404", description = "Subject not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/stats/{subjectName}")
        public ResponseEntity<RestResponse<List<ResultStat>>> getResultStatsBySubjectName(
                        @PathVariable("subjectName") String subjectName) {
                List<ResultStat> resultStats = resultService.getResultStatsBySubjectName(subjectName);
                RestResponse<List<ResultStat>> restResponse = RestResponse.<List<ResultStat>>builder()
                                .status(HttpStatus.OK.value())
                                .message("OK")
                                .data(resultStats)
                                .build();
                return new ResponseEntity<>(restResponse, HttpStatus.OK);
        }

        @Operation(summary = "Create a new result", description = "Create a new result for a student in a specific subject")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Result created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input or result already exists"),
                        @ApiResponse(responseCode = "404", description = "Student or subject not found")
        })
        @PostMapping("/students/{registrationNumber}")
        public ResponseEntity<RestResponse<ResultDTO>> createResult(
                        @Parameter(description = "Student registration number", required = true) @PathVariable String registrationNumber,
                        @Valid @RequestBody ResultDTO resultDTO) {
                ResultDTO createdResult = resultService.createResult(registrationNumber, resultDTO);
                RestResponse<ResultDTO> restResponse = RestResponse.<ResultDTO>builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Result created successfully")
                                .data(createdResult)
                                .build();
                return new ResponseEntity<>(restResponse, HttpStatus.CREATED);
        }

        @Operation(summary = "Update a result", description = "Update an existing result for a student in a specific subject")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Result updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "404", description = "Student, subject, or result not found")
        })
        @PutMapping("/students/{registrationNumber}/subjects/{subjectName}")
        public ResponseEntity<RestResponse<ResultDTO>> updateResult(
                        @Parameter(description = "Student registration number", required = true) @PathVariable String registrationNumber,
                        @Parameter(description = "Subject name", required = true) @PathVariable String subjectName,
                        @Valid @RequestBody ResultDTO resultDTO) {
                ResultDTO updatedResult = resultService.updateResult(registrationNumber, subjectName, resultDTO);
                RestResponse<ResultDTO> restResponse = RestResponse.<ResultDTO>builder()
                                .status(HttpStatus.OK.value())
                                .message("Result updated successfully")
                                .data(updatedResult)
                                .build();
                return new ResponseEntity<>(restResponse, HttpStatus.OK);
        }

        @Operation(summary = "Delete a result", description = "Delete a result for a student in a specific subject")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Result deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Student, subject, or result not found")
        })
        @DeleteMapping("/students/{registrationNumber}/subjects/{subjectName}")
        public ResponseEntity<RestResponse<Void>> deleteResult(
                        @Parameter(description = "Student registration number", required = true) @PathVariable String registrationNumber,
                        @Parameter(description = "Subject name", required = true) @PathVariable String subjectName) {
                resultService.deleteResult(registrationNumber, subjectName);
                RestResponse<Void> restResponse = RestResponse.<Void>builder()
                                .status(HttpStatus.OK.value())
                                .message("Result deleted successfully")
                                .build();
                return new ResponseEntity<>(restResponse, HttpStatus.OK);
        }
}
