package com.project.canvasBag.converter;

import javafx.scene.control.Alert;
import javafx.util.converter.DoubleStringConverter;

public class CustomDoubleStringConverter extends DoubleStringConverter {
    private final DoubleStringConverter converter = new DoubleStringConverter();

    @Override
    public Double fromString(String string) {
        try {
            return converter.fromString(string);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Недопустимое значение! " + string);
            alert.showAndWait();
        }
        return 0.0;
    }
}