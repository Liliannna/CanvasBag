package com.project.canvasBag;

import lombok.Getter;

import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class Config {
    public static final String regexName = "^[а-яёА-ЯЁ, \\-\\. ]{2,20}";
    public static final String regexComment = "^[а-яёА-ЯЁ, a-zA-Z, \\-\\. ]{0,200}";
    public static final String regexDouble = "^[\\d*]|[\\d+\\.\\d]{2,5}";
    public static final String regexSize = "^[\\d*]|[\\d+\\-\\,\\.\\d]{0,5}";
    public static final String regexNumber = "^[\\d]{0,5}";
}
