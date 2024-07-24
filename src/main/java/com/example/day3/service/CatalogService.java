package com.example.day3.service;

import com.example.day3.entity.Catalog;
import com.example.day3.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    public Catalog createCatalog(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    public Catalog getCatalogById(Long id) {
        return catalogRepository.findById(id).orElse(null);
    }

    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    public Catalog updateCatalog(Long id, Catalog catalogDetails) {
        Catalog catalog = catalogRepository.findById(id).orElse(null);
        if (catalog != null) {
            catalog.setName(catalogDetails.getName());
            return catalogRepository.save(catalog);
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
}
