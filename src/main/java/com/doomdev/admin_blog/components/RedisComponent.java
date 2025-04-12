package com.doomdev.admin_blog.components;

import com.doomdev.admin_blog.configs.redis.RedisConfig;
import com.doomdev.admin_blog.utils.StrUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisComponent {
    private static final String DELIMITER = "-";
    private final RedisConfig redisConfig;
    private final RedisTemplate<Object, Object> redisTemplate;

    public String getRedisKey(String identifier) {
        if (!StringUtils.hasText(redisConfig.getPrefixKeyServerToken())) {
            return identifier;
        }
        return String.format("%s%s", redisConfig.getPrefixKeyServerToken(), identifier);
    }

    public String getRedisKey(List<String> identifiers) {
        if (!StringUtils.hasText(redisConfig.getPrefixKeyServerToken())) {
            return StrUtils.join(identifiers, DELIMITER);
        }
        return String.format("%s%s", redisConfig.getPrefixKeyServerToken(), StrUtils.join(identifiers, DELIMITER));
    }

    public void addValue(Object redisKey, Object redisValue, final long timeout, final TimeUnit unit) {
        redisTemplate.opsForValue().set(redisKey, redisValue);
        redisTemplate.expire(redisKey, timeout, unit);
    }

    public Object getValue(Object redisKey) {
        return redisTemplate.opsForValue().get(redisKey);
    }

    public Long incrementValue(String redisKey, long delta) {
        return redisTemplate.opsForValue().increment(redisKey, delta);
    }

    public void setExpire(String redisKey, long timeout, TimeUnit unit) {
        redisTemplate.expire(redisKey, timeout, unit);
    }
}
