package com.doomdev.admin_blog.contants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum StatusPost implements PersistableEnum<Integer> {
    DELETED(0),
    ACTIVE(1),
    PRIVATE(2),
    PENDING(3),
    INHERIT(4),
    DRAFT(5),
    AUTO_DRAFT(6),;

    private final Integer value;

    public static StatusPost of(Integer value) {
        return Stream.of(StatusPost.values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElse(null);
    }
}
