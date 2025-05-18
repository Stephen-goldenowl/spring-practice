package com.gscores.backend.service.impl;

import com.gscores.backend.dto.mapper.SubjectDTOMapper;
import com.gscores.backend.dto.model.SubjectDTO;
import com.gscores.backend.entity.Subject;
import com.gscores.backend.exception.ResourceNotFoundException;
import com.gscores.backend.repository.SubjectRepository;
import com.gscores.backend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectDTOMapper subjectDTOMapper;

    @Override
    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(subjectDTOMapper::mapToDTO)
                .toList();
    }

    @Override
    public SubjectDTO createSubject(SubjectDTO subjectDTO) {
        Subject subject = subjectRepository.save(subjectDTOMapper.mapToEntity(subjectDTO));
        return subjectDTOMapper.mapToDTO(subject);
    }

    @Override
    @Transactional
    public SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found with id: " + id);
        }

        Subject updatedSubject = subjectDTOMapper.mapToEntity(subjectDTO);
        updatedSubject.setId(id);
        return subjectDTOMapper.mapToDTO(subjectRepository.save(updatedSubject));
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new RuntimeException("Subject not found with id: " + id);
        }
        subjectRepository.deleteById(id);
    }
}
