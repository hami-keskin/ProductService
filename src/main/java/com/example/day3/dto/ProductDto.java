package com.example.day3.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String manufacturer;
    private Integer catalogId;
}
