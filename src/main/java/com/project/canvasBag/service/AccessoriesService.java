package com.project.canvasBag.service;


import com.project.canvasBag.data.AccessoriesRepository;
import com.project.canvasBag.model.Accessories;
import org.springframework.stereotype.Service;

@Service
public class AccessoriesService {
    private final AccessoriesRepository repository;

    public AccessoriesService(AccessoriesRepository repository) {
        this.repository = repository;
    }

    public Iterable<Accessories> getAllAccessories() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void save(Accessories accessories) {
        repository.save(accessories);
    }

    public void updateAccessoriesName(Long id, String name) {
        repository.updateAccessoriesName(id, name);
    }

    public void updateAccessoriesComment(Long id, String comment) {
        repository.updateAccessoriesComment(id, comment);
    }

    public void updateAccessoriesRemainder(Long id, Integer remainder) {
        repository.updateAccessoriesRemainder(id, remainder);
    }

    public void updateAccessoriesSize(Long id, String size) {
        repository.updateAccessoriesSize(id, size);
    }

    public void updateAccessoriesMaterial(Long id, String material) {
        repository.updateAccessoriesMaterial(id, material);
    }

    public void updateAccessoriesPrice(Long id, Double price) {
        repository.updateAccessoriesPrice(id, price);
    }
}
