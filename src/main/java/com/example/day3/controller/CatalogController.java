package com.example.day3.controller;

import com.example.day3.dto.CatalogDto;
import com.example.day3.service.CatalogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    private final CatalogService catalogService;

    // Constructor injection
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<CatalogDto> getAllCatalogs() {
        return catalogService.getAllCatalogs();
    }

    @GetMapping("/{id}")
    public CatalogDto getCatalogById(@PathVariable Integer id) {
        return catalogService.getCatalogById(id);
    }

    @PostMapping
    public CatalogDto createCatalog(@RequestBody CatalogDto catalogDto) {
        return catalogService.createCatalog(catalogDto);
    }

    @PutMapping("/{id}")
    public CatalogDto updateCatalog(@PathVariable Integer id, @RequestBody CatalogDto catalogDto) {
        return catalogService.updateCatalog(id, catalogDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCatalog(@PathVariable Integer id) {
        catalogService.deleteCatalog(id);
    }

    @DeleteMapping
    public void deleteAllCatalogs() {
        catalogService.deleteAllCatalogs();
    }
}
