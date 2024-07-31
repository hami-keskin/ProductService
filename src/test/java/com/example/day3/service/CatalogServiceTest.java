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
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CatalogServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    @InjectMocks
    private CatalogService catalogService;

    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cacheManager = new ConcurrentMapCacheManager();
        // Ensure cache is cleared before each test
        cacheManager.getCache("catalogs").clear();
    }

    @Test
    public void testGetAllCatalogs() {
        // Given
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Catalog 1");
        CatalogDto catalogDto = CatalogMapper.INSTANCE.toDto(catalog);
        when(catalogRepository.findAll()).thenReturn(Arrays.asList(catalog));

        // When
        var result = catalogService.getAllCatalogs();

        // Then
        verify(catalogRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(catalogDto.getName(), result.get(0).getName());
    }

    @Test
    public void testGetCatalogById() {
        // Given
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Catalog 1");
        CatalogDto catalogDto = CatalogMapper.INSTANCE.toDto(catalog);
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));

        // When
        var result = catalogService.getCatalogById(1);

        // Then
        verify(catalogRepository, times(1)).findById(1);
        assertEquals(catalogDto.getName(), result.getName());
    }

    @Test
    public void testCreateCatalog() {
        // Given
        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("Catalog 1");
        Catalog catalog = CatalogMapper.INSTANCE.toEntity(catalogDto);
        when(catalogRepository.save(catalog)).thenReturn(catalog);

        // When
        var result = catalogService.createCatalog(catalogDto);

        // Then
        verify(catalogRepository, times(1)).save(catalog);
        assertEquals(catalogDto.getName(), result.getName());
    }

    @Test
    public void testUpdateCatalog() {
        // Given
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Catalog 1");
        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("Updated Catalog");
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(catalog)).thenReturn(catalog);

        // When
        var result = catalogService.updateCatalog(1, catalogDto);

        // Then
        verify(catalogRepository, times(1)).findById(1);
        verify(catalogRepository, times(1)).save(catalog);
        assertEquals(catalogDto.getName(), result.getName());
    }

    @Test
    public void testDeleteCatalog() {
        // When
        catalogService.deleteCatalog(1);

        // Then
        verify(catalogRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAllCatalogs() {
        // When
        catalogService.deleteAllCatalogs();

        // Then
        verify(catalogRepository, times(1)).deleteAll();
    }
}
