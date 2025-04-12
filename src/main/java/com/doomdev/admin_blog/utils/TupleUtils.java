package com.doomdev.admin_blog.utils;

import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class TupleUtils {

  @SuppressWarnings("unchecked")
  public static <T> T get(Tuple tuple, String key, Class<T> clazz) {
    try {
      Object value = tuple.get(key);
      if (Objects.isNull(value)) {
        return null;
      }
      if (clazz.equals(Long.class)) {
        Number number = (Number) value;
        return (T) Long.valueOf(number.longValue());
      }
      if (clazz.equals(Integer.class)) {
        Number number = (Number) value;
        return (T) Integer.valueOf(number.intValue());
      }
      if (clazz.equals(BigDecimal.class)) {
        Number number = (Number) value;
        return (T) new BigDecimal(number.toString());
      }
      if (clazz.equals(Boolean.class)) {
        if (value instanceof Byte) {
          byte b = (byte) value;
          return (T) Boolean.valueOf(b != 0);
        } else if (value instanceof byte[]) {
          char c = (char) ((byte[]) value)[0];
          return (T) Boolean.valueOf(c != '0');
        }
      }
      if (clazz.equals(LocalDateTime.class)) {
        return (T) DateUtils.toLocalDateTime((Date) value);
      }
      if (clazz.equals(LocalDate.class)) {
        return (T) DateUtils.toLocalDate((Date) value);
      }
      return (T) value;
    } catch (Exception e) {
      log.warn("Error occur when get tuple value with key {} - Detail {}", key, e.getMessage());
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T fromTuple(Tuple tuple, Class<T> clazz, Integer numberSuperClasses) {
    T instance = null;
    for (Constructor<?> constructor : clazz.getConstructors()) {
      var params = constructor.getParameterCount();
      if (params == 0) {
        try {
          instance = (T) constructor.newInstance();
        } catch (Exception e) {
          // do nothing
        }
        break;
      }
    }
    if (Objects.isNull(instance)) {
      throw new IllegalArgumentException(
          String.format("No-arg constructor is required in class %s", clazz));
    }
    if (Objects.isNull(numberSuperClasses) || numberSuperClasses < 0) {
      numberSuperClasses = 0;
    }
    int countSuperClasses = 0;
    Class<?> currentClass = clazz;
    while (!Object.class.equals(currentClass) && countSuperClasses <= numberSuperClasses) {
      Method[] methods = currentClass.getDeclaredMethods();
      for (var method : methods) {
        if (!Modifier.isStatic(method.getModifiers()) && method.canAccess(instance)
            && method.getParameterCount() == 1
            && method.getName().startsWith("set")) {
          String paramName = StringUtils.uncapitalize(method.getName().substring(3));
          Class<?> paramType = method.getParameterTypes()[0];
          Object paramValue = TupleUtils.get(tuple, paramName, paramType);
          try {
            method.invoke(instance, paramValue);
          } catch (Exception e) {
            // do nothing
          }
        }
      }
      countSuperClasses++;
      currentClass = currentClass.getSuperclass();
    }
    return instance;
  }

  @SuppressWarnings("unchecked")
  public static <T> T fromTuple(Tuple tuple, Class<T> clazz) {
    return fromTuple(tuple, clazz, null);
  }
}