package com.doomdev.admin_blog.dtos.requests;

import com.doomdev.admin_blog.contants.enums.StatusPost;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePostRequest {
    @NotNull
    private Long id;

    private String title;

    private String content;

    private String imageThumbnail;

    private List<Long> categoryIds;

    private StatusPost status;
}
