package com.gscores.backend.service.impl;

import com.gscores.backend.dto.mapper.StudentDTOMapper;
import com.gscores.backend.dto.model.ResultDTO;
import com.gscores.backend.dto.model.StudentDTO;
import com.gscores.backend.entity.Student;
import com.gscores.backend.exception.ResourceNotFoundException;
import com.gscores.backend.repository.ResultRepository;
import com.gscores.backend.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ResultRepository resultRepository;
    @Mock
    private StudentDTOMapper studentDTOMapper;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void getStudentByRegistrationNumber_returnsFromCache() {
        String regNum = "123";
        StudentDTO cachedStudent = new StudentDTO();
        when(valueOperations.get("student::" + regNum)).thenReturn(cachedStudent);

        StudentDTO result = studentService.getStudentByRegistrationNumber(regNum);

        assertSame(cachedStudent, result);
        verify(studentRepository, never()).findByRegistrationNumber(any());
    }

    @Test
    void getStudentByRegistrationNumber_returnsFromDbAndCaches() {
        String regNum = "123";
        Student student = new Student();
        StudentDTO studentDTO = new StudentDTO();
        Set<ResultDTO> results = Set.of();

        when(valueOperations.get("student::" + regNum)).thenReturn(null);
        when(studentRepository.findByRegistrationNumber(regNum)).thenReturn(Optional.of(student));
        when(studentDTOMapper.mapToDTO(student)).thenReturn(studentDTO);
        when(resultRepository.findByStudentRegistrationNumber(regNum)).thenReturn(Set.of());

        StudentDTO result = studentService.getStudentByRegistrationNumber(regNum);

        assertSame(studentDTO, result);
        verify(valueOperations).set(eq("student::" + regNum), eq(studentDTO), any());
    }

    @Test
    void getStudentByRegistrationNumber_notFound_throwsException() {
        String regNum = "notfound";
        when(valueOperations.get("student::" + regNum)).thenReturn(null);
        when(studentRepository.findByRegistrationNumber(regNum)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentByRegistrationNumber(regNum));
    }
}