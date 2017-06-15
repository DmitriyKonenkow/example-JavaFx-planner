package ru.spb.konenkov.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Service;

/**
 * Created by dmko1016 on 28.12.2016.
 */
@Service
public class PlannerController {


    public Tab tabPane;
    public ScrollPane nowTaskTab;
    @FXML
    public NowTasksController nowTaskTabController;
    public GridPane allPeriodsTab;
    @FXML
    public AllPeriodsController allPeriodsTabController;
    public GridPane sphereTab;
    @FXML
    public SphereController sphereTabController;
    public VBox graphicsTab;
    @FXML
    public GraphicsController graphicsTabController;

    public void selectAllPeriodsTab(Event event) {
        if (((Tab) event.getTarget()).isSelected()) {
            allPeriodsTabController.renderDataOnAllPeriodsTab();
        }
    }

    public void selectNowTaskTab(Event event) {
        if (((Tab) event.getTarget()).isSelected()) {
            nowTaskTabController.renderDataOnNowTaskTab();
        }
    }

    public void selectSphereTab(Event event) {
        if (((Tab) event.getTarget()).isSelected()) {
            sphereTabController.renderDataOnSphereTab();
        }
    }

    public void selectGraphicsTab(Event event) {
        if (((Tab) event.getTarget()).isSelected()) {
            graphicsTabController.renderDataOnGraphicsTab();
        }
    }
}
