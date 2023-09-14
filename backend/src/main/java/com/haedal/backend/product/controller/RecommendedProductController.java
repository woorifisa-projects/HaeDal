package com.haedal.backend.product.controller;

import com.haedal.backend.auth.dto.response.UserLoginResponse;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.repository.ProductRepository;
import com.haedal.backend.product.service.ProductService;
import com.haedal.backend.product.service.RecommendedProductService;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.service.SubscribeService;
import lombok.Getter;
import org.apache.catalina.users.SparseUserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RestController
@RequestMapping("/recommendedProduct")
@CrossOrigin(origins = {"http://localhost:3000", "http://13.209.167.190"})
public class RecommendedProductController {

    private final RecommendedProductService recommendedProductService;
    private final SubscribeService subscribeService;
    private final UserService userService;
    private final ProfileService profileService;
    private final ProductRepository productRepository;



    @Autowired
    public RecommendedProductController(RecommendedProductService recommendedProductService, SubscribeService subscribeService, UserService userService, ProfileService profileService, ProductRepository productRepository) {
        this.recommendedProductService = recommendedProductService;
        this.subscribeService = subscribeService;
        this.userService = userService;
        this.profileService = profileService;
        this.productRepository = productRepository;
    }


    /*
    1. '추천상품탭'에 접속시,
    2. 로그인한 사용에 한해
    3. '자산별'추천상품이 1,2,3 순위별로 나타나도록 조회
     */


    // 자산정보에 따라 상품을 추천해주는 서비스 구현
    //TODO : 인증 기능 구현 후 @RequestParam("userId") Long userId 추가
    @GetMapping
    @ResponseBody
    public List<ProductResponse> getRecommendedByAsset(Authentication authentication, @Param("userId") Long userId ) {
        // 접속한 사용자가 로그인했는지, 안했는지 구분하는 로직

//        return new ResponseEntity<>(new UserLoginResponse(token, userName), HttpStatus.OK);

        String id = userService.getUserId(authentication);

//        String id = authentication.getName();
        User user = profileService.findById(id);
//        Long userId = user.getUserId();


        List<Product> products = recommendedProductService.filterProductsByAsset(userId);

        List<ProductResponse> productResponses = new ArrayList<>();

        productResponses = products.stream()
                .map(ProductResponse::recommendedFrom)
                .collect(Collectors.toList());

        return productResponses;
//
//        List<Long> productsIdsInAsset = recommendedProductService.filterProductsByAsset(userId);
//        System.out.println(productsIdsInAsset);
//
//        // filterByAsset() : ProductRepository에서 선언한 JPQL 쿼리가 담겨있음
//        List<Product> sortedAssets = recommendedProductService.orderByAsset(productsIdsInAsset);
//
//        //상품 중 금리가 높은 것으로 정렬
//        List<Product> highInterests = recommendedProductService.orderByInterestRate(sortedAssets); //service단에서만 정렬
//
//        //status가 true인 것들만 저장
//        List<Product> sellProducts = new ArrayList<>();
//        for(int i =0;i<highInterests.size();i++){
//            if(highInterests.get(i).getProductStatus() == true){
//                sellProducts.add(sortedAssets.get(i));
//            }
//        }
//
//        List<Product> rankingAssets = new ArrayList<>();
//        List<ProductResponse> productResponses = new ArrayList<>();
//
//        // 상위 3개의 상품을 선택
////        int maxRankingCount = Math.max(sortedAssets.size(), 3);
//        if (sellProducts.size()>=3) {
//            for (int i = 0; i < 3 ; i++) {
//                Product product = sellProducts.get(i);
//                rankingAssets.add(product);
//            }
//        } else if(sellProducts.size()<3) {
//            for (int i = 0;  i < sellProducts.size(); i++) {
//                Product product = sellProducts.get(i);
//                rankingAssets.add(product);
//            }
//        }
//        else if(sellProducts.size()==0) {
//            System.out.println("추천상품이 없습니다");
//        }
//
//        productResponses = rankingAssets.stream()
//                    .map(ProductResponse::recommendedFrom)
//                    .collect(Collectors.toList());
//
//
//        return productResponses;
    }



