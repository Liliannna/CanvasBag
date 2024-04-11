package com.project.canvasBag.controller.material;

import com.project.canvasBag.Config;
import com.project.canvasBag.model.Accessories;
import com.project.canvasBag.model.MaterialType;
import com.project.canvasBag.service.AccessoriesService;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;

@Component
@FxmlView("newAccessories.fxml")
public class NewAccessoriesController {
    private static final Logger logger = LoggerFactory.getLogger(NewAccessoriesController.class);
    private final AccessoriesService service;
    private Stage stage;

    public NewAccessoriesController(AccessoriesService service) {
        this.service = service;
    }

    @FXML
    private AnchorPane addNew;

    @FXML
    private TextField comment;

    @FXML
    private TextField material;

    @FXML
    private TextField size;

    @FXML
    private TextField nameAccessories;

    @FXML
    private TextField price;

    @FXML
    private TextField remainder;

    @FXML
    private Label successLabel;

    @FXML
    private void initialize() {
        stage = new Stage();
        Scene scene = new Scene(addNew);
        stage.setScene(scene);
        stage.setTitle("Добавить аксессуар");
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
    void saveButton() {
        Accessories accessories = new Accessories();
        //Проверяем - если не пустые значения то добавляем в базу новый аксессуар
        try {
            if (!nameAccessories.getText().isEmpty() && !comment.getText().isEmpty() &&
                    !material.getText().isEmpty() && !remainder.getText().isEmpty() &&
                    !price.getText().isEmpty()) {

                accessories.setType(MaterialType.ACCESSORIES);
                accessories.setName(nameAccessories.getText());
                accessories.setComment(comment.getText());
                accessories.setSize(size.getText());
                accessories.setMaterial(material.getText());
                accessories.setRemainder(Integer.parseInt(remainder.getText()));
                accessories.setPrice(Double.parseDouble(price.getText()));
                service.save(accessories);
                successLabel.setText("Сохранено!");
                successLabel.setStyle("-fx-text-fill: GREEN");
                clearTextField();
                logger.debug("Added new accessories - {}", accessories);
            } else {
                successLabel.setText("Заполните поля!");
                successLabel.setStyle("-fx-text-fill: RED");
            }
        } catch (ConstraintViolationException ex) {
            successLabel.setText("Проверьте данные!");
            successLabel.setStyle("-fx-text-fill: RED");
        }


    }

    //Проверяем правильно ли заполняются поля ввода
    private void listenerTextField() {
        nameAccessories.textProperty().addListener(event -> {
            nameAccessories.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !nameAccessories.getText().isEmpty() &&
                            !nameAccessories.getText().matches(Config.regexName)
            );
        });
        comment.textProperty().addListener(event -> {
            comment.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !comment.getText().isEmpty() &&
                            !comment.getText().matches(Config.regexComment)
            );
        });
        size.textProperty().addListener(event -> {
            size.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !size.getText().isEmpty() &&
                            !size.getText().matches(Config.regexSize)
            );
        });
        material.textProperty().addListener(event -> {
            material.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !material.getText().isEmpty() &&
                            !material.getText().matches(Config.regexName)
            );
        });
        remainder.textProperty().addListener(event -> {
            remainder.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !remainder.getText().matches(Config.regexNumber)
            );
        });
        price.textProperty().addListener(event -> {
            price.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !price.getText().isEmpty() &&
                            !price.getText().matches(Config.regexDouble)
            );
        });
    }

    //очищаем поля
    private void clearTextField() {
        nameAccessories.clear();
        comment.clear();
        material.clear();
        remainder.clear();
        size.clear();
        price.clear();
    }
}
