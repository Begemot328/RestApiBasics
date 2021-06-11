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

/**
 * {@link Tag} to  {@link TagDTO} mapper class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
public class TagDTOMapper {
    private final ModelMapper mapper;

    /**
     * Constructor.
     *
     * @param mapper {@link ModelMapper} bean to add mapping.
     */
    @Autowired
    public TagDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * {@link Tag} to  {@link TagDTO} mapping.
     *
     * @param tag {@link Tag} to map.
     */
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

    /**
     * {@link List} of {@link Tag} to {@link CollectionModel} of {@link TagDTO} mapping.
     *
     * @param tags {@link List} of {@link Tag} to map.
     * @return {@link CollectionModel} of {@link TagDTO} objects.
     */
    public CollectionModel<TagDTO> toTagDTOList(List<Tag> tags) {
        return CollectionModel.of(
                tags.stream().map(this::toTagDTO).collect(Collectors.toList()));
    }

    /**
     * {@link TagDTO} to  {@link Tag} mapping.
     *
     * @param tagDTO {@link TagDTO} to map.
     */
    public Tag toTag(TagDTO tagDTO) {
        return mapper.map(tagDTO, Tag.class);
    }

}
