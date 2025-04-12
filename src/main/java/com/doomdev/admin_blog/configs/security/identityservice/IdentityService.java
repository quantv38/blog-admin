package com.doomdev.admin_blog.configs.security.identityservice;

import com.doomdev.admin_blog.configs.security.identityservice.responses.IdentityDefaultResponse;
import com.doomdev.admin_blog.configs.security.identityservice.responses.IntrospectResponse;
import com.doomdev.admin_blog.configs.security.identityservice.responses.UserInfoResponse;
import com.doomdev.admin_blog.responses.DefaultResponse;

public interface IdentityService {
    IntrospectResponse introspect(String token);

    UserInfoResponse getUserInfo(String token);
}
