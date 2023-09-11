package com.haedal.backend.Dibs.controller;

import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.Dibs.service.DibsService;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.model.LogType;
import com.haedal.backend.log.service.LogService;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.haedal.backend.profile.service.ProfileService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://13.209.167.190"})
@RequestMapping("/dibs")
public class DibsController {

    private final DibsService dibsService;
    private final ProfileService profileService;
    private final LogService logService;
    private final UserService userService;

    private ProductService productService;

    public DibsController(DibsService dibsService, ProfileService profileService, LogService logService, UserService userService, ProductService productService) {
        this.dibsService = dibsService;
        this.profileService = profileService;
        this.logService = logService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/{productId}/add")
    public ResponseEntity <String> addDibs(Authentication authentication , @PathVariable Long productId){
        String id = authentication.getName();
        User user = profileService.findById(id);
        Long userId = profileService.getUserId(user);

        List<Dibs> duplicateDibs = dibsService.findDibsByUserIDProductID(userId,productId);
        System.out.println(duplicateDibs);
        if(duplicateDibs == null || duplicateDibs.isEmpty()) {
            Product foundProduct = productService.findByProductId(productId);

            LogType logType = LogType.valueOf("DIB");
            LocalDateTime logDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9) ;
            String logDib = "id"+" "+productId+" "+productService.getProductName(foundProduct)+" 찜";

            Log savelog = logService.save( new Log(user, logType, logDateTime,logDib));
            System.out.println(logDateTime);

            Dibs newDibs = new Dibs(user, foundProduct, LocalDate.now());
            Dibs saveDibs = dibsService.save(newDibs);

            System.out.println("찜 완료");
            return ResponseEntity.ok("찜이 완료되었습니다.");
        }else{
            System.out.println("찜 중복");
            return ResponseEntity.ok("이미 찜 된 상품입니다.");
        }
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<String> deleteDibs(Authentication authentication, @PathVariable Long productId) {
        String id = userService.getUserId(authentication);
        User user = profileService.findById(id);
        Long userId = profileService.getUserId(user);
        Product foundProduct = productService.findByProductId(productId);


        List<Dibs> deleteDibs = dibsService.findDibsByUserIDProductID(userId,productId);
        Dibs deleteDib=dibsService.getFirstDibs(deleteDibs);
        dibsService.delete(deleteDib);

        LogType logType = LogType.valueOf("CANCELDIB");
        LocalDateTime logDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9);
        String logDib = "id"+" "+productId+" "+productService.getProductName(foundProduct)+" 찜 취소";

        Log savelog = logService.save( new Log(user, logType, logDateTime,logDib));

        System.out.println("찜 취소");
        return ResponseEntity.ok("찜이 취소되었습니다.");
    }

    //해당상품의 찜 여부를 확인
    @GetMapping("/{productId}/check")
    public ResponseEntity<Boolean> checkDibs(Authentication authentication, @PathVariable Long productId) {
        String id = userService.getUserId(authentication);
        User user = profileService.findById(id);
        Long userId = profileService.getUserId(user);


        // 해당 사용자의 productId에 대한 찜 여부를 확인합니다.
        List<Dibs> dibs = dibsService.findDibsByUserIDProductID(userId, productId);

        // 찜 여부를 클라이언트에게 전달합니다.
        boolean isDibs = (!dibs.isEmpty());
        return ResponseEntity.ok(isDibs);
    }
}
