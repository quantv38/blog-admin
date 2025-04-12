package com.doomdev.admin_blog.dtos.responses;

import com.doomdev.admin_blog.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostDetailResponse {
    Long id;

    String title;

    String slug;

    String content;

    @JsonFormat(pattern = DateUtils.ES_DATE_FORMAT)
    LocalDate postDate;

    String imageThumbnail;

    List<String> categories;
}
