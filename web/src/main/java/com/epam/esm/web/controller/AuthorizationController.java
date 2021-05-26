package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.web.security.userdetails.RegistrationRequest;
import com.epam.esm.web.security.userdetails.Account;
import com.epam.esm.service.service.user.UserService;
import com.epam.esm.web.security.userdetails.AuthorizationRequest;
import com.epam.esm.web.dto.user.UserDTO;
import com.epam.esm.web.dto.user.UserDTOMapper;
import com.epam.esm.web.security.jwt.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final UserDTOMapper userDTOMapper;

    public AuthorizationController(AuthenticationManager authenticationManager,
                                   JwtTokenUtil jwtTokenUtil,
                                   UserService userService,
                                   UserDTOMapper userDTOMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody AuthorizationRequest request) {

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword());
            Authentication authenticate = authenticationManager
                    .authenticate(token);

            Account user = (Account) authenticate.getPrincipal();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION,
                Collections.singletonList(jwtTokenUtil.generateAccessToken(user)));
            return new ResponseEntity<>(userDTOMapper.toUserDTO(user.getUser()), headers, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO>  register(@RequestBody RegistrationRequest request)
            throws BadRequestException, ValidationException {
        User user = new User(request.getFirstName(), request.getLastName(),
                request.getUsername(), BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12)));
        user.setActive(true);
        user.addRole(Role.USER);
        userService.create(user);
         return new ResponseEntity<>(userDTOMapper.toUserDTO(user), HttpStatus.CREATED);
    }
}
