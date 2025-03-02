package com.dongsan.core.support.format;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeFormat {
    private TimeFormat() {
    }

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 7;
    public static final int WEEK = 4;
    public static final int MONTH = 12;

    public static String formatTimeString(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        long time = Duration.between(createdAt, now).getSeconds();

        if (time < SEC) {
            return "방금 전";
        }

        time = calculateTime(time, SEC);
        if (time < MIN) {
            return time + "분 전";
        }

        time = calculateTime(time, MIN);
        if (time < HOUR) {
            return time + "시간 전";
        }

        time = calculateTime(time, HOUR);
        if (time < DAY) {
            return time + "일 전";
        }

        time = calculateTime(time, DAY);
        if (time < WEEK) {
            return time + "주 전";
        }

        time = calculateTime(time, WEEK);
        if (time < MONTH) {
            return time + "달 전";
        }

        time = calculateTime(time, MONTH);
        return time + "년 전";
    }

    private static long calculateTime(long time, int divisor) {
        return time / divisor;
    }
}
