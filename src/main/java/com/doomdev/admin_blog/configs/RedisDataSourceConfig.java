package com.doomdev.admin_blog.configs;

import com.doomdev.admin_blog.utils.DateUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.ReadFrom;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@Configuration
@EnableTransactionManagement
@EnableRedisRepositories(basePackages = RedisDataSourceConfig.REPOSITORY_BASE_PACKAGES)
public class RedisDataSourceConfig {
    public static final String REPOSITORY_BASE_PACKAGES = "com.doomdev.qportal.repositories.redis";

    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration(RedisProperties redisProperties) {
        var configuration = new RedisSentinelConfiguration();
        configuration.setMaster(redisProperties.getSentinel().getMaster());
        configuration.setSentinels(redisProperties.getSentinel().getNodes().stream()
                .map(RedisNode::fromString)
                .toList());
        configuration.setPassword(RedisPassword.of(redisProperties.getSentinel().getPassword()));
        return configuration;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisSentinelConfiguration configuration) {
        var clientConfiguration = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        return new LettuceConnectionFactory(configuration, clientConfiguration);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplateObjectKV(LettuceConnectionFactory connectionFactory) {
        var mapper = objectMapper();
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new Jackson2JsonRedisSerializer<>(mapper, Object.class));
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(mapper, Object.class));
        redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(mapper, Object.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(mapper, Object.class));
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = JsonMapper.builder()
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .defaultTimeZone(TimeZone.getTimeZone(DateUtils.TIMEZONE))
                .build();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
