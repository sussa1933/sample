package com.study.project.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.project.api.user.dto.LoginResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    ObjectMapper mapper = new ObjectMapper();
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        String header = request.getHeader("Request-type");
        if (header == null) {
            log.info("### Authenticated successfully! [Request Client View]");
            response.sendRedirect("/");
        } else {
            log.info("### Authenticated successfully! [Request API]");
            LoginResponseDto responseDto = LoginResponseDto.of(
                    response.getHeader("Authorization"),
                    Long.valueOf(response.getHeader("userId"))
            );
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.OK.value());
            String responseJson = mapper.writeValueAsString(responseDto);
            response.getWriter().write(responseJson);
        }
    }
}
