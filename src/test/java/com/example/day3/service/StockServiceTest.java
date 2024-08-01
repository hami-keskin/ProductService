package com.example.day3.service;

import com.example.day3.dto.StockDto;
import com.example.day3.entity.Stock;
import com.example.day3.mapper.StockMapper;
import com.example.day3.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockServiceTestFieldInjection {

    @Mock
    private StockRepository stockRepository;

    private StockService stockService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        stockService = new StockService();
        stockService.stockRepository = stockRepository;
    }

    @Test
    public void testGetAllStocks() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(10);
        when(stockRepository.findAll()).thenReturn(Collections.singletonList(stock));

        List<StockDto> stocks = stockService.getAllStocks();
        assertEquals(1, stocks.size());
        assertEquals(10, stocks.get(0).getQuantity());
    }

    @Test
    public void testGetStockById() {
        Stock stock = new Stock();
        stock.setId(1);
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        StockDto stockDto = stockService.getStockById(1);
        assertNotNull(stockDto);
        assertEquals(1, stockDto.getId());
    }

    @Test
    public void testCreateStock() {
        StockDto stockDto = new StockDto();
        stockDto.setQuantity(10);
        Stock stock = StockMapper.INSTANCE.toEntity(stockDto);
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDto createdStock = stockService.createStock(stockDto);
        assertNotNull(createdStock);
        assertEquals(10, createdStock.getQuantity());
    }

    @Test
    public void testUpdateStock() {
        StockDto stockDto = new StockDto();
        stockDto.setQuantity(20);
        Stock stock = new Stock();
        stock.setId(1);
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDto updatedStock = stockService.updateStock(1, stockDto);
        assertNotNull(updatedStock);
        assertEquals(20, updatedStock.getQuantity());
    }

    @Test
    public void testDeleteStock() {
        doNothing().when(stockRepository).deleteById(1);
        stockService.deleteStock(1);
        verify(stockRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAllStocks() {
        doNothing().when(stockRepository).deleteAll();
        stockService.deleteAllStocks();
        verify(stockRepository, times(1)).deleteAll();
    }

    @Test
    public void testReduceStock() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(10);
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        stockService.reduceStock(1, 5);
        verify(stockRepository, times(1)).save(stock);
        assertEquals(5, stock.getQuantity());
    }
}
