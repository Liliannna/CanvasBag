package com.project.canvasBag.erroritem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorDtoContainer {
    private List<ErrorDtoItem> errors;
}
