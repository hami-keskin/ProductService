package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Cacheable("products")
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDto getProductById(Integer id) {
        return productRepository.findById(id)
                .map(ProductMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Cacheable(value = "productsByCatalog", key = "#catalogId")
    public List<ProductDto> getProductsByCatalogId(Integer catalogId) {
        return productRepository.findByCatalogId(catalogId).stream()
                .map(ProductMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductDto createProduct(ProductDto productDto) {
        if (productDto.getPrice() == null || productDto.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must not be null or negative");
        }
        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        return ProductMapper.INSTANCE.toDto(productRepository.save(product));
    }

    @Transactional
    @CachePut(value = "products", key = "#id")
    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setStatus(productDto.getStatus());
        return ProductMapper.INSTANCE.toDto(productRepository.save(product));
    }

    @Transactional
    @CacheEvict(value = {"products", "productsByCatalog"}, key = "#id", allEntries = true)
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
