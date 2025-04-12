package com.doomdev.admin_blog.contants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Status implements PersistableEnum<Integer> {
    DELETED(0),
    ACTIVE(1),
    ;

    private final Integer value;

    public static Status of(Integer value) {
        return Stream.of(Status.values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElse(null);
    }
}
