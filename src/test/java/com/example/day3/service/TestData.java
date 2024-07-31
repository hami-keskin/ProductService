package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.dto.ProductDto;
import com.example.day3.dto.StockDto;
import com.example.day3.entity.Catalog;
import com.example.day3.entity.Product;
import com.example.day3.entity.Stock;

public class TestData {

    public static CatalogDto createCatalogDto() {
        CatalogDto dto = new CatalogDto();
        dto.setId(1);
        dto.setName("Test Catalog");
        dto.setDescription("Test Description");
        dto.setStatus(true);
        return dto;
    }

    public static Catalog createCatalog() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Test Catalog");
        catalog.setDescription("Test Description");
        catalog.setStatus(true);
        return catalog;
    }

    public static ProductDto createProductDto() {
        ProductDto dto = new ProductDto();
        dto.setId(1);
        dto.setName("Test Product");
        dto.setPrice(100.0);
        dto.setDescription("Test Description");
        dto.setStatus(true);
        return dto;
    }

    public static Product createProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setDescription("Test Description");
        product.setStatus(true);
        return product;
    }

    public static StockDto createStockDto() {
        StockDto dto = new StockDto();
        dto.setId(1);
        dto.setQuantity(10);
        return dto;
    }

    public static Stock createStock() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(10);
        return stock;
    }
}
