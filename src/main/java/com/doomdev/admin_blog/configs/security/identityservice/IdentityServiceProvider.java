package com.doomdev.admin_blog.configs.security.identityservice;

import com.doomdev.admin_blog.configs.security.AuthProviderSource;
import com.doomdev.admin_blog.configs.security.AuthServiceProvider;
import com.doomdev.admin_blog.configs.security.UserContext;
import com.doomdev.admin_blog.configs.security.identityservice.responses.IntrospectResponse;
import com.doomdev.admin_blog.configs.security.identityservice.responses.UserInfoResponse;
import com.doomdev.admin_blog.contants.TokenType;
import com.doomdev.admin_blog.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityServiceProvider implements AuthServiceProvider {
    private final IdentityService identityService;

    @Override
    public UserContext authenticate(String accessToken, TokenType tokenType) throws AppException {
        if (!TokenType.OAUTH2.equals(tokenType)) {
            return null;
        }

        IntrospectResponse introspectResponse = identityService.introspect(accessToken);
        if (Objects.isNull(introspectResponse) || Boolean.FALSE.equals(introspectResponse.getValid())) {
            return null;
        }

        UserInfoResponse userInfo = identityService.getUserInfo(accessToken);
        if (Objects.isNull(userInfo)) {
            return null;
        }

        var authorities = Collections.<GrantedAuthority>emptyList();
        return new UserContext(userInfo.getUsername(), "-", userInfo.getId(), authorities);
    }

    @Override
    public AuthProviderSource getAuthProviderSource() {
        return AuthProviderSource.IAM_IDENTITY;
    }
}
