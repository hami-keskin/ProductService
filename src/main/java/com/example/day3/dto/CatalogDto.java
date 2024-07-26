// CatalogDto.java
package com.example.day3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDto {
    private Long id;
    private String name;
    private String description;
    private List<ProductDto> products;
}
