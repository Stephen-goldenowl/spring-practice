package com.gscores.backend.dto.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;
}
