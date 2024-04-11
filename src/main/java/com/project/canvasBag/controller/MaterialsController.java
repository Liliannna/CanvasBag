package com.project.canvasBag.controller;

import com.project.canvasBag.controller.material.AccessoriesController;
import com.project.canvasBag.controller.material.FabricController;
import com.project.canvasBag.controller.material.TapeController;
import com.project.canvasBag.controller.material.ZipperController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("materials.fxml")
public class MaterialsController {
    private final FxWeaver fxWeaver;

    public MaterialsController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    private BorderPane content;


    public Node show() {
        return content;
    }

    //просмотр тканей
    @FXML
    void onMouseClickedFabric() {
        content.setCenter(fxWeaver.loadController(FabricController.class).show());
    }

    //просмотр фурнитуры
    @FXML
    void onMouseClickedAccessories() {
        content.setCenter(fxWeaver.loadController(AccessoriesController.class).show());
    }

    //просмотр строп/лент
    @FXML
    void onMouseClickedTape() {
        content.setCenter(fxWeaver.loadController(TapeController.class).show());
    }

    //просмотр молний
    @FXML
    void onMouseClickedZipper() {
        content.setCenter(fxWeaver.loadController(ZipperController.class).show());
    }

    @FXML
    void onMouseClickedLeather() {

    }

    @FXML
    private void initialize() {
        content.setCenter(fxWeaver.loadController(FabricController.class).show());
    }
}
