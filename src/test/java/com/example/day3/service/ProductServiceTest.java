package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, productMapper);
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        ProductDto productDto = new ProductDto();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        Optional<ProductDto> result = productService.getProductById(1);
        assertTrue(result.isPresent());
        assertEquals(productDto, result.get());
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        ProductDto productDto = new ProductDto();
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.createProduct(productDto);
        assertEquals(productDto, result);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        ProductDto productDto = new ProductDto();
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.updateProduct(productDto);
        assertEquals(productDto, result);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1);
        productService.deleteProduct(1);
        verify(productRepository, times(1)).deleteById(1);
    }
}
