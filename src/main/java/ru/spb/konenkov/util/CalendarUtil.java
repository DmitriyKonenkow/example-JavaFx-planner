package ru.spb.konenkov.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Утильный класс для получения дат
 * начала и конца периодов
 * Created by konenkov on 10/17/2016.
 */
public class CalendarUtil {

    public static LocalDate getMondayAtWeek(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    public static LocalDate getSundayAtWeek(LocalDate date) {
        return date.with(DayOfWeek.SUNDAY);
    }

    public static LocalDate getFirstMondayAtMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static LocalDate getLastDayAtMonth(LocalDate date) {
        LocalDate localDate = date.plusMonths(1);
        return localDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }


    public static LocalDate getFirstMondayAtYear(LocalDate date) {
        return LocalDate.of(date.getYear(), 1, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static LocalDate getLastDayAtYear(LocalDate date) {
        LocalDate localDate = LocalDate.of(date.getYear(), 1, 1);
        localDate = localDate.plusYears(1);
        return localDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static LocalDate getFirstDayAtYear5(LocalDate date) {
        LocalDate localDate = LocalDate.of((date.getYear() / 5) * 5, 1, 1);
        return localDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static LocalDate getLastDayAtYear5(LocalDate date) {
        LocalDate localDate = LocalDate.of((date.getYear() / 5 + 1) * 5, 1, 1);
        return localDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static LocalDate getFirstDayAtYear10(LocalDate date) {
        LocalDate localDate = LocalDate.of((date.getYear() / 10) * 10, 1, 1);
        return localDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static LocalDate getLastDayAtYear10(LocalDate date) {
        LocalDate localDate = LocalDate.of((date.getYear() / 10 + 1) * 10, 1, 1);
        return localDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

    public static LocalDate getFirstDayAtLife(LocalDate date) {
        return LocalDate.of(1986, 7, 12);
    }

    public static LocalDate getLastDayAtLife(LocalDate date) {
        return LocalDate.of(2086, 7, 12);
    }

}