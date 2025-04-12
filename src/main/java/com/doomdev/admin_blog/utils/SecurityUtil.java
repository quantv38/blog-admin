package com.doomdev.admin_blog.utils;

import com.doomdev.admin_blog.configs.security.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserContext) {
            return ((UserContext) authentication.getPrincipal()).getUserId();
        }
        return null;
    }
}
