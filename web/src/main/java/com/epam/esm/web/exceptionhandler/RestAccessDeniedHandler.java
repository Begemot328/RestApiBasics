package com.epam.esm.web.exceptionhandler;

import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.web.dto.ExceptionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * {@link AccessDeniedException} handler class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e)
            throws IOException {
        ExceptionDTO response = new ExceptionDTO(
                e, HttpStatus.FORBIDDEN.value());
        httpServletResponse.setHeader("Content-Type", "application/json");
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}