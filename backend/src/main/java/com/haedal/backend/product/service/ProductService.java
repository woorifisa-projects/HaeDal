package com.haedal.backend.product.service;

import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService extends CrudService<Product, Long> {

    List<Product> findAll();

    List<Product> findByTag(Tag tag);

    //상품 명으로 상품 검색
    List<Product> findByProductNameLike(String productName);

    //상품 id로 상품 검색
    Product findByProductId(Long productId);
}
