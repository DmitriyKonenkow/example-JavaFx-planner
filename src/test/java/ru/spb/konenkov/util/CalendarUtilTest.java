package ru.spb.konenkov.util;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Created by konenkov on 10/17/2016.
 */
public class CalendarUtilTest {
    @Test
    public void getMondayAtWeek() throws Exception {
        assertEquals("2016-09-26", CalendarUtil.getMondayAtWeek(LocalDate.of(2016, 10, 1)).toString());
    }

    @Test
    public void getFirstMondayAtMonth() {
        assertEquals("2016-10-03", CalendarUtil.getFirstMondayAtMonth(LocalDate.of(2016, 10, 1)).toString());
    }

    @Test
    public void getLastDayAtMonth() throws Exception {
        assertEquals("2016-10-03", CalendarUtil.getLastDayAtMonth(LocalDate.of(2016, 9, 1)).toString());
    }

    @Test
    public void getFirstMondayAtYear() throws Exception {
        assertEquals("2016-01-04", CalendarUtil.getFirstMondayAtYear(LocalDate.of(2016, 5, 1)).toString());
    }

    @Test
    public void getLastDayAtYear() throws Exception {
        assertEquals("2017-01-02", CalendarUtil.getLastDayAtYear(LocalDate.of(2016, 1, 9)).toString());
    }

    @Test
    public void getFirstMondayAtYear5() throws Exception {
        assertEquals("2015-01-05", CalendarUtil.getFirstDayAtYear5(LocalDate.of(2016, 1, 9)).toString());
    }

    @Test
    public void getLastDayAtYear5() throws Exception {
        assertEquals("2020-01-06", CalendarUtil.getLastDayAtYear5(LocalDate.of(2016, 1, 9)).toString());
    }

    @Test
    public void getFirstMondayAtYear10() throws Exception {
        assertEquals("2010-01-04", CalendarUtil.getFirstDayAtYear10(LocalDate.of(2016, 1, 9)).toString());
    }

    @Test
    public void getLastDayAtYear10() throws Exception {
        assertEquals("2020-01-06", CalendarUtil.getLastDayAtYear10(LocalDate.of(2016, 1, 9)).toString());
    }

}