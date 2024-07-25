package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Catalog;
import com.example.day3.entity.Product;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.repository.CatalogRepository;
import com.example.day3.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;
    private final ProductRepository productRepository;
    private final CatalogMapper catalogMapper;
    private final ProductMapper productMapper;

    public CatalogService(CatalogRepository catalogRepository, ProductRepository productRepository) {
        this.catalogRepository = catalogRepository;
        this.productRepository = productRepository;
        this.catalogMapper = CatalogMapper.INSTANCE;
        this.productMapper = ProductMapper.INSTANCE;
    }

    @CacheEvict(value = "catalogs", allEntries = true)
    public CatalogDto createCatalog(CatalogDto catalogDto) {
        Catalog catalog = catalogMapper.catalogDtoToCatalog(catalogDto);
        List<Product> products = Optional.ofNullable(catalogDto.getProducts()).orElse(new ArrayList<>())
                .stream()
                .map(productDto -> {
                    Product product = productMapper.productDtoToProduct(productDto);
                    product.setCatalog(catalog);
                    return product;
                })
                .collect(Collectors.toList());
        catalog.setProducts(products);
        Catalog savedCatalog = catalogRepository.save(catalog);
        return catalogMapper.catalogToCatalogDto(savedCatalog);
    }

    @Cacheable(value = "catalogs", key = "#id")
    public Optional<CatalogDto> getCatalogById(Long id) {
        return catalogRepository.findById(id)
                .map(catalog -> {
                    CatalogDto catalogDto = catalogMapper.catalogToCatalogDto(catalog);
                    catalogDto.setProducts(Optional.ofNullable(catalog.getProducts()).orElse(new ArrayList<>())
                            .stream()
                            .map(product -> {
                                ProductDto productDto = productMapper.productToProductDto(product);
                                productDto.setCatalogId(catalog.getId());
                                return productDto;
                            })
                            .collect(Collectors.toList()));
                    return catalogDto;
                });
    }

    @Cacheable(value = "catalogs")
    public List<CatalogDto> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(catalog -> {
                    CatalogDto catalogDto = catalogMapper.catalogToCatalogDto(catalog);
                    catalogDto.setProducts(Optional.ofNullable(catalog.getProducts()).orElse(new ArrayList<>())
                            .stream()
                            .map(product -> {
                                ProductDto productDto = productMapper.productToProductDto(product);
                                productDto.setCatalogId(catalog.getId());
                                return productDto;
                            })
                            .collect(Collectors.toList()));
                    return catalogDto;
                })
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "catalogs", key = "#id")
    public Optional<CatalogDto> updateCatalog(Long id, CatalogDto catalogDto) {
        return catalogRepository.findById(id)
                .map(existingCatalog -> {
                    existingCatalog.setName(catalogDto.getName());
                    existingCatalog.setDescription(catalogDto.getDescription());
                    List<Product> products = Optional.ofNullable(catalogDto.getProducts()).orElse(new ArrayList<>())
                            .stream()
                            .map(productDto -> {
                                Product product = productMapper.productDtoToProduct(productDto);
                                product.setCatalog(existingCatalog);
                                return product;
                            })
                            .collect(Collectors.toList());
                    existingCatalog.setProducts(products);
                    Catalog updatedCatalog = catalogRepository.save(existingCatalog);
                    return catalogMapper.catalogToCatalogDto(updatedCatalog);
                });
    }

    @CacheEvict(value = "catalogs", key = "#id")
    public void deleteCatalog(Long id) {
        catalogRepository.deleteById(id);
    }

    @CacheEvict(value = "catalogs", allEntries = true)
    public void deleteAllCatalogs() {
        catalogRepository.deleteAll();
    }
}
