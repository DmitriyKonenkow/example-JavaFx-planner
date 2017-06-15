package ru.spb.konenkov.controller;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spb.konenkov.entity.Period;
import ru.spb.konenkov.entity.PeriodTypeEnum;
import ru.spb.konenkov.entity.Sphere;
import ru.spb.konenkov.persistence.PeriodRepository;
import ru.spb.konenkov.persistence.SphereRepository;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static ru.spb.konenkov.controller.TaskViewHelper.getSphereUnit;

/**
 * Контроллер отображения графиков
 * Created by konenkov on 10/26/2016.
 */
@Service
public class GraphicsController {
    private static final Logger log = LoggerFactory.getLogger(GraphicsController.class);

    public VBox pane;
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private SphereRepository sphereRepository;
    /**
     * График прошлого и текущего периодов
     */
    private BarChart<Number, String> prevCurMonth;
    /**
     * График колеса жизни во времени
     */
    private StackedAreaChart<String, Number> wheelChart;

    public void initialize() {

        ObservableList<Node> children = pane.getChildren();
        {
            wheelChart = createStackedWheelChart();
            ScrollPane pane = new ScrollPane(wheelChart);
            pane.setFitToHeight(true);
            pane.setMinHeight(500);
            VBox.setVgrow(pane, Priority.ALWAYS);
            children.add(pane);
        }
        {
            prevCurMonth = createBarCurPrevMonthChart();
            VBox.setVgrow(prevCurMonth, Priority.ALWAYS);
            children.add(prevCurMonth);
        }
    }

    /**
     * Заполнение графиков данными
     */
    public void renderDataOnGraphicsTab() {
        ObservableList<XYChart.Series<Number, String>> prevCurMonthData = prevCurMonth.getData();
        prevCurMonthData.clear();
        prevCurMonthData.addAll(createBarSeries());
        ObservableList<XYChart.Series<String, Number>> wheelChartData = wheelChart.getData();
        wheelChartData.clear();
        wheelChartData.addAll(createStactedSeries());
        wheelChart.setPrefSize(wheelChartData.size() * 120, 500);
    }

    /**
     * Создание графика прошлого и текущего периодов
     */
    public BarChart<Number, String> createBarCurPrevMonthChart() {
        final NumberAxis xAxis = new NumberAxis(0, 9, 1);
        xAxis.setLabel("Оценка");
        xAxis.setTickLabelRotation(90);
        final CategoryAxis yAxis = new CategoryAxis();
        yAxis.setLabel("Сфера");
        BarChart<Number, String> prevCurMonth = new BarChart<Number, String>(xAxis, yAxis);
        prevCurMonth.setTitle("Сравнение с предыдущим месяцем");
        prevCurMonth.setMinSize(500, 500);
        prevCurMonth.setMaxWidth(1000);
        return prevCurMonth;
    }

    /**
     * Создание графика колеса жизни во времени
     */
    public StackedAreaChart<String, Number> createStackedWheelChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0, 60, 5);
        StackedAreaChart<String, Number> wheelChart = new StackedAreaChart(xAxis, yAxis);
        wheelChart.setTitle("Оценки по сферам");
        wheelChart.setMinHeight(500);
        wheelChart.getData().addListener((ListChangeListener<XYChart.Series<String, Number>>) c -> wheelChart.setPrefWidth(wheelChart.getData().size() * 120));
        return wheelChart;
    }

    public List<XYChart.Series<String, Number>> createStactedSeries() {
        List<Period> periods = periodRepository.getPeriodByDatesOnlyExists(LocalDate.of(1990, 1, 1), LocalDate.now(), PeriodTypeEnum.MONTH);
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
        for (Sphere sphere : sphereRepository.findAllByOrderByNumberRowDesc()) {
            XYChart.Series sphereSeries = new XYChart.Series<>();
            sphereSeries.setName(sphere.getName());
            for (Period period : periods) {
                LocalDate beginDate = period.getBeginDate();
                sphereSeries.getData().add(new XYChart.Data<String, Number>(beginDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + beginDate.getYear(), getSphereUnit(period, sphere).getScore()));
            }
            seriesList.add(sphereSeries);
        }
        return seriesList;
    }


    public List<XYChart.Series<Number, String>> createBarSeries() {
        Period current = periodRepository.customGetNowMonth();
        LocalDate now = LocalDate.now();
        Period previous = periodRepository.getPeriodByDates(now.minusMonths(1).minusDays(1), now.minusMonths(1), PeriodTypeEnum.MONTH).get(0);
        List<XYChart.Series<Number, String>> seriesList = new ArrayList<>();
        XYChart.Series prevSeries = new XYChart.Series();
        prevSeries.setName(previous.getBeginDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        XYChart.Series curSeries = new XYChart.Series();
        curSeries.setName(current.getBeginDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        seriesList.add(prevSeries);
        seriesList.add(curSeries);
        for (Sphere sphere : sphereRepository.findAllByOrderByNumberRowDesc()) {
            curSeries.getData().add(new XYChart.Data<Number, String>(getSphereUnit(current, sphere).getScore(), sphere.getName()));
            prevSeries.getData().add(new XYChart.Data<Number, String>(getSphereUnit(previous, sphere).getScore(), sphere.getName()));
        }
        return seriesList;
    }
}
