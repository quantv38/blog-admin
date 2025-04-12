package com.doomdev.admin_blog.configs.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RedisConfig {

    @Value("${redis.prefix-key.server-token}")
    private String prefixKeyServerToken;
}
