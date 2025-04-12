package com.doomdev.admin_blog.configs.security;


import com.doomdev.admin_blog.contants.TokenType;
import com.doomdev.admin_blog.contants.errorCode.ErrorCode;
import com.doomdev.admin_blog.exceptions.AppException;

public interface AuthServiceProvider {

    default UserContext authenticate(String accessToken, TokenType tokenType) throws AppException {
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    AuthProviderSource getAuthProviderSource();
}