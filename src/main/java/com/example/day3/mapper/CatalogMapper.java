// CatalogMapper.java
package com.example.day3.mapper;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CatalogMapper {
    CatalogMapper INSTANCE = Mappers.getMapper(CatalogMapper.class);

    CatalogDto catalogToCatalogDto(Catalog catalog);
    Catalog catalogDtoToCatalog(CatalogDto catalogDto);
}
