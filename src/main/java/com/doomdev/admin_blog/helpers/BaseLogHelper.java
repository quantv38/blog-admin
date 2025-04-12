package com.doomdev.admin_blog.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseLogHelper {

  public static void addMDC(Map<String, String> values) {
    if (!CollectionUtils.isEmpty(values)) {
      values.forEach(MDC::put);
    }
  }
}
