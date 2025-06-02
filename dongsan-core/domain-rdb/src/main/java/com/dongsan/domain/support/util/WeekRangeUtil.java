package com.dongsan.domain.support.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class WeekRangeUtil {
    private WeekRangeUtil() {
    }

    public static LocalDate getStartOfWeek(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.KOREA);
        DayOfWeek firstDayOfWeek = weekFields.getFirstDayOfWeek();
        return date.with(firstDayOfWeek);
    }

    public static LocalDate getEndOfWeek(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.KOREA);
        DayOfWeek firstDayOfWeek = weekFields.getFirstDayOfWeek();
        DayOfWeek lastDayOfWeek = firstDayOfWeek.plus(6);
        return date.with(lastDayOfWeek);
    }
}
