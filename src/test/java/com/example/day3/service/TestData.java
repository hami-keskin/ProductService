package com.example.day3.service;

import com.example.day3.entity.Catalog;
import com.example.day3.entity.Product;
import com.example.day3.entity.Stock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestData {

    public static Catalog createCatalog(Integer id, String name, String description, Boolean status, List<Product> products) {
        Catalog catalog = new Catalog();
        catalog.setId(id);
        catalog.setName(name);
        catalog.setDescription(description);
        catalog.setStatus(status);
        catalog.setProducts(products);
        return catalog;
    }

    public static Product createProduct(Integer id, String name, Double price, String description, Boolean status, Catalog catalog, Stock stock) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setStatus(status);
        product.setCatalog(catalog);
        product.setStock(stock);
        return product;
    }

    public static Stock createStock(Integer id, Product product, Integer quantity) {
        Stock stock = new Stock();
        stock.setId(id);
        stock.setProduct(product);
        stock.setQuantity(quantity);
        return stock;
    }

    public static List<Catalog> getSampleCatalogs() {
        Catalog catalog1 = createCatalog(1, "Electronics", "Various electronic products", true, null);
        Catalog catalog2 = createCatalog(2, "Books", "Different genres of books", true, null);
        return Arrays.asList(catalog1, catalog2);
    }

    public static List<Product> getSampleProducts(Catalog catalog) {
        Product product1 = createProduct(1, "Laptop", 1200.00, "High-end gaming laptop", true, catalog, null);
        Product product2 = createProduct(2, "Smartphone", 800.00, "Latest model smartphone", true, catalog, null);
        return Arrays.asList(product1, product2);
    }

    public static List<Stock> getSampleStocks(Product product) {
        Stock stock1 = createStock(1, product, 50);
        Stock stock2 = createStock(2, product, 30);
        return Arrays.asList(stock1, stock2);
    }
}
