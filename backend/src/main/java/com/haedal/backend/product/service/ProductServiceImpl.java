package com.haedal.backend.product.service;


import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {



    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public List<Product> findByTag(Tag tag) {
        return null;
    }

    @Override
    public Product findById(Long userId) {
        return null;
    }






}
