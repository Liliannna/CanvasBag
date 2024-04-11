package com.project.canvasBag.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import javafx.scene.input.MouseEvent;

@Component
@FxmlView("main.fxml")
public class MainController {

    private final FxWeaver fxWeaver;

    public MainController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private BorderPane main;
    @FXML
    private VBox mainMenu;
    @FXML
    private ImageView material;
    @FXML
    private ImageView model;
    @FXML
    private ImageView product;
    @FXML
    private ImageView order;
    @FXML
    private ImageView graph;

    @FXML
    private void initialize(){
        main.setCenter(fxWeaver.loadController(MaterialsController.class).show());
    }

    //открываем окно с материалами
    @FXML
    void onMouseClickedMaterial() {
        main.setCenter(fxWeaver.loadController(MaterialsController.class).show());
    }

    //всплывающая подсказка
    @FXML
    void onMouseEnteredMaterial() {
        tooltipForMenu(material, "Материалы");
    }

    @FXML
    void onMouseClickedModel(MouseEvent event) {

    }

    //всплывающая подсказка
    @FXML
    void onMouseEnteredModel() {
        tooltipForMenu(model, "Модели");
    }

    @FXML
    void onMouseClickedProduct(MouseEvent event) {

    }

    //всплывающая подсказка
    @FXML
    void onMouseEnteredProduct() {
        tooltipForMenu(product, "В наличии");
    }

    @FXML
    void onMouseClickedOrder(MouseEvent event) {

    }

    //всплывающая подсказка
    @FXML
    void onMouseEnteredOrder() {
        tooltipForMenu(order, "Заказы");
    }

    @FXML
    void onMouseClickedGraph(MouseEvent event) {

    }

    //всплывающая подсказка
    @FXML
    void onMouseEnteredGraph() {
        tooltipForMenu(graph, "Статистика");
    }

    //настройка всплывающих подсказок
    private void tooltipForMenu(ImageView image, String name){
        Tooltip tooltip = new Tooltip(name);
        tooltip.setFont(Font.font("Corbel", 14));
        tooltip.setShowDelay(Duration.millis(10));
        Tooltip.install(image, tooltip);
    }
}
