package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.repository.CatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CatalogServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    private CatalogService catalogService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        catalogService = new CatalogService();
        catalogService.catalogRepository = catalogRepository;
    }

    @Test
    public void testGetAllCatalogs() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Test Catalog");
        when(catalogRepository.findAll()).thenReturn(Collections.singletonList(catalog));

        List<CatalogDto> catalogs = catalogService.getAllCatalogs();
        assertEquals(1, catalogs.size());
        assertEquals("Test Catalog", catalogs.get(0).getName());
    }

    @Test
    public void testGetCatalogById() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));

        CatalogDto catalogDto = catalogService.getCatalogById(1);
        assertNotNull(catalogDto);
        assertEquals(1, catalogDto.getId());
    }

    @Test
    public void testCreateCatalog() {
        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("Test Catalog");
        Catalog catalog = CatalogMapper.INSTANCE.toEntity(catalogDto);
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto createdCatalog = catalogService.createCatalog(catalogDto);
        assertNotNull(createdCatalog);
        assertEquals("Test Catalog", createdCatalog.getName());
    }

    @Test
    public void testUpdateCatalog() {
        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("Updated Catalog");
        Catalog catalog = new Catalog();
        catalog.setId(1);
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto updatedCatalog = catalogService.updateCatalog(1, catalogDto);
        assertNotNull(updatedCatalog);
        assertEquals("Updated Catalog", updatedCatalog.getName());
    }

    @Test
    public void testDeleteCatalog() {
        doNothing().when(catalogRepository).deleteById(1);
        catalogService.deleteCatalog(1);
        verify(catalogRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAllCatalogs() {
        doNothing().when(catalogRepository).deleteAll();
        catalogService.deleteAllCatalogs();
        verify(catalogRepository, times(1)).deleteAll();
    }
}
