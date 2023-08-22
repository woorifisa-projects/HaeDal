package com.haedal.backend.subscribe.controller;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.repository.UserRepository;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.dto.response.SubscribeResponse;
import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.subscribe.service.SubscribeService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/subscribe")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SubscribeController {
    private SubscribeService subscribeService;
    private ProfileService profileService;
    private ProductService productService;

    public SubscribeController(SubscribeService subscribeService, ProfileService profileService, ProductService productService) {
        this.subscribeService = subscribeService;
        this.profileService = profileService;
        this.productService = productService;
    }

    //상품 신청 버튼 클릭시 상품 종류 인식, 해당 url로 전달 (예, 적금에 따라 다르게 이동해주기 위함)
    // TODO : url 변경의 경우 클라이언트에서 처리
//    @PostMapping("/{productId}")
//    public RedirectView subscribeProduct(@PathVariable Long productId) {
//
//        Product foundProduct = productService.findByProductId(productId);
//
//        if (foundProduct==null) {
//            // 상품을 찾을 수 없을 경우, 예를 들어 에러 페이지로 리다이렉트
//            return new RedirectView("/error");
//        }
//
//        //TODO : 인증 관련 공부 이후, userId를 인증으로 수정, 처리합니다.
//        User user = profileService.findById(1L);
//        String userAccount = user.getAccountNumber();
//        //리다이렉트 된 값들 프론트에 전달
//        if ("I".equals(foundProduct.getProductType())) {
//            // 적금 신청 페이지로 리다이렉트
//            model.addAttribute("product", foundProduct);
//            return new RedirectView("/" + productId + "/I");
//            // foundProduct 를 반환하며 "/{productId}/D" 로 이동하도록 동작(redirect)
//        } else if ("D".equals(foundProduct.getProductType())) {
//            // 예금 신청 페이지로 리다이렉트
//            model.addAttribute("product", foundProduct);
//            return new RedirectView("/" + productId + "/D");
//        } else {
//            // 유효하지 않은 상품 타입인 경우, 예를 들어 에러 페이지로 리다이렉트
//            return new RedirectView("/error");
//        }
//    }

    @GetMapping("/{productId}")
    public ProductResponse showSubcribeProduct(@PathVariable Long productId){
        Product foundProduct = productService.findByProductId(productId);
        System.out.println(productId + "정보 조회");
        ProductResponse productResponse = ProductResponse.from(foundProduct);
        return productResponse;
    }


    //상품 신청 페이지 '신청'버튼 클릭
    @PostMapping("/{productId}/*")
    public ResponseEntity<String> subscribeproduct(@PathVariable Long productId,  @RequestBody Map<String, String> requestData){
        //TODO : 인증 관련 공부 이후, userId를 인증으로 수정, 처리합니다.
        User user = profileService.findById(1L);

        Product foundProduct = productService.findByProductId(productId);

        System.out.println(productId + " 접근 성공했다.");

        long authNumber = Long.parseLong(requestData.get("authenticationNumber"));
        long startMoney = Long.parseLong(requestData.get("startMoney"));
        
        //user가 입력한 인증번호가 DB와 같고, user_start_money(입력값)이 userAsset(본인 소유 자산)이상, productAsset(최대 금액)이하일 때
        if(authNumber == user.getAuthNumber() && user.getAsset() >= startMoney && startMoney<=foundProduct.getMaxProductMoney()){
            Subscribe subscribe = new Subscribe(user, foundProduct , startMoney, startMoney, LocalDate.now());
            Subscribe saveSubscribe = subscribeService.save(subscribe);
            System.out.println(saveSubscribe);
            return ResponseEntity.ok("신청이 완료되었습니다.");
        } else{
            return ResponseEntity.badRequest().body("신청 정보가 올바르지 않습니다.");
        }
    }

//    //예금 신청 페이지 '신청'버튼 클릭
//    @PostMapping("/{productId}/D")
//    public void subscribeDProduct(@PathVariable Long productId,  @RequestBody Map<String, String> requestData){
//        //user의 인증번호와 같고,
//        //아닐 경우 다시 신청페이지로 이동
//    }

}
