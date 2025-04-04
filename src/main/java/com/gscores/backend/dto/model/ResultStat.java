package com.gscores.backend.dto.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResultStat {
    private String range;
    private Long totalStudents;
}
