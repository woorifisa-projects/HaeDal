package com.haedal.backend.product.controller;

import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.product.service.ProductServiceImpl;
import com.haedal.backend.profile.dto.response.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://13.209.167.190"})
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //상품 전체 리스트 조회
    @GetMapping
    public List<ProductResponse> getAllproducts( ) {
        List<Product> products  =  productService.findAll();
        List<ProductResponse> productResponse = products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return productResponse;
    }

    //상품 태그에 따라 필터링 
    @GetMapping("/filter/{tag}")
    public List<ProductResponse> filterProductsByTag(@PathVariable String tag) {
        if (tag.equals("FINANCE")) {
            List<Product> products = productService.findByTag(Tag.FINANCE);
            List<ProductResponse> productResponse = products.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList());
            return productResponse;

        } else if (tag.equals("THEMA")) {
            List<Product> products =  productService.findByTag(Tag.THEMA);
            List<ProductResponse> productResponse = products.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList());
            return productResponse;
        } else {
            List<Product> products =  productService.findAll();
            List<ProductResponse> productResponse = products.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList());
            return productResponse;
        }
    }
    // 키워드 검색
    @PostMapping("/{search}")
    public List<ProductResponse> searchProduct(@PathVariable String search){
        List<Product> products = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            products = productService.findByProductNameLike("%" + search + "%");
        } else {
            System.out.println("검색어가 없습니다.");
            System.out.println("테스트용");
        }

        List<ProductResponse> productResponse = products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return productResponse;
    }

}

