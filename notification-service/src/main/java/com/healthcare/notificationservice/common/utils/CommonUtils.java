package com.healthcare.notificationservice.common.utils;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class CommonUtils {

    private CommonUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String joinString(String delimiter, List<String> listOfString) {
        StringBuilder builder = new StringBuilder();
        listOfString
                .forEach(code -> builder.append(code).append(delimiter));

        String joinedString = builder.toString();
        return joinedString.substring(0, joinedString.length() - 1);
    }

    public static String joinString(String delimiter, String... strings) {
        return joinString(delimiter, Arrays.asList(strings));
    }

    public static <T> boolean contains(T data, List<T> list) {
        return list.contains(data);
    }

    public static <T> boolean notContains(T data, List<T> list) {
        return !contains(data, list);
    }

    public static <T> boolean isNull(T obj) {
        return obj == null;
    }

    public static boolean validatePasswordByRegex(String password, String regex){
        return password.matches(regex);
    }

    public static void setEmpty(Object object) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (String.class.equals(field.getType())) {
                field.setAccessible(true);
                if (Objects.isNull(field.get(object))) {
                    field.set(object, StringUtils.EMPTY);
                }
            }
        }
    }


    public static String encodeUrl(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            return value;
        }
    }

    public static String decodeUrl(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            return value;
        }

    }

    public static <T> Optional<T> resolve(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

}
