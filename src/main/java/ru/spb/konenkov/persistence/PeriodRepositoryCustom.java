package ru.spb.konenkov.persistence;

import ru.spb.konenkov.entity.Period;
import ru.spb.konenkov.entity.PeriodTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by dmko1016 on 26.12.2016.
 */
public interface PeriodRepositoryCustom {
    List<Period> getPeriods(LocalDate from, LocalDate to, Boolean weeks, Boolean month, Boolean year, Boolean year5, Boolean year10, Boolean life);

    List<Period> getPeriodByDates(LocalDate from, LocalDate to, PeriodTypeEnum typeEnum);

    List<Period> getPeriodByDatesOnlyExists(LocalDate from, LocalDate to, PeriodTypeEnum typeEnum);

    Period customGetNowWeek();

    Period customGetNowMonth();

    Period getNowYear();
}
