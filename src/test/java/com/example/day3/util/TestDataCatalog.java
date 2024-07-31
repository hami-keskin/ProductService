package com.example.day3.util;

import com.example.day3.dto.CatalogDto;
import com.example.day3.entity.Catalog;

public class TestDataCatalog {

    public static Catalog createCatalog() {
        Catalog catalog = new Catalog();
        catalog.setId(1);
        catalog.setName("Electronics");
        catalog.setDescription("Electronic items");
        catalog.setStatus(true);
        return catalog;
    }

    public static CatalogDto createCatalogDto() {
        CatalogDto catalogDto = new CatalogDto();
        catalogDto.setId(1);
        catalogDto.setName("Electronics");
        catalogDto.setDescription("Electronic items");
        catalogDto.setStatus(true);
        return catalogDto;
    }
}
