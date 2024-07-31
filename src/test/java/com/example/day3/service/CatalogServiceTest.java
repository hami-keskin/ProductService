package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.repository.CatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatalogServiceTest {

    @InjectMocks
    private CatalogService catalogService;

    @Mock
    private CatalogRepository catalogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCatalogs() {
        Catalog catalog1 = new Catalog();
        catalog1.setId(1);
        catalog1.setName("Catalog1");

        Catalog catalog2 = new Catalog();
        catalog2.setId(2);
        catalog2.setName("Catalog2");

        when(catalogRepository.findAll()).thenReturn(Arrays.asList(catalog1, catalog2));

        List<CatalogDto> catalogDtos = catalogService.getAllCatalogs();
        assertEquals(2, catalogDtos.size());
        verify(catalogRepository, times(1)).findAll();
    }

    @Test
    void testGetCatalogById() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Catalog1");

        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));

        CatalogDto catalogDto = catalogService.getCatalogById(1);
        assertNotNull(catalogDto);
        assertEquals("Catalog1", catalogDto.getName());
        verify(catalogRepository, times(1)).findById(1);
    }

    @Test
    void testCreateCatalog() {
        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("New Catalog");

        Catalog catalog = new Catalog();
        catalog.setName("New Catalog");

        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto savedCatalogDto = catalogService.createCatalog(catalogDto);
        assertNotNull(savedCatalogDto);
        assertEquals("New Catalog", savedCatalogDto.getName());
        verify(catalogRepository, times(1)).save(any(Catalog.class));
    }

    @Test
    void testUpdateCatalog() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Old Catalog");

        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("Updated Catalog");

        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto updatedCatalogDto = catalogService.updateCatalog(1, catalogDto);
        assertNotNull(updatedCatalogDto);
        assertEquals("Updated Catalog", updatedCatalogDto.getName());
        verify(catalogRepository, times(1)).findById(1);
        verify(catalogRepository, times(1)).save(any(Catalog.class));
    }

    @Test
    void testDeleteCatalog() {
        doNothing().when(catalogRepository).deleteById(1);

        catalogService.deleteCatalog(1);
        verify(catalogRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAllCatalogs() {
        doNothing().when(catalogRepository).deleteAll();

        catalogService.deleteAllCatalogs();
        verify(catalogRepository, times(1)).deleteAll();
    }
}
