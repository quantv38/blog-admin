package com.doomdev.admin_blog.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessLogHelper {

  public static void logInfo(String content, Map<String, String> contextInfo) {
    BaseLogHelper.addMDC(contextInfo);
    log.info(content);
  }

  public static void logWarn(String content, Map<String, String> contextInfo) {
    BaseLogHelper.addMDC(contextInfo);
    log.warn(content);
  }

  public static void logError(String content, Map<String, String> contextInfo) {
    BaseLogHelper.addMDC(contextInfo);
    log.error(content);
  }
}
