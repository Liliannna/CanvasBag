package com.project.canvasBag.validator;

import com.project.canvasBag.Config;
import com.project.canvasBag.controller.material.NewFabricController;
import com.project.canvasBag.dto.request.AddNewFabricDtoRequest;
import com.project.canvasBag.erroritem.code.ErrorField;
import com.project.canvasBag.erroritem.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ValidatorFabric {

    private static final Logger logger = LoggerFactory.getLogger(NewFabricController.class);
    public static void validationNewAddFabric(AddNewFabricDtoRequest newFabric) throws AppException{
        if(!newFabric.getName().matches(Config.regexName)){
            logger.debug("Invalid name - {}", newFabric.getName());
            throw new AppException(ErrorField.INVALID_NAME);
        }
        if(!newFabric.getColor().matches(Config.regexName)){
            logger.debug("Invalid color - {}", newFabric.getColor());
            throw new AppException(ErrorField.INVALID_NAME);
        }
        if(!newFabric.getWidth().matches(Config.regexDouble)){
            logger.debug("Invalid width - {}", newFabric.getWidth());
            throw new AppException(ErrorField.INVALID_WIDTH);
        }
        if(!newFabric.getLength().matches(Config.regexDouble)){
            logger.debug("Invalid length - {}", newFabric.getLength());
            throw new AppException(ErrorField.INVALID_LENGTH);
        }
        if(newFabric.getThickness() != null){
            if(!newFabric.getThickness().matches(Config.regexNumber)){
                logger.debug("Invalid thickness - {}", newFabric.getThickness());
                throw new AppException(ErrorField.INVALID_THICKNESS);
            }
        }
        if(!newFabric.getPrice().matches(Config.regexDouble)){
            logger.debug("Invalid price - {}", newFabric.getPrice());
            throw new AppException(ErrorField.INVALID_PRICE);
        }
    }
}
