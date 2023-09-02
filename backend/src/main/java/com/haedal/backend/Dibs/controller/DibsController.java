package com.haedal.backend.Dibs.controller;

import com.haedal.backend.Dibs.dto.response.DibsResponse;
import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.Dibs.service.DibsService;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.haedal.backend.profile.service.ProfileService;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://13.209.167.190"})
@RequestMapping("/dibs")
public class DibsController {

    private final DibsService dibsService;
    private final ProfileService profileService;

    private ProductService productService;

    public DibsController(DibsService dibsService, ProfileService profileService, ProductService productService) {
        this.dibsService = dibsService;
        this.profileService = profileService;
        this.productService = productService;
    }

    @PostMapping("/{productId}/add")
    public ResponseEntity <String> addDibs(Authentication authentication , @PathVariable Long productId){
        String id = authentication.getName();
        User user = profileService.findById(id);
        Dibs duplicateDibs = dibsService.findDibsByUserIDProductID(user.getUserId(),productId);
        System.out.println(duplicateDibs);
        if(duplicateDibs==null) {
            Product foundProduct = productService.findByProductId(productId);
            Dibs newDibs = new Dibs(user, foundProduct, LocalDate.now());
            Dibs saveDibs = dibsService.save(newDibs);
            System.out.println(saveDibs);
            System.out.println("찜 완료");
            return ResponseEntity.ok("찜이 완료되었습니다.");
        }else{
            System.out.println("찜 중복");
            return ResponseEntity.ok("이미 찜 된 상품입니다.");
        }
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<String> deleteDibs(Authentication authentication, @PathVariable Long productId) {
        String id = authentication.getName();
        User user = profileService.findById(id);

        Dibs deleteDibs = dibsService.findDibsByUserIDProductID(user.getUserId(),productId);
        dibsService.delete(deleteDibs);
        System.out.println("찜 취소");
        return ResponseEntity.ok("찜이 취소되었습니다.");
    }
}
