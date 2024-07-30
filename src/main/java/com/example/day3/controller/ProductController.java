package com.example.day3.controller;

import com.example.day3.dto.ProductDto;
import com.example.day3.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @GetMapping("/catalog/{catalogId}")
    public List<ProductDto> getProductsByCatalogId(@PathVariable Integer catalogId) {
        return productService.getProductsByCatalogId(catalogId);
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Integer id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }

    @DeleteMapping
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }
}
