package com.project.canvasBag.controller.material;

import com.project.canvasBag.Config;
import com.project.canvasBag.model.MaterialType;
import com.project.canvasBag.model.Tape;
import com.project.canvasBag.service.TapeService;
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
@FxmlView("newTape.fxml")
public class NewTapeController {
    private static final Logger logger = LoggerFactory.getLogger(NewTapeController.class);
    private final TapeService service;
    private Stage stage;

    public NewTapeController(TapeService service) {
        this.service = service;
    }

    @FXML
    private AnchorPane addNew;

    @FXML
    private TextField comment;

    @FXML
    private TextField color;

    @FXML
    private TextField width;

    @FXML
    private TextField name;

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
        stage.setTitle("Добавить стропу/косую бейку/шнур/кант");
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
        Tape tape = new Tape();
        //Проверяем - если не пустые значения то добавляем в базу
        try {
            if (!name.getText().isEmpty() && !comment.getText().isEmpty() &&
                    !color.getText().isEmpty() && !remainder.getText().isEmpty() &&
                    !price.getText().isEmpty()) {

                tape.setType(MaterialType.TAPE);
                tape.setName(name.getText());
                tape.setComment(comment.getText());
                if(!width.getText().isEmpty()){
                    tape.setWidth(Integer.parseInt(width.getText()));
                }
                tape.setColor(color.getText());
                tape.setRemainder(Double.parseDouble(remainder.getText()));
                tape.setPrice(Double.parseDouble(price.getText()));
                service.save(tape);
                successLabel.setText("Сохранено!");
                successLabel.setStyle("-fx-text-fill: GREEN");
                clearTextField();
                logger.debug("Added new tape - {}", tape);
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
        name.textProperty().addListener(event -> {
            name.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !name.getText().isEmpty() &&
                            !name.getText().matches(Config.regexName)
            );
        });
        comment.textProperty().addListener(event -> {
            comment.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !comment.getText().isEmpty() &&
                            !comment.getText().matches(Config.regexComment)
            );
        });
        width.textProperty().addListener(event -> {
            width.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !width.getText().isEmpty() &&
                            !width.getText().matches(Config.regexDouble)
            );
        });
        color.textProperty().addListener(event -> {
            color.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !color.getText().isEmpty() &&
                            !color.getText().matches(Config.regexName)
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
        name.clear();
        comment.clear();
        color.clear();
        remainder.clear();
        width.clear();
        price.clear();
    }
}
