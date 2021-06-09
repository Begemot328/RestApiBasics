package com.epam.esm.web.security.service;

import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.RoleDAO;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.user.UserService;
import com.epam.esm.model.entity.Account;
import com.epam.esm.web.security.auth.UserAuthorizationRequest;
import com.epam.esm.web.security.auth.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final RoleDAO roleDAO;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserService userService,
                                 AuthenticationManager authenticationManager,
                                 RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public Account login(UserAuthorizationRequest request){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());
        Authentication authenticate = authenticationManager
                .authenticate(token);

        return (Account) authenticate.getPrincipal();
    }

    public User register(UserRegistrationRequest request)
            throws BadRequestException, ValidationException {
        User user = new User(request.getFirstName(), request.getLastName(),
                request.getUsername(), BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12)));
        user.setActive(true);
        user.addRole(roleDAO.getByName("USER"));
        return userService.create(user);
    }
}
