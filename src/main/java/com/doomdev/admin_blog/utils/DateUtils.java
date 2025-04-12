package com.doomdev.admin_blog.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    public static final String DASHED_DATE_FORMAT = "dd-MM-yyyy";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final String GLOBAL_TIMEZONE = "UTC";

    public static final String ES_DATE_FORMAT = "yyyy-MM-dd";
    public static final String BINLOG_DATETIME_FORMAT = "dd/MM/yyyy";
    public static final String LOG_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String REQUEST_DATETIME = "yyyy/MM/dd HH:mm:ss.SSS";
    public static final String TIMEZONE = "Asia/Ho_Chi_Minh";

    public static TimeZone defaultTimeZone() {
        return TimeZone.getTimeZone(TIMEZONE);
    }

    public static String format(TemporalAccessor temporal, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(temporal);
    }

    public static LocalDateTime fromKafka(Date date) {
        return date.toInstant().atZone(ZoneId.of(GLOBAL_TIMEZONE)).toLocalDateTime();
    }

    public static ZoneId defaultZoneId() {
        return ZoneId.of(DateUtils.TIMEZONE);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        try {
            return toLocalDateTime(date.toInstant());
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public static LocalDate toLocalDate(Date date) {
        try {
            return LocalDate.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.of(DateUtils.TIMEZONE));
        } catch (Exception e) {
            // ignore
            return null;
        }
    }

    public static Date of(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of(TIMEZONE)).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, defaultZoneId());
    }

    public static Date convertDateStrToDate(String dateStr, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setLenient(false);
        try {
            return formatter.parse(dateStr);
        } catch (Exception ex) {
            log.error("Can not parse date string {}, details: {}", dateStr, ex.getMessage());
        }
        return null;
    }

    public static LocalDate convertDateFormat(String dateStr, DateTimeFormatter dateFormatter) {
        return LocalDate.parse(dateStr, dateFormatter);
    }

    public static Long getCurrentMillis(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of(GLOBAL_TIMEZONE)).toInstant().toEpochMilli();
    }

    public static long getNumberOfDayBetween(Date fromDate, Date toDate) {
        return ChronoUnit.DAYS.between(
                Objects.requireNonNull(DateUtils.toLocalDate(fromDate)), DateUtils.toLocalDate(toDate));
    }

    public static long getNumberOfDayBetween(LocalDate fromDate, LocalDate toDate) {
        return ChronoUnit.DAYS.between(fromDate, toDate);
    }

    public static long getNumberOfDayBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return ChronoUnit.DAYS.between(fromDate, toDate);
    }

    public static Date addHourDate(Date date, int hour) {
        Calendar createAt = Calendar.getInstance();
        createAt.setTime(date);
        createAt.add(Calendar.HOUR, hour);
        return createAt.getTime();
    }

    public static LocalDateTime fromKafkaBigDataToLocalDateTime(Date date) {
        return Objects.isNull(date)
                ? null
                : date.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static LocalDateTime isoDateTimeToLocalDateTime(String isoDateTime) {
        if (Objects.isNull(isoDateTime)) {
            return null;
        }

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoDateTime);
        OffsetDateTime utcOffsetDateTime = offsetDateTime.withOffsetSameInstant(ZoneOffset.ofHours(7));

        return utcOffsetDateTime.toLocalDateTime();

    }
}
