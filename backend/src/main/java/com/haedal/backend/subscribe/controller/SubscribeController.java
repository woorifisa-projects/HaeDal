package com.haedal.backend.subscribe.controller;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.repository.UserRepository;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.dto.response.SubscribeResponse;
import com.haedal.backend.subscribe.service.SubscribeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/subscribe")
@RestController
public class SubscribeController {
    private SubscribeService subscribeService;
    private ProfileService profileService;
    private ProductService productService;

    public SubscribeController(SubscribeService subscribeService, ProfileService profileService, ProductService productService) {
        this.subscribeService = subscribeService;
        this.profileService = profileService;
        this.productService = productService;
    }

    //상품 신청 페이지로 이동 (예, 적금에 따라 다르게 이동해주기 위함)
    @PostMapping("/{productId}")
    public ResponseEntity<String> subscribeProduct(@RequestBody Product product,
                                                   @PathVariable Long productId
                                                   ) {
        Product foundProduct = productService.findById(productId);

        if (foundProduct==null) {
            return ResponseEntity.badRequest().body("상품을 찾을 수 없습니다.");
        }

        if ("I".equals(foundProduct.getProductType())) {
            // foudProduct 를 반환하며 "/{productId}/I" 로 이동하도록 동작
            return ResponseEntity.ok("적금 신청 페이지로 이동합니다.");
            // foudProduct 를 반환하며 "/{productId}/D" 로 이동하도록 동작
        } else if ("D".equals(foundProduct.getProductId())) {
            return ResponseEntity.ok("예금 신청 페이지로 이동합니다.");
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 상품 타입입니다.");
        }
    }

    //예금 신청 페이지
    @PostMapping("/{productId}/I")
    public void subscribeIproduct(){
        //user의 인증번호와 같을 경우 save, 성공 페이지로 이동
        //아닐 경우 다시 신청페이지로 이동
    }

    //적금 신청 페이지
    @PostMapping("/{productId}/D")
    public void subscribeDProduct(){
        //user의 인증번호와 같을 경우 save, 성공 페이지로 이동
        //아닐 경우 다시 신청페이지로 이동
    }

}
