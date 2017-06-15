package ru.spb.konenkov.entity;

/**
 * Тип периода
 * Created by konenkov on 10/17/2016.
 */
public enum PeriodTypeEnum {

    WEEK("Неделя", 6), MONTH("Месяц", 5), YEAR("Год",4), YEAR5("5 лет", 3), YEAR10("10 лет", 2), LIFE("Жизнь", 1);

    private String name;
    private int order;

    PeriodTypeEnum(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

}
