package com.project.canvasBag.controller.material;

import com.project.canvasBag.Config;
import com.project.canvasBag.converter.CustomDoubleStringConverter;
import com.project.canvasBag.converter.CustomIntegerStringConverter;
import com.project.canvasBag.model.Tape;
import com.project.canvasBag.service.TapeService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@FxmlView("tape.fxml")
public class TapeController {

    private final TapeService service;
    private final FxWeaver fxWeaver;
    private final ObservableList<Tape> tapes = FXCollections.observableArrayList();
    private static final Logger logger = LoggerFactory.getLogger(TapeController.class);

    public TapeController(TapeService service, FxWeaver fxWeaver) {
        this.service = service;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private TableView<Tape> tapeTable;

    @FXML
    private BorderPane borderPaneTape;

    @FXML
    private MenuItem deleteContext;

    @FXML
    private TableColumn<Tape, String> comment;

    @FXML
    private TableColumn<Tape, Integer> width;

    @FXML
    private TableColumn<Tape, Long> id;

    @FXML
    private TableColumn<Tape, String> color;

    @FXML
    private TableColumn<Tape, String> name;

    @FXML
    private TableColumn<Tape, Double> price;

    @FXML
    private TableColumn<Tape, Double> remainder;

    @FXML
    private TextField searchTextField;

    public Node show() {
        return borderPaneTape;
    }

    @FXML
    private void initialize() {
        tapes.clear();
        //получаем данные из БД
        service.getAllTape().forEach(tapes::add);

        // устанавливаем тип и значение которое должно хранится в колонке
        id.setCellValueFactory(new PropertyValueFactory<Tape, Long>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Tape, String>("name"));
        comment.setCellValueFactory(new PropertyValueFactory<Tape, String>("comment"));
        color.setCellValueFactory(new PropertyValueFactory<Tape, String>("color"));
        width.setCellValueFactory(new PropertyValueFactory<Tape, Integer>("width"));
        remainder.setCellValueFactory(new PropertyValueFactory<Tape, Double>("remainder"));
        price.setCellValueFactory(new PropertyValueFactory<Tape, Double>("price"));
        //заполняем таблицу данными
        tapeTable.getItems().addAll(tapes);
        //сортировка по имени
        tapeTable.getSortOrder().add(name);
        //поиск аксессуаров
        searchTape();
        //изменять поля
        updateTape();
        //контекстное меню
        contextMenuChanged();
    }

    //контекстное меню
    private void contextMenuChanged() {
        tapeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tape>() {
            @Override
            public void changed(ObservableValue<? extends Tape> observableValue, Tape tape, Tape t1) {
                deleteContext.setVisible(!tapeTable.getItems().isEmpty());
            }
        });
    }

    @FXML
    void addTape() {
        fxWeaver.loadController(NewTapeController.class);
    }

