package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Catalog;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.CatalogRepository;
import com.example.day3.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CatalogRepository catalogRepository;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(productMapper::toProductDto).orElse(null);
    }

    public ProductDto createProduct(ProductDto productDto) {
        Optional<Catalog> catalog = catalogRepository.findById(productDto.getCatalogId());
        if (catalog.isPresent()) {
            Product product = productMapper.toProduct(productDto);
            product.setCatalog(catalog.get());
            product = productRepository.save(product);
            return productMapper.toProductDto(product);
        } else {
            throw new IllegalArgumentException("Invalid catalog ID");
        }
    }

    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        if (productRepository.existsById(id)) {
            Optional<Catalog> catalog = catalogRepository.findById(productDto.getCatalogId());
            if (catalog.isPresent()) {
                Product product = productMapper.toProduct(productDto);
                product.setId(id);
                product.setCatalog(catalog.get());
                product = productRepository.save(product);
                return productMapper.toProductDto(product);
            } else {
                throw new IllegalArgumentException("Invalid catalog ID");
            }
        } else {
            return null;
        }
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
