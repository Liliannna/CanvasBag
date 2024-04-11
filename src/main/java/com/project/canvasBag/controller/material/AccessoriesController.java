package com.project.canvasBag.controller.material;

import com.project.canvasBag.Config;
import com.project.canvasBag.model.Accessories;
import com.project.canvasBag.service.AccessoriesService;
import com.project.canvasBag.converter.CustomDoubleStringConverter;
import com.project.canvasBag.converter.CustomIntegerStringConverter;
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
@FxmlView("accessories.fxml")
public class AccessoriesController {

    private final AccessoriesService service;
    private final FxWeaver fxWeaver;
    private final ObservableList<Accessories> accessoriesList = FXCollections.observableArrayList();
    private static final Logger logger = LoggerFactory.getLogger(AccessoriesController.class);

    public AccessoriesController(AccessoriesService service, FxWeaver fxWeaver) {
        this.service = service;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private TableView<Accessories> accessoriesTable;

    @FXML
    private BorderPane borderPaneAccessories;

    @FXML
    private MenuItem deleteContext;

    @FXML
    private TableColumn<Accessories, String> comment;

    @FXML
    private TableColumn<Accessories, String> size;

    @FXML
    private TableColumn<Accessories, Long> id;

    @FXML
    private TableColumn<Accessories, String> material;

    @FXML
    private TableColumn<Accessories, String> name;

    @FXML
    private TableColumn<Accessories, Double> price;

    @FXML
    private TableColumn<Accessories, Integer> remainder;

    @FXML
    private TextField searchTextField;

    public Node show() {
        return borderPaneAccessories;
    }

    @FXML
    private void initialize() throws Exception {
        accessoriesList.clear();
        //получаем данные из БД
        service.getAllAccessories().forEach(accessoriesList::add);

        // устанавливаем тип и значение которое должно хранится в колонке
        id.setCellValueFactory(new PropertyValueFactory<Accessories, Long>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Accessories, String>("name"));
        comment.setCellValueFactory(new PropertyValueFactory<Accessories, String>("comment"));
        size.setCellValueFactory(new PropertyValueFactory<Accessories, String>("size"));
        material.setCellValueFactory(new PropertyValueFactory<Accessories, String>("material"));
        remainder.setCellValueFactory(new PropertyValueFactory<Accessories, Integer>("remainder"));
        price.setCellValueFactory(new PropertyValueFactory<Accessories, Double>("price"));
        //заполняем таблицу данными
        accessoriesTable.getItems().addAll(accessoriesList);
        //сортировка по имени
        accessoriesTable.getSortOrder().add(name);
        //поиск аксессуаров
        searchAccessories();
        //изменять поля
        updateAccessories();
        //контекстное меню
        contextMenuChanged();
    }

    //контекстное меню
    private void contextMenuChanged() {
        accessoriesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Accessories>() {
            @Override
            public void changed(ObservableValue<? extends Accessories> observableValue, Accessories accessories, Accessories t1) {
                deleteContext.setVisible(!accessoriesTable.getItems().isEmpty());
            }
        });
    }

    @FXML
    void addAccessories() {
        fxWeaver.loadController(NewAccessoriesController.class);
    }

