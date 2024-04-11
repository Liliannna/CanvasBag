package com.project.canvasBag.converter;

import javafx.scene.control.Alert;
import javafx.util.converter.IntegerStringConverter;

public class CustomIntegerStringConverter extends IntegerStringConverter {
    private final IntegerStringConverter converter = new IntegerStringConverter();

    @Override
    public Integer fromString(String string) {
        try {
            return converter.fromString(string);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Недопустимое значение! " + string);
            alert.showAndWait();
        }
        return 0;
    }
}
