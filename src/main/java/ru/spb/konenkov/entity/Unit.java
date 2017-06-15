package ru.spb.konenkov.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Задачи в сфере на конкретный период
 * Created by konenkov on 10/17/2016.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"period_uid", "sphere_uid"}))
@Access(AccessType.PROPERTY)
public class Unit {

    private SimpleLongProperty uid = new SimpleLongProperty();
    /**
     * Результаты завершения юнита
     */
    private SimpleStringProperty result = new SimpleStringProperty();
    /**
     * Оценка Юнита
     */
    private SimpleIntegerProperty score = new SimpleIntegerProperty();
    /**
     * Связь с конкретным временным периодом
     */
    private Period period;
    /**
     * Связь с конкретной сферой жизни
     */
    private Sphere sphere;
    /**
     * Связь с задачами
     */
    private List<Task> tasks = new ArrayList<>();

    public Unit() {
    }

    public Unit(Period period, Sphere sphere) {
        this.period = period;
        this.sphere = sphere;
    }

    @Id
    @GeneratedValue
    public Long getUid() {
        return uid.get();
    }

    public void setUid(Long uid) {
        this.uid = new SimpleLongProperty(uid);
    }

    public String getResult() {
        return result.get();
    }

    public void setResult(String result) {
        this.result = new SimpleStringProperty(result);
    }

    @Transient
    public SimpleStringProperty getResultProperty() {
        return result;
    }

    public Integer getScore() {
        return score.get();
    }

    public void setScore(Integer score) {
        this.score = new SimpleIntegerProperty(score);
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    public Sphere getSphere() {
        return sphere;
    }

    public void setSphere(Sphere sphere) {
        this.sphere = sphere;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "unit_id")
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
