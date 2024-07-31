package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = TestData.createProduct(1, "Laptop", 1200.0, "A high-end laptop", true, null, null);
        productDto = ProductMapper.INSTANCE.toDto(product);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductDto> productDtos = productService.getAllProducts();

        assertNotNull(productDtos);
        assertEquals(1, productDtos.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductDto foundProduct = productService.getProductById(1);

        assertNotNull(foundProduct);
        assertEquals(productDto.getId(), foundProduct.getId());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        ProductDto foundProduct = productService.getProductById(1);

        assertNull(foundProduct);
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto savedProduct = productService.createProduct(productDto);

        assertNotNull(savedProduct);
        assertEquals(productDto.getId(), savedProduct.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto updatedProduct = productService.updateProduct(1, productDto);

        assertNotNull(updatedProduct);
        assertEquals(productDto.getId(), updatedProduct.getId());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.updateProduct(1, productDto));
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1);

        productService.deleteProduct(1);

        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAllProducts() {
        doNothing().when(productRepository).deleteAll();

        productService.deleteAllProducts();

        verify(productRepository, times(1)).deleteAll();
    }
}
