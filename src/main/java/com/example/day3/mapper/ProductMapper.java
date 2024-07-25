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

    @Mapping(source = "catalogId", target = "catalog.id")
    Product productDtoToProduct(ProductDto productDto);

    default Catalog mapCatalogIdToCatalog(Long catalogId) {
        if (catalogId == null) {
            return null;
        }
        Catalog catalog = new Catalog();
        catalog.setId(catalogId);
        return catalog;
    }

    default Long mapCatalogToCatalogId(Catalog catalog) {
        if (catalog == null) {
            return null;
        }
        return catalog.getId();
    }
}
