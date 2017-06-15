package ru.spb.konenkov;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.spb.konenkov.javafx.FxMain;

public class MainApp extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);
    protected ApplicationContext context;
    FxMain fxMain;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext("ru.spb.konenkov");
        context.getAutowireCapableBeanFactory().autowireBean(this);
        fxMain = context.getBean(FxMain.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOG.info("Start application");
        fxMain.start(stage);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        ((ConfigurableApplicationContext) context).close();
    }
}