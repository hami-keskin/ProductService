package com.example.day3.mapper;

import com.example.day3.dto.StockDto;
import com.example.day3.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockMapper {
    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    @Mapping(source = "product.id", target = "productId")
    StockDto toDto(Stock stock);

    @Mapping(source = "productId", target = "product.id")
    Stock toEntity(StockDto stockDto);
}
