package com.example.day3.controller;

import com.example.day3.dto.CatalogDto;
import com.example.day3.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PostMapping
    public ResponseEntity<CatalogDto> createCatalog(@RequestBody CatalogDto catalogDto) {
        return ResponseEntity.ok(catalogService.createCatalog(catalogDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogDto> getCatalogById(@PathVariable Long id) {
        CatalogDto catalogDto = catalogService.getCatalogById(id);
        return catalogDto != null ? ResponseEntity.ok(catalogDto) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CatalogDto>> getAllCatalogs() {
        return ResponseEntity.ok(catalogService.getAllCatalogs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogDto> updateCatalog(@PathVariable Long id, @RequestBody CatalogDto catalogDto) {
        CatalogDto updatedCatalog = catalogService.updateCatalog(id, catalogDto);
        return updatedCatalog != null ? ResponseEntity.ok(updatedCatalog) : ResponseEntity.notFound().build();
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
