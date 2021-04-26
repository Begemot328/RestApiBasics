package com.epam.esm.web.dto;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.exceptions.DTOException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TagDTOMapper {
    private ModelMapper mapper;

    @Autowired
    public TagDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public TagDTO toTagDTO(Tag tag) {
        if (Objects.isNull(tag)) {
            return null;
        } else {
            TagDTO tagDTO =  mapper.map(tag, TagDTO.class);
            try {
                Link link = linkTo(methodOn(TagController.class).read(tag.getId())).withSelfRel();
                tagDTO.add(link);
                link = linkTo(methodOn(TagController.class).readCertificates(tag.getId())).withRel("/certificates");
            } catch (NotFoundException e) {
                throw new DTOException(e);
            }
            return tagDTO;
        }
    }

    public CollectionModel<TagDTO> toTagDTOList(List<Tag> tags) {
        CollectionModel<TagDTO> tagDTOs = CollectionModel.of(
                tags.stream().map(this::toTagDTO).collect(Collectors.toList()));
            return tagDTOs;
        }

    public Tag toTag(TagDTO tagDTO) {
        return Objects.isNull(tagDTO) ? null : mapper.map(tagDTO, Tag.class);
    }

}
