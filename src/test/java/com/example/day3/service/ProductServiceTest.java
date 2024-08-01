package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    public void testGetAllProducts() {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<ProductDto> products = productService.getAllProducts();
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
    }

    @Test
    public void testGetProductById() {
        Product product = new Product();
        product.setId(1);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductDto productDto = productService.getProductById(1);
        assertNotNull(productDto);
        assertEquals(1, productDto.getId());
    }

    @Test
    public void testCreateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto createdProduct = productService.createProduct(productDto);
        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
    }

    @Test
    public void testUpdateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");
        Product product = new Product();
        product.setId(1);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto updatedProduct = productService.updateProduct(1, productDto);
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1);
        productService.deleteProduct(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAllProducts() {
        doNothing().when(productRepository).deleteAll();
        productService.deleteAllProducts();
        verify(productRepository, times(1)).deleteAll();
    }
}
