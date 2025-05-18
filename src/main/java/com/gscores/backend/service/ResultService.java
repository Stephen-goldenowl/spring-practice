package com.gscores.backend.service;

import com.gscores.backend.dto.model.ResultDTO;
import com.gscores.backend.dto.model.ResultStat;

import java.util.List;

public interface ResultService {
    List<ResultStat> getResultStatsBySubjectName(String subjectName);

    ResultDTO createResult(String registrationNumber, ResultDTO resultDTO);

    ResultDTO updateResult(String registrationNumber, String subjectName, ResultDTO resultDTO);

    void deleteResult(String registrationNumber, String subjectName);
}
