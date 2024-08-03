package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Cacheable("product")
    public Optional<ProductDto> getProductById(Integer id) {
        return productRepository.findById(id)
                .map(productMapper::toDto);
    }

    @CachePut(value = "product", key = "#result.id")
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @CachePut(value = "product", key = "#productDto.id")
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @CacheEvict(value = "product", key = "#id")
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Cacheable("productsByCatalog")
    public List<ProductDto> getProductsByCatalogId(Integer catalogId) {
        return productRepository.findByCatalogId(catalogId)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}
