package com.doomdev.admin_blog.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IAMScopeHelper {
    public static String getScopeName(String scope) {
        return StringUtils.hasText(scope)
                ? scope.replace('.', '_').replace(':', '_').replace('.', '_').toUpperCase()
                : null;
    }
}
