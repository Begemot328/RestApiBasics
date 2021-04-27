package com.epam.esm.web.dto;

import com.epam.esm.model.entity.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TagDTOMapper {
    private ModelMapper mapper;

    @Autowired
    public TagDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public TagDTO toTagDTO(Tag tag) {
        return mapper.map(tag, TagDTO.class);
    }

    public Tag toTag(TagDTO tagDTO) {
        return mapper.map(tagDTO, Tag.class);
    }

}
