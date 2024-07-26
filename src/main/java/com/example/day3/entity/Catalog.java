package com.example.day3.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}
