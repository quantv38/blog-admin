package com.doomdev.admin_blog.filters;

import com.doomdev.admin_blog.components.RedisComponent;
import com.doomdev.admin_blog.contants.errorCode.ErrorCode;
import com.doomdev.admin_blog.responses.DefaultResponse;
import com.doomdev.admin_blog.utils.JSONUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {
    private final RedisComponent redisComponent;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        String key = "rate-limit:" + ip;

        Long requestCount = redisComponent.incrementValue(key, 1L);

        if (requestCount == 1) {
            redisComponent.setExpire(key, 1, TimeUnit.MINUTES);
        }

        if (requestCount > 60) {
            response.setContentType("application/json");
            response.setStatus(ErrorCode.RATE_LIMIT.getStatusCode().value());
            response.getOutputStream().write(JSONUtils.toJson(DefaultResponse.error(
                    ErrorCode.RATE_LIMIT.getMessage(), ErrorCode.RATE_LIMIT.getCode()))
                    .getBytes(StandardCharsets.UTF_8));

            return;
        }

        filterChain.doFilter(request, response);
    }
}
