package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import com.example.day3.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.productDtoToProduct(productDto);
        product.setCatalog(catalogRepository.findById(productDto.getCatalogId()).orElse(null));
        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductDto(savedProduct);
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        return product != null ? productMapper.productToProductDto(product) : null;
    }

    @Cacheable(value = "products")
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "products", key = "#id")
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            product.setCatalog(catalogRepository.findById(productDto.getCatalogId()).orElse(null));
            Product updatedProduct = productRepository.save(product);
            return productMapper.productToProductDto(updatedProduct);
        }
        return null;
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
