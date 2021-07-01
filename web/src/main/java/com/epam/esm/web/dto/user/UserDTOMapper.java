package com.epam.esm.web.dto.user;

import com.epam.esm.persistence.model.entity.Role;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.dto.tag.TagDTO;
import com.epam.esm.web.exceptions.DTOException;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
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
public class UserDTOMapper {
    private final ModelMapper mapper;

    /**
     * Constructor.
     *
     * @param mapper {@link ModelMapper} bean to add mapping.
     */
    @Autowired
    public UserDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;

        Converter<Set<Role>, Set<String>> converterToDTOSet =
                set -> set.getSource().stream().map(Role::getName).collect(Collectors.toSet());

        mapper.typeMap(User.class, UserDTO.class).addMappings(
                newMapper -> newMapper.using(converterToDTOSet).map(
                        User::getRoles, UserDTO::setRoles));
    }

    /**
     * {@link User} to  {@link UserDTO} mapping.
     *
     * @param user {@link User} to map.
     */
    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        try {
            Link link = linkTo(methodOn(UserController.class).get(user.getId()))
                    .withSelfRel();
            userDTO.add(link);
        } catch (NotFoundException e) {
            throw new DTOException(e);
        }
        return userDTO;
    }

    /**
     * {@link List} of {@link User} to {@link CollectionModel} of {@link UserDTO} mapping.
     *
     * @param users {@link List} of {@link User} to map.
     * @return {@link CollectionModel} of {@link UserDTO} objects.
     */
    public CollectionModel<UserDTO> toUserDTOList(List<User> users) {
        return CollectionModel.of(
                users.stream().map(this::toUserDTO).collect(Collectors.toList()));
    }

    /**
     * {@link UserDTO} to  {@link User} mapping.
     *
     * @param userDTO {@link UserDTO} to map.
     */
    public User toUser(UserDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }

}
