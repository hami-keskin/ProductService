package com.example.day3.service;

import com.example.day3.dto.StockDto;
import com.example.day3.entity.Stock;
import com.example.day3.mapper.StockMapper;
import com.example.day3.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockService stockService;

    private StockDto stockDto;
    private Stock stock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        stockDto = TestData.createStockDto();
        stock = TestData.createStock();
        when(stockMapper.toDto(stock)).thenReturn(stockDto);
        when(stockMapper.toEntity(stockDto)).thenReturn(stock);
    }

    @Test
    public void testGetAllStocks() {
        // Given
        when(stockRepository.findAll()).thenReturn(List.of(stock));

        // When
        List<StockDto> result = stockService.getAllStocks();

        // Then
        assertEquals(1, result.size());
        assertEquals(stockDto, result.get(0));
        verify(stockRepository).findAll();
    }

    @Test
    public void testGetStockById_Success() {
        // Given
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        // When
        StockDto result = stockService.getStockById(1);

        // Then
        assertEquals(stockDto, result);
        verify(stockRepository).findById(1);
    }

    @Test
    public void testGetStockById_NotFound() {
        // Given
        when(stockRepository.findById(1)).thenReturn(Optional.empty());

        // When
        StockDto result = stockService.getStockById(1);

        // Then
        assertNull(result);
        verify(stockRepository).findById(1);
    }

    @Test
    public void testCreateStock() {
        // Given
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockMapper.toDto(any(Stock.class))).thenReturn(stockDto);
        when(stockMapper.toEntity(any(StockDto.class))).thenReturn(stock);

        // When
        StockDto result = stockService.createStock(stockDto);

        // Then
        assertNotNull(result);
        assertEquals(stockDto, result);
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    public void testUpdateStock() {
        // Given
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(stock)).thenReturn(stock);

        // When
        StockDto result = stockService.updateStock(1, stockDto);

        // Then
        assertEquals(stockDto, result);
        verify(stockRepository).findById(1);
        verify(stockRepository).save(stock);
    }

    @Test
    public void testDeleteStock() {
        // When
        stockService.deleteStock(1);

        // Then
        verify(stockRepository).deleteById(1);
    }

    @Test
    public void testDeleteAllStocks() {
        // When
        stockService.deleteAllStocks();

        // Then
        verify(stockRepository).deleteAll();
    }

    @Test
    public void testReduceStock_Success() {
        // Given
        stock.setQuantity(10);
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(stock)).thenReturn(stock);

        // When
        stockService.reduceStock(1, 5);

        // Then
        assertEquals(5, stock.getQuantity());
        verify(stockRepository).findByProductId(1);
        verify(stockRepository).save(stock);
    }

    @Test
    public void testReduceStock_InsufficientStock() {
        // Given
        stock.setQuantity(10);
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            stockService.reduceStock(1, 15);
        });
        assertEquals("Stokta yeterli ürün yok.", exception.getMessage());
        verify(stockRepository).findByProductId(1);
    }

    @Test
    public void testReduceStock_NotFound() {
        // Given
        when(stockRepository.findByProductId(1)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            stockService.reduceStock(1, 5);
        });
        assertEquals("Stok bulunamadı.", exception.getMessage());
        verify(stockRepository).findByProductId(1);
    }
    @Test
    public void testTransactionOnCreateStock() {
        // Given
        StockDto newStockDto = TestData.createStockDto(); // Yeni bir stok DTO'su
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // When
        StockDto result = stockService.createStock(newStockDto);

        // Then
        // Veritabanı işlemlerinin düzgün şekilde yapıldığını kontrol edebilirsiniz.
        // Örneğin, veritabanı kaydının gerçekten yapılıp yapılmadığını doğrulayabilirsiniz.
    }

    @Test
    public void testTransactionOnUpdateStock() {
        // Given
        StockDto updatedStockDto = TestData.createStockDto(); // Güncellenmiş stok DTO'su
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // When
        StockDto result = stockService.updateStock(1, updatedStockDto);

        // Then
        // Veritabanı işlemlerinin düzgün şekilde yapıldığını kontrol edebilirsiniz.
        // Örneğin, veritabanı kaydının gerçekten güncellendiğini doğrulayabilirsiniz.
    }

}
