package com.example.day3.service;

import com.example.day3.dto.StockDto;
import com.example.day3.entity.Stock;
import com.example.day3.mapper.StockMapper;
import com.example.day3.repository.StockRepository;
import jakarta.persistence.LockModeType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final EntityManager entityManager;

    public Optional<StockDto> getStockById(Integer id) {
        return stockRepository.findById(id)
                .map(stockMapper::toDto);
    }

    public StockDto createStock(StockDto stockDto) {
        Stock stock = stockMapper.toEntity(stockDto);
        stock = stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }

    public StockDto updateStock(StockDto stockDto) {
        Stock stock = stockMapper.toEntity(stockDto);
        stock = stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }

    public void deleteStock(Integer id) {
        stockRepository.deleteById(id);
    }

    @Transactional
    public void reduceStock(Integer productId, Integer quantity) {
        Stock stock = entityManager.find(Stock.class, productId, LockModeType.PESSIMISTIC_WRITE);
        if (stock != null) {
            stock.setQuantity(stock.getQuantity() - quantity);
            stockRepository.save(stock);
        }
    }
}
