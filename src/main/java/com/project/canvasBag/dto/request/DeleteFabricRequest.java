package com.project.canvasBag.dto.request;

import lombok.Data;

@Data
public class DeleteFabricRequest {
    private Long id;
    private String name;
    private String color;
}
