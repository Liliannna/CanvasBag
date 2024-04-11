package com.project.canvasBag.converter;

import com.project.canvasBag.dto.request.AddNewFabricDtoRequest;
import com.project.canvasBag.dto.response.FabricTableResponse;

import com.project.canvasBag.erroritem.exception.AppException;
import com.project.canvasBag.model.Fabric;
import com.project.canvasBag.model.MaterialType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricConvert {
    public static Fabric AddNewFabricDtoRequestToFabric(AddNewFabricDtoRequest newFabric) throws IOException, AppException {
        Fabric fabric = new Fabric();
        fabric.setName(newFabric.getName());
        fabric.setColor(newFabric.getColor());
        fabric.setRemainder((Double.parseDouble(newFabric.getWidth()) * Double.parseDouble(newFabric.getLength())) / 100);
        fabric.setThickness(Integer.parseInt(newFabric.getThickness()));
        fabric.setPrice(Double.parseDouble(newFabric.getPrice()));
        fabric.setComment(newFabric.getComment());
        fabric.setType(MaterialType.FABRIC);

        System.out.println(newFabric.getURLImage());
        String fileName = GeneratorRandomNameFile.generatorNameFile();
        File file = new File(newFabric.getURLImage());
        System.out.println(file);
        BufferedImage input = ImageIO.read(new File(newFabric.getURLImage()));
        System.out.println(input);
        if (input != null) {
            File outputFile = new File("src/main/resources/com/project/canvasBag/image/" + fileName);
            ImageIO.write(input, "PNG", outputFile);
            fabric.setNameFile(fileName);
        } else {
            fabric.setNameFile("noImage.png");
            //throw new AppException(ErrorField.INVALID_FILE);
        }
        return fabric;
    }

    public static Iterable<FabricTableResponse> convertFabricToFabricTableResponse(Iterable<Fabric> fabrics) throws IOException {
        List<FabricTableResponse> fabricTableResponseList = new ArrayList<>();
        for (Fabric fabric : fabrics) {
            FabricTableResponse fabricTableResponse = new FabricTableResponse();
            fabricTableResponse.setId(fabric.getId());
            fabricTableResponse.setName(fabric.getName());
            fabricTableResponse.setRemainder(fabric.getRemainder());
            fabricTableResponse.setThickness(fabric.getThickness());
            fabricTableResponse.setColor(fabric.getColor());
            fabricTableResponse.setComment(fabric.getComment());

            ImageView image = new ImageView();
            image.setFitHeight(50);
            image.setFitWidth(50);
            System.out.println(fabric.getNameFile());
            image.setImage(new Image("com/project/canvasBag/image/" + fabric.getNameFile()));
            fabricTableResponse.setImage(image);

            fabricTableResponseList.add(fabricTableResponse);
        }

        return fabricTableResponseList;
    }
}
