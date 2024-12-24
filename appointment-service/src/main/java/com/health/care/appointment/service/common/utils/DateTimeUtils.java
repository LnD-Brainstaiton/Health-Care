package com.health.care.appointment.service.common.utils;

import io.micrometer.common.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateTimeUtils {

    public static final String APP_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String ADI_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    // Format LocalDateTime to string
    public static String formatDateTime(LocalDateTime dateTime, String dateFormat) {
        return (dateTime == null || StringUtils.isBlank(dateFormat))
                ? ""
                : dateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    // Default format using ADI_DATE_FORMAT
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, ADI_DATE_FORMAT);
    }

    // Convert LocalDateTime to string with the given format
    public static String convertToString(LocalDateTime dateTime, String dateFormat) {
        return formatDateTime(dateTime, dateFormat);
    }

    // Get current date and time as string with a custom format
    public static String getCurrentDateTimeString(String dateFormat) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat));
    }

    // Convert string to LocalDateTime
    public static LocalDateTime convertToLocalDateTime(String dateStr, String dateFormat) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(dateFormat)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(dateFormat));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDate convertToLocalDate(String dateStr, String dateFormat) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(dateFormat)) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(dateFormat));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocalTime convertToLocalTime(String dateStr, String dateFormat) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(dateFormat)) {
            return null;
        }
        try {
            return LocalTime.parse(dateStr, DateTimeFormatter.ofPattern(dateFormat));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add hours to LocalDateTime
    public static LocalDateTime addHours(LocalDateTime dateTime, int hours) {
        return dateTime.plusHours(hours);
    }

    // Add minutes to LocalDateTime
    public static LocalDateTime addMinutes(LocalDateTime dateTime, int minutes) {
        return dateTime.plusMinutes(minutes);
    }

    // Add days to LocalDateTime
    public static LocalDateTime addDays(LocalDateTime dateTime, int days) {
        return dateTime.plusDays(days);
    }

    // Get the first day of the current month as LocalDateTime
    public static LocalDateTime getFirstDayOfMonth() {
        return LocalDate.now().withDayOfMonth(1).atStartOfDay();
    }

    // Get the difference between two LocalDateTimes in the given TimeUnit
    public static long getDateTimeDifference(LocalDateTime dateTime1, LocalDateTime dateTime2, TimeUnit timeUnit) {
        long diffInMillis = ChronoUnit.MILLIS.between(dateTime1, dateTime2);
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    // Get the difference in days between two LocalDateTimes
    public static long getDateTimeDifferenceInDays(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return getDateTimeDifference(dateTime1, dateTime2, TimeUnit.DAYS);
    }

    // Get a future LocalDateTime by adding a number of hours
    public static LocalDateTime expireAtHour(int hour) {
        return addHours(LocalDateTime.now(), hour);
    }

    // Convert minutes to milliseconds based on the given calendar flag
    public static int convertToMilli(int minute, int calendarFlag) {
        if (calendarFlag == java.util.Calendar.HOUR) {
            return 1000 * 60 * 60 * minute;
        }
        if (calendarFlag == java.util.Calendar.MINUTE) {
            return 1000 * 60 * minute;
        }
        return 0;
    }

    // Convert milliseconds to minutes
    public static long millisecondsToMinute(long milliseconds) {
        return TimeUnit.MILLISECONDS.toMinutes(milliseconds);
    }

    // Convert milliseconds to seconds
    public static long millisecondsToSeconds(long milliseconds) {
        return TimeUnit.MILLISECONDS.toSeconds(milliseconds);
    }

    // Convert milliseconds to a formatted string (minutes and seconds)
    public static String convertMillisecondsToMinute(long milliseconds) {
        long minutes = millisecondsToMinute(milliseconds);
        long seconds = millisecondsToSeconds(milliseconds % 60);
        return minutes + "m " + seconds + "s";
    }

    // Convert minutes to milliseconds
    public static long minuteToMillis(Integer minute) {
        return TimeUnit.MINUTES.toMillis(minute);
    }

    // Validate a string date in the format "yyyy-MM-dd"
    public static boolean validateStringDate(String date) {
        Pattern pattern = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    // Get the current timestamp in milliseconds
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    // Convert a LocalDateTime to a string with the APP_DATE_FORMAT format
    public static String convertToPostgresDefaultFormat(String dateString, String dateFormat) {
        if (StringUtils.isBlank(dateString) || StringUtils.isBlank(dateFormat)) {
            return "";
        }
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(dateFormat));
            return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // Get the LocalDateTime representing the end of the given date (23:59:59.999)
    public static LocalDateTime convertToEndOfDay(final LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
    }

    public static String getFormattedErrorMessageWithTime(final LocalDateTime tempUnblockDate, final LocalDateTime currentTime) {
        // Calculate the remaining time between tempUnblockDate and currentTime as Duration
        Duration remainingTime = Duration.between(currentTime, tempUnblockDate);

        // Convert remaining time to minutes and seconds
        long remainingMinutes = remainingTime.toMinutes();
        long remainingSeconds = remainingTime.getSeconds() % 60;

        // Get the message from your response message (assuming it's defined elsewhere)
        final String message = "Please try again in %d minutes %d seconds";

        // Format the message with the calculated remaining minutes and seconds
        return String.format(message, remainingMinutes, remainingSeconds);
    }
}