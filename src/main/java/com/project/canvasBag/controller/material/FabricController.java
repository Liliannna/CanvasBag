package com.project.canvasBag.controller.material;

import com.project.canvasBag.Config;
import com.project.canvasBag.dto.request.DeleteFabricRequest;
import com.project.canvasBag.dto.response.FabricTableResponse;
import com.project.canvasBag.model.Fabric;
import com.project.canvasBag.service.FabricService;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Component
@FxmlView("fabric_v2.fxml")
public class FabricController {
    private final FabricService service;
    private final FxWeaver fxWeaver;
    private final ObservableList<FabricTableResponse> fabrics = FXCollections.observableArrayList();
    private static final Logger logger = LoggerFactory.getLogger(FabricController.class);

    public FabricController(FabricService service, FxWeaver fxWeaver) {
        this.service = service;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private BorderPane borderPaneFabric;
    @FXML
    private TableView<FabricTableResponse> fabricTable;
    @FXML
    private TableColumn<FabricTableResponse, Long> id;
    @FXML
    private TableColumn<FabricTableResponse, String> name;
    @FXML
    private TableColumn<FabricTableResponse, String> color;
    @FXML
    private TableColumn<FabricTableResponse, Double> remainder;
    @FXML
    private TableColumn<FabricTableResponse, Integer> thickness;
    @FXML
    private TableColumn<FabricTableResponse, ImageView> image;
    @FXML
    private TableColumn<FabricTableResponse, String> comment;
    @FXML
    private MenuItem deleteContext;
    @FXML
    private TextField searchTextField;

    public Node show() {
        return borderPaneFabric;
    }

    @FXML
    private void initialize() throws Exception {
        fabrics.clear();
        //получаем данные из БД
        service.getAllFabric().forEach(fabrics::add);

        // устанавливаем тип и значение которое должно хранится в колонке
        id.setCellValueFactory(new PropertyValueFactory<FabricTableResponse, Long>("id"));
        name.setCellValueFactory(new PropertyValueFactory<FabricTableResponse, String>("name"));
        color.setCellValueFactory(new PropertyValueFactory<FabricTableResponse, String>("color"));
        remainder.setCellValueFactory(new PropertyValueFactory<FabricTableResponse, Double>("remainder"));
        thickness.setCellValueFactory(new PropertyValueFactory<FabricTableResponse, Integer>("thickness"));
        image.setCellValueFactory(new PropertyValueFactory<FabricTableResponse, ImageView>("image"));
        comment.setCellValueFactory(new PropertyValueFactory<FabricTableResponse, String>("comment"));
        //заполняем таблицу данными
        fabricTable.getItems().addAll(fabrics);
        //сортировка по имени
        fabricTable.getSortOrder().add(name);
        //поиск ткани
        searchFabric();
        //изменять поля
        //updateFabric();
        //контекстное меню
        contextMenuChanged();
    }

    //контекстное меню
    private void contextMenuChanged() {
        fabricTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FabricTableResponse>() {
            @Override
            public void changed(ObservableValue<? extends FabricTableResponse> observableValue, FabricTableResponse fabric, FabricTableResponse t1) {
                deleteContext.setVisible(!fabricTable.getItems().isEmpty());
            }
        });
    }

    //добавить новую ткань
    @FXML
    void addFabric() {
        fxWeaver.loadController(NewFabricController.class);
    }

