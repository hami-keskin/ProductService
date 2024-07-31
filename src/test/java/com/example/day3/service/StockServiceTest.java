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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStocks() {
        List<Stock> stocks = TestData.getSampleStocks(null);
        when(stockRepository.findAll()).thenReturn(stocks);

        List<StockDto> stockDtos = stockService.getAllStocks();

        assertEquals(2, stockDtos.size());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void testGetStockById() {
        Stock stock = TestData.createStock(1, null, 50);
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        StockDto stockDto = stockService.getStockById(1);

        assertNotNull(stockDto);
        assertEquals(50, stockDto.getQuantity());
        verify(stockRepository, times(1)).findById(1);
    }

    @Test
    void testCreateStock() {
        StockDto stockDto = new StockDto();
        stockDto.setQuantity(50);

        Stock stock = StockMapper.INSTANCE.toEntity(stockDto);
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDto createdStock = stockService.createStock(stockDto);

        assertNotNull(createdStock);
        assertEquals(50, createdStock.getQuantity());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testUpdateStock() {
        Stock stock = TestData.createStock(1, null, 50);
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        StockDto stockDto = new StockDto();
        stockDto.setQuantity(100);

        Stock updatedStock = StockMapper.INSTANCE.toEntity(stockDto);
        when(stockRepository.save(any(Stock.class))).thenReturn(updatedStock);

        StockDto result = stockService.updateStock(1, stockDto);

        assertNotNull(result);
        assertEquals(100, result.getQuantity());
        verify(stockRepository, times(1)).findById(1);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testDeleteStock() {
        doNothing().when(stockRepository).deleteById(1);

        stockService.deleteStock(1);

        verify(stockRepository, times(1)).deleteById(1);
    }

    @Test
    void testReduceStock() {
        Stock stock = TestData.createStock(1, null, 50);
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));

        stockService.reduceStock(1, 20);

        assertEquals(30, stock.getQuantity());
        verify(stockRepository, times(1)).findByProductId(1);
        verify(stockRepository, times(1)).save(stock);
    }
}
