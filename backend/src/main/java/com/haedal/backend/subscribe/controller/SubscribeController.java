package com.haedal.backend.subscribe.controller;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.model.LogType;
import com.haedal.backend.log.service.LogService;
import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.dto.response.PortfolioResponse;
import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.subscribe.service.SubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/subscribe")
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://13.209.167.190"})
public class SubscribeController {
    private SubscribeService subscribeService;
    private ProfileService profileService;
    private ProductService productService;
    private LogService logService;

    public SubscribeController(SubscribeService subscribeService, ProfileService profileService, ProductService productService,LogService logService) {
        this.subscribeService = subscribeService;
        this.profileService = profileService;
        this.productService = productService;
        this.logService = logService;
    }


    //신청 상품 정보 표출
    @GetMapping("/{productId}")
    public ProductResponse showSubscribeProduct( @PathVariable(name = "productId") Long productId){
        Product foundProduct = productService.findByProductId(productId);
        System.out.println(productId + "정보 조회");

        ProductResponse productResponse = ProductResponse.from(foundProduct);

        System.out.println(productResponse);
        return productResponse;
    }

    //유저와 상품 페이지 정보 표출
    @GetMapping("/{productId}/semi")
    public ProductResponse showSubscribeProductAndUser(Authentication authentication, @PathVariable(name = "productId") Long productId) {
        String id = authentication.getName();
        User user = profileService.findById(id);

        Product foundProduct = productService.findByProductId(productId);
        System.out.println(productId + "신청 페이지");

        ProductResponse productResponse = ProductResponse.mapProductToResponse(foundProduct, user);

        System.out.println(productResponse);
        return productResponse;
    }

    //상품 신청 페이지 '신청'버튼 클릭
    @PostMapping("/{productId}/final")
    public ResponseEntity<String> subscribeproduct(Authentication authentication , @PathVariable Long productId, @RequestBody Map<String, String> requestData){
        //인증에 맞춰 정보 수정
        String id = authentication.getName();
        User user = profileService.findById(id);
        Long userId = user.getUserId();

        System.out.println(requestData);

        Product foundProduct = productService.findByProductId(productId);
        //해당 고객이 해당 상품에 대해 구독한 기록이 있는지 여부 확인
        List<Subscribe> alreadySubscribe = subscribeService.findSubscriptionsByProductsAndUser(userId,productId);

        System.out.println(productId + " 접근 성공했다.");
        long authNumber = Long.parseLong(requestData.get("authenticationNumber"));
        long startMoney = Long.parseLong(requestData.get("startMoney"));

        //user가 입력한 인증번호가 DB와 같고, 시작금액이 상품 최소, 최대 금액 범주 안이자 userAsset보다 작을 경우
        // + user가 해당 상품을 구독한 기록이 없을 때 구독
        if(authNumber == user.getAuthNumber() && user.getAsset() >= startMoney && startMoney<=foundProduct.getMaxProductMoney()
        && alreadySubscribe.size()==0 && startMoney>=foundProduct.getRequiredStartMoney()){
            Subscribe subscribe = new Subscribe(user, foundProduct , startMoney, startMoney, LocalDate.now(), LocalDate.now()); // 마지막 LocalDate.now()가 현재날짜생성부분임
            Subscribe saveSubscribe = subscribeService.save(subscribe);
            System.out.println(saveSubscribe);

            //유저 자산 업데이트
            user.updateAsset(user.getAsset()-startMoney);
            System.out.println(user.getAsset());

            //로그 저장
            LogType logType = LogType.valueOf("SUBSCRIBE");

            LocalDateTime logDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(9);
            String logDib = "id"+" "+productId+" "+productService.findByProductId(productId).getProductName()+" 구독";

            Log savelog = logService.save( new Log(user, logType, logDateTime,logDib));

            return ResponseEntity.ok("신청이 완료되었습니다.");
        }else if(alreadySubscribe.size()!=0) {
            return ResponseEntity.badRequest().body("이미 해당 상품을 구독하고 있습니다.");
        }
        else{
            return ResponseEntity.badRequest().body("입력하신 정보가 올바르지 않습니다.");
        }
    }

    @GetMapping("/portfolio")
    public  List<PortfolioResponse>  getUserSubscribeListOrderByMoney(Authentication authentication){
        String id = authentication.getName();
        List<PortfolioResponse> subscribeList = subscribeService.findSubscriptionsAndProductsByUserSortedByMoney(id);
        System.out.println(subscribeList);

        return subscribeList;
    }

    @GetMapping("/portfolio/days") // 구독일자별 정렬
    public  List<PortfolioResponse>  getUserSubscribeListOrderByDay(Authentication authentication){
        String id = authentication.getName();
        List<PortfolioResponse> subscribeList = subscribeService.findSubscriptionsAndProductsByUserSortedByDays(id);
        System.out.println(subscribeList);

        return subscribeList;
    }

    @GetMapping("/portfolio/dibs") // 좋아요 누른것
    public List<PortfolioResponse> getUserDibsList(Authentication authentication){
        String id = authentication.getName();
        List<PortfolioResponse> dibsList = subscribeService.findDibsAndProductsByUser(id);
        log.info("실행완료"+id);
        System.out.println(dibsList);

        return dibsList;
    }

    @DeleteMapping("/{productId}/cancle")
    public ResponseEntity<String> cancelUserSubscribeProduct(Authentication authentication, @PathVariable Long productId){
        String userId = authentication.getName();
        Subscribe subscribe = subscribeService.findByUserIdAndProductId(userId,productId);
        Long refund = subscribeService.deleteBySubscribe(subscribe);
        return ResponseEntity.ok( refund+"원이 환급되었습니다.");
    }

}
