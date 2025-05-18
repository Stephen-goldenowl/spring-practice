package com.gscores.backend.dto.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO {

    @NotBlank(message = "Subject cannot be blank")
    private String subject;

    @NotNull(message = "Score cannot be null")
    @Min(value = 0, message = "Score must be greater than or equal to 0")
    @Max(value = 10, message = "Score must be less than or equal to 10")
    private Double score;
}