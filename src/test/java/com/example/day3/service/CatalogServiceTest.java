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

    @Test
    public void testUpdateCatalog_NotFound() {
        // Given
        when(catalogRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            catalogService.updateCatalog(1, catalogDto);
        });
        verify(catalogRepository).findById(1);
    }
    @Test
    public void testUpdateCatalog_Success() {
        // Given
        CatalogDto updatedCatalogDto = new CatalogDto();
        updatedCatalogDto.setId(1);
        updatedCatalogDto.setName("Updated Catalog");
        updatedCatalogDto.setDescription("Updated Description");
        updatedCatalogDto.setStatus(true);

        Catalog updatedCatalog = new Catalog();
        updatedCatalog.setId(1);
        updatedCatalog.setName("Updated Catalog");
        updatedCatalog.setDescription("Updated Description");
        updatedCatalog.setStatus(true);

        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogMapper.toEntity(updatedCatalogDto)).thenReturn(updatedCatalog);
        when(catalogRepository.save(updatedCatalog)).thenReturn(updatedCatalog);
        when(catalogMapper.toDto(updatedCatalog)).thenReturn(updatedCatalogDto);

        // When
        CatalogDto result = catalogService.updateCatalog(1, updatedCatalogDto);

        // Then
        assertNotNull(result); // Sonuç null olmamalıdır
        assertEquals(updatedCatalogDto, result); // Güncellenmiş katalog DTO'sunun doğru olduğunu doğrular
        verify(catalogRepository).findById(1); // findById çağrısının yapıldığını doğrular
        verify(catalogRepository).save(updatedCatalog); // save çağrısının yapıldığını doğrular
    }
    @Test
    public void testCacheEvictionOnCreateCatalog() {
        // Given
        CatalogDto newCatalogDto = TestData.createCatalogDto(); // Yeni bir katalog DTO'su
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        // When
        CatalogDto result = catalogService.createCatalog(newCatalogDto);

        // Then
        // Cache'deki tüm entry'lerin temizlendiğini kontrol edin.
        // Cache'e dair doğrudan bir kontrol yapılabilir veya cache yöneticisinin işleyişini doğrulayan testler yapılabilir.
    }

    @Test
    public void testCacheUpdateOnUpdateCatalog() {
        // Given
        CatalogDto updatedCatalogDto = TestData.createCatalogDto();
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        // When
        CatalogDto result = catalogService.updateCatalog(1, updatedCatalogDto);

        // Then
        // Cache'in güncellenip güncellenmediğini kontrol edin.
        // Bu genellikle cache'e dair doğrudan bir kontrol veya cache yöneticisinin işleyişini doğrulayan testler ile yapılabilir.
    }
    @Test
    public void testCacheEvictionOnDeleteCatalog() {
        // Given
        // Mevcut cache verilerini kontrol etmeden önce bir katalog silme işlemi yapılır.

        // When
        catalogService.deleteCatalog(1);

        // Then
        // Belirli bir katalog ID'si için cache'deki verinin temizlendiğini doğrulayın.
        // Bu doğrudan cache kontrolü veya cache yöneticisinin işleyişini doğrulayan testlerle yapılabilir.
    }
    @Test
    public void testCreateCatalog_ThrowsExceptionOnDatabaseError() {
        // Given
        when(catalogRepository.save(any(Catalog.class))).thenThrow(new RuntimeException("Database error"));

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> {
            catalogService.createCatalog(catalogDto);
        });

        // Then
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testUpdateCatalog_ThrowsExceptionOnDatabaseError() {
        // Given
        when(catalogRepository.findById(1)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(any(Catalog.class))).thenThrow(new RuntimeException("Database error"));

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> {
            catalogService.updateCatalog(1, catalogDto);
        });

        // Then
        assertEquals("Database error", exception.getMessage());
    }

}
