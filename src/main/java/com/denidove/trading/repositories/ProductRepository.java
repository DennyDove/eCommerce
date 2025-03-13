package com.denidove.trading.repositories;

import com.denidove.trading.entities.Product;
import com.denidove.trading.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
