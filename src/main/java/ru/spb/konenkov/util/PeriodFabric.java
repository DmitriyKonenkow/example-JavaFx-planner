package ru.spb.konenkov.util;

import ru.spb.konenkov.entity.Period;
import ru.spb.konenkov.entity.PeriodTypeEnum;

import java.time.LocalDate;

/**
 * Создание следующего периода такого же типа
 * Created by konenkov on 10/20/2016.
 */
public class PeriodFabric {


    public static Period createNextPeriod(Period period) {
        LocalDate endDate = period.getEndDate();
        switch (period.getPeriodType()) {
            case WEEK:
                return createPeriodWeek(endDate);
            case MONTH:
                return createPeriodMonth(endDate);
            case YEAR:
                return createPeriodYear(endDate);
            case YEAR5:
                return createPeriodYear5(endDate);
            case YEAR10:
                return createPeriodYear10(endDate);
            case LIFE:
                return createPeriodLife(endDate);
        }
        throw new RuntimeException("Incorrect type for Period ");
    }

    public static Period createPeriodWeek(LocalDate date) {
        Period period = new Period();
        final LocalDate mondayAtWeek = CalendarUtil.getMondayAtWeek(date);
        period.setBeginDate(mondayAtWeek);
        period.setEndDate(mondayAtWeek.plusDays(7));
        period.setPeriodType(PeriodTypeEnum.WEEK);
        return period;
    }

    public static Period createPeriodMonth(LocalDate date) {
        Period period = new Period();
        period.setBeginDate(CalendarUtil.getFirstMondayAtMonth(date));
        period.setEndDate(CalendarUtil.getLastDayAtMonth(date));
        period.setPeriodType(PeriodTypeEnum.MONTH);
        return period;
    }

    public static Period createPeriodYear(LocalDate date) {
        Period period = new Period();
        period.setBeginDate(CalendarUtil.getFirstMondayAtYear(date));
        period.setEndDate(CalendarUtil.getLastDayAtYear(date));
        period.setPeriodType(PeriodTypeEnum.YEAR);
        return period;
    }

    public static Period createPeriodYear5(LocalDate date) {
        Period period = new Period();
        period.setBeginDate(CalendarUtil.getFirstDayAtYear5(date));
        period.setEndDate(CalendarUtil.getLastDayAtYear5(date));
        period.setPeriodType(PeriodTypeEnum.YEAR5);
        return period;
    }
    public static Period createPeriodYear10(LocalDate date) {
        Period period = new Period();
        period.setBeginDate(CalendarUtil.getFirstDayAtYear10(date));
        period.setEndDate(CalendarUtil.getLastDayAtYear10(date));
        period.setPeriodType(PeriodTypeEnum.YEAR10);
        return period;
    }
    public static Period createPeriodLife(LocalDate date) {
        Period period = new Period();
        period.setBeginDate(CalendarUtil.getFirstDayAtLife(date));
        period.setEndDate(CalendarUtil.getLastDayAtLife(date));
        period.setPeriodType(PeriodTypeEnum.LIFE);
        return period;
    }
}
