package com.example.day3.service;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Catalog;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product1");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductDto> productDtos = productService.getAllProducts();
        assertEquals(2, productDtos.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId(1);
        product.setName("Product1");

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductDto productDto = productService.getProductById(1);
        assertNotNull(productDto);
        assertEquals("Product1", productDto.getName());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testGetProductsByCatalogId() {
        Catalog catalog = new Catalog();
        catalog.setId(1);

        Product product1 = new Product();
        product1.setId(1);
        product1.setCatalog(catalog);
        product1.setName("Product1");

        Product product2 = new Product();
        product2.setId(2);
        product2.setCatalog(catalog);
        product2.setName("Product2");

        when(productRepository.findByCatalogId(1)).thenReturn(Arrays.asList(product1, product2));

        List<ProductDto> productDtos = productService.getProductsByCatalogId(1);
        assertEquals(2, productDtos.size());
        verify(productRepository, times(1)).findByCatalogId(1);
    }

    @Test
    void testCreateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("New Product");
        productDto.setPrice(100.0);

        Product product = new Product();
        product.setName("New Product");
        product.setPrice(100.0);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto savedProductDto = productService.createProduct(productDto);
        assertNotNull(savedProductDto);
        assertEquals("New Product", savedProductDto.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Old Product");

        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");
        productDto.setPrice(200.0);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto updatedProductDto = productService.updateProduct(1, productDto);
        assertNotNull(updatedProductDto);
        assertEquals("Updated Product", updatedProductDto.getName());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
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
