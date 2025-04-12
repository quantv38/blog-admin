package com.doomdev.admin_blog.contants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {
    THUMBNAIL(1),
    BANNER(2),
    PRIZE(3)
    ;

    private final Integer value;
}
