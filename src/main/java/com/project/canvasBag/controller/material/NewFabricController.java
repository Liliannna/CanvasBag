package com.project.canvasBag.controller.material;

import com.project.canvasBag.Config;
import com.project.canvasBag.controller.ErrorController;
import com.project.canvasBag.dto.request.AddNewFabricDtoRequest;
import com.project.canvasBag.erroritem.exception.AppException;
import com.project.canvasBag.service.FabricService;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;


@Component
@FxmlView("newFabric_v2.fxml")
public class NewFabricController {
    private static final Logger logger = LoggerFactory.getLogger(NewFabricController.class);

    private final FabricService service;

    private File fileSave;
    private Stage stage;

    public NewFabricController(FabricService service) {
        this.service = service;
    }

    @FXML
    private AnchorPane addNew;

    @FXML
    private TextField color;

    @FXML
    private TextField length;

    @FXML
    private TextField nameFabric;

    @FXML
    private TextField price;

    @FXML
    private TextField thickness;

    @FXML
    private TextField width;

    @FXML
    private Label successLabel;

    @FXML
    private TextField comment;

    @FXML
    private TextField file;

    @FXML
    private void initialize() {
        stage = new Stage();
        Scene scene = new Scene(addNew);
        stage.setScene(scene);
        stage.setTitle("Добавить ткань");
        stage.setResizable(false);
        listenerTextField();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    void cancelButton() {
        stage.close();
    }

    @FXML
    void downloadFile() throws IOException {
        //Загружаем изображение
        FileChooser fileChooser = new FileChooser();
        fileSave = fileChooser.showOpenDialog(stage);
        file.setText(fileSave.getAbsolutePath());
    }


    @FXML
    void saveButton() throws AppException {
        AddNewFabricDtoRequest newFabric = new AddNewFabricDtoRequest();
        //Проверяем - если не пустые значения то передаем сервису
        try {
            if (!nameFabric.getText().isEmpty() && !color.getText().isEmpty() &&
                    !width.getText().isEmpty() && !length.getText().isEmpty() &&
                    !price.getText().isEmpty()) {

                newFabric.setName(nameFabric.getText());
                newFabric.setColor(color.getText());
                newFabric.setLength(length.getText());
                newFabric.setWidth(width.getText());
                if (!thickness.getText().isEmpty()) {
                    newFabric.setThickness(thickness.getText());
                }
                newFabric.setComment(comment.getText());
                newFabric.setPrice(price.getText());
                newFabric.setURLImage(file.getText());
                service.save(newFabric);

                successLabel.setText("Сохранено!");
                successLabel.setStyle("-fx-text-fill: GREEN");
                clearTextField();
            } else {
                successLabel.setText("Заполните поля!");
                successLabel.setStyle("-fx-text-fill: RED");
            }
        } catch (AppException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText(e.getErrorField().getField() + "\n" +
                    e.getErrorField().getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            successLabel.setText("Не удалось загрузить файл!");
            successLabel.setStyle("-fx-text-fill: RED");
        }
    }

    //Проверяем правильно ли заполняются поля ввода
    private void listenerTextField() {
        nameFabric.textProperty().addListener(event -> {
            nameFabric.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !nameFabric.getText().isEmpty() &&
                            !nameFabric.getText().matches(Config.regexName)
            );
        });
        color.textProperty().addListener(event -> {
            color.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !color.getText().isEmpty() &&
                            !color.getText().matches(Config.regexName)
            );
        });
        width.textProperty().addListener(event -> {
            width.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !width.getText().isEmpty() &&
                            !width.getText().matches(Config.regexDouble)
            );
        });
        length.textProperty().addListener(event -> {
            length.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !length.getText().isEmpty() &&
                            !length.getText().matches(Config.regexDouble)
            );
        });
        thickness.textProperty().addListener(event -> {
            thickness.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !thickness.getText().matches(Config.regexNumber)
            );
        });
        price.textProperty().addListener(event -> {
            price.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !price.getText().isEmpty() &&
                            !price.getText().matches(Config.regexDouble)
            );
        });
        comment.textProperty().addListener(event -> {
            comment.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !comment.getText().isEmpty() &&
                            !comment.getText().matches(Config.regexComment)
            );
        });
    }

    //очищаем поля
    private void clearTextField() {
        nameFabric.clear();
        color.clear();
        width.clear();
        length.clear();
        thickness.clear();
        price.clear();
        comment.clear();
        file.clear();
    }
}

