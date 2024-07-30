package com.example.day3.controller;

import com.example.day3.dto.StockDto;
import com.example.day3.service.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<StockDto> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public StockDto getStockById(@PathVariable Integer id) {
        return stockService.getStockById(id);
    }

    @PostMapping
    public StockDto createStock(@RequestBody StockDto stockDto) {
        return stockService.createStock(stockDto);
    }

    @PutMapping("/{id}")
    public StockDto updateStock(@PathVariable Integer id, @RequestBody StockDto stockDto) {
        return stockService.updateStock(id, stockDto);
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Integer id) {
        stockService.deleteStock(id);
    }

    @DeleteMapping
    public void deleteAllStocks() {
        stockService.deleteAllStocks();
    }

    @PostMapping("/reduce")
    public void reduceStock(@RequestBody StockDto stockDto) {
        stockService.reduceStock(stockDto.getProductId(), stockDto.getQuantity());
    }
}
