package com.project.canvasBag.erroritem.exception;


import com.project.canvasBag.erroritem.code.ErrorField;
import lombok.Getter;

@Getter
public class AppException extends Exception {
    private final ErrorField errorField;

    public AppException(ErrorField errorField) {
        this.errorField = errorField;
    }

}
