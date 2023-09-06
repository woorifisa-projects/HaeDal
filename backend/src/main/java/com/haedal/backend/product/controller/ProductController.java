package com.haedal.backend.product.controller;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.model.LogType;
import com.haedal.backend.log.service.LogService;
import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://13.209.167.190"})
public class ProductController {

    private final ProductService productService;
    private final ProfileService profileService;
    private final LogService logService;

    @Autowired
    public ProductController(ProductService productService, ProfileService profileService, LogService logService) {
        this.productService = productService;
        this.profileService = profileService;
        this.logService = logService;
    }

    //상품 전체 리스트 조회
    @GetMapping
    public List<ProductResponse> getAllproducts( ) {
        List<Product> products  =  productService.findAll();
        List<Product> sellProducts = new ArrayList<>();
        for(int i =0;i<products.size();i++){
            if(products.get(i).getProductStatus()== true){
                sellProducts.add(products.get(i));
            }
        }
        List<ProductResponse> productResponse = sellProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return productResponse;
    }

    //상품 태그에 따라 필터링 
    @GetMapping("/filter/{tag}")
    public List<ProductResponse> filterProductsByTag(@PathVariable String tag) {
        if (tag.equals("FINANCE")) {
            List<Product> products = productService.findByTag(Tag.FINANCE);

            List<Product> sellProducts = new ArrayList<>();
            for(int i =0;i<products.size();i++){
                if(products.get(i).getProductStatus()== true){
                    sellProducts.add(products.get(i));
                }
            }
            List<ProductResponse> productResponse = sellProducts.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList());
            return productResponse;

        } else if (tag.equals("THEMA")) {
            List<Product> products =  productService.findByTag(Tag.THEMA);
            List<Product> sellProducts = new ArrayList<>();
            for(int i =0;i<products.size();i++){
                if(products.get(i).getProductStatus()== true){
                    sellProducts.add(products.get(i));
                }
            }
            List<ProductResponse> productResponse = sellProducts.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList());
            return productResponse;
        } else {
            List<Product> products =  productService.findAll();
            List<Product> sellProducts = new ArrayList<>();
            for(int i =0;i<products.size();i++){
                if(products.get(i).getProductStatus()== true){
                    sellProducts.add(products.get(i));
                }
            }
            List<ProductResponse> productResponse = sellProducts.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList());
            return productResponse;
        }
    }

    // 로그인 하지 않은 유저가 키워드 검색
    @PostMapping("/{search}")
    public List<ProductResponse> searchProduct(@PathVariable String search){
        List<Product> products = new ArrayList<>();
        List<Product> sellProducts = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            products = productService.findByProductNameLike("%" + search + "%");

            for(int i =0;i<products.size();i++){
                if(products.get(i).getProductStatus()== true){
                    sellProducts.add(products.get(i));
                }
            }
        } else {
            System.out.println("검색어가 없습니다.");
            System.out.println("테스트용");
        }

        List<ProductResponse> productResponse = sellProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return productResponse;
    }

    //로그인한 유저가 키워드 검색
    @PostMapping("/{search}/login")
    public List<ProductResponse> searchProductLogin(Authentication authentication, @PathVariable String search){

        String id = authentication.getName();
        User user = profileService.findById(id);
        LogType logType = LogType.valueOf("SEARCH");
        String logSearch = search+" 검색";
        System.out.println(logSearch);
        LocalDateTime logDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9);

        Log savelog = logService.save( new Log(user, logType, logDateTime,logSearch));

        List<Product> products = new ArrayList<>();
        List<Product> sellProducts = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            products = productService.findByProductNameLike("%" + search + "%");

            for(int i =0;i<products.size();i++){
                if(products.get(i).getProductStatus()== true){
                    sellProducts.add(products.get(i));
                }
            }
        } else {
            System.out.println("검색어가 없습니다.");
            System.out.println("테스트용");
        }

        List<ProductResponse> productResponse = sellProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return productResponse;
    }
}

