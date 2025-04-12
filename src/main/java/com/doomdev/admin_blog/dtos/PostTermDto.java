package com.doomdev.admin_blog.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostTermDto {
    Long id;

    Long postId;

    Long termId;

    String termName;

    String termAlias;

    Integer termType;
}
