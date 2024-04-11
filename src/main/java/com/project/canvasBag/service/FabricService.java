package com.project.canvasBag.service;

import com.project.canvasBag.controller.material.NewFabricController;
import com.project.canvasBag.converter.FabricConvert;
import com.project.canvasBag.data.FabricRepository;
import com.project.canvasBag.dto.request.AddNewFabricDtoRequest;
import com.project.canvasBag.dto.response.FabricTableResponse;
import com.project.canvasBag.erroritem.code.ErrorField;
import com.project.canvasBag.erroritem.exception.AppException;
import com.project.canvasBag.model.Fabric;
import com.project.canvasBag.validator.ValidatorFabric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class FabricService {
    private static final Logger logger = LoggerFactory.getLogger(NewFabricController.class);
    private final FabricRepository repository;

    public FabricService(FabricRepository repository) {
        this.repository = repository;
    }

    public Iterable<FabricTableResponse> getAllFabric() throws IOException {
        System.out.println("fabric service");
        return FabricConvert.convertFabricToFabricTableResponse(repository.findAll());
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void updateFabricName(Long id, String name) {
        repository.updateFabricName(id, name);
    }

    public void updateFabricRemainder(Long id, Double remainder) {
        repository.updateFabricRemainder(id, remainder);
    }

    public void updateFabricPrice(Long id, Double price) {
        repository.updateFabricPrice(id, price);
    }

    public void updateFabricThickness(Long id, Integer thickness) {
        repository.updateFabricThickness(id, thickness);
    }

    public void updateFabricColor(Long id, String color) {
        repository.updateFabricColor(id, color);
    }

    public void save(AddNewFabricDtoRequest newFabric) throws AppException, IOException {
        //валидация
        ValidatorFabric.validationNewAddFabric(newFabric);
        //конвертировать в Fabric
        Fabric fabric = FabricConvert.AddNewFabricDtoRequestToFabric(newFabric);
        //сохранить в базу
        repository.save(fabric);
        logger.debug("Saved fabric to repository - {}", fabric);
    }
}
