package com.example.day3.repository;

import com.example.day3.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "stocks", path = "stocks")
public interface StockRepository extends JpaRepository<Stock, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Stock> findByProductId(Integer productId);
}
