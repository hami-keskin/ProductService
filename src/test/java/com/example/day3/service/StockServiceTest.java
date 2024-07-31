package com.example.day3.service;

import com.example.day3.dto.StockDto;
import com.example.day3.entity.Stock;
import com.example.day3.repository.StockRepository;
import com.example.day3.util.TestDataStock;
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
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Stock stock;
    private StockDto stockDto;

    @BeforeEach
    void setUp() {
        stock = TestDataStock.createStock();
        stockDto = TestDataStock.createStockDto();
    }

    @Test
    public void testGetStockById() {
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));
        StockDto foundStock = stockService.getStockById(1);
        assertNotNull(foundStock);
        assertEquals(1, foundStock.getId());
    }

    @Test
    public void testCreateStock() {
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        StockDto savedStockDto = stockService.createStock(stockDto);
        assertNotNull(savedStockDto);
        assertEquals(stock.getId(), savedStockDto.getId());
    }

    @Test
    public void testUpdateStock() {
        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        StockDto updatedStockDto = stockService.updateStock(1, stockDto);
        assertNotNull(updatedStockDto);
        assertEquals(stock.getId(), updatedStockDto.getId());
    }

    @Test
    public void testDeleteStock() {
        doNothing().when(stockRepository).deleteById(1);
        stockService.deleteStock(1);
        verify(stockRepository, times(1)).deleteById(1);
    }

    @Test
    public void testReduceStock() {
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));
        stockService.reduceStock(1, 5);
        assertEquals(5, stock.getQuantity());
    }

    @Test
    public void testGetStockById_NotFound() {
        when(stockRepository.findById(1)).thenReturn(Optional.empty());
        StockDto foundStock = stockService.getStockById(1);
        assertNull(foundStock);
    }

    @Test
    public void testCreateStock_InvalidData() {
        stockDto.setQuantity(-10);
        assertThrows(IllegalArgumentException.class, () -> stockService.createStock(stockDto));
    }

    @Test
    public void testUpdateStock_NotFound() {
        when(stockRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> stockService.updateStock(1, stockDto));
    }

    @Test
    public void testDeleteStock_NotFound() {
        doThrow(new RuntimeException("Stock not found")).when(stockRepository).deleteById(1);
        assertThrows(RuntimeException.class, () -> stockService.deleteStock(1));
    }

    @Test
    public void testReduceStock_NotEnoughQuantity() {
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));
        assertThrows(RuntimeException.class, () -> stockService.reduceStock(1, 15));
    }
}
