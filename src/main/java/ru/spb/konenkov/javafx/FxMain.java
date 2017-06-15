package ru.spb.konenkov.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

@Service
@Configurable
public class FxMain {

    private static final Logger log = LoggerFactory.getLogger(FxMain.class);
    @Autowired
    private FXMLLoader loader;

    public void start(Stage stage) throws Exception {
        log.info("Starting Hello JavaFX and Maven demonstration application");

        String fxmlFile = "/fxml/planner.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        loader.setLocation(getClass().getResource(fxmlFile));
        Parent rootNode = (Parent) loader.load();

        log.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode, 1000, 600);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("Стратегическое планирование жизни");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
