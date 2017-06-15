package ru.spb.konenkov.controller;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spb.konenkov.entity.Period;
import ru.spb.konenkov.entity.PeriodTypeEnum;
import ru.spb.konenkov.entity.Sphere;
import ru.spb.konenkov.persistence.SphereRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static ru.spb.konenkov.controller.TaskViewHelper.*;

/**
 * Экран редактирования периода
 * Created by konen on 30.10.2016.
 */
@Service
public class PeriodEditController {

    public Label periodName;
    private GridPane gridPane;
    public ScrollPane scrollPane;
    @Autowired
    private SphereRepository sphereRepository;

    private Stage dialogStage;
    private Period period;
    private List<Node> gridChildren = new ArrayList<>();

    public void initialize() {
    }

    private void createGrid() {
        gridPane = new GridPane();
        scrollPane.setContent(gridPane);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        ColumnConstraints constraints1 = new ColumnConstraints();
        constraints1.setPrefWidth(200);
        constraints1.setMinWidth(100);
        constraints1.setHalignment(HPos.CENTER);
        ColumnConstraints constraints2 = new ColumnConstraints();
        constraints2.setMinWidth(100);
        ColumnConstraints constraints3 = new ColumnConstraints();
        constraints3.setMinWidth(100);
        constraints3.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(constraints1, constraints2, constraints3);

        RowConstraints rowConstraints = new RowConstraints();
        gridPane.getRowConstraints().add(rowConstraints);

        Label unitTasks = new Label("Задачи");
        unitTasks.setAlignment(Pos.CENTER);
        gridPane.add(unitTasks, 1, 0);
        Label unitResults = new Label("Результаты");
        unitResults.setAlignment(Pos.CENTER);
        gridPane.add(unitResults, 2, 0);

        if (period.getPeriodType() == PeriodTypeEnum.MONTH) {
            ColumnConstraints constraints4 = new ColumnConstraints();
            constraints4.setMinWidth(60);
            gridPane.getColumnConstraints().add(constraints4);
            Label unitScore = new Label("Оценка");
            unitScore.setAlignment(Pos.CENTER);
            gridPane.add(unitScore, 3, 0);
        }
        createSpheresCell(gridPane, (List<Sphere>) sphereRepository.findAll());

    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPeriod(Period period) {
        this.period = period;
        periodName.setText(period.toString());
        renderGrid();
    }

    private void renderGrid() {
        createGrid();
        gridPane.getChildren().removeAll(gridChildren);
        gridChildren.clear();
        int maxRow = 1;
        for (Sphere sphere : sphereRepository.findAll()) {
            Node vBox = createTaskUnitTableView(period, sphere,x->{});
            gridChildren.add(vBox);
            Integer numberRow = sphere.getNumberRow();
            if (maxRow < numberRow) {
                maxRow = numberRow;
            }
            gridPane.add(vBox, 1, numberRow);

            Node result = createResultUnitTextArea(period, sphere,x->{});
            gridChildren.add(result);
            gridPane.add(result, 2, numberRow);
            if (period.getPeriodType() == PeriodTypeEnum.MONTH) {
                Node score = createScoreUnitTextField(period, sphere,x->{});
                gridChildren.add(score);
                gridPane.add(score, 3, numberRow);
            }
        }
        Node periodResult = createResultPeriodTextArea(period,x->{});
        gridChildren.add(periodResult);
        gridPane.add(periodResult, 1, maxRow+1, 2,1);
    }

    public void handleSave(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void handleCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
