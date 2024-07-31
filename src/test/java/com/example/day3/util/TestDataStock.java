package com.example.day3.util;

import com.example.day3.dto.StockDto;
import com.example.day3.entity.Stock;

public class TestDataStock {

    public static Stock createStock() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setQuantity(10);
        return stock;
    }

    public static StockDto createStockDto() {
        StockDto stockDto = new StockDto();
        stockDto.setId(1);
        stockDto.setProductId(1);
        stockDto.setQuantity(10);
        return stockDto;
    }
}
