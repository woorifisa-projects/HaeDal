package com.haedal.backend.product.service;


import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import com.haedal.backend.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public List<Product> findByTag(Tag tag) {

        return productRepository.findByTag(tag);
    }

    @Override
    public Product findById(Long userId) {
        return null;
    }


    //상품 명으로 DB 검색하여 product 리스트 검색
    public List<Product> findByProductNameLike(String productName){
        return productRepository.findByProductNameLike(productName);
    }
}
