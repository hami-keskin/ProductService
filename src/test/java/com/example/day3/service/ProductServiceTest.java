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
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cacheManager = new ConcurrentMapCacheManager();
        // Ensure cache is cleared before each test
        cacheManager.getCache("products").clear();
        cacheManager.getCache("productsByCatalog").clear();
    }

    @Test
    public void testGetAllProducts() {
        // Given
        Product product = new Product();
        product.setId(1);
        product.setName("Product 1");
        ProductDto productDto = ProductMapper.INSTANCE.toDto(product);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // When
        var result = productService.getAllProducts();

        // Then
        verify(productRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(productDto.getName(), result.get(0).getName());
    }

    @Test
    public void testGetProductById() {
        // Given
        Product product = new Product();
        product.setId(1);
        product.setName("Product 1");
        ProductDto productDto = ProductMapper.INSTANCE.toDto(product);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // When
        var result = productService.getProductById(1);

        // Then
        verify(productRepository, times(1)).findById(1);
        assertEquals(productDto.getName(), result.getName());
    }

    @Test
    public void testGetProductsByCatalogId() {
        // Given
        Product product = new Product();
        product.setId(1);
        product.setName("Product 1");
        ProductDto productDto = ProductMapper.INSTANCE.toDto(product);
        when(productRepository.findByCatalogId(1)).thenReturn(Arrays.asList(product));

        // When
        var result = productService.getProductsByCatalogId(1);

        // Then
        verify(productRepository, times(1)).findByCatalogId(1);
        assertEquals(1, result.size());
        assertEquals(productDto.getName(), result.get(0).getName());
    }

    @Test
    public void testCreateProduct() {
        // Given
        ProductDto productDto = new ProductDto();
        productDto.setName("Product 1");
        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        when(productRepository.save(product)).thenReturn(product);

        // When
        var result = productService.createProduct(productDto);

        // Then
        verify(productRepository, times(1)).save(product);
        assertEquals(productDto.getName(), result.getName());
    }

    @Test
    public void testUpdateProduct() {
        // Given
        Product product = new Product();
        product.setId(1);
        product.setName("Product 1");
        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        // When
        var result = productService.updateProduct(1, productDto);

        // Then
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(product);
        assertEquals(productDto.getName(), result.getName());
    }

    @Test
    public void testDeleteProduct() {
        // When
        productService.deleteProduct(1);

        // Then
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAllProducts() {
        // When
        productService.deleteAllProducts();

        // Then
        verify(productRepository, times(1)).deleteAll();
    }
}
