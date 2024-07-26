// Catalog.java
package com.example.day3.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