    @FXML
    void deleteAccessories() {
        Accessories accessories = accessoriesTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удалить?");
        alert.setHeaderText(null);
        alert.setContentText(accessories.getName() + " " + accessories.getComment());
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            logger.debug("Deleted this accessories - {}", accessories);
            service.deleteById(accessories.getId());
            refreshTable();
        }
    }

    @FXML
    void deleteAllAccessories() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удалить?");
        alert.setHeaderText(null);
        alert.setContentText("Очистить таблицу?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            logger.debug("Deleted all accessories!");
            service.deleteAll();
            accessoriesTable.getItems().clear();
        }
    }

    //Изменить данные в ячейке
    private void updateAccessories() {
        accessoriesTable.setEditable(true);
        //Изменить имя
        name.setEditable(true);
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                t -> {
                    Accessories accessories = (Accessories) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexName)) {
                        logger.debug("Updated name this accessories - {}, new name - {}", accessories, t.getNewValue());
                        accessories.setName(t.getNewValue());
                        service.updateAccessoriesName(accessories.getId(), accessories.getName());
                    } else {
                        errorAlert(t.getNewValue());
                        accessoriesTable.refresh();
                        logger.debug("Error updated name this accessories - {}, invalid value - {}", accessories, t.getNewValue());
                    }
                }
        );
        //Изменить комментарий
        comment.setEditable(true);
        comment.setCellFactory(TextFieldTableCell.forTableColumn());
        comment.setOnEditCommit(
                t -> {
                    Accessories accessories = (Accessories) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexComment)) {
                        logger.debug("Updated comment this accessories - {}, new comment - {}", accessories, t.getNewValue());
                        accessories.setComment(t.getNewValue());
                        service.updateAccessoriesComment(accessories.getId(), accessories.getComment());
                    } else {
                        errorAlert(t.getNewValue());
                        accessoriesTable.refresh();
                        logger.debug("Error updated comment this accessories - {}, invalid value - {}", accessories, t.getNewValue());
                    }
                }
        );
        //Изменить остаток
        remainder.setEditable(true);
        remainder.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        remainder.setOnEditCommit(
                t -> {
                    Accessories accessories = (Accessories) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue() > 0) {
                        logger.debug("Updated remainder this accessories - {}, new remainder - {}", accessories, t.getNewValue());
                        accessories.setRemainder(t.getNewValue());
                        service.updateAccessoriesRemainder(accessories.getId(), accessories.getRemainder());
                    } else {
                        accessories.setRemainder(t.getOldValue());
                        accessoriesTable.refresh();
                        logger.debug("Error updated remainder accessories, invalid value");
                    }

                });
        //Изменить размер
        size.setEditable(true);
        size.setCellFactory(TextFieldTableCell.forTableColumn());
        size.setOnEditCommit(
                t -> {
                    Accessories accessories = (Accessories) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexSize)) {
                        logger.debug("Updated size this accessories - {}, new size - {}", accessories, t.getNewValue());
                        accessories.setSize(t.getNewValue());
                        service.updateAccessoriesSize(accessories.getId(), accessories.getSize());
                    } else {
                        errorAlert(t.getNewValue());
                        accessoriesTable.refresh();
                        logger.debug("Error updated size this accessories - {}, invalid value - {}", accessories, t.getNewValue());
                    }

                });
        //Изменить материал
        material.setEditable(true);
        material.setCellFactory(TextFieldTableCell.forTableColumn());
        material.setOnEditCommit(
                t -> {
                    Accessories accessories = (Accessories) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexName)) {
                        logger.debug("Updated material this accessories - {}, new material - {}", accessories, t.getNewValue());
                        accessories.setMaterial(t.getNewValue());
                        service.updateAccessoriesMaterial(accessories.getId(), accessories.getMaterial());
                    } else {
                        errorAlert(t.getNewValue());
                        accessoriesTable.refresh();
                        logger.debug("Error updated material this accessories - {}, invalid value - {}", accessories, t.getNewValue());
                    }

                });
        //Изменить цену (1 шт.)
        price.setEditable(true);
        price.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        price.setOnEditCommit(
                t -> {
                    Accessories accessories = (Accessories) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());

                    if (t.getNewValue() > 0) {
                        logger.debug("Updated price this accessories - {}, new price - {}", accessories, t.getNewValue());
                        accessories.setPrice(t.getNewValue());
                        service.updateAccessoriesPrice(accessories.getId(), accessories.getPrice());
                    } else {
                        accessories.setPrice(t.getOldValue());
                        accessoriesTable.refresh();
                        logger.debug("Error updated price accessories, invalid value");
                    }
                });
    }

    //поиск по таблице(имя/комментарий/материал)
    private void searchAccessories() {
        searchTextField.textProperty().addListener(event -> {
            searchTextField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !searchTextField.getText().matches(Config.regexComment)
            );
        });
        FilteredList<Accessories> filteredList = new FilteredList<>(accessoriesList, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(accessories -> {
                        if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                            return true;
                        }
                        String searchKeyword = newValue.toLowerCase();
                        if (accessories.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else if (accessories.getComment().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else if (accessories.getMaterial().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else return false;
                    }
            );
        });
        SortedList<Accessories> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(accessoriesTable.comparatorProperty());
        accessoriesTable.setItems(sortedList);
    }

    private void refreshTable() {
        accessoriesList.clear();
        service.getAllAccessories().forEach(accessoriesList::add);
        accessoriesTable.getItems().addAll(accessoriesList);
        accessoriesTable.getSortOrder().add(name);
    }

    private void errorAlert(String newValue) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(newValue + " - недопустимое значение!");
        alert.showAndWait();
    }
}
