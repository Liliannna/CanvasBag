package com.project.canvasBag.controller.material;

import com.project.canvasBag.Config;
import com.project.canvasBag.converter.CustomDoubleStringConverter;
import com.project.canvasBag.converter.CustomIntegerStringConverter;
import com.project.canvasBag.model.Zipper;
import com.project.canvasBag.service.ZipperService;
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
@FxmlView("zipper.fxml")
public class ZipperController {
    private final ZipperService service;
    private final FxWeaver fxWeaver;
    private final ObservableList<Zipper> zippers = FXCollections.observableArrayList();
    private static final Logger logger = LoggerFactory.getLogger(ZipperController.class);

    public ZipperController(ZipperService service, FxWeaver fxWeaver) {
        this.service = service;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private BorderPane borderPaneZipper;

    @FXML
    private TableColumn<Zipper, String> color;

    @FXML
    private MenuItem deleteContext;

    @FXML
    private TableColumn<Zipper, String> comment;

    @FXML
    private TableColumn<Zipper, Long> id;

    @FXML
    private TableColumn<Zipper, String> name;

    @FXML
    private TableColumn<Zipper, Double> price;

    @FXML
    private TableColumn<Zipper, Integer> remainder;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableColumn<Zipper, Integer> size;

    @FXML
    private TableView<Zipper> zipperTable;

    public Node show() {
        return borderPaneZipper;
    }

    @FXML
    private void initialize() {
        zippers.clear();
        //получаем данные из БД
        service.getAllZipper().forEach(zippers::add);

        // устанавливаем тип и значение которое должно хранится в колонке
        id.setCellValueFactory(new PropertyValueFactory<Zipper, Long>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Zipper, String>("name"));
        comment.setCellValueFactory(new PropertyValueFactory<Zipper, String>("comment"));
        color.setCellValueFactory(new PropertyValueFactory<Zipper, String>("color"));
        size.setCellValueFactory(new PropertyValueFactory<Zipper, Integer>("size"));
        remainder.setCellValueFactory(new PropertyValueFactory<Zipper, Integer>("remainder"));
        price.setCellValueFactory(new PropertyValueFactory<Zipper, Double>("price"));
        //заполняем таблицу данными
        zipperTable.getItems().addAll(zippers);
        //сортировка по имени
        zipperTable.getSortOrder().add(name);
        //поиск молний
        searchZipper();
        //изменять поля
        updateZipper();
        //контекстное меню
        contextMenuChanged();
    }

    //контекстное меню
    private void contextMenuChanged(){
        zipperTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Zipper>() {
            @Override
            public void changed(ObservableValue<? extends Zipper> observableValue, Zipper zipper, Zipper t1) {
                deleteContext.setVisible(!zipperTable.getItems().isEmpty());
            }
        });
    }

    @FXML
    void addZipper() {
        fxWeaver.loadController(NewZipperController.class);
    }