    //удалить выбранную ткань
    @FXML
    void deleteFabric() throws IOException {
        FabricTableResponse fabric = fabricTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удалить?");
        alert.setHeaderText(null);
        alert.setContentText(fabric.getName() + " " + fabric.getColor());
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            logger.debug("Deleted this fabric - {}", fabric);
            service.deleteById(fabric.getId());
            refreshTable();
        }
    }

    //удалить все
    @FXML
    void deleteAllFabric() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удалить?");
        alert.setHeaderText(null);
        alert.setContentText("Очистить таблицу?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            logger.debug("Deleted all fabrics!");
            service.deleteAll();
            fabrics.clear();
        }
    }

    //Изменить данные в ячейке
    private void updateFabric() {
        fabricTable.setEditable(true);
        //Изменить имя
        name.setEditable(true);
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                t -> {
                    //изменить название
                    FabricTableResponse fabric = (FabricTableResponse) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexName)) {
                        logger.debug("Updated name this fabric - {}, new name - {}", fabric, t.getNewValue());
                        fabric.setName(t.getNewValue());
                        service.updateFabricName(fabric.getId(), fabric.getName());
                    } else {
                        errorAlert(t.getNewValue());
                        fabricTable.refresh();
                        logger.debug("Error updated name this fabric - {}, invalid value - {}", fabric, t.getNewValue());
                    }
                }
        );
        //Изменить цвет
        color.setEditable(true);
        color.setCellFactory(TextFieldTableCell.forTableColumn());
        color.setOnEditCommit(
                t -> {
                    FabricTableResponse fabric = (FabricTableResponse) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexName)) {
                        logger.debug("Updated color this fabric - {}, new color - {}", fabric, t.getNewValue());
                        fabric.setColor(t.getNewValue());
                        service.updateFabricColor(fabric.getId(), fabric.getColor());
                    } else {
                        errorAlert(t.getNewValue());
                        fabricTable.refresh();
                        logger.debug("Error updated color this fabric - {}, invalid value - {}", fabric, t.getNewValue());
                    }
                }
        );
        //Изменить остаток (дм2)
        remainder.setEditable(true);
        remainder.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        remainder.setOnEditCommit(
                t -> {
                    FabricTableResponse fabric = (FabricTableResponse) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue() > 0) {
                        logger.debug("Updated remainder this fabric - {}, new remainder - {}", fabric, t.getNewValue());
                        fabric.setRemainder(t.getNewValue());
                        service.updateFabricRemainder(fabric.getId(), fabric.getRemainder());
                    } else {
                        fabric.setRemainder(t.getOldValue());
                        fabricTable.refresh();
                        logger.debug("Error updated remainder fabric, invalid value");
                    }

                });
        //Изменить плотность (г/м2)
        thickness.setEditable(true);
        thickness.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        thickness.setOnEditCommit(
                t -> {
                    FabricTableResponse fabric = (FabricTableResponse) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue() > 0) {
                        logger.debug("Updated thickness this fabric - {}, new thickness - {}", fabric, t.getNewValue());
                        fabric.setThickness(t.getNewValue());
                        service.updateFabricThickness(fabric.getId(), fabric.getThickness());
                    } else {
                        fabric.setThickness(t.getOldValue());
                        fabricTable.refresh();
                        logger.debug("Error updated thickness fabric, invalid value");
                    }

                });
        //Изменить комментарий
        comment.setEditable(true);
        comment.setCellFactory(TextFieldTableCell.forTableColumn());
        comment.setOnEditCommit(
                t -> {
                    FabricTableResponse fabric = (FabricTableResponse) t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    if (t.getNewValue().matches(Config.regexComment)) {
                        logger.debug("Updated comment this fabric - {}, new comment - {}", fabric, t.getNewValue());
                        fabric.setComment(t.getNewValue());
                        service.updateFabricColor(fabric.getId(), fabric.getComment());
                    } else {
                        errorAlert(t.getNewValue());
                        fabricTable.refresh();
                        logger.debug("Error updated comment this fabric - {}, invalid value - {}", fabric, t.getNewValue());
                    }
                }
        );
    }

    //поиск по таблице(имя/цвет/комментарий)
    private void searchFabric() {
        searchTextField.textProperty().addListener(event -> {
            searchTextField.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    !searchTextField.getText().matches(Config.regexName)
            );
        });
        FilteredList<FabricTableResponse> filteredList = new FilteredList<>(fabrics, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(fabric -> {
                        if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                            return true;
                        }
                        String searchKeyword = newValue.toLowerCase();
                        if (fabric.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else if (fabric.getColor().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else if (fabric.getComment().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else return false;
                    }
            );
        });
        SortedList<FabricTableResponse> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(fabricTable.comparatorProperty());
        fabricTable.setItems(sortedList);
    }

    private void refreshTable() throws IOException {
        fabrics.clear();
        service.getAllFabric().forEach(fabrics::add);
        fabricTable.getItems().addAll(fabrics);
        fabricTable.getSortOrder().add(name);
    }

    private void errorAlert(String newValue) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(newValue + " - недопустимое значение!");
        alert.showAndWait();
    }


}
