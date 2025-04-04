package com.gscores.backend.service.impl;

import com.gscores.backend.dto.mapper.ResultDTOMapper;
import com.gscores.backend.dto.mapper.StudentDTOMapper;
import com.gscores.backend.dto.model.ResultDTO;
import com.gscores.backend.dto.model.StudentDTO;
import com.gscores.backend.dto.model.TopStudentDTO;
import com.gscores.backend.entity.Student;
import com.gscores.backend.exception.ResourceNotFoundException;
import com.gscores.backend.repository.ResultRepository;
import com.gscores.backend.repository.StudentRepository;
import com.gscores.backend.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gscores.backend.utils.AppConstants.GROUPS;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ResultRepository resultRepository;
    private final StudentDTOMapper studentDTOMapper;

    @Override
    public StudentDTO getStudentByRegistrationNumber(String registrationNumber) {
        StudentDTO studentDTO = studentDTOMapper.mapToDTO(studentRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student with registration number [%s] not found".formatted(registrationNumber))));

        Set<ResultDTO> resultDTO = resultRepository.findByStudentRegistrationNumber(studentDTO.getRegistrationNumber())
                .stream()
                .map(r ->new ResultDTO(r.getSubject().getName(), r.getScore()))
                .collect(Collectors.toSet());

        studentDTO.setResults(resultDTO);

        return studentDTO;
    }

    @Override
    public List<TopStudentDTO> getTopStudentsByGroup(String group, int top) {
        List<String> subjects = List.of(GROUPS.get(group).split(", "));

        Pageable pageable = PageRequest.of(0, top, Sort.by(Sort.Order.desc("totalScore")));

        return studentRepository
                .findTopStudentsByGroup(subjects, pageable)
                .stream()
                .map(row -> {
                    StudentDTO studentDTO = studentDTOMapper.mapToDTO((Student) row[0]);

                    Set<ResultDTO> resultDTO = resultRepository.findByStudentRegistrationNumber(studentDTO.getRegistrationNumber())
                            .stream()
                            .map(r ->new ResultDTO(r.getSubject().getName(), r.getScore()))
                            .collect(Collectors.toSet());

                    studentDTO.setResults(resultDTO);

                    Double totalScore = (Double) row[1];

                    return TopStudentDTO.builder()
                            .student(studentDTO)
                            .totalScore(totalScore)
                            .build();
                })
                .toList();
    }
}
