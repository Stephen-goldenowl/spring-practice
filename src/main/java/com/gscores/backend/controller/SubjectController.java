package com.gscores.backend.controller;

import com.gscores.backend.common.RestResponse;
import com.gscores.backend.dto.model.SubjectDTO;
import com.gscores.backend.service.SubjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subjects", description = "APIs for retrieving subject data")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<RestResponse<List<SubjectDTO>>> getSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        RestResponse<List<SubjectDTO>> restResponse = RestResponse.<List<SubjectDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(subjects)
                .build();
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RestResponse<SubjectDTO>> createSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
        SubjectDTO createdSubject = subjectService.createSubject(subjectDTO);
        RestResponse<SubjectDTO> restResponse = RestResponse.<SubjectDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Subject created successfully")
                .data(createdSubject)
                .build();
        return new ResponseEntity<>(restResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<SubjectDTO>> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectDTO subjectDTO) {
        SubjectDTO updatedSubject = subjectService.updateSubject(id, subjectDTO);
        RestResponse<SubjectDTO> restResponse = RestResponse.<SubjectDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Subject updated successfully")
                .data(updatedSubject)
                .build();
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        RestResponse<Void> restResponse = RestResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Subject deleted successfully")
                .build();
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }
}
