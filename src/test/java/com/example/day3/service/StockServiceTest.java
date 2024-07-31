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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Stock stock;
    private StockDto stockDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stock = TestData.createStock(1, null, 50);
        stockDto = StockMapper.INSTANCE.toDto(stock);
    }

    @Test
    void testGetAllStocks() {
        when(stockRepository.findAll()).thenReturn(List.of(stock));

        List<StockDto> stockDtos = stockService.getAllStocks();

        assertNotNull(stockDtos);
        assertEquals(1, stockDtos.size());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void testGetStockById_Success() {
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        StockDto foundStock = stockService.getStockById(1);

        assertNotNull(foundStock);
        assertEquals(stockDto.getId(), foundStock.getId());
        verify(stockRepository, times(1)).findById(1);
    }

    @Test
    void testGetStockById_NotFound() {
        when(stockRepository.findById(1)).thenReturn(Optional.empty());

        StockDto foundStock = stockService.getStockById(1);

        assertNull(foundStock);
        verify(stockRepository, times(1)).findById(1);
    }

    @Test
    void testCreateStock() {
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDto savedStock = stockService.createStock(stockDto);

        assertNotNull(savedStock);
        assertEquals(stockDto.getId(), savedStock.getId());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testUpdateStock_Success() {
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDto updatedStock = stockService.updateStock(1, stockDto);

        assertNotNull(updatedStock);
        assertEquals(stockDto.getId(), updatedStock.getId());
        verify(stockRepository, times(1)).findById(1);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testUpdateStock_NotFound() {
        when(stockRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> stockService.updateStock(1, stockDto));
        verify(stockRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteStock() {
        doNothing().when(stockRepository).deleteById(1);

        stockService.deleteStock(1);

        verify(stockRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAllStocks() {
        doNothing().when(stockRepository).deleteAll();

        stockService.deleteAllStocks();

        verify(stockRepository, times(1)).deleteAll();
    }
}
