package com.example.day3.service;

import com.example.day3.dto.StockDto;
import com.example.day3.entity.Product;
import com.example.day3.entity.Stock;
import com.example.day3.mapper.StockMapper;
import com.example.day3.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {

    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStocks() {
        Stock stock1 = new Stock();
        stock1.setId(1);
        stock1.setQuantity(10);

        Stock stock2 = new Stock();
        stock2.setId(2);
        stock2.setQuantity(20);

        when(stockRepository.findAll()).thenReturn(Arrays.asList(stock1, stock2));

        List<StockDto> stockDtos = stockService.getAllStocks();
        assertEquals(2, stockDtos.size());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void testGetStockById() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(10);

        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        StockDto stockDto = stockService.getStockById(1);
        assertNotNull(stockDto);
        assertEquals(10, stockDto.getQuantity());
        verify(stockRepository, times(1)).findById(1);
    }

    @Test
    void testCreateStock() {
        StockDto stockDto = new StockDto();
        stockDto.setQuantity(10);

        Stock stock = new Stock();
        stock.setQuantity(10);

        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDto savedStockDto = stockService.createStock(stockDto);
        assertNotNull(savedStockDto);
        assertEquals(10, savedStockDto.getQuantity());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testUpdateStock() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(10);

        StockDto stockDto = new StockDto();
        stockDto.setQuantity(20);

        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDto updatedStockDto = stockService.updateStock(1, stockDto);
        assertNotNull(updatedStockDto);
        assertEquals(20, updatedStockDto.getQuantity());
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
    void testDeleteAllStocks() {
        doNothing().when(stockRepository).deleteAll();

        stockService.deleteAllStocks();
        verify(stockRepository, times(1)).deleteAll();
    }

    @Test
    void testReduceStock() {
        Stock stock = new Stock();
        stock.setId(1);
        Product product = new Product();
        product.setId(1);
        stock.setProduct(product);
        stock.setQuantity(10);

        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        stockService.reduceStock(1, 5);
        assertEquals(5, stock.getQuantity());
        verify(stockRepository, times(1)).findByProductId(1);
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void testReduceStockInsufficientQuantity() {
        Stock stock = new Stock();
        stock.setId(1);
        Product product = new Product();
        product.setId(1);
        stock.setProduct(product);
        stock.setQuantity(5);

        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(stock));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            stockService.reduceStock(1, 10);
        });

        assertEquals("Stokta yeterli ürün yok.", thrown.getMessage());
        verify(stockRepository, times(1)).findByProductId(1);
        verify(stockRepository, times(0)).save(any(Stock.class));
    }
}
