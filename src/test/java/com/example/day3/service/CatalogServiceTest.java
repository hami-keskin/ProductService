package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.repository.CatalogRepository;
import com.example.day3.util.TestDataCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class CatalogServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    @InjectMocks
    private CatalogService catalogService;

    private Catalog catalog;
    private CatalogDto catalogDto;

    @BeforeEach
    void setUp() {
        catalog = TestDataCatalog.createCatalog();
        catalogDto = TestDataCatalog.createCatalogDto();
    }

    @Test
    public void testGetCatalogById() {
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        CatalogDto foundCatalog = catalogService.getCatalogById(1);
        assertNotNull(foundCatalog);
        assertEquals(1, foundCatalog.getId());
    }

    @Test
    public void testCreateCatalog() {
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);
        CatalogDto savedCatalogDto = catalogService.createCatalog(catalogDto);
        assertNotNull(savedCatalogDto);
        assertEquals(catalog.getId(), savedCatalogDto.getId());
    }

    @Test
    public void testUpdateCatalog() {
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);
        CatalogDto updatedCatalogDto = catalogService.updateCatalog(1, catalogDto);
        assertNotNull(updatedCatalogDto);
        assertEquals(catalog.getId(), updatedCatalogDto.getId());
    }

    @Test
    public void testDeleteCatalog() {
        doNothing().when(catalogRepository).deleteById(1);
        catalogService.deleteCatalog(1);
        verify(catalogRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetCatalogById_NotFound() {
        when(catalogRepository.findById(1)).thenReturn(Optional.empty());
        CatalogDto foundCatalog = catalogService.getCatalogById(1);
        assertNull(foundCatalog);
    }

    @Test
    public void testCreateCatalog_InvalidData() {
        catalogDto.setName(null);
        assertThrows(IllegalArgumentException.class, () -> catalogService.createCatalog(catalogDto));
    }

    @Test
    public void testUpdateCatalog_NotFound() {
        when(catalogRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> catalogService.updateCatalog(1, catalogDto));
    }

    @Test
    public void testDeleteCatalog_NotFound() {
        doThrow(new RuntimeException("Catalog not found")).when(catalogRepository).deleteById(1);
        assertThrows(RuntimeException.class, () -> catalogService.deleteCatalog(1));
    }
}
