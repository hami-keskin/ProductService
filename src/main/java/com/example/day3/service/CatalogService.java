package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Catalog;
import com.example.day3.entity.Product;
import com.example.day3.repository.CatalogRepository;
import com.example.day3.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;
    private final ProductRepository productRepository;

    public CatalogService(CatalogRepository catalogRepository, ProductRepository productRepository) {
        this.catalogRepository = catalogRepository;
        this.productRepository = productRepository;
    }

    public CatalogDto createCatalog(CatalogDto catalogDto) {
        Catalog catalog = new Catalog();
        BeanUtils.copyProperties(catalogDto, catalog);
        Catalog savedCatalog = catalogRepository.save(catalog);
        return mapToDto(savedCatalog);
    }

    public Optional<CatalogDto> getCatalogById(Long id) {
        return catalogRepository.findById(id)
                .map(this::mapToDto);
    }

    public List<CatalogDto> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<CatalogDto> updateCatalog(Long id, CatalogDto catalogDto) {
        return catalogRepository.findById(id)
                .map(existingCatalog -> {
                    BeanUtils.copyProperties(catalogDto, existingCatalog, "id", "products");
                    Catalog updatedCatalog = catalogRepository.save(existingCatalog);
                    return mapToDto(updatedCatalog);
                });
    }

    public void deleteCatalog(Long id) {
        catalogRepository.deleteById(id);
    }

    public void deleteAllCatalogs() {
        catalogRepository.deleteAll();
    }

    private CatalogDto mapToDto(Catalog catalog) {
        List<ProductDto> products = catalog.getProducts().stream()
                .map(product -> new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getStock(), catalog.getId()))
                .collect(Collectors.toList());
        return new CatalogDto(catalog.getId(), catalog.getName(), catalog.getDescription(), products);
    }
}
