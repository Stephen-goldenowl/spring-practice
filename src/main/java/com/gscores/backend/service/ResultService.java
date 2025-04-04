package com.gscores.backend.service;

import com.gscores.backend.dto.model.ResultStat;

import java.util.List;

public interface ResultService {
    List<ResultStat> getResultStatsBySubjectName(String subjectName);
}
