package ru.spb.konenkov.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spb.konenkov.entity.Sphere;
import ru.spb.konenkov.persistence.SphereRepository;

import java.util.Comparator;
import java.util.List;

/**
 * Контроллер для экрана редактирования сфер
 * Created by konenkov on 10/19/2016.
 */
@Service
public class SphereController {
    @FXML
    public TableView<Sphere> sphereTable;
    @FXML
    public TextField numberRowProperty;
    @FXML
    public TextField name;
    @FXML
    public TextArea lifeViewProperty;
    @Autowired
    private SphereRepository sphereRepository;
    private Sphere selectedModel = new Sphere();

    public void initialize() {
        renderDataOnSphereTab();
        sphereTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectModel(newValue));

    }

    public void renderDataOnSphereTab() {
        final List<Sphere> spheresList = (List<Sphere>) sphereRepository.findAll();
        ObservableList<Sphere> spheres = FXCollections.observableList(spheresList);
        spheres.sort(Comparator.comparing(Sphere::getNumberRow));
        sphereTable.setItems(spheres);
    }

    private void selectModel(Sphere sphere) {
        if (sphere == null) {
            selectedModel = new Sphere();
        } else {
            selectedModel = sphere;
        }

        numberRowProperty.setText(String.valueOf(selectedModel.getNumberRow()));
        name.setText(selectedModel.getName());
        lifeViewProperty.setText(selectedModel.getLifeView());

    }

    @FXML
    protected void addSphere(ActionEvent event) {
        selectModel(new Sphere());
    }

    @FXML
    protected void saveToDB() {
        selectedModel.setName(name.getText());
        selectedModel.setNumberRow(Integer.valueOf(numberRowProperty.getText()));
        selectedModel.setLifeView(lifeViewProperty.getText());
        sphereRepository.save(selectedModel);
        renderDataOnSphereTab();
    }

    @FXML
    protected void deleteModel() {
        sphereTable.getItems().remove(selectedModel);
        sphereRepository.save(selectedModel);
        renderDataOnSphereTab();
    }

}
