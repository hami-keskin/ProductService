package com.example.day3.mapper;

import com.example.day3.dto.CatalogDto;
import com.example.day3.dto.ProductDto;
import com.example.day3.entity.Catalog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper
public interface CatalogMapper {
    CatalogMapper INSTANCE = Mappers.getMapper(CatalogMapper.class);

    CatalogDto catalogToCatalogDto(Catalog catalog);
    Catalog catalogDtoToCatalog(CatalogDto catalogDto);

    default CatalogDto convertToCatalogDtoWithProducts(Catalog catalog) {
        CatalogDto catalogDto = catalogToCatalogDto(catalog);
        if (catalog.getProducts() != null) {
            catalogDto.setProducts(
                    catalog.getProducts().stream()
                            .map(ProductMapper.INSTANCE::productToProductDto)
                            .collect(Collectors.toList())
            );
        }
        return catalogDto;
    }
}
