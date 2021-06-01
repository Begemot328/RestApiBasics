package com.epam.esm.web.filter;

import com.epam.esm.web.dto.ExceptionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain)
            throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            commence(request, response, e);
        }
    }

    private void commence(HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse,
                          Exception e)
            throws IOException {
        ExceptionDTO response = new ExceptionDTO(
                e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setHeader("Content-Type", "application/json");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}