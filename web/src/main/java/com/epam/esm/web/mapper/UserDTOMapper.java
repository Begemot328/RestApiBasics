package com.epam.esm.web.mapper;

import com.epam.esm.model.entity.User;
import com.epam.esm.web.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserDTOMapper {
    private ModelMapper mapper;

    @Autowired
    public UserDTOMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UserDTO toUserDTO(User user) {
        return mapper.map(user, UserDTO.class);
    }

    public User toUser(UserDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }

}