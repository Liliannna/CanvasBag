package com.project.canvasBag.service;

import com.project.canvasBag.data.TapeRepository;
import com.project.canvasBag.model.Tape;
import org.springframework.stereotype.Service;

@Service
public class TapeService {
    private final TapeRepository repository;
    public TapeService(TapeRepository repository){
        this.repository = repository;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void updateTapeName(Long id, String name) {
        repository.updateTapeName(id, name);
    }

    public void updateTapeComment(Long id, String comment) {
        repository.updateTapeComment(id, comment);
    }

    public void updateTapeRemainder(Long id, Double remainder) {
        repository.updateTapeRemainder(id, remainder);
    }

    public void updateTapeWidth(Long id, Integer width) {
        repository.updateTapeWidth(id, width);
    }

    public void updateTapeColor(Long id, String color) {
        repository.updateTapeColor(id, color);
    }

    public void updateTapePrice(Long id, Double price) {
        repository.updateTapePrice(id, price);
    }

    public Iterable<Tape> getAllTape() {
        return repository.findAll();
    }

    public void save(Tape tape) {
        repository.save(tape);
    }
}
