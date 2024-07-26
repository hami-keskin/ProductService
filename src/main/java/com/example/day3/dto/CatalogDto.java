package com.example.day3.dto;

import lombok.Data;

import java.util.List;

@Data
public class CatalogDto {
    private Integer id;
    private String name;
    private String description;
    private List<ProductDto> products;
}
