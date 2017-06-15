package ru.spb.konenkov.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;

/**
 * Сферы жизни (здоровье, семья, карьера ...)
 * Created by konenkov on 10/17/2016.
 */
@Entity
@Table
@Access(AccessType.PROPERTY)
public class Sphere {

    private SimpleLongProperty uid = new SimpleLongProperty();
    /**
     * Название сферы
     */
    private SimpleStringProperty name = new SimpleStringProperty();
    /**
     * Порядковый номер колонки
     */
    private SimpleIntegerProperty numberRowProperty = new SimpleIntegerProperty();
    /**
     * Видение жизни
     */
    private SimpleStringProperty lifeViewProperty = new SimpleStringProperty();

    public Sphere() {
    }

    public Sphere(String numberRowProperty, String name, String lifeViewProperty) {
        this.numberRowProperty = new SimpleIntegerProperty(Integer.valueOf(numberRowProperty));
        this.name = new SimpleStringProperty(name);
        this.lifeViewProperty = new SimpleStringProperty(lifeViewProperty);
    }

    public Sphere(String name) {
        this.name = new SimpleStringProperty(name);
    }

    @Id
    @GeneratedValue
    public Long getUid() {
        return uid.get();
    }

    public void setUid(Long uid) {
        this.uid = new SimpleLongProperty(uid);
    }

    @Transient
    public SimpleLongProperty getUidProperty() {
        return uid;
    }

    @Basic
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    @Transient
    public SimpleStringProperty getNameProperty() {
        return name;
    }

    @Basic
    public Integer getNumberRow() {
        return numberRowProperty.get();
    }

    public void setNumberRow(Integer numberRow) {
        this.numberRowProperty = new SimpleIntegerProperty(numberRow);
    }

    @Transient
    public SimpleIntegerProperty getNumberRowProperty() {
        return numberRowProperty;
    }

    public SimpleIntegerProperty numberRowPropertyProperty() {
        return numberRowProperty;
    }

    @Basic
    public String getLifeView() {
        return lifeViewProperty.get();
    }

    public void setLifeView(String lifeView) {
        this.lifeViewProperty = new SimpleStringProperty(lifeView);
    }

    @Transient
    public SimpleStringProperty getLifeViewProperty() {
        return lifeViewProperty;
    }

    public SimpleStringProperty lifeViewPropertyProperty() {
        return lifeViewProperty;
    }
}
