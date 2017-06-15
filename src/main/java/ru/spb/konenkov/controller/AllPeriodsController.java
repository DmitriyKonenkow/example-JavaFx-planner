package ru.spb.konenkov.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spb.konenkov.entity.Period;
import ru.spb.konenkov.entity.Sphere;
import ru.spb.konenkov.persistence.PeriodRepository;
import ru.spb.konenkov.persistence.SphereRepository;

import javax.inject.Provider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static ru.spb.konenkov.controller.TaskViewHelper.createTaskUnitTableView;

/**
 * Created by konenkov on 10/24/2016.
 */
@Service
public class AllPeriodsController {

    public TableView periodTable;
    public DatePicker periodFrom;
    public DatePicker periodTo;
    /**
     * Чекбоксы видов периода
     */
    public CheckBox weekCheckBox;
    public CheckBox monthCheckBox;
    public CheckBox yearCheckBox;
    public CheckBox year5CheckBox;
    public CheckBox year10CheckBox;
    public CheckBox lifeCheckBox;

    @Autowired
    Provider<FXMLLoader> fxmlLoaderProvider;
    @Autowired
    PeriodRepository periodRepository;
    @Autowired
    SphereRepository sphereRepository;

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    public void initialize() {
        periodFrom.setValue(LocalDate.now());
        periodTo.setValue(LocalDate.now().plusMonths(1));
        renderDataOnAllPeriodsTab();
        renderTable();
    }

    private void renderTable() {
        List<TableColumn> columns = new ArrayList<>();

        TableColumn<Period, String> periodTypeColumn = new TableColumn<>("Период");
        {
            periodTypeColumn.setPrefWidth(100);
            periodTypeColumn.setCellValueFactory(cel -> {
                String value = null;
                Period period = cel.getValue();
                LocalDate beginDate = period.getBeginDate();
                switch (period.getPeriodType()) {
                    case WEEK:
                        value = beginDate.toString();
                        break;
                    case MONTH:
                        value = beginDate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru")) +
                                " " + String.valueOf(beginDate.getYear());
                        break;
                    case YEAR:
                        value = String.format("%1$s год;\n%2$s год жизни", beginDate.getYear(),beginDate.getYear()-1986L) ;
                        break;
                    case YEAR5:
                    case YEAR10:
                        value = String.valueOf(beginDate.getYear()) + " - " + String.valueOf(period.getEndDate().getYear());
                        break;
                    case LIFE:
                        value = "Жизнь";
                        break;
                }
                return new SimpleStringProperty(value);
            });
            columns.add(periodTypeColumn);
        }

        for (Sphere sphere : sphereRepository.findAll()) {
            TableColumn<Period, Period> sphereColumn = new TableColumn<>(sphere.getName());
            {
                sphereColumn.setPrefWidth(200);
                sphereColumn.setSortable(false);
                sphereColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
                sphereColumn.setCellFactory(new TableColumnTableCellCallback(sphere, period -> {
                    periodRepository.save(period);
                    renderDataOnAllPeriodsTab();
                }));
                columns.add(sphereColumn);
            }
        }

        TableColumn<Period, String> resultColumn = new TableColumn<>("Результат");
        {
            resultColumn.setCellValueFactory(cel -> new SimpleObjectProperty<String>(cel.getValue().getResult()));
            resultColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            resultColumn.setOnEditCommit(event -> {
                Period period = event.getRowValue();
                period.setResult(event.getNewValue());
                periodRepository.save(period);
                renderDataOnAllPeriodsTab();
            });
            resultColumn.setPrefWidth(500);
            columns.add(resultColumn);
        }
        periodTable.setRowFactory(tv -> {
            TableRow<Period> row = new PeriodTableRow();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Period rowData = row.getItem();
                    showPeriodEditDialog(rowData);
                }
            });
            return row;
        });
        periodTable.setEditable(true);
        periodTable.getColumns().addAll(columns);
        periodTable.setTableMenuButtonVisible(true);

    }

    /**
     * Вызов диалога редактирования периода
     *
     * @param period период
     */
    private void showPeriodEditDialog(Period period) {
        try {
            FXMLLoader loader = fxmlLoaderProvider.get();
            // Load the fxml file and create a new stage for the popup dialog.
            BorderPane page = (BorderPane) loader.load(getClass().getResourceAsStream("/fxml/period_edit.fxml"));
            Stage primaryStage = (Stage) periodTable.getScene().getWindow();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование периода");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add("/styles/styles.css");
            dialogStage.setScene(scene);

            // Set the period into the controller.
            PeriodEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPeriod(period);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            periodRepository.save(period);
            renderDataOnAllPeriodsTab();

        } catch (IOException e) {
            LOG.error("Error open edit period dialog");
        }
    }

    public void renderDataOnAllPeriodsTab() {
        List<Period> periods = periodRepository.getPeriods(periodFrom.getValue(), periodTo.getValue(),
                weekCheckBox.isSelected(), monthCheckBox.isSelected(), yearCheckBox.isSelected(),
                year5CheckBox.isSelected(), year10CheckBox.isSelected(), lifeCheckBox.isSelected());
        Collections.sort(periods);
        periodTable.setItems(FXCollections.observableArrayList(periods));
        periodTable.refresh();
    }

    private static class TableColumnTableCellCallback implements Callback<TableColumn<Period, Period>, TableCell<Period, Period>> {
        private final Sphere sphere;
        private final Consumer<Period> periodConsumer;

        public TableColumnTableCellCallback(Sphere sphere, Consumer<Period> periodConsumer) {
            this.sphere = sphere;
            this.periodConsumer = periodConsumer;
        }

        @Override
        public TableCell<Period, Period> call(TableColumn<Period, Period> param) {
            return new PeriodPeriodTableCell(periodConsumer, sphere);
        }

        private static class PeriodPeriodTableCell extends TableCell<Period, Period> {
            private final Consumer<Period> periodConsumer;
            private Sphere sphere;

            private PeriodPeriodTableCell(Consumer<Period> periodConsumer, Sphere sphere) {
                this.periodConsumer = periodConsumer;
                this.sphere = sphere;
            }

            @Override
            protected void updateItem(Period item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(createTaskUnitTableView(item, sphere, periodConsumer));
                }
            }
        }
    }

    private static class PeriodTableRow extends TableRow<Period> {
        @Override
        protected void updateItem(Period item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                switch (item.getPeriodType()) {
                    case MONTH:
                        setId("month");
                        break;
                    case YEAR:
                        setId("year");
                        break;
                    case YEAR5:
                        setId("year5");
                        break;
                    case YEAR10:
                        setId("year10");
                        break;
                    case LIFE:
                        setId("life");
                        break;
                    default:
                        setId(null);
                        break;
                }
            }
        }
    }
}

