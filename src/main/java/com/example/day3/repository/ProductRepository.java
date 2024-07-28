// ProductRepository.java
package com.example.day3.repository;

import com.example.day3.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCatalogId(Integer catalogId);
}
