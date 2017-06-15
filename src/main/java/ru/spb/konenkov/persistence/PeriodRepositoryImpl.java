package ru.spb.konenkov.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.spb.konenkov.util.PeriodFabric;
import ru.spb.konenkov.entity.Period;
import ru.spb.konenkov.entity.PeriodTypeEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by konenkov on 10/20/2016.
 */
@Repository
@Transactional
public class PeriodRepositoryImpl implements PeriodRepositoryCustom {
    @PersistenceContext
    EntityManager em;


    @Override
    public List<Period> getPeriods(LocalDate from, LocalDate to, Boolean weeks, Boolean month, Boolean year, Boolean year5, Boolean year10, Boolean life) {
        List<Period> periodList = new ArrayList<>();
        if (weeks) periodList.addAll(getPeriodByDates(from, to, PeriodTypeEnum.WEEK));
        if (month) periodList.addAll(getPeriodByDates(from, to, PeriodTypeEnum.MONTH));
        if (year) periodList.addAll(getPeriodByDates(from, to, PeriodTypeEnum.YEAR));
        if (year5) periodList.addAll(getPeriodByDates(from, to, PeriodTypeEnum.YEAR5));
        if (year10) periodList.addAll(getPeriodByDates(from, to, PeriodTypeEnum.YEAR10));
        if (life) periodList.addAll(getPeriodByDates(from, to, PeriodTypeEnum.LIFE));
        periodList.sort((s1, s2) -> s1.getBeginDate().compareTo(s2.getBeginDate()) * 5 + s1.getPeriodType().compareTo(s2.getPeriodType()));
        return periodList;

    }

    @Override
    public List<Period> getPeriodByDates(LocalDate from, LocalDate to, PeriodTypeEnum typeEnum) {
        String additional = "";
        Supplier<Period> consumer = Period::new;
        switch (typeEnum) {
            case WEEK:
                additional = " and p.periodType='WEEK' ";
                consumer = () -> PeriodFabric.createPeriodWeek(from);
                break;
            case MONTH:
                additional = " and p.periodType='MONTH' ";
                consumer = () -> PeriodFabric.createPeriodMonth(from);
                break;
            case YEAR:
                additional = " and p.periodType='YEAR' ";
                consumer = () -> PeriodFabric.createPeriodYear(from);
                break;
            case YEAR5:
                additional = " and p.periodType='YEAR5' ";
                consumer = () -> PeriodFabric.createPeriodYear5(from);
                break;
            case YEAR10:
                additional = " and p.periodType='YEAR10' ";
                consumer = () -> PeriodFabric.createPeriodYear10(from);
                break;
            case LIFE:
                additional = " and p.periodType='LIFE' ";
                consumer = () -> PeriodFabric.createPeriodLife(from);
                break;
        }
        String queryString = "select p from Period p where :from_date <= p.endDate and :to_date >= p.beginDate " + additional + " order by p.beginDate";
        TypedQuery<Period> query = em.createQuery(queryString, Period.class);
        query.setParameter("from_date", from);
        query.setParameter("to_date", to);
        final List<Period> resultList = query.getResultList();
        Period current = consumer.get();
        Set<Period> periodSet = new HashSet<>();
        periodSet.addAll(resultList);
        periodSet.add(current);
        while (!current.getEndDate().isAfter(to)) {
            current = PeriodFabric.createNextPeriod(current);
            periodSet.add(current);
        }
        return periodSet.stream().collect(Collectors.toList());
    }

    @Override
    public List<Period> getPeriodByDatesOnlyExists(LocalDate from, LocalDate to, PeriodTypeEnum typeEnum) {
        String additional = "";
        switch (typeEnum) {
            case WEEK:
                additional = " and p.periodType='WEEK' ";
                break;
            case MONTH:
                additional = " and p.periodType='MONTH' ";
                break;
            case YEAR:
                additional = " and p.periodType='YEAR' ";
                break;
            case YEAR5:
                additional = " and p.periodType='YEAR5' ";
                break;
            case YEAR10:
                additional = " and p.periodType='YEAR10' ";
                break;
            case LIFE:
                additional = " and p.periodType='LIFE' ";
                break;
        }
        String queryString = "select p from Period p where :from_date < p.endDate and :to_date > p.beginDate " + additional + " order by p.beginDate ";
        TypedQuery<Period> query = em.createQuery(queryString, Period.class);
        query.setParameter("from_date", from);
        query.setParameter("to_date", to);
        return query.getResultList();
    }


    @Override
    public Period customGetNowWeek() {
        TypedQuery<Period> query = em.createQuery("select p from Period p where current_date >= p.beginDate and current_date < p.endDate and p.periodType='WEEK'", Period.class);
        List<Period> periods = query.getResultList();
        Period period;
        if (periods.size() == 0) {
            period = PeriodFabric.createPeriodWeek(LocalDate.now());
            period = em.merge(period);
        } else {
            period = periods.get(0);
        }
        return period;
    }

    @Override
    public Period customGetNowMonth() {
        TypedQuery<Period> query = em.createQuery("select p from Period p where current_date between p.beginDate and p.endDate and p.periodType='MONTH'", Period.class);
        List<Period> periods = query.getResultList();
        Period period;
        if (periods.size() == 0) {
            period = PeriodFabric.createPeriodMonth(LocalDate.now());
            period = em.merge(period);
        } else {
            period = periods.get(0);
        }
        return period;
    }

    @Override
    public Period getNowYear() {
        TypedQuery<Period> query = em.createQuery("select p from Period p where current_date between p.beginDate and p.endDate and p.periodType='YEAR'", Period.class);
        List<Period> periods = query.getResultList();
        Period period;
        if (periods.size() == 0) {
            period = PeriodFabric.createPeriodYear(LocalDate.now());
            period = em.merge(period);
        } else {
            period = periods.get(0);
        }
        return period;
    }

}
