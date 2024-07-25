package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.CatalogRepository;
import com.example.day3.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CatalogRepository catalogRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CatalogRepository catalogRepository) {
        this.productRepository = productRepository;
        this.catalogRepository = catalogRepository;
        this.productMapper = ProductMapper.INSTANCE;
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.productDtoToProduct(productDto);
        catalogRepository.findById(productDto.getCatalogId()).ifPresent(product::setCatalog);
        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductDto(savedProduct);
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    ProductDto productDto = productMapper.productToProductDto(product);
                    Optional.ofNullable(product.getCatalog()).ifPresent(catalog -> productDto.setCatalogId(catalog.getId()));
                    return productDto;
                });
    }

    @Cacheable(value = "products")
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> {
                    ProductDto productDto = productMapper.productToProductDto(product);
                    Optional.ofNullable(product.getCatalog()).ifPresent(catalog -> productDto.setCatalogId(catalog.getId()));
                    return productDto;
                })
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "products", key = "#id")
    public Optional<ProductDto> updateProduct(Long id, ProductDto productDto) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDto.getName());
                    existingProduct.setPrice(productDto.getPrice());
                    existingProduct.setStock(productDto.getStock());
                    catalogRepository.findById(productDto.getCatalogId()).ifPresent(existingProduct::setCatalog);
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
