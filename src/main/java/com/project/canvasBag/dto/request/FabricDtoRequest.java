package com.project.canvasBag.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class FabricDtoRequest {
    private String name;
    private String color;
    private Double remainder;
    private Integer thickness;
    private Double price;
    private Date createdAt;
    //добавить поля фото, комментарий
}
