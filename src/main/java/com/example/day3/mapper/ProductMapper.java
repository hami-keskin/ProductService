package com.example.day3.mapper;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "catalog.id", target = "catalogId")
    ProductDto toDto(Product product);

    @Mapping(source = "catalogId", target = "catalog.id")
    Product toEntity(ProductDto productDto);
}
