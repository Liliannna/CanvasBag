package com.project.canvasBag.service;

import com.project.canvasBag.data.ZipperRepository;
import com.project.canvasBag.model.Zipper;
import org.springframework.stereotype.Service;

@Service
public class ZipperService {
    private final ZipperRepository repository;

    public ZipperService(ZipperRepository repository){
        this.repository = repository;
    }

    public Iterable<Zipper> getAllZipper() {
        return repository.findAll();
    }

    public void updateZipperName(Long id, String name) {
        repository.updateZipperName(id, name);
    }

    public void updateZipperComment(Long id, String comment) {
        repository.updateZipperComment(id, comment);
    }

    public void updateZipperColor(Long id, String color) {
        repository.updateZipperColor(id, color);
    }

    public void updateZipperSize(Long id, Integer size) {
        repository.updateZipperSize(id, size);
    }

    public void updateZipperRemainder(Long id, Integer remainder) {
        repository.updateZipperRemainder(id, remainder);
    }

    public void updateZipperPrice(Long id, Double price) {
        repository.updateZipperPrice(id, price);
    }

    public void deletedById(Long id) {
        repository.deleteById(id);
    }

    public void deletedAll() {
        repository.deleteAll();
    }

    public void save(Zipper zipper) {
        repository.save(zipper);
    }
}
