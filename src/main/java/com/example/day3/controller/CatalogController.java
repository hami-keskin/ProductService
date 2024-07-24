package com.example.day3.controller;

import com.example.day3.entity.Catalog;
import com.example.day3.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @PostMapping
    public ResponseEntity<Catalog> createCatalog(@RequestBody Catalog catalog) {
        return ResponseEntity.ok(catalogService.createCatalog(catalog));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Catalog> getCatalogById(@PathVariable Long id) {
        Catalog catalog = catalogService.getCatalogById(id);
        if (catalog != null) {
            return ResponseEntity.ok(catalog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Catalog>> getAllCatalogs() {
        return ResponseEntity.ok(catalogService.getAllCatalogs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Catalog> updateCatalog(@PathVariable Long id, @RequestBody Catalog catalogDetails) {
        Catalog updatedCatalog = catalogService.updateCatalog(id, catalogDetails);
        if (updatedCatalog != null) {
            return ResponseEntity.ok(updatedCatalog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long id) {
        catalogService.deleteCatalog(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCatalogs() {
        catalogService.deleteAllCatalogs();
        return ResponseEntity.noContent().build();
    }
}
