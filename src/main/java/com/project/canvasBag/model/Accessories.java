package com.project.canvasBag.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Accessories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MaterialType type;
    private String name;
    private String comment;
    private String size;
    private String material;
    private Integer remainder;
    private Double price;
    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
