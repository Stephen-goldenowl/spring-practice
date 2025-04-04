package com.gscores.backend.service.impl;

import com.gscores.backend.dto.model.ResultStat;
import com.gscores.backend.entity.Result;
import com.gscores.backend.entity.Subject;
import com.gscores.backend.repository.ResultRepository;
import com.gscores.backend.repository.SubjectRepository;
import com.gscores.backend.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gscores.backend.utils.SlugConverter.slugify;

@RequiredArgsConstructor
@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public List<ResultStat> getResultStatsBySubjectName(String subjectName) {
        Map<String, String> subjectMap = new HashMap<>();
        List<Subject> subjects = subjectRepository.findAll();

        for (Subject subject : subjects) {
            subjectMap.put(slugify(subject.getName()), subject.getName());
        }

        return List.of(ResultStat.builder()
                        .range("8-10")
                        .totalStudents(resultRepository.countHighScores(subjectMap.get(subjectName)))
                        .build(),

                ResultStat.builder()
                        .range("6-8")
                        .totalStudents(resultRepository.countMediumHighScores(subjectMap.get(subjectName)))
                        .build(),

                ResultStat.builder()
                        .range("4-6")
                        .totalStudents(resultRepository.countMediumLowScores(subjectMap.get(subjectName)))
                        .build(),

                ResultStat.builder()
                        .range("0-4")
                        .totalStudents(resultRepository.countLowScores(subjectMap.get(subjectName)))
                        .build()
        );
    }
}
