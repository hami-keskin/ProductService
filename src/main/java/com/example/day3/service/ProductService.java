package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Cacheable(value = "products")
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper.INSTANCE::productToProductDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper.INSTANCE::productToProductDto)
                .orElse(null);
    }

    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "products", key = "#result.id")
    })
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.INSTANCE.productDtoToProduct(productDto);
        return ProductMapper.INSTANCE.productToProductDto(productRepository.save(product));
    }

    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "products", key = "#id")
    })
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        return ProductMapper.INSTANCE.productToProductDto(productRepository.save(product));
    }

    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "products", key = "#id")
    })
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
