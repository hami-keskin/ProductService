package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Catalog;
import com.example.day3.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    public CatalogDto createCatalog(CatalogDto catalogDto) {
        Catalog catalog = new Catalog();
        catalog.setName(catalogDto.getName());
        Catalog savedCatalog = catalogRepository.save(catalog);
        return convertToDto(savedCatalog);
    }

    public CatalogDto getCatalogById(Long id) {
        Catalog catalog = catalogRepository.findById(id).orElse(null);
        return catalog != null ? convertToDto(catalog) : null;
    }

    public List<CatalogDto> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CatalogDto updateCatalog(Long id, CatalogDto catalogDto) {
        Catalog catalog = catalogRepository.findById(id).orElse(null);
        if (catalog != null) {
            catalog.setName(catalogDto.getName());
            Catalog updatedCatalog = catalogRepository.save(catalog);
            return convertToDto(updatedCatalog);
        } else {
            return null;
        }
    }

    public void deleteCatalog(Long id) {
        catalogRepository.deleteById(id);
    }

    public void deleteAllCatalogs() {
        catalogRepository.deleteAll();
    }

    private CatalogDto convertToDto(Catalog catalog) {
        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setId(catalog.getId());
        catalogDto.setName(catalog.getName());
        // Null kontrolü eklenir ve boş listeye dönüştürülür
        catalogDto.setProducts(catalog.getProducts() != null ?
                catalog.getProducts().stream()
                        .map(product -> {
                            ProductDto productDto = new ProductDto();
                            productDto.setId(product.getId());
                            productDto.setName(product.getName());
                            productDto.setCatalogId(product.getCatalog().getId());
                            return productDto;
                        })
                        .collect(Collectors.toList()) :
                List.of());
        return catalogDto;
    }
}
