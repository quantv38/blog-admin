package com.doomdev.admin_blog.configs.security;

import com.doomdev.admin_blog.contants.errorCode.ErrorCode;
import com.doomdev.admin_blog.responses.DefaultResponse;
import com.doomdev.admin_blog.utils.JSONUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream()
                .write(JSONUtils.toJson(DefaultResponse.error(
                                ErrorCode.UNAUTHENTICATED.getMessage(), ErrorCode.UNAUTHENTICATED.getCode()
                        ))
                        .getBytes(StandardCharsets.UTF_8));
    }
}
