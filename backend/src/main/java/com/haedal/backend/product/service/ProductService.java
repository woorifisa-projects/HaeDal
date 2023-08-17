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





}
