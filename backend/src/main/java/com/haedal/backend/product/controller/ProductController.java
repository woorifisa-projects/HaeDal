package com.haedal.backend.product.controller;

import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.dto.response.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductResponse> getAllproducts() {
        List<Product> products  =  productService.findAll();
        return null;
    }


    @GetMapping("/filter/{tag}")
    public List<ProductResponse> filterProductsByTag(@PathVariable String tag) {
        if (tag.equals("FINANCE")) {
            return productService.findByTag(Tag.FINANCE);
        } else if (tag.equals("THEMA")) {
            return productService.findByTag(Tag.THEMA);
        } else {
            return productService.findAll();
        }
    }



    }

}
