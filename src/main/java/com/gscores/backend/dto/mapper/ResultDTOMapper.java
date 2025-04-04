package com.gscores.backend.dto.mapper;

import com.gscores.backend.dto.model.ResultDTO;
import com.gscores.backend.entity.Result;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResultDTOMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public ResultDTO mapToDTO(Result result) {
        modelMapper
                .typeMap(Result.class, ResultDTO.class)
                .addMapping(src -> src.getSubject().getName(), ResultDTO::setSubject);
        return modelMapper.map(result, ResultDTO.class);
    }

    public Result mapToEntity(ResultDTO resultDTO) {
        return modelMapper.map(resultDTO, Result.class);
    }
}