package com.gscores.backend.service.impl;

import com.gscores.backend.dto.model.ResultDTO;
import com.gscores.backend.dto.model.ResultStat;
import com.gscores.backend.entity.Result;
import com.gscores.backend.entity.Student;
import com.gscores.backend.entity.Subject;
import com.gscores.backend.exception.ResourceNotFoundException;
import com.gscores.backend.repository.ResultRepository;
import com.gscores.backend.repository.StudentRepository;
import com.gscores.backend.repository.SubjectRepository;
import com.gscores.backend.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gscores.backend.utils.SlugConverter.slugify;

@RequiredArgsConstructor
@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;

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
                        .build());
    }

    @Override
    @Transactional
    public ResultDTO createResult(String registrationNumber, ResultDTO resultDTO) {
        Student student = studentRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with registration number [%s] not found"
                                .formatted(registrationNumber)));

        Subject subject = subjectRepository.findByName(resultDTO.getSubject())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject with name [%s] not found"
                                .formatted(resultDTO.getSubject())));

        // Check if result already exists
        if (resultRepository.existsByStudentAndSubject(student, subject)) {
            throw new IllegalArgumentException(
                    "Result already exists for student [%s] and subject [%s]"
                            .formatted(registrationNumber, resultDTO.getSubject()));
        }

        Result result = new Result();
        result.setStudent(student);
        result.setSubject(subject);
        result.setScore(resultDTO.getScore());

        Result savedResult = resultRepository.save(result);
        return new ResultDTO(savedResult.getSubject().getName(), savedResult.getScore());
    }

    @Override
    @Transactional
    public ResultDTO updateResult(String registrationNumber, String subjectName, ResultDTO resultDTO) {
        Student student = studentRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with registration number [%s] not found"
                                .formatted(registrationNumber)));

        Subject subject = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject with name [%s] not found"
                                .formatted(subjectName)));

        Result result = resultRepository.findByStudentAndSubject(student, subject)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Result not found for student [%s] and subject [%s]"
                                .formatted(registrationNumber, subjectName)));

        result.setScore(resultDTO.getScore());
        Result updatedResult = resultRepository.save(result);
        return new ResultDTO(updatedResult.getSubject().getName(), updatedResult.getScore());
    }

    @Override
    @Transactional
    public void deleteResult(String registrationNumber, String subjectName) {
        Student student = studentRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with registration number [%s] not found"
                                .formatted(registrationNumber)));

        Subject subject = subjectRepository.findByName(subjectName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject with name [%s] not found"
                                .formatted(subjectName)));

        Result result = resultRepository.findByStudentAndSubject(student, subject)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Result not found for student [%s] and subject [%s]"
                                .formatted(registrationNumber, subjectName)));

        resultRepository.delete(result);
    }
}
