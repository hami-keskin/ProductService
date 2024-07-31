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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = TestData.getSampleProducts(null);
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDto> productDtos = productService.getAllProducts();

        assertEquals(2, productDtos.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        Product product = TestData.createProduct(1, "Laptop", 1200.00, "High-end gaming laptop", true, null, null);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductDto productDto = productService.getProductById(1);

        assertNotNull(productDto);
        assertEquals("Laptop", productDto.getName());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testGetProductsByCatalogId() {
        Catalog catalog = TestData.createCatalog(1, "Electronics", "Various electronic products", true, null);
        List<Product> products = TestData.getSampleProducts(catalog);
        when(productRepository.findByCatalogId(1)).thenReturn(products);

        List<ProductDto> productDtos = productService.getProductsByCatalogId(1);

        assertEquals(2, productDtos.size());
        verify(productRepository, times(1)).findByCatalogId(1);
    }

    @Test
    void testCreateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("New Product");
        productDto.setPrice(100.0);
        productDto.setDescription("New Description");
        productDto.setStatus(true);

        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto createdProduct = productService.createProduct(productDto);

        assertNotNull(createdProduct);
        assertEquals("New Product", createdProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        Product product = TestData.createProduct(1, "Old Name", 100.0, "Old Description", true, null, null);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Name");
        productDto.setPrice(200.0);
        productDto.setDescription("Updated Description");
        productDto.setStatus(true);

        Product updatedProduct = ProductMapper.INSTANCE.toEntity(productDto);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductDto result = productService.updateProduct(1, productDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
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
