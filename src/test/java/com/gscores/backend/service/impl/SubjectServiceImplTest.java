package com.gscores.backend.service.impl;

import com.gscores.backend.dto.mapper.SubjectDTOMapper;
import com.gscores.backend.dto.model.SubjectDTO;
import com.gscores.backend.entity.Subject;
import com.gscores.backend.exception.ResourceNotFoundException;
import com.gscores.backend.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectServiceImplTest {

    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private SubjectDTOMapper subjectDTOMapper;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSubjects_success() {
        Subject subject = new Subject();
        SubjectDTO subjectDTO = new SubjectDTO();
        when(subjectRepository.findAll()).thenReturn(List.of(subject));
        when(subjectDTOMapper.mapToDTO(subject)).thenReturn(subjectDTO);

        List<SubjectDTO> result = subjectService.getAllSubjects();

        assertEquals(1, result.size());
        assertSame(subjectDTO, result.get(0));
    }

    @Test
    void createSubject_success() {
        SubjectDTO subjectDTO = new SubjectDTO();
        Subject subject = new Subject();
        when(subjectDTOMapper.mapToEntity(subjectDTO)).thenReturn(subject);
        when(subjectRepository.save(subject)).thenReturn(subject);
        when(subjectDTOMapper.mapToDTO(subject)).thenReturn(subjectDTO);

        SubjectDTO result = subjectService.createSubject(subjectDTO);

        assertSame(subjectDTO, result);
    }

    @Test
    void updateSubject_success() {
        Long id = 1L;
        SubjectDTO subjectDTO = new SubjectDTO();
        Subject subject = new Subject();
        subject.setId(id);

        when(subjectRepository.existsById(id)).thenReturn(true);
        when(subjectDTOMapper.mapToEntity(subjectDTO)).thenReturn(subject);
        when(subjectRepository.save(subject)).thenReturn(subject);
        when(subjectDTOMapper.mapToDTO(subject)).thenReturn(subjectDTO);

        SubjectDTO result = subjectService.updateSubject(id, subjectDTO);

        assertSame(subjectDTO, result);
    }

    @Test
    void updateSubject_notFound() {
        Long id = 1L;
        SubjectDTO subjectDTO = new SubjectDTO();
        when(subjectRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> subjectService.updateSubject(id, subjectDTO));
    }

    @Test
    void deleteSubject_success() {
        Long id = 1L;
        when(subjectRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> subjectService.deleteSubject(id));
        verify(subjectRepository).deleteById(id);
    }

    @Test
    void deleteSubject_notFound() {
        Long id = 1L;
        when(subjectRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> subjectService.deleteSubject(id));
    }
}