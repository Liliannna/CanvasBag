package com.project.canvasBag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.canvasBag.event.StageReadyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class JavaFxApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(JavaFxApp.class);
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        ApplicationContextInitializer<GenericApplicationContext> initializer =
                context -> {
                    context.registerBean(Application.class, () -> JavaFxApp.this);
                    context.registerBean(Parameters.class, this::getParameters);
                };
        this.context = new SpringApplicationBuilder()
                .sources(SpringBootApp.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Start JavaFxApp");
        this.context.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
        logger.info("Stop JavaFxApp");
    }
}
