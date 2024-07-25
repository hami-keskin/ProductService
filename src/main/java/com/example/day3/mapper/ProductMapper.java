package com.example.day3.mapper;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(source = "catalog.id", target = "catalogId")
    })
    ProductDto productToProductDto(Product product);

    @Mappings({
            @Mapping(source = "catalogId", target = "catalog.id")
    })
    Product productDtoToProduct(ProductDto productDto);
}
