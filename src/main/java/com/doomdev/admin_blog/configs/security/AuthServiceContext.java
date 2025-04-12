package com.doomdev.admin_blog.configs.security;

import com.doomdev.admin_blog.contants.TokenType;
import com.doomdev.admin_blog.contants.errorCode.ErrorCode;
import com.doomdev.admin_blog.exceptions.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthServiceContext {
    private final Map<AuthProviderSource, AuthServiceProvider> authServiceProviderMap;

    @Autowired
    public AuthServiceContext(Collection<AuthServiceProvider> strategies) {
        this.authServiceProviderMap = strategies.stream().collect(Collectors.toMap(AuthServiceProvider::getAuthProviderSource, Function.identity()));
    }

    public UserContext authenticate(String tokenSource, String accessToken, TokenType tokenType) {
        AuthServiceProvider authServiceProvider = getAuthServiceProvider(tokenSource);

        return authServiceProvider.authenticate(accessToken, tokenType);
    }

    public AuthServiceProvider getAuthServiceProvider(String tokenSource) {
        try {
            return authServiceProviderMap.get(AuthProviderSource.valueOf(tokenSource.toUpperCase()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ErrorCode.ACTION_AUTHENTICATION_NOT_SUPPORT);
        }
    }
}
