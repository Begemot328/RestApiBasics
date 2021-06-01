package com.epam.esm.web.controller;

import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.web.dto.user.UserDTO;
import com.epam.esm.web.dto.user.UserDTOMapper;
import com.epam.esm.web.security.auth.Account;
import com.epam.esm.web.security.auth.UserAuthorizationRequest;
import com.epam.esm.web.security.auth.UserRegistrationRequest;
import com.epam.esm.web.security.jwt.JwtTokenUtil;
import com.epam.esm.web.security.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
public class AuthorizationController {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationService authenticationService;
    private final UserDTOMapper userDTOMapper;

    public AuthorizationController(JwtTokenUtil jwtTokenUtil,
                                   AuthenticationService authenticationService,
                                   UserDTOMapper userDTOMapper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationService = authenticationService;
        this.userDTOMapper = userDTOMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserAuthorizationRequest request) {
        Account user = authenticationService.login(request);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION,
                Collections.singletonList(jwtTokenUtil.generateAccessToken(user)));
        return new ResponseEntity<>(userDTOMapper.toUserDTO(user.getUser()),
                headers, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegistrationRequest request)
            throws BadRequestException, ValidationException {

        return new ResponseEntity<>(
                userDTOMapper.toUserDTO(
                        authenticationService.register(request)), HttpStatus.CREATED);
    }
}
