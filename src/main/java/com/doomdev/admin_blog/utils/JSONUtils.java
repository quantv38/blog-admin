package com.doomdev.admin_blog.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JSONUtils {

    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .defaultTimeZone(TimeZone.getTimeZone(DateUtils.TIMEZONE))
            .build();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static <T> String toJson(T object) {
        if (Objects.isNull(object)) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    public static <T> T convert(String str, Class<T> tClass) {
        if (Objects.isNull(str)) {
            return null;
        }
        try {
            return objectMapper.readValue(str, tClass);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static <T> T convert(String str, TypeReference<T> typeReference) {
        if (Objects.isNull(str)) {
            return null;
        }
        try {
            return objectMapper.readValue(str, typeReference);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static <T> T convert(Object obj, Class<T> clazz) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return objectMapper.convertValue(obj, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static Map<String, Object> jsonToMap(String str) {
        try {
            return objectMapper.readValue(str, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}