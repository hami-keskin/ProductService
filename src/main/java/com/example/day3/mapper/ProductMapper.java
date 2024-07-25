package com.example.day3.mapper;

import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Catalog;
import com.example.day3.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "catalog.id", target = "catalogId")
    ProductDto productToProductDto(Product product);

    @Mapping(source = "catalogId", target = "catalog")
    Product productDtoToProduct(ProductDto productDto);

    default Catalog map(Long id) {
        if (id == null) {
            return null;
        }
        Catalog catalog = new Catalog();
        catalog.setId(id);
        return catalog;
    }

    default Long map(Catalog catalog) {
        if (catalog == null) {
            return null;
        }
        return catalog.getId();
    }
}
