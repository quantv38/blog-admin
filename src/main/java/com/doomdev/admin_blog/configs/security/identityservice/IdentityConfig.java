package com.doomdev.admin_blog.configs.security.identityservice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(
prefix = "admin-blog.iam"
)
public class IdentityConfig {
    private String url;
}
