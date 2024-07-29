package com.example.day3.service;

import com.example.day3.dto.StockDto;
import com.example.day3.entity.Product;
import com.example.day3.entity.Stock;
import com.example.day3.mapper.StockMapper;
import com.example.day3.repository.StockRepository;
import com.example.day3.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Cacheable("stocks")
    public List<StockDto> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(StockMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "stocks", key = "#id")
    public StockDto getStockById(Integer id) {
        return stockRepository.findById(id)
                .map(StockMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Transactional
    @CacheEvict(value = "stocks", allEntries = true)
    public StockDto createStock(StockDto stockDto) {
        Stock stock = StockMapper.INSTANCE.toEntity(stockDto);
        return StockMapper.INSTANCE.toDto(stockRepository.save(stock));
    }

    @Transactional
    @CachePut(value = "stocks", key = "#id")
    public StockDto updateStock(Integer id, StockDto stockDto) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.setQuantity(stockDto.getQuantity());
        return StockMapper.INSTANCE.toDto(stockRepository.save(stock));
    }

    @Transactional
    @CacheEvict(value = {"stocks"}, key = "#id", allEntries = true)
    public void deleteStock(Integer id) {
        stockRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "stocks", allEntries = true)
    public void deleteAllStocks() {
        stockRepository.deleteAll();
    }

    @Transactional
    @CacheEvict(value = "stocks", key = "#productId")
    public void reduceStock(Integer productId, Integer quantity) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stok bulunamadı."));
        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("Stokta yeterli ürün yok.");
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.save(stock);
    }
}
