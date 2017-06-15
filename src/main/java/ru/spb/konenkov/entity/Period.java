package ru.spb.konenkov.entity;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Период времени (неделя, месяц, год, 5 лет, 10 лет, жизнь)
 *
 * @see ru.spb.konenkov.entity.PeriodTypeEnum
 * Created by konenkov on 10/17/2016.
 */
@Entity
@Table
@Access(AccessType.PROPERTY)
public class Period implements Comparable<Period> {

    private SimpleLongProperty uid = new SimpleLongProperty();
    /**
     * Начало периода
     */
    private SimpleObjectProperty<LocalDate> beginDate = new SimpleObjectProperty<>();
    /**
     * Окончание периода, последний день включительно
     */
    private SimpleObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
    /**
     * Тип периода, неделя, месяц, год
     */
    private PeriodTypeEnum periodType;
    /**
     * Общий результаты по периоду
     */
    private String result;

    private List<Unit> units = new ArrayList<>();

    @Id
    @GeneratedValue
    public Long getUid() {
        return uid.get();
    }

    public void setUid(Long uid) {
        this.uid = new SimpleLongProperty(uid);
    }

    @Convert(converter = LocalDateAttributeConverter.class)
    public LocalDate getBeginDate() {
        return beginDate.get();
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = new SimpleObjectProperty<>(beginDate);
    }

    @Transient
    public SimpleObjectProperty<LocalDate> getBeginDateProperty() {
        return beginDate;
    }

    @Convert(converter = LocalDateAttributeConverter.class)
    public LocalDate getEndDate() {
        return endDate.get();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = new SimpleObjectProperty<>(endDate);
    }

    @Transient
    public SimpleObjectProperty<LocalDate> getEndDateProperty() {
        return endDate;
    }

    @Enumerated(EnumType.STRING)
    public PeriodTypeEnum getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodTypeEnum periodType) {
        this.periodType = periodType;
    }

    @Column(length = 2147483647)
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @OneToMany(mappedBy = "period", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!beginDate.get().equals(period.beginDate.get())) return false;
        return endDate.get().equals(period.endDate.get());

    }

    @Override
    public int hashCode() {
        int result = beginDate.get().hashCode();
        result = 31 * result + endDate.get().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return periodType.getName() + " с " + getBeginDate() + " по " + getEndDate();
    }

    @Override
    public int compareTo(Period o) {
        int compare = this.getBeginDate().compareTo(o.getBeginDate());
        if (compare != 0) return compare;
        else return this.getPeriodType().getOrder() - o.getPeriodType().getOrder();
    }
}
