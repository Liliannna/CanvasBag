package com.project.canvasBag.erroritem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDtoItem {
    private String errorCode;
    private String field;
    private String message;
}
