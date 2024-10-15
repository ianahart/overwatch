package com.hart.overwatch.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public final class MyUtil {

    private MyUtil() {

    }

    public static String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();

    }

    public static boolean validatePassword(String str) {
        boolean isUpperCase = false;
        boolean isLowerCase = false;
        boolean isDigit = false;
        boolean isSpecialChar = false;

        for (char ch : str.toCharArray()) {
            if (Character.isLetter(ch) && Character.isUpperCase(ch)) {
                isUpperCase = true;
            }
            if (Character.isLetter(ch) && Character.isLowerCase(ch)) {
                isLowerCase = true;
            }
            if (Character.isDigit(ch)) {
                isDigit = true;
            }
            if (!Character.isWhitespace(ch) && !Character.isDigit(ch) && !Character.isLetter(ch)) {
                isSpecialChar = true;
            }
        }
        List<Boolean> cases = List.of(isUpperCase, isLowerCase, isDigit, isSpecialChar);
        return cases.stream().allMatch(c -> c);
    }

    public static String deslugify(String str) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            sb.append(str.charAt(i) == '-' ? ' ' : str.charAt(i));
        }

        return sb.toString();
    }

    public static String makeReadableDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        String readableDate = "";
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long durationInSeconds = duration.getSeconds();

        if (dateTime.getHour() + 1 <= 24 && durationInSeconds < 86400) {
            readableDate = "Last active today";
        } else {
            int daysElapsed = (int) Math.floor(durationInSeconds / 86400);
            readableDate = "Last active " + daysElapsed + " days ago";
        }
        return readableDate;
    }

}

