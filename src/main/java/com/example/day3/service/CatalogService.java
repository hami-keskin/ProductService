package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    @Autowired
    CatalogRepository catalogRepository;

    @Cacheable("catalogs")
    public List<CatalogDto> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(CatalogMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "catalogs", key = "#id")
    public CatalogDto getCatalogById(Integer id) {
        return catalogRepository.findById(id)
                .map(CatalogMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Transactional
    @CacheEvict(value = "catalogs", allEntries = true)
    public CatalogDto createCatalog(CatalogDto catalogDto) {
        Catalog catalog = CatalogMapper.INSTANCE.toEntity(catalogDto);
        return CatalogMapper.INSTANCE.toDto(catalogRepository.save(catalog));
    }

    @Transactional
    @CachePut(value = "catalogs", key = "#id")
    public CatalogDto updateCatalog(Integer id, CatalogDto catalogDto) {
        Catalog catalog = catalogRepository.findById(id).orElseThrow();
        catalog.setName(catalogDto.getName());
        catalog.setDescription(catalogDto.getDescription());
        catalog.setStatus(catalogDto.getStatus());
        return CatalogMapper.INSTANCE.toDto(catalogRepository.save(catalog));
    }

    @Transactional
    @CacheEvict(value = {"catalogs", "productsByCatalog"}, key = "#id", allEntries = true)
    public void deleteCatalog(Integer id) {
        catalogRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "catalogs", allEntries = true)
    public void deleteAllCatalogs() {
        catalogRepository.deleteAll();
    }
}
