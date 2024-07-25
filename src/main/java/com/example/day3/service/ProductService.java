package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.repository.CatalogRepository;
import com.example.day3.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CatalogRepository catalogRepository;

    public ProductService(ProductRepository productRepository, CatalogRepository catalogRepository) {
        this.productRepository = productRepository;
        this.catalogRepository = catalogRepository;
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto createProduct(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        catalogRepository.findById(productDto.getCatalogId()).ifPresent(product::setCatalog);
        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToDto);
    }

    @Cacheable(value = "products")
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "products", key = "#id")
    public Optional<ProductDto> updateProduct(Long id, ProductDto productDto) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    BeanUtils.copyProperties(productDto, existingProduct, "id");
                    catalogRepository.findById(productDto.getCatalogId()).ifPresent(existingProduct::setCatalog);
                    Product updatedProduct = productRepository.save(existingProduct);
                    return mapToDto(updatedProduct);
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

    private ProductDto mapToDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getStock(),
                product.getCatalog() != null ? product.getCatalog().getId() : null);
    }
}
