package com.example.day3.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;
}
