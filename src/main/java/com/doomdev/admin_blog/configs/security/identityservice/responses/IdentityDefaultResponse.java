package com.doomdev.admin_blog.configs.security.identityservice.responses;

import com.doomdev.admin_blog.clients.responses.ClientResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IdentityDefaultResponse<T> implements ClientResponse<T> {
    private Boolean success;

    private String message;

    private Integer code;

    private T data;

    @Override
    public T getResponseData() {
        return hasData() ? this.data : null;
    }

    @Override
    public boolean hasData() {
        return isSuccess() && Objects.nonNull(this.data);
    }

    @Override
    public boolean isSuccess() {
        return Boolean.TRUE.equals(this.success);
    }
}