    @FXML
    void deleteAllZipper() {
        Zipper zipper = zipperTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удалить");
        alert.setHeaderText(null);
        alert.setContentText(zipper.getName() + " " + zipper.getComment() + " " + zipper.getColor());
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == ButtonType.OK){
            logger.debug("Deleted this zipper - {}", zipper);
            service.deletedById(zipper.getId());
            refreshTable();
        }
    }

    @FXML
    void deleteZipper() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удалить");
        alert.setHeaderText(null);
        alert.setContentText("Очистить таблицу?");
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == ButtonType.OK){
            logger.debug("Deleted all zippers!");
            service.deletedAll();
            refreshTable();
        }
    }

    //Изменить данные в ячейке
    private void updateZipper() {
        zipperTable.setEditable(true);
        //изменить имя
        name.setEditable(true);
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                t -> {
                    Zipper zipper = (Zipper) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexName)) {
                        logger.debug("Updated name this zipper - {}, new name - {}", zipper, t.getNewValue());
                        zipper.setName(t.getNewValue());
                        service.updateZipperName(zipper.getId(), zipper.getName());
                    } else {
                        errorAlert(t.getNewValue());
                        zipperTable.refresh();
                        logger.debug("Error updated name this zipper - {}, invalid value - {}", zipper, t.getNewValue());
                    }
                }
        );
        //изменить комментарий
        comment.setEditable(true);
        comment.setCellFactory(TextFieldTableCell.forTableColumn());
        comment.setOnEditCommit(
                t -> {
                    Zipper zipper = (Zipper) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexComment)) {
                        logger.debug("Updated description this zipper - {}, new description - {}", zipper, t.getNewValue());
                        zipper.setComment(t.getNewValue());
                        service.updateZipperComment(zipper.getId(), zipper.getComment());
                    } else {
                        errorAlert(t.getNewValue());
                        zipperTable.refresh();
                        logger.debug("Error updated description this zipper - {}, invalid value - {}", zipper, t.getNewValue());
                    }
                }
        );
        //изменить цвет
        color.setEditable(true);
        color.setCellFactory(TextFieldTableCell.forTableColumn());
        color.setOnEditCommit(
                t -> {
                    Zipper zipper = (Zipper) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexName)) {
                        logger.debug("Updated color this zipper - {}, new color - {}", zipper, t.getNewValue());
                        zipper.setColor(t.getNewValue());
                        service.updateZipperColor(zipper.getId(), zipper.getColor());
                    } else {
                        errorAlert(t.getNewValue());
                        zipperTable.refresh();
                        logger.debug("Error update color this zipper - {}, invalid value - {}", zipper, t.getNewValue());
                    }
                }
        );
        //изменить размер
        size.setEditable(true);
        size.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        size.setOnEditCommit(
                t -> {
                    Zipper zipper = (Zipper) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue() > 0) {
                        logger.debug("Updated size this zipper - {}, new size - {}", zipper, t.getNewValue());
                        zipper.setSize(t.getNewValue());
                        service.updateZipperSize(zipper.getId(), zipper.getSize());
                    } else {
                        zipper.setSize(t.getOldValue());
                        zipperTable.refresh();
                        logger.debug("Error updated size zipper, invalid value!");
                    }
                }
        );
        //изменить остаток
        remainder.setEditable(true);
        remainder.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        remainder.setOnEditCommit(
                t -> {
                    Zipper zipper = (Zipper) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue() > 0) {
                        logger.debug("Updated remainder this zipper - {}, new remainder - {}", zipper, t.getNewValue());
                        zipper.setRemainder(t.getNewValue());
                        service.updateZipperRemainder(zipper.getId(), zipper.getRemainder());
                    } else {
                        zipper.setRemainder(t.getOldValue());
                        zipperTable.refresh();
                        logger.debug("Error updated remainder, invalid value!");
                    }
                }
        );
        //изменить цену
        price.setEditable(true);
        price.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        price.setOnEditCommit(
                t -> {
                    Zipper zipper = (Zipper) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue() > 0) {
                        logger.debug("Updated price this zipper - {}, new price - {}", zipper, t.getNewValue());
                        zipper.setPrice(t.getNewValue());
                        service.updateZipperPrice(zipper.getId(), zipper.getPrice());
                    } else {
                        zipper.setPrice(t.getOldValue());
                        zipperTable.refresh();
                        logger.debug("Error updated price, invalid value!");
                    }
                }
        );
    }

    //поиск по таблице(имя/описание/цвет)
    private void searchZipper() {
        searchTextField.textProperty().addListener(event -> {
            searchTextField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !searchTextField.getText().matches(Config.regexName)
            );
        });

        FilteredList<Zipper> filteredList = new FilteredList<>(zippers, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(zipper -> {
                        if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                            return true;
                        }
                        String searchKeyword = newValue.toLowerCase();
                        if (zipper.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else if (zipper.getComment().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else if (zipper.getColor().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else return false;
                    }
            );
        });

        SortedList<Zipper> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(zipperTable.comparatorProperty());
        zipperTable.setItems(sortedList);
    }


    private void refreshTable() {
        zippers.clear();
        service.getAllZipper().forEach(zippers::add);
        zipperTable.getItems().addAll(zippers);
        zipperTable.getSortOrder().add(name);
    }

    private void errorAlert(String newValue) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(newValue + " - недопустимое значение!");
        alert.showAndWait();
    }
}
