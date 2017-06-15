package ru.spb.konenkov.entity;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;

/**
 * Задача
 * Created by konenkov on 10/14/2016.
 */
@Entity
@Table
@Access(AccessType.PROPERTY)
public class Task {

    private SimpleLongProperty uid = new SimpleLongProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleBooleanProperty status = new SimpleBooleanProperty(false);

    public Task() {
    }

    public Task(String name) {
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

    public Boolean getStatus() {
        return status.get();
    }

    public void setStatus(Boolean status) {
        this.status = new SimpleBooleanProperty(status);
    }

    @Transient
    public SimpleBooleanProperty getStatusProperty() {
        return status;
    }
}
