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
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllStocks() {
        // Given
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(100);
        StockDto stockDto = StockMapper.INSTANCE.toDto(stock);
        when(stockRepository.findAll()).thenReturn(Arrays.asList(stock));

        // When
        var result = stockService.getAllStocks();

        // Then
        verify(stockRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(stockDto.getQuantity(), result.get(0).getQuantity());
    }

    @Test
    public void testGetStockById() {
        // Given
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(100);
        StockDto stockDto = StockMapper.INSTANCE.toDto(stock);
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        // When
        var result = stockService.getStockById(1);

        // Then
        verify(stockRepository, times(1)).findById(1);
        assertEquals(stockDto.getQuantity(), result.getQuantity());
    }

    @Test
    public void testCreateStock() {
        // Given
        StockDto stockDto = new StockDto();
        stockDto.setQuantity(100);
        Stock stock = StockMapper.INSTANCE.toEntity(stockDto);
        when(stockRepository.save(stock)).thenReturn(stock);

        // When
        var result = stockService.createStock(stockDto);

        // Then
        verify(stockRepository, times(1)).save(stock);
        assertEquals(stockDto.getQuantity(), result.getQuantity());
    }

    @Test
    public void testUpdateStock() {
        // Given
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(100);
        StockDto stockDto = new StockDto();
        stockDto.setQuantity(200);
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(stock)).thenReturn(stock);

        // When
        var result = stockService.updateStock(1, stockDto);

        // Then
        verify(stockRepository, times(1)).findById(1);
        verify(stockRepository, times(1)).save(stock);
        assertEquals(stockDto.getQuantity(), result.getQuantity());
    }

    @Test
    public void testDeleteStock() {
        // When
        stockService.deleteStock(1);

        // Then
        verify(stockRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAllStocks() {
        // When
        stockService.deleteAllStocks();

        // Then
        verify(stockRepository, times(1)).deleteAll();
    }

    @Test
    public void testReduceStock() {
        // Given
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(100);
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));

        // When
        stockService.reduceStock(1, 50);

        // Then
        verify(stockRepository, times(1)).findByProductId(1);
        verify(stockRepository, times(1)).save(stock);
        assertEquals(50, stock.getQuantity());
    }

    @Test
    public void testReduceStockNotEnough() {
        // Given
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(10);
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> stockService.reduceStock(1, 50));
        assertEquals("Stokta yeterli ürün yok.", thrown.getMessage());
    }
}
