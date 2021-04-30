package com.epam.esm.service.service.user;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.service.EntityService;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * {@link Tag} service interface.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public interface UserService extends EntityService<User> {

    List<User> read(MultiValueMap<String, String> params) throws NotFoundException, BadRequestException;
}
