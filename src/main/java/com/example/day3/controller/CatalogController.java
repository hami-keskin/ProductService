package com.example.day3.controller;

import com.example.day3.dto.CatalogDto;
import com.example.day3.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;

    @GetMapping
    public List<CatalogDto> getAllCatalogs() {
        return catalogService.getAllCatalogs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogDto> getCatalogById(@PathVariable Integer id) {
        CatalogDto catalogDto = catalogService.getCatalogById(id);
        return catalogDto != null ? ResponseEntity.ok(catalogDto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public CatalogDto createCatalog(@RequestBody CatalogDto catalogDto) {
        return catalogService.createCatalog(catalogDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogDto> updateCatalog(@PathVariable Integer id, @RequestBody CatalogDto catalogDto) {
        CatalogDto updatedCatalog = catalogService.updateCatalog(id, catalogDto);
        return updatedCatalog != null ? ResponseEntity.ok(updatedCatalog) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Integer id) {
        catalogService.deleteCatalog(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCatalogs() {
        catalogService.deleteAllCatalogs();
        return ResponseEntity.noContent().build();
    }
}
