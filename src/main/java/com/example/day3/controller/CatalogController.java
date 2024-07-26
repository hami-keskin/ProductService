package com.example.day3.controller;

import com.example.day3.dto.CatalogDto;
import com.example.day3.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping
    public List<CatalogDto> getAllCatalogs() {
        return catalogService.getAllCatalogs();
    }

    @GetMapping("/{id}")
    public CatalogDto getCatalogById(@PathVariable Long id) {
        return catalogService.getCatalogById(id);
    }

    @PostMapping
    public CatalogDto createCatalog(@RequestBody CatalogDto catalogDto) {
        return catalogService.createCatalog(catalogDto);
    }

    @PutMapping("/{id}")
    public CatalogDto updateCatalog(@PathVariable Long id, @RequestBody CatalogDto catalogDto) {
        return catalogService.updateCatalog(id, catalogDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCatalog(@PathVariable Long id) {
        catalogService.deleteCatalog(id);
    }
}
