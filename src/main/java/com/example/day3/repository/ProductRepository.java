package com.example.day3.repository;

import com.example.day3.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCatalogId(Integer catalogId);
}
