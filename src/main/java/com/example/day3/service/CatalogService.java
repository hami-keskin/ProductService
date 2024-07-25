package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    private final CatalogMapper catalogMapper = CatalogMapper.INSTANCE;

    public CatalogDto createCatalog(CatalogDto catalogDto) {
        Catalog catalog = catalogMapper.catalogDtoToCatalog(catalogDto);
        Catalog savedCatalog = catalogRepository.save(catalog);
        return catalogMapper.catalogToCatalogDto(savedCatalog);
    }

    public CatalogDto getCatalogById(Long id) {
        Catalog catalog = catalogRepository.findById(id).orElse(null);
        return catalog != null ? catalogMapper.catalogToCatalogDto(catalog) : null;
    }

    public List<CatalogDto> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(catalogMapper::catalogToCatalogDto)
                .collect(Collectors.toList());
    }

    public CatalogDto updateCatalog(Long id, CatalogDto catalogDto) {
        Catalog catalog = catalogRepository.findById(id).orElse(null);
        if (catalog != null) {
            catalog.setName(catalogDto.getName());
            catalog.setDescription(catalogDto.getDescription());
            Catalog updatedCatalog = catalogRepository.save(catalog);
            return catalogMapper.catalogToCatalogDto(updatedCatalog);
        }
        return null;
    }

    public void deleteCatalog(Long id) {
        catalogRepository.deleteById(id);
    }

    public void deleteAllCatalogs() {
        catalogRepository.deleteAll();
    }
}
