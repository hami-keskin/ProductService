// Product.java
package com.example.day3.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Double price;
    private String description;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Stock stock;
}
