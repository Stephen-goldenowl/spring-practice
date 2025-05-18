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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResultServiceImplTest {

    @Mock
    private ResultRepository resultRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private ResultServiceImpl resultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createResult_success() {
        String regNum = "123";
        String subjectName = "Math";
        Student student = new Student();
        Subject subject = new Subject();
        subject.setName(subjectName);
        ResultDTO dto = new ResultDTO(subjectName, 9.0);

        when(studentRepository.findByRegistrationNumber(regNum)).thenReturn(Optional.of(student));
        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));
        when(resultRepository.existsByStudentAndSubject(student, subject)).thenReturn(false);

        Result saved = new Result();
        saved.setSubject(subject);
        saved.setScore(9.0);
        when(resultRepository.save(any())).thenReturn(saved);

        ResultDTO result = resultService.createResult(regNum, dto);

        assertEquals(subjectName, result.getSubject());
        assertEquals(9.0, result.getScore());
    }

    @Test
    void createResult_studentNotFound() {
        when(studentRepository.findByRegistrationNumber("x")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> resultService.createResult("x", new ResultDTO("Math", 8.0)));
    }

    @Test
    void createResult_subjectNotFound() {
        String regNum = "123";
        when(studentRepository.findByRegistrationNumber(regNum)).thenReturn(Optional.of(new Student()));
        when(subjectRepository.findByName("Math")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> resultService.createResult(regNum, new ResultDTO("Math", 8.0)));
    }

    @Test
    void createResult_alreadyExists() {
        String regNum = "123";
        Student student = new Student();
        Subject subject = new Subject();
        when(studentRepository.findByRegistrationNumber(regNum)).thenReturn(Optional.of(student));
        when(subjectRepository.findByName("Math")).thenReturn(Optional.of(subject));
        when(resultRepository.existsByStudentAndSubject(student, subject)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> resultService.createResult(regNum, new ResultDTO("Math", 8.0)));
    }

    @Test
    void updateResult_success() {
        String regNum = "123";
        String subjectName = "Math";
        Student student = new Student();
        Subject subject = new Subject();
        subject.setName(subjectName);
        Result result = new Result();
        result.setSubject(subject);

        when(studentRepository.findByRegistrationNumber(regNum)).thenReturn(Optional.of(student));
        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));
        when(resultRepository.findByStudentAndSubject(student, subject)).thenReturn(Optional.of(result));
        when(resultRepository.save(any())).thenReturn(result);

        ResultDTO updated = resultService.updateResult(regNum, subjectName, new ResultDTO(subjectName, 7.5));
        assertEquals(subjectName, updated.getSubject());
        assertEquals(7.5, updated.getScore());
    }

    @Test
    void updateResult_notFound() {
        when(studentRepository.findByRegistrationNumber("x")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> resultService.updateResult("x", "Math", new ResultDTO("Math", 7.0)));
    }

    @Test
    void deleteResult_success() {
        String regNum = "123";
        String subjectName = "Math";
        Student student = new Student();
        Subject subject = new Subject();
        Result result = new Result();

        when(studentRepository.findByRegistrationNumber(regNum)).thenReturn(Optional.of(student));
        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));
        when(resultRepository.findByStudentAndSubject(student, subject)).thenReturn(Optional.of(result));

        assertDoesNotThrow(() -> resultService.deleteResult(regNum, subjectName));
        verify(resultRepository).delete(result);
    }

    @Test
    void deleteResult_notFound() {
        when(studentRepository.findByRegistrationNumber("x")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> resultService.deleteResult("x", "Math"));
    }
}