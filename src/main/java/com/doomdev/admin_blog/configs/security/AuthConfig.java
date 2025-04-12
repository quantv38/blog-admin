package com.doomdev.admin_blog.configs.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AuthConfig {
    @Value("${auth.provider.default}")
    private String authProviderDefault;
}
