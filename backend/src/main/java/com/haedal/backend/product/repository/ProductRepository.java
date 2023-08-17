package com.haedal.backend.product.repository;

import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByTag(String tag);
}
