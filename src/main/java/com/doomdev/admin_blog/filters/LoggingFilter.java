package com.doomdev.admin_blog.filters;

import com.doomdev.admin_blog.entities.Principal;
import com.doomdev.admin_blog.utils.AccessLogHelper;
import com.doomdev.admin_blog.utils.DateUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    var start = LocalDateTime.now();
    filterChain.doFilter(request, response);
    Map<String, String> contextInfo = new HashMap<>();;

    contextInfo.put("lt", "dbal");
    contextInfo.put("url", getRequestURL(request));
    contextInfo.put("urp", request.getRequestURI());
    contextInfo.put("urq", request.getQueryString());
    contextInfo.put("rt", getResponseTime(start).toString());
    contextInfo.put("st", String.valueOf(response.getStatus()));
    contextInfo.put("mt", request.getMethod());
    contextInfo.put("rmip", request.getRemoteAddr());
    contextInfo.put("cip", request.getHeader("http_client_ip"));
    contextInfo.put("bbs", getContentLength(response));
    contextInfo.put("cl", String.valueOf(Math.max(request.getContentLength(), 0)));
    contextInfo.put("rf", request.getHeader(HttpHeaders.REFERER));
    contextInfo.put("ua", request.getHeader(HttpHeaders.USER_AGENT));
    contextInfo.put("host", request.getHeader(HttpHeaders.HOST));
    contextInfo.put("tl", DateUtils.format(LocalDateTime.now(), DateUtils.LOG_DATETIME_FORMAT));
    contextInfo.put("rid", UUID.randomUUID().toString());

    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.nonNull(authentication)
            && Objects.nonNull(authentication.getPrincipal())
            && authentication.getPrincipal() instanceof Principal principal) {
      contextInfo.put("uid", principal.clientId());
      contextInfo.put("usrc", "IAM");
    } else {
      contextInfo.put("uid", "");
      contextInfo.put("usrc", "anonymous");
    }

    AccessLogHelper.logInfo("access_log", contextInfo);
  }

  private String getRequestURL(HttpServletRequest request) {
    if (Objects.isNull(request.getQueryString())) {
      return request.getRequestURI();
    }
    return request.getRequestURI() + "?" + request.getQueryString();
  }

  private Double getResponseTime(LocalDateTime start) {
    return Duration.between(start, LocalDateTime.now()).toMillis() / 1000.0;
  }

  private String getContentLength(HttpServletResponse response) {
    var contentLength = response.getHeader(HttpHeaders.CONTENT_LENGTH);
    var length = 0;
    try {
      length = Integer.parseInt(contentLength);
    } catch (Exception e) {
      // ignore
    }
    return String.valueOf(Math.max(length, 0));
  }
}