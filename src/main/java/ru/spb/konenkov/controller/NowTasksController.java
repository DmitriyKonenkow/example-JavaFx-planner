package ru.spb.konenkov.controller;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spb.konenkov.entity.Period;
import ru.spb.konenkov.entity.Sphere;
import ru.spb.konenkov.persistence.PeriodRepository;
import ru.spb.konenkov.persistence.SphereRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static ru.spb.konenkov.controller.TaskViewHelper.*;

/**
 * Отображение текущей недели
 * Created by konenkov on 10/20/2016.
 */
@Service
public class NowTasksController {
    public GridPane gridPane;
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private SphereRepository sphereRepository;
    private List<Node> gridChildren = new ArrayList<>();
    private Label weekLabel = new Label();
    private Label monthLabel = new Label();
    private Label yearLabel = new Label();


    public void initialize() {
        createGrid();
        renderDataOnNowTaskTab();

    }

    private void createGrid() {

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        ColumnConstraints constraints1 = new ColumnConstraints();
        constraints1.setPrefWidth(100);
        constraints1.setMinWidth(100);
        constraints1.setHalignment(HPos.CENTER);
        ColumnConstraints constraints2 = new ColumnConstraints();
        constraints2.setMinWidth(100);
        ColumnConstraints constraints3 = new ColumnConstraints();
        constraints3.setMinWidth(100);
        ColumnConstraints constraints4 = new ColumnConstraints();
        constraints4.setMinWidth(100);

        gridPane.getColumnConstraints().addAll(constraints1, constraints2, constraints3, constraints4);

        RowConstraints rowConstraints = new RowConstraints();
        gridPane.getRowConstraints().add(rowConstraints);


        VBox weekBox = new VBox(new Label("Неделя"), weekLabel);
        weekBox.setAlignment(Pos.CENTER);
        gridPane.add(weekBox, 1, 0);
        VBox monthBox = new VBox(new Label("Месяц"), monthLabel);
        monthBox.setAlignment(Pos.CENTER);
        gridPane.add(monthBox, 2, 0);
        VBox yearBox = new VBox(new Label("Год"), yearLabel);
        yearBox.setAlignment(Pos.CENTER);
        gridPane.add(yearBox, 3, 0);

        createSpheresCell(gridPane, (List<Sphere>) sphereRepository.findAll());


    }


    public void renderDataOnNowTaskTab() {
        Period periodWeek = periodRepository.customGetNowWeek();
        weekLabel.setText(periodWeek.getBeginDate().toString() + " - " + periodWeek.getEndDate().toString());
        Period periodMonth = periodRepository.customGetNowMonth();
        monthLabel.setText(periodMonth.getBeginDate().toString() + " - " + periodMonth.getEndDate().toString());
        Period periodYear = periodRepository.getNowYear();
        yearLabel.setText(periodYear.getBeginDate().toString() + " - " + periodYear.getEndDate().toString());
        gridPane.getChildren().removeAll(gridChildren);
        gridChildren.clear();
        Consumer<Period> updatePeriod = period -> {
            periodRepository.save(period);
            renderDataOnNowTaskTab();
        };
        List<Sphere> spheres = (List<Sphere>) sphereRepository.findAllByOrderByNumberRowAsc();
        for (Sphere sphere : spheres) {
            Node vBox = createTaskUnitTableView(periodWeek, sphere, updatePeriod);
            gridChildren.add(vBox);
            gridPane.add(vBox, 1, sphere.getNumberRow());
        }
        for (Sphere sphere : spheres) {
            Node vBox = createTaskUnitTableView(periodMonth, sphere, updatePeriod);
            gridChildren.add(vBox);
            gridPane.add(vBox, 2, sphere.getNumberRow());
        }
        for (Sphere sphere : spheres) {
            Node vBox = createTaskUnitTableView(periodYear, sphere, updatePeriod);
            gridChildren.add(vBox);
            gridPane.add(vBox, 3, sphere.getNumberRow());
        }
        Node weekResult = createResultPeriodTextArea(periodWeek, updatePeriod);
        gridChildren.add(weekResult);
        gridPane.add(weekResult, 1, gridPane.getRowConstraints().size() + 1);
        Node monthResult = createResultPeriodTextArea(periodMonth, updatePeriod);
        gridChildren.add(monthResult);
        gridPane.add(monthResult, 2, gridPane.getRowConstraints().size() + 1);
        Node yearResult = createResultPeriodTextArea(periodYear, updatePeriod);
        gridChildren.add(yearResult);
        gridPane.add(yearResult, 3, gridPane.getRowConstraints().size() + 1);
    }


}
