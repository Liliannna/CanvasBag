package com.project.canvasBag.dto.response;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;

@Data

public class FabricTableResponse {
    private Long id;
    private String name;
    private String color;
    private Double remainder;
    private Integer thickness;
    private ImageView image;
    private String comment;
}
