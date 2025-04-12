package com.doomdev.admin_blog.dtos.responses;

import com.doomdev.admin_blog.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostListResponse {
    Long id;

    String title;

    String slug;

    @JsonFormat(pattern = DateUtils.ES_DATE_FORMAT)
    LocalDate postDate;

    String imageThumbnail;

    List<String> categories;
}
