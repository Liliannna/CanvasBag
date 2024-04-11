package com.project.canvasBag.dto.request;

import lombok.Data;

@Data
public class AddNewFabricDtoRequest {
    private String name;
    private String color;
    private String length;
    private String width;
    private String thickness;
    private String comment;
    private String URLImage;
    private String price;
}