    // 사용자의 이용목적에 따라, 추천상품을 조회
    @GetMapping("/filter/servicePurpose")
    @ResponseBody
    public List<ProductResponse> getRecommendedByServicePurpose(Authentication authentication, @Param("userId") Long userId ) {
//        String id = authentication.getName();
        String id = userService.getUserId(authentication);
        User user = profileService.findById(id);
//        Long userId = user.getUserId();

//        // userId를 기준으로 같은 이용 목적을 가진 상품들의 productId 목록을 가져옴
//        List<Long> productIdsByServicePurpose = recommendedProductService.filterProductsByServicePurpose(userId);
//        System.out.println("찾은 목록 아이디들 : " + productIdsByServicePurpose);
//
//        // 기준 충족 상품을 정렬
//        // filterByServicePurpose() : ProductRepository에서 선언한 JPQL 쿼리가 담겨있음
//        List<Product> sortedServicePurpose = recommendedProductService.orderByServicePurpose(productIdsByServicePurpose);
//        System.out.println("목록들 정렬 : " + sortedServicePurpose);
//
//        //status가 true인 것들만 저장
//        List<Product> sellProducts = new ArrayList<>();
//
//        for(int i =0;i<sortedServicePurpose.size();i++){
//            if(sortedServicePurpose.get(i).getProductStatus()== true){
//                sellProducts.add(sortedServicePurpose.get(i));
//            }
//        }
//
//        // 인기 순위 상위 3개의 상품 저장
//        List<Product> rankingServicePurpose = new ArrayList<>();
//
//        if (sellProducts.size()>=3) {
//            for (int i = 0; i < 3 ; i++) {
//                Product product = sellProducts.get(i);
//                rankingServicePurpose.add(product);
//            }
//        } else if(sellProducts.size()<3) {
//            for (int i = 0;  i < sellProducts.size(); i++) {
//                Product product = sellProducts.get(i);
//                rankingServicePurpose.add(product);
//            }
//        }
//        else if(sellProducts.size()==0) {
//            System.out.println("추천상품이 없습니다");
//        }

        List<Product> products = recommendedProductService.filterProductsByServicePurpose(userId);

        List<ProductResponse> productResponses = new ArrayList<>();

        // 상품을 ProductResponse로 변환하는 작업
        productResponses = products.stream()
                .map(ProductResponse::recommendedFrom)
                .collect(Collectors.toList());

        return productResponses;

    }

    // 사용자의 연령대에 따라, 추천상품을 조회
    @GetMapping("/filter/userAgeGroup")
    public List<ProductResponse> getRecommendedByUserAgeGroup(Authentication authentication, @Param("userId") Long userId) {
//        String id = authentication.getName();
        String id = userService.getUserId(authentication);
        User user = profileService.findById(id);
//        Long userId = user.getUserId();

//        // userId를 기준으로 같은 연령대를 가진 상품들의 productId 목록을 가져옴
//        List<Long> productIdsInUserAgeGroup = recommendedProductService.filterProductsByUserAgeGroup(userId);
//        System.out.println("찾은 목록 아이디들 : " + productIdsInUserAgeGroup);
//
//        // 기준 충족 상품을 정렬
//        List<Product> sortedServicePurpose = recommendedProductService.orderByUserAgeGroup(productIdsInUserAgeGroup);
//        System.out.println("목록들 정렬 : " + sortedServicePurpose);
//
//        //status가 true인 것들만 저장
//        List<Product> sellProducts = new ArrayList<>();
//        for(int i =0;i<sortedServicePurpose.size();i++){
//            if(sortedServicePurpose.get(i).getProductStatus()== true){
//                sellProducts.add(sortedServicePurpose.get(i));
//            }
//        }
//
//        // 인기 순위 상위 3개의 상품 저장
//        List<Product> rankingServicePurpose = new ArrayList<>();
//        if (sellProducts.size()>=3) {
//            for (int i = 0; i < 3 ; i++) {
//                Product product = sellProducts.get(i);
//                rankingServicePurpose.add(product);
//            }
//        } else if(sellProducts.size()<3) {
//            for (int i = 0;  i < sellProducts.size(); i++) {
//                Product product = sellProducts.get(i);
//                rankingServicePurpose.add(product);
//            }
//        }
//        else if(sellProducts.size()==0) {
//            System.out.println("추천상품이 없습니다");
//        }

        List<Product> products = recommendedProductService.filterProductsByUserAgeGroup(userId);

        List<ProductResponse> productResponses = new ArrayList<>();

        // 상품을 ProductResponse로 변환하는 작업
        productResponses = products.stream()
                .map(ProductResponse::recommendedFrom)
                .collect(Collectors.toList());

        return productResponses;
    }

}