package com.project.canvasBag.converter;

import java.util.Random;

public class GeneratorRandomNameFile {
    public static String generatorNameFile(){
        String string = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
        StringBuilder nameFile = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            nameFile.append(string.charAt(rnd.nextInt(string.length())));
        }
        nameFile.append(".png");
        return nameFile.toString();
    }
}
