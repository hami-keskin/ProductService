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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CatalogServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    @InjectMocks
    private CatalogService catalogService;

    private Catalog catalog;
    private CatalogDto catalogDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        catalog = TestData.createCatalog(1, "Electronics", "Various electronic products", true, null);
        catalogDto = CatalogMapper.INSTANCE.toDto(catalog);
    }

    @Test
    void testGetAllCatalogs() {
        when(catalogRepository.findAll()).thenReturn(List.of(catalog));

        List<CatalogDto> catalogDtos = catalogService.getAllCatalogs();

        assertNotNull(catalogDtos);
        assertEquals(1, catalogDtos.size());
        verify(catalogRepository, times(1)).findAll();
    }

    @Test
    void testGetCatalogById_Success() {
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));

        CatalogDto foundCatalog = catalogService.getCatalogById(1);

        assertNotNull(foundCatalog);
        assertEquals(catalogDto.getId(), foundCatalog.getId());
        verify(catalogRepository, times(1)).findById(1);
    }

    @Test
    void testGetCatalogById_NotFound() {
        when(catalogRepository.findById(1)).thenReturn(Optional.empty());

        CatalogDto foundCatalog = catalogService.getCatalogById(1);

        assertNull(foundCatalog);
        verify(catalogRepository, times(1)).findById(1);
    }

    @Test
    void testCreateCatalog() {
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto savedCatalog = catalogService.createCatalog(catalogDto);

        assertNotNull(savedCatalog);
        assertEquals(catalogDto.getId(), savedCatalog.getId());
        verify(catalogRepository, times(1)).save(any(Catalog.class));
    }

    @Test
    void testUpdateCatalog_Success() {
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto updatedCatalog = catalogService.updateCatalog(1, catalogDto);

        assertNotNull(updatedCatalog);
        assertEquals(catalogDto.getId(), updatedCatalog.getId());
        verify(catalogRepository, times(1)).findById(1);
        verify(catalogRepository, times(1)).save(any(Catalog.class));
    }

    @Test
    void testUpdateCatalog_NotFound() {
        when(catalogRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> catalogService.updateCatalog(1, catalogDto));
        verify(catalogRepository, times(1)).findById(1);
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
