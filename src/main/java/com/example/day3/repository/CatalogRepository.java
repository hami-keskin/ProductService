// CatalogRepository.java
package com.example.day3.repository;

import com.example.day3.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "catalogs", path = "catalogs")
public interface CatalogRepository extends JpaRepository<Catalog, Integer> {
}
