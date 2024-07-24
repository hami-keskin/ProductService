package com.example.day3.service;

import com.example.day3.entity.Catalog;
import com.example.day3.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    public Catalog createCatalog(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    public Optional<Catalog> getCatalogById(Long id) {
        return catalogRepository.findById(id);
    }

    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    public Optional<Catalog> updateCatalog(Long id, Catalog catalogDetails) {
        return catalogRepository.findById(id).map(catalog -> {
            catalog.setName(catalogDetails.getName());
            return catalogRepository.save(catalog);
        });
    }

    public void deleteCatalog(Long id) {
        catalogRepository.deleteById(id);
    }

    public void deleteAllCatalogs() {
        catalogRepository.deleteAll();
    }
}
