package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
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

    public List<CatalogDto> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(catalogMapper::toCatalogDto)
                .collect(Collectors.toList());
    }

    public CatalogDto getCatalogById(Integer id) {
        Optional<Catalog> catalog = catalogRepository.findById(id);
        return catalog.map(catalogMapper::toCatalogDto).orElse(null);
    }

    public CatalogDto createCatalog(CatalogDto catalogDto) {
        Catalog catalog = catalogMapper.toCatalog(catalogDto);
        catalog = catalogRepository.save(catalog);
        return catalogMapper.toCatalogDto(catalog);
    }

    public CatalogDto updateCatalog(Integer id, CatalogDto catalogDto) {
        if (catalogRepository.existsById(id)) {
            Catalog catalog = catalogMapper.toCatalog(catalogDto);
            catalog.setId(id);
            catalog = catalogRepository.save(catalog);
            return catalogMapper.toCatalogDto(catalog);
        } else {
            return null;
        }
    }

    public void deleteCatalog(Integer id) {
        catalogRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllCatalogs() {
        catalogRepository.deleteAll();
    }
}
