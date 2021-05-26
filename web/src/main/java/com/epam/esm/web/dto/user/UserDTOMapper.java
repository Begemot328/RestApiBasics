package com.epam.esm.web.dto.user;

import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.web.controller.UserController;
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

@Component
public class UserDTOMapper {
    private final ModelMapper mapper;

    @Autowired
    public UserDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;

        Converter<Set<Role>, Set<String>> converterToDTOSet =
                set -> set.getSource().stream().map(Role::getValue).collect(Collectors.toSet());

        mapper.typeMap(User.class, UserDTO.class).addMappings(
                newMapper -> newMapper.using(converterToDTOSet).map(
                        User::getRoles, UserDTO::setRoles));
    }

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        try {
            Link link = linkTo(methodOn(UserController.class).readUser(user.getId())).withSelfRel();
            userDTO.add(link);
        } catch (NotFoundException e) {
            throw new DTOException(e);
        }
        return userDTO;
    }

    public User toUser(UserDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }

    public CollectionModel<UserDTO> toUserDTOList(List<User> users) {
        return CollectionModel.of(
                users.stream().map(this::toUserDTO).collect(Collectors.toList()));
    }
}
