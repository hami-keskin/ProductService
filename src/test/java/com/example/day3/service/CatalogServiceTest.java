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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CatalogServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private CatalogMapper catalogMapper;

    @InjectMocks
    private CatalogService catalogService;

    private CatalogDto catalogDto;
    private Catalog catalog;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        catalogDto = TestData.createCatalogDto();
        catalog = TestData.createCatalog();
        when(catalogMapper.toDto(catalog)).thenReturn(catalogDto);
        when(catalogMapper.toEntity(catalogDto)).thenReturn(catalog);
    }

    @Test
    public void testGetAllCatalogs() {
        // Given
        when(catalogRepository.findAll()).thenReturn(List.of(catalog));

        // When
        List<CatalogDto> result = catalogService.getAllCatalogs();

        // Then
        assertEquals(1, result.size());
        assertEquals(catalogDto, result.get(0));
        verify(catalogRepository).findAll();
    }

    @Test
    public void testGetCatalogById_Success() {
        // Given
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));

        // When
        CatalogDto result = catalogService.getCatalogById(1);

        // Then
        assertEquals(catalogDto, result);
        verify(catalogRepository).findById(1);
    }

    @Test
    public void testGetCatalogById_NotFound() {
        // Given
        when(catalogRepository.findById(1)).thenReturn(Optional.empty());

        // When
        CatalogDto result = catalogService.getCatalogById(1);

        // Then
        assertNull(result);
        verify(catalogRepository).findById(1);
    }

    @Test
    public void testCreateCatalog() {
        // Given
        when(catalogRepository.save(catalog)).thenReturn(catalog);

        // When
        CatalogDto result = catalogService.createCatalog(catalogDto);

        // Then
        assertEquals(catalogDto, result);
        verify(catalogRepository).save(catalog);
    }

    @Test
    public void testUpdateCatalog() {
        // Given
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(catalog)).thenReturn(catalog);

        // When
        CatalogDto result = catalogService.updateCatalog(1, catalogDto);

        // Then
        assertEquals(catalogDto, result);
        verify(catalogRepository).findById(1);
        verify(catalogRepository).save(catalog);
    }

    @Test
    public void testDeleteCatalog() {
        // When
        catalogService.deleteCatalog(1);

        // Then
        verify(catalogRepository).deleteById(1);
    }

    @Test
    public void testDeleteAllCatalogs() {
        // When
        catalogService.deleteAllCatalogs();

        // Then
        verify(catalogRepository).deleteAll();
    }
}
