package com.doomdev.admin_blog.contants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TokenType {
    OAUTH2("OAuth2"),
    DIRECT("Direct");

    private final String name;

    public static TokenType of(String name) {
        return Stream.of(values())
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}