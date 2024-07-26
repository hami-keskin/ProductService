package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Cacheable(value = "catalogs")
    public List<CatalogDto> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(CatalogMapper.INSTANCE::catalogToCatalogDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "catalogs", key = "#id")
    public CatalogDto getCatalogById(Long id) {
        return catalogRepository.findById(id)
                .map(CatalogMapper.INSTANCE::catalogToCatalogDto)
                .orElse(null);
    }

    @Caching(evict = {
            @CacheEvict(value = "catalogs", allEntries = true),
            @CacheEvict(value = "catalogs", key = "#result.id")
    })
    public CatalogDto createCatalog(CatalogDto catalogDto) {
        Catalog catalog = CatalogMapper.INSTANCE.catalogDtoToCatalog(catalogDto);
        return CatalogMapper.INSTANCE.catalogToCatalogDto(catalogRepository.save(catalog));
    }

    @Caching(evict = {
            @CacheEvict(value = "catalogs", allEntries = true),
            @CacheEvict(value = "catalogs", key = "#id")
    })
    public CatalogDto updateCatalog(Long id, CatalogDto catalogDto) {
        Catalog catalog = catalogRepository.findById(id).orElseThrow();
        catalog.setName(catalogDto.getName());
        catalog.setDescription(catalogDto.getDescription());
        return CatalogMapper.INSTANCE.catalogToCatalogDto(catalogRepository.save(catalog));
    }

    @Caching(evict = {
            @CacheEvict(value = "catalogs", allEntries = true),
            @CacheEvict(value = "catalogs", key = "#id")
    })
    public void deleteCatalog(Long id) {
        catalogRepository.deleteById(id);
    }
}
