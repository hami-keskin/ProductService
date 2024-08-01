package com.example.day3.service;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import com.example.day3.mapper.CatalogMapper;
import com.example.day3.repository.CatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    private CatalogService catalogService;

    @BeforeEach
    void setUp() {
        catalogService = new CatalogService();
        catalogService.catalogRepository = catalogRepository;  // Autowired ile dependency injection
    }

    @Test
    void testGetAllCatalogs() {
        List<Catalog> catalogs = List.of(new Catalog());
        when(catalogRepository.findAll()).thenReturn(catalogs);

        List<CatalogDto> catalogDtos = catalogService.getAllCatalogs();

        assertEquals(1, catalogDtos.size());
        verify(catalogRepository, times(1)).findAll();
    }

    @Test
    void testGetCatalogById() {
        Catalog catalog = new Catalog();
        when(catalogRepository.findById(anyInt())).thenReturn(Optional.of(catalog));

        CatalogDto catalogDto = catalogService.getCatalogById(1);

        assertEquals(CatalogMapper.INSTANCE.toDto(catalog), catalogDto);
        verify(catalogRepository, times(1)).findById(1);
    }

    @Test
    void testCreateCatalog() {
        Catalog catalog = new Catalog();
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto catalogDto = new CatalogDto();
        CatalogDto savedCatalogDto = catalogService.createCatalog(catalogDto);

        assertEquals(CatalogMapper.INSTANCE.toDto(catalog), savedCatalogDto);
        verify(catalogRepository, times(1)).save(any(Catalog.class));
    }

    @Test
    void testUpdateCatalog() {
        Catalog catalog = new Catalog();
        when(catalogRepository.findById(anyInt())).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogDto catalogDto = new CatalogDto();
        CatalogDto updatedCatalogDto = catalogService.updateCatalog(1, catalogDto);

        assertEquals(CatalogMapper.INSTANCE.toDto(catalog), updatedCatalogDto);
        verify(catalogRepository, times(1)).findById(1);
        verify(catalogRepository, times(1)).save(catalog);
    }

    @Test
    void testDeleteCatalog() {
        doNothing().when(catalogRepository).deleteById(anyInt());

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
