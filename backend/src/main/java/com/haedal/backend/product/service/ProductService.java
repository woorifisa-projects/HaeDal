package com.haedal.backend.product.service;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService extends CrudService<Product, Long> {

    List<Product> findAll();

    List<Product> findByTag(Tag tag);


//    List<Product> findById(User userId);

    Product findByProductId(Long productId);


    //상품 명으로 상품 검색
    List<Product> findByProductNameLike(String productName);


//    List<Product> filterProductsByAsset(Long userId);
//
//
//    List<Product> filterByAsset(List<Product> products);
    Product findById(Long userId);

    String getProductName(Product foundProduct);
}
