package com.project.canvasBag.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("error.fxml")
public class ErrorController {
    @FXML
    private Label errorMessage;

    public void setErrorText(String text) {
        errorMessage.setText(text);
    }

    @FXML
    private void close() {
        errorMessage.getScene().getWindow().hide();
    }
}
