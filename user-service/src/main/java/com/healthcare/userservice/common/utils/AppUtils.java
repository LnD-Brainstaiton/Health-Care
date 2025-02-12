package com.healthcare.userservice.common.utils;

import java.util.Random;

public final class AppUtils {

    public static final String BASE_URL = "/api/v1/user";

    private AppUtils() {

    }

    public static String generateRandomString(Integer size) {
        int leftLimit = 48; // numeral '0'
         int rightLimit = 122; // letter 'z'
         int targetStringLength = size;
         Random random = new Random();
         String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
         return generatedString;}
}
