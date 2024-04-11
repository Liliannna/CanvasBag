package com.project.canvasBag.controller.material;

import com.project.canvasBag.Config;
import com.project.canvasBag.model.MaterialType;
import com.project.canvasBag.model.Zipper;
import com.project.canvasBag.service.ZipperService;
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
@FxmlView("newZipper.fxml")
public class NewZipperController {
    private static final Logger logger = LoggerFactory.getLogger(NewZipperController.class);
    private final ZipperService service;
    private Stage stage;

    public NewZipperController(ZipperService service) {
        this.service = service;
    }

    @FXML
    private AnchorPane addNew;
    @FXML
    private TextField name;
    @FXML
    private TextField comment;
    @FXML
    private TextField color;
    @FXML
    private TextField size;
    @FXML
    private TextField remainder;
    @FXML
    private TextField price;
    @FXML
    private Label successLabel;

    @FXML
    private void initialize(){
        stage = new Stage();
        Scene scene = new Scene(addNew);
        stage.setScene(scene);
        stage.setTitle("Добавить молнию");
        stage.setResizable(false);
        listenerTextField();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void cancelButton(){
        stage.close();
    }

    @FXML
    private void saveButton(){
        Zipper zipper = new Zipper();
        //Проверяем - если не пустые значения то добавляем в базу
        try{
            if(!name.getText().isEmpty() && !comment.getText().isEmpty() && !color.getText().isEmpty() &&
            !size.getText().isEmpty() && !remainder.getText().isEmpty() && !price.getText().isEmpty()){
                zipper.setType(MaterialType.ZIPPER);
                zipper.setName(name.getText());
                zipper.setComment(comment.getText());
                zipper.setColor(color.getText());
                zipper.setRemainder(Integer.parseInt(remainder.getText()));
                zipper.setSize(Integer.parseInt(size.getText()));
                zipper.setPrice(Double.parseDouble(price.getText()));
                service.save(zipper);
                successLabel.setText("Сохранено!");
                successLabel.setStyle("-fx-text-fill: GREEN");
                clearTextField();
                logger.debug("Added new zipper - {}", zipper);
            } else {
                successLabel.setText("Заполните поля!");
                successLabel.setStyle("-fx-text-fill: RED");
            }
        } catch (ConstraintViolationException ex){
            logger.debug(ex.getMessage());
            successLabel.setText("Проверьте данные!");
            successLabel.setStyle("-fx-text-fill: RED");
        }
    }

    //Проверяем правильно ли заполняются поля ввода
    private void listenerTextField(){
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
        color.textProperty().addListener(event -> {
            color.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !color.getText().isEmpty() &&
                            !color.getText().matches(Config.regexName)
            );
        });
        size.textProperty().addListener(event -> {
            size.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !size.getText().isEmpty() &&
                            !size.getText().matches(Config.regexNumber)
            );
        });
        remainder.textProperty().addListener(event -> {
            remainder.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !remainder.getText().isEmpty() &&
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
    private void clearTextField(){
        name.clear();
        comment.clear();
        color.clear();
        size.clear();
        remainder.clear();
        price.clear();
    }
}
