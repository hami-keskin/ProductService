package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.repository.CatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CatalogServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    private CatalogService catalogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        catalogService = new CatalogService(catalogRepository);
    }

    @Test
    void getAllCatalogsTest() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Catalog1");

        when(catalogRepository.findAll()).thenReturn(Collections.singletonList(catalog));

        List<CatalogDto> catalogDtos = catalogService.getAllCatalogs();
        assertEquals(1, catalogDtos.size());
        assertEquals("Catalog1", catalogDtos.get(0).getName());
    }

    @Test
    void getCatalogByIdTest() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Catalog1");

        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));

        CatalogDto catalogDto = catalogService.getCatalogById(1);
        assertNotNull(catalogDto);
        assertEquals("Catalog1", catalogDto.getName());
    }

    @Test
    void createCatalogTest() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Catalog1");

        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("Catalog1");

        CatalogDto createdCatalogDto = catalogService.createCatalog(catalogDto);
        assertNotNull(createdCatalogDto);
        assertEquals("Catalog1", createdCatalogDto.getName());
    }

    @Test
    void updateCatalogTest() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Catalog1");

        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("Updated Catalog1");

        CatalogDto updatedCatalogDto = catalogService.updateCatalog(1, catalogDto);
        assertNotNull(updatedCatalogDto);
        assertEquals("Updated Catalog1", updatedCatalogDto.getName());
    }

    @Test
    void deleteCatalogTest() {
        doNothing().when(catalogRepository).deleteById(1);

        catalogService.deleteCatalog(1);
        verify(catalogRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteAllCatalogsTest() {
        doNothing().when(catalogRepository).deleteAll();

        catalogService.deleteAllCatalogs();
        verify(catalogRepository, times(1)).deleteAll();
    }
}
