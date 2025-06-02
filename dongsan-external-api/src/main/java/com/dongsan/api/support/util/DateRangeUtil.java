package com.dongsan.api.support.util;

import com.dongsan.api.domains.crew.CrewRankingPeriod;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateRangeUtil {
    private DateRangeUtil() {
    }

    // 주어진 날짜가 속한 주의 시작일 (월요일) 을 반환. (한국 기준)
    public static LocalDate getStartOfWeek(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.KOREA);
        DayOfWeek firstDayOfWeek = weekFields.getFirstDayOfWeek();
        return date.with(firstDayOfWeek);
    }

    // 주어진 날짜가 속한 주의 시작일 (일요일) 을 반환. (한국 기준)
    public static LocalDate getEndOfWeek(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.KOREA);
        DayOfWeek firstDayOfWeek = weekFields.getFirstDayOfWeek();
        DayOfWeek lastDayOfWeek = firstDayOfWeek.plus(6);
        return date.with(lastDayOfWeek);
    }

    // 주어진 날짜가 속한 월의 첫째 날을 반환
    public static LocalDate getStartOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    // 주어진 날짜가 속한 월의 마지막 날을 반환
    public static LocalDate getEndOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate getStartDay(LocalDate date, CrewRankingPeriod period) {
        return switch (period) {
            case DAILY -> date;
            case WEEKLY -> getStartOfWeek(date);
            case MONTHLY -> getStartOfMonth(date);
        };
    }

    public static LocalDate getEndDay(LocalDate date, CrewRankingPeriod period) {
        return switch (period) {
            case DAILY -> date;
            case WEEKLY -> getEndOfWeek(date);
            case MONTHLY -> getEndOfMonth(date);
        };
    }
}
