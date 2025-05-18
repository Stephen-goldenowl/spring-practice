package com.gscores.backend.dto.mapper;

import com.gscores.backend.dto.model.SubjectDTO;
import com.gscores.backend.entity.Subject;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubjectDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();


    public SubjectDTO mapToDTO(Subject subject) {
        return modelMapper.map(subject, SubjectDTO.class);
    }

    public Subject mapToEntity(SubjectDTO studentDTO) {
        return modelMapper.map(studentDTO, Subject.class);
    }
}