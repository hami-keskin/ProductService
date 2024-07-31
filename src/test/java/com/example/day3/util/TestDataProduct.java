package com.example.day3.util;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;

public class TestDataProduct {

    public static Product createProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1000.0);
        product.setDescription("Gaming Laptop");
        product.setStatus(true);
        return product;
    }

    public static ProductDto createProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1);
        productDto.setName("Laptop");
        productDto.setPrice(1000.0);
        productDto.setDescription("Gaming Laptop");
        productDto.setStatus(true);
        return productDto;
    }
}
