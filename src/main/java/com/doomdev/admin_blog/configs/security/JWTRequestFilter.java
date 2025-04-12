package com.doomdev.admin_blog.configs.security;

import com.doomdev.admin_blog.contants.HeaderField;
import com.doomdev.admin_blog.contants.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final AuthServiceContext authServiceContext;

    private final AuthConfig authConfig;

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String TOKEN_TYPE = "Token-Type";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (!StringUtils.hasText(accessToken)) {
            log.warn("Thiếu thông tin accessToken");
            filterChain.doFilter(request, response);
            return;
        }

        TokenType tokenType = StringUtils.hasText(request.getHeader(TOKEN_TYPE)) ? TokenType.valueOf(request.getHeader(TOKEN_TYPE)) : TokenType.OAUTH2;

        if (Objects.isNull(tokenType)) {
            log.warn("Token Type không được hỗ trợ");
            filterChain.doFilter(request, response);
            return;
        }

        var tokenSource = request.getHeader(HeaderField.TOKEN_SOURCE);
        if (!StringUtils.hasText(tokenSource)) {
            tokenSource = authConfig.getAuthProviderDefault();
        }

        UserContext user;
        try {
            user = authServiceContext.authenticate(tokenSource, accessToken, tokenType);
        } catch (Exception e) {
            user = null;
        }

        if (Objects.isNull(user)) {
            log.warn("Token không hợp lệ");
            filterChain.doFilter(request, response);
            return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorization) {
        return
                Optional.ofNullable(authorization)
                        .filter(
                                authToken -> StringUtils.hasText(authToken) && authToken.startsWith(TOKEN_PREFIX))
                        .map(authToken -> authToken.substring(TOKEN_PREFIX.length()).strip())
                        .orElse(null);
    }
}
