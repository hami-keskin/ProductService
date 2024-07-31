package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1000.0);
        product.setDescription("Gaming Laptop");
        product.setStatus(true);

        productDto = new ProductDto();
        productDto.setId(1);
        productDto.setName("Laptop");
        productDto.setPrice(1000.0);
        productDto.setDescription("Gaming Laptop");
        productDto.setStatus(true);
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        ProductDto foundProduct = productService.getProductById(1);
        assertNotNull(foundProduct);
        assertEquals(1, foundProduct.getId());
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDto savedProductDto = productService.createProduct(productDto);
        assertNotNull(savedProductDto);
        assertEquals(product.getId(), savedProductDto.getId());
    }

    @Test
    public void testUpdateProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDto updatedProductDto = productService.updateProduct(1, productDto);
        assertNotNull(updatedProductDto);
        assertEquals(product.getId(), updatedProductDto.getId());
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1);
        productService.deleteProduct(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        ProductDto foundProduct = productService.getProductById(1);
        assertNull(foundProduct);
    }

    @Test
    public void testCreateProduct_InvalidData() {
        productDto.setPrice(-1000.0);
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDto));
    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.updateProduct(1, productDto));
    }

    @Test
    public void testDeleteProduct_NotFound() {
        doThrow(new RuntimeException("Product not found")).when(productRepository).deleteById(1);
        assertThrows(RuntimeException.class, () -> productService.deleteProduct(1));
    }
}
