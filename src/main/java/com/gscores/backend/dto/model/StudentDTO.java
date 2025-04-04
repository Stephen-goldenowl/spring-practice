package com.gscores.backend.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private String registrationNumber;
    private Set<ResultDTO> results;
    private String foreignLanguageCode;
}