    @FXML
    void deleteTape() {
        Tape tape = tapeTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удалить?");
        alert.setHeaderText(null);
        alert.setContentText(tape.getName() + " " + tape.getComment() + " " + tape.getColor());
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            logger.debug("Deleted this tape - {}", tape);
            service.deleteById(tape.getId());
            refreshTable();
        }
    }

    @FXML
    void deleteAllTape() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удалить?");
        alert.setHeaderText(null);
        alert.setContentText("Очистить таблицу?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            logger.debug("Deleted all tapes!");
            service.deleteAll();
            tapeTable.getItems().clear();
        }
    }

    //Изменить данные в ячейке
    private void updateTape() {
        tapeTable.setEditable(true);
        //Изменить имя
        name.setEditable(true);
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                t -> {
                    Tape tape = (Tape) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexName)) {
                        logger.debug("Updated name this tape - {}, new name - {}", tape, t.getNewValue());
                        tape.setName(t.getNewValue());
                        service.updateTapeName(tape.getId(), tape.getName());
                    } else {
                        errorAlert(t.getNewValue());
                        tapeTable.refresh();
                        logger.debug("Error updated name this tape - {}, invalid value - {}", tape, t.getNewValue());
                    }
                }
        );
        //Изменить комментарий
        comment.setEditable(true);
        comment.setCellFactory(TextFieldTableCell.forTableColumn());
        comment.setOnEditCommit(
                t -> {
                    Tape tape = (Tape) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexComment)) {
                        logger.debug("Updated description this tape - {}, new description - {}", tape, t.getNewValue());
                        tape.setComment(t.getNewValue());
                        service.updateTapeComment(tape.getId(), tape.getComment());
                    } else {
                        errorAlert(t.getNewValue());
                        tapeTable.refresh();
                        logger.debug("Error updated description this tape - {}, invalid value - {}", tape, t.getNewValue());
                    }
                }
        );
        //Изменить остаток
        remainder.setEditable(true);
        remainder.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        remainder.setOnEditCommit(
                t -> {
                    Tape tape = (Tape) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue() > 0) {
                        logger.debug("Updated remainder this tape - {}, new remainder - {}", tape, t.getNewValue());
                        tape.setRemainder(t.getNewValue());
                        service.updateTapeRemainder(tape.getId(), tape.getRemainder());
                    } else {
                        tape.setRemainder(t.getOldValue());
                        tapeTable.refresh();
                        logger.debug("Error updated remainder tape, invalid value");
                    }

                });
        //Изменить ширину
        width.setEditable(true);
        width.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        width.setOnEditCommit(
                t -> {
                    Tape tape = (Tape) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue() > 0) {
                        logger.debug("Updated width this tape - {}, new width - {}", tape, t.getNewValue());
                        tape.setWidth(t.getNewValue());
                        service.updateTapeWidth(tape.getId(), tape.getWidth());
                    } else {
                        tape.setWidth(t.getOldValue());
                        tapeTable.refresh();
                        logger.debug("Error updated width this tape - {}, invalid value - {}", tape, t.getNewValue());
                    }

                });
        //Изменить цвет
        color.setEditable(true);
        color.setCellFactory(TextFieldTableCell.forTableColumn());
        color.setOnEditCommit(
                t -> {
                    Tape tape = (Tape) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexName)) {
                        logger.debug("Updated color this tape - {}, new color - {}", tape, t.getNewValue());
                        tape.setColor(t.getNewValue());
                        service.updateTapeColor(tape.getId(), tape.getColor());
                    } else {
                        errorAlert(t.getNewValue());
                        tapeTable.refresh();
                        logger.debug("Error updated color this tape - {}, invalid value - {}", tape, t.getNewValue());
                    }

                });
        //Изменить цену (1 шт.)
        price.setEditable(true);
        price.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        price.setOnEditCommit(
                t -> {
                    Tape tape = (Tape) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());

                    if (t.getNewValue() > 0) {
                        logger.debug("Updated price this tape - {}, new price - {}", tape, t.getNewValue());
                        tape.setPrice(t.getNewValue());
                        service.updateTapePrice(tape.getId(), tape.getPrice());
                    } else {
                        tape.setPrice(t.getOldValue());
                        tapeTable.refresh();
                        logger.debug("Error updated price tape, invalid value");
                    }
                });
    }

    //поиск по таблице(имя/описание/цвет)
    private void searchTape() {
        searchTextField.textProperty().addListener(event -> {
            searchTextField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !searchTextField.getText().matches(Config.regexComment)
            );
        });
        FilteredList<Tape> filteredList = new FilteredList<>(tapes, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(tape -> {
                        if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                            return true;
                        }
                        String searchKeyword = newValue.toLowerCase();
                        if (tape.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else if (tape.getComment().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else if (tape.getColor().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else return false;
                    }
            );
        });
        SortedList<Tape> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tapeTable.comparatorProperty());
        tapeTable.setItems(sortedList);
    }

    private void refreshTable() {
        tapes.clear();
        service.getAllTape().forEach(tapes::add);
        tapeTable.getItems().addAll(tapes);
        tapeTable.getSortOrder().add(name);
    }

    private void errorAlert(String newValue) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(newValue + " - недопустимое значение!");
        alert.showAndWait();
    }
}
