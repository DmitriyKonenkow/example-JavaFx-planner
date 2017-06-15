package ru.spb.konenkov.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.converter.IntegerStringConverter;
import ru.spb.konenkov.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


/**
 * Хелпер формирования частей интерфейса
 * Created by konenkov on 10/24/2016.
 */

public class TaskViewHelper {
    private static Task dragAndDropTask;

    /**
     * Создание компонента результата периода
     *
     * @param period   период
     * @param consumer действие выполняемое при изменнении
     * @return компонент
     */
    public static Node createResultPeriodTextArea(Period period, Consumer<Period> consumer) {
        Label resultLabel = new Label("Результат периода:");
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefRowCount(3);
        textArea.setText(period.getResult());
        textArea.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue) {
                period.setResult(textArea.getText());
                consumer.accept(period);
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(resultLabel);
        borderPane.setCenter(textArea);
        return borderPane;
    }

    /**
     * Создания компонента результата юнита
     *
     * @param period   период
     * @param sphere   сфера
     * @param consumer действие при изменнении
     * @return компонент
     */
    public static Node createResultUnitTextArea(Period period, Sphere sphere, Consumer<Period> consumer) {
        Unit unit = getSphereUnit(period, sphere);
        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(2);
        textArea.setWrapText(true);
        textArea.setText(unit.getResult());
        textArea.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue) {
                unit.setResult(textArea.getText());
                consumer.accept(period);
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(textArea);
        return borderPane;
    }

    /**
     * Создание компонента оценки для юнита
     *
     * @param period   период
     * @param sphere   сфера
     * @param consumer действие при изменении
     * @return компонент
     */
    public static Node createScoreUnitTextField(Period period, Sphere sphere, Consumer<Period> consumer) {
        Unit unit = getSphereUnit(period, sphere);
        TextField textField = new TextField();
        textField.setMaxWidth(30);
        textField.setMinWidth(30);
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter());
        textField.setTextFormatter(formatter);
        textField.setText(String.valueOf(unit.getScore()));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            unit.setScore(Integer.valueOf(newValue));
            consumer.accept(period);
        });
        return textField;
    }

    /**
     * Создание вертикальной колонны с ячейками сфер в таблице
     *
     * @param gridPane таблица
     * @param spheres  сферы
     */
    public static void createSpheresCell(GridPane gridPane, List<Sphere> spheres) {
        for (Sphere sphere : spheres) {
            gridPane.add(new Label(sphere.getName()), 0, sphere.getNumberRow());
            RowConstraints rowConstraints1 = new RowConstraints();
            rowConstraints1.setValignment(VPos.TOP);
            gridPane.getRowConstraints().add(rowConstraints1);
        }
    }

    /**
     * Создание компонента юнита для таблицы
     *
     * @param period   период
     * @param sphere   сфера
     * @param consumer действие при обновлении
     * @return компонент
     */
    public static Node createTaskUnitTableView(Period period, Sphere sphere, Consumer<Period> consumer) {
        final Unit unit = getSphereUnit(period, sphere);
        final String tableStyle = "score" + unit.getScore();

        ObservableList<Task> taskList = FXCollections.observableList(unit.getTasks());
        TableView<Task> taskTableView = new TableView<>(taskList);
        {
            taskTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            taskTableView.setId("task");
            taskTableView.setFixedCellSize(25);
            taskTableView.prefHeightProperty().bind(Bindings.size(taskTableView.getItems()).multiply(taskTableView.getFixedCellSize()).add(4));
            taskTableView.setEditable(true);
            taskTableView.setRowFactory(tv -> {
                TableRow<Task> row = new TaskTableRow();
                row.setOnDragDetected(event -> {
                    Task selected = taskTableView.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        Dragboard db = taskTableView.startDragAndDrop(TransferMode.MOVE);
                        dragAndDropTask = selected;
                        ClipboardContent content = new ClipboardContent();
                        content.putString(dragAndDropTask.getUid().toString());
                        db.setContent(content);
                        event.consume();
                    }
                });
                return row;
            });

            taskTableView.setOnDragOver(event -> {
                if (event.getGestureSource() != taskTableView) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            });

            taskTableView.setOnDragDropped(event -> {
                boolean success = false;
                if (event.getDragboard().hasString()) {
                    unit.getTasks().add(dragAndDropTask);
                    success = true;
                }
                dragAndDropTask = null;
                consumer.accept(period);
                event.setDropCompleted(success);
                event.consume();
            });

            List<TableColumn<Task, ?>> columns = new ArrayList<>();

            TableColumn<Task, String> taskNameColumn = new TableColumn<>();
            {
                taskNameColumn.setPrefWidth(160);
                taskNameColumn.setCellValueFactory(cel -> cel.getValue().getNameProperty());
                taskNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                taskNameColumn.setOnEditCommit(event -> {
                    event.getRowValue().setName(event.getNewValue());
                    consumer.accept(period);
                });
                columns.add(taskNameColumn);
            }

            TableColumn<Task, Boolean> statusTableColumn = new TableColumn<>();
            {
                statusTableColumn.setMinWidth(30);
                statusTableColumn.setMaxWidth(30);
                statusTableColumn.setCellValueFactory(cellData -> {
                    final SimpleBooleanProperty statusProperty = cellData.getValue().getStatusProperty();
                    statusProperty.addListener(((observable, oldValue, newValue) -> {
                        consumer.accept(period);
                    }));
                    return statusProperty;
                });
                statusTableColumn.setCellFactory(param -> new CheckBoxTableCell<>());
                columns.add(statusTableColumn);
            }

            if (unit.getPeriod().getPeriodType().equals(PeriodTypeEnum.MONTH)) {
                taskTableView.getStyleClass().add(tableStyle);
                taskNameColumn.getStyleClass().add(tableStyle);
                statusTableColumn.getStyleClass().add(tableStyle);
            }


            ContextMenu contextMenu = new ContextMenu();
            {
                MenuItem addMenuItem = new MenuItem("Добавить");
                addMenuItem.setOnAction(event -> taskList.add(new Task("")));
                MenuItem delMenuItem = new MenuItem("Удалить");
                delMenuItem.setOnAction(event -> {
                    Task delTask = taskTableView.getSelectionModel().getSelectedItem();
                    taskList.remove(delTask);
                    consumer.accept(period);
                });
                contextMenu.getItems().addAll(addMenuItem, delMenuItem);
                taskTableView.setContextMenu(contextMenu);
            }
            taskTableView.getColumns().addAll(columns);
        }

        return taskTableView;
    }

    /**
     * Получение юнита для периода и конкретной сферы
     *
     * @param period период
     * @param sphere сфера
     * @return юнит
     */
    public static Unit getSphereUnit(Period period, Sphere sphere) {
        List<Unit> units = period.getUnits();
        Unit unit = units.stream().filter(unit1 -> Objects.equals(unit1.getSphere().getUid(), sphere.getUid())).findFirst().orElse(new Unit(period, sphere));
        if (!units.contains(unit)) {
            units.add(unit);
        }
        return unit;
    }

    private static class TaskTableRow extends TableRow<Task> {
        private Tooltip tooltip = new Tooltip();

        @Override
        public void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (task == null) {
                setTooltip(null);
            } else {
                tooltip.setText(task.getName());
                setTooltip(tooltip);
                if (task.getStatus()) {
                    setId("done_task");
                }
            }
        }
    }
}
