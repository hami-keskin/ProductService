package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.entity.Product;
import com.example.day3.mapper.ProductMapper;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final CatalogRepository catalogRepository;
    private final CatalogMapper catalogMapper = CatalogMapper.INSTANCE;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @CacheEvict(value = "catalogs", allEntries = true)
    @Transactional
    public CatalogDto createCatalog(CatalogDto catalogDto) {
        Catalog catalog = catalogMapper.catalogDtoToCatalog(catalogDto);
        if (catalogDto.getProducts() != null) {
            List<Product> products = catalogDto.getProducts().stream()
                    .map(productDto -> {
                        Product product = productMapper.productDtoToProduct(productDto);
                        product.setCatalog(catalog);
                        return product;
                    }).collect(Collectors.toList());
            catalog.setProducts(products);
        }
        Catalog savedCatalog = catalogRepository.save(catalog);
        return catalogMapper.convertToCatalogDtoWithProducts(savedCatalog);
    }

    @Cacheable(value = "catalogs", key = "#id")
    public Optional<CatalogDto> getCatalogById(Long id) {
        return catalogRepository.findById(id)
                .map(catalogMapper::convertToCatalogDtoWithProducts);
    }

    @Cacheable(value = "catalogs")
    public List<CatalogDto> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(catalogMapper::convertToCatalogDtoWithProducts)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "catalogs", key = "#id")
    public Optional<CatalogDto> updateCatalog(Long id, CatalogDto catalogDto) {
        return catalogRepository.findById(id)
                .map(existingCatalog -> {
                    existingCatalog.setName(catalogDto.getName());
                    existingCatalog.setDescription(catalogDto.getDescription());

                    existingCatalog.getProducts().clear();
                    if (catalogDto.getProducts() != null) {
                        existingCatalog.getProducts().addAll(
                                catalogDto.getProducts().stream()
                                        .map(productDto -> {
                                            Product product = productMapper.productDtoToProduct(productDto);
                                            product.setCatalog(existingCatalog);
                                            return product;
                                        })
                                        .collect(Collectors.toList())
                        );
                    }

                    Catalog updatedCatalog = catalogRepository.save(existingCatalog);
                    return catalogMapper.convertToCatalogDtoWithProducts(updatedCatalog);
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
