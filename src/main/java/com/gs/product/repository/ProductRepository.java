package com.gs.product.repository;

import com.gs.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByProductCategory(String productCategory);
}
