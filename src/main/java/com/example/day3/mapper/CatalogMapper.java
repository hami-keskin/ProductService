package com.example.day3.mapper;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatalogMapper {
    CatalogDto toDto(Catalog catalog);
    Catalog toEntity(CatalogDto catalogDto);
}
