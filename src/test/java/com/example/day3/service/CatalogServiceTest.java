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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CatalogServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    @InjectMocks
    private CatalogService catalogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCatalogs() {
        List<Catalog> catalogs = TestData.getSampleCatalogs();
        when(catalogRepository.findAll()).thenReturn(catalogs);

        List<CatalogDto> catalogDtos = catalogService.getAllCatalogs();

        assertEquals(2, catalogDtos.size());
        verify(catalogRepository, times(1)).findAll();
    }

    @Test
    void testGetCatalogById() {
        Catalog catalog = TestData.createCatalog(1, "Electronics", "Various electronic products", true, null);
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));

        CatalogDto catalogDto = catalogService.getCatalogById(1);

        assertNotNull(catalogDto);
        assertEquals("Electronics", catalogDto.getName());
        verify(catalogRepository, times(1)).findById(1);
    }

    @Test
    void testCreateCatalog() {
        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("New Catalog");
        catalogDto.setDescription("New Description");
        catalogDto.setStatus(true);

        Catalog catalog = CatalogMapper.INSTANCE.toEntity(catalogDto);
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto createdCatalog = catalogService.createCatalog(catalogDto);

        assertNotNull(createdCatalog);
        assertEquals("New Catalog", createdCatalog.getName());
        verify(catalogRepository, times(1)).save(any(Catalog.class));
    }

    @Test
    void testUpdateCatalog() {
        Catalog catalog = TestData.createCatalog(1, "Old Name", "Old Description", true, null);
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));

        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setName("Updated Name");
        catalogDto.setDescription("Updated Description");
        catalogDto.setStatus(true);

        Catalog updatedCatalog = CatalogMapper.INSTANCE.toEntity(catalogDto);
        when(catalogRepository.save(any(Catalog.class))).thenReturn(updatedCatalog);

        CatalogDto result = catalogService.updateCatalog(1, catalogDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
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
