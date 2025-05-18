package com.gscores.backend.dto.mapper;

import com.gscores.backend.dto.model.StudentDTO;
import com.gscores.backend.entity.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubjectDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();


    public StudentDTO mapToDTO(Student student) {
        modelMapper
                .typeMap(Student.class, StudentDTO.class)
                .addMapping(src -> null, StudentDTO::setResults);
        return modelMapper.map(student, StudentDTO.class);
    }

    public Student mapToEntity(StudentDTO studentDTO) {
        return modelMapper.map(studentDTO, Student.class);
    }
}