package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.productMapper = ProductMapper.INSTANCE;
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.productDtoToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductDto(savedProduct);
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::productToProductDto);
    }

    @Cacheable(value = "products")
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "products", key = "#id")
    public Optional<ProductDto> updateProduct(Long id, ProductDto productDto) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDto.getName());
                    existingProduct.setPrice(productDto.getPrice());
                    existingProduct.setStock(productDto.getStock());
                    Product updatedProduct = productRepository.save(existingProduct);
                    return productMapper.productToProductDto(updatedProduct);
                });
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
