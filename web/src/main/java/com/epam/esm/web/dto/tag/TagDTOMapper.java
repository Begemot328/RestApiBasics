package com.epam.esm.web.dto.tag;

import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.exceptions.DTOException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagDTOMapper {
    private final ModelMapper mapper;

    @Autowired
    public TagDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public TagDTO toTagDTO(Tag tag) {
        TagDTO tagDTO = mapper.map(tag, TagDTO.class);
        try {
            Link link = linkTo(methodOn(TagController.class).get(tag.getId()))
                    .withSelfRel();
            tagDTO.add(link);
        } catch (NotFoundException e) {
            throw new DTOException(e);
        }
        return tagDTO;

    }

    public CollectionModel<TagDTO> toTagDTOList(List<Tag> tags) {
        return CollectionModel.of(
                tags.stream().map(this::toTagDTO).collect(Collectors.toList()));
    }

    public Tag toTag(TagDTO tagDTO) {
        return mapper.map(tagDTO, Tag.class);
    }

}
