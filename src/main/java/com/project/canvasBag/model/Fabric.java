package com.project.canvasBag.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Fabric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MaterialType type;
    private String name;
    private String color;
    private Double remainder;
    private Integer thickness;
    private Double price;
    private String comment;
    private String nameFile;
    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
