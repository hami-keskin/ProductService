package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.dto.ProductDto;
import com.example.day3.dto.StockDto;
import org.springframework.stereotype.Service;

@Service
public class TestDataService {

    public CatalogDto createCatalogDto() {
        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setId(1);
        catalogDto.setName("Test Catalog");
        catalogDto.setDescription("Test Description");
        catalogDto.setStatus(true);
        return catalogDto;
    }

    public ProductDto createProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setStatus(true);
        productDto.setPrice(99.99);
        productDto.setCatalogId(1);
        return productDto;
    }

    public StockDto createStockDto() {
        StockDto stockDto = new StockDto();
        stockDto.setId(1);
        stockDto.setQuantity(100);
        stockDto.setProductId(1);
        return stockDto;
    }
}
