package com.haedal.backend.product.controller;

import com.haedal.backend.auth.service.UserService;
import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.repository.ProductRepository;
import com.haedal.backend.product.service.RecommendedProductService;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.service.SubscribeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RestController
@RequestMapping("/recommendedProduct")
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
    public List<ProductResponse> getRecommendedByAsset() {

        // TODO: 접속한 사용자가 로그인했는지, 안했는지 구분하는 로직


        // userId를 기준으로, 기준을 충족하는 상품들만 저장
        Long userId = 1L;
        List<Product> recommendedByAsset = recommendedProductService.filterProductsByAsset(userId);
        System.out.println(recommendedByAsset);


        // 상위의 3개만 선택해서 1,2,3위 순위 보여주기
        // filterByAsset() : ProductRepository에서 선언한 JPQL 쿼리가 담겨있음
        List<Product> sortedAssets = recommendedProductService.filterByAsset(recommendedByAsset);
        List<Product> rankingAssets = new ArrayList<>();
        List<ProductResponse> productResponses = new ArrayList<>();

        // 상위 3개의 상품을 선택
//        int maxRankingCount = Math.max(sortedAssets.size(), 3);
        for (int i = 0; i < 3; i++) {
            if (sortedAssets.isEmpty() == false) {
                Product product = sortedAssets.get(i);
                rankingAssets.add(product);

                // 예외처리1 : 추천상품이 없는 경우
            } else {
                System.out.println("추천상품이 없습니다");
            }


            productResponses = rankingAssets.stream()
                    .map(ProductResponse::recommendedFrom)
                    .collect(Collectors.toList());
        }

        return productResponses;
    }


    // 사용자의 이용목적에 따라, 추천상품을 조회
    @GetMapping("/filter/servicePurpose")
    public List<ProductResponse> getRecommendedByServicePurpose() {

        // userId를 기준으로, 기준을 충족하는 상품들만 저장
        Long userId = 3L;
        List<Product> recommendedByServicePurpose = recommendedProductService.filterProductsByServicePurpose(userId);
        System.out.println("넘어옴"+recommendedProductService.filterProductsByServicePurpose(userId));
        System.out.println(recommendedByServicePurpose.toString());


        // 상위의 3개만 선택해서 1,2,3위 순위 보여주기
        // filterByServicePurpose() : ProductRepository에서 선언한 JPQL 쿼리가 담겨있음
        List<Product> sortedServicePurpose = recommendedProductService.filterByServicePurpose(recommendedByServicePurpose);

        List<Product> rankingServicePurpose = new ArrayList<>();

        List<ProductResponse> productResponses = new ArrayList<>();

        // 상위 3개의 상품을 선택
//        int maxRankingCount = Math.max(sortedAssets.size(), 3);
        for (int i = 0; i < 3; i++) {
            if (sortedServicePurpose.isEmpty() == false) {
                Product product = sortedServicePurpose.get(i);
                rankingServicePurpose.add(product);
                // 예외처리1 : 추천상품이 없는 경우
            } else {
                System.out.println("추천상품이 없습니다");
            }


            productResponses = rankingServicePurpose.stream()
                    .map(ProductResponse::recommendedFrom)
                    .collect(Collectors.toList());
        }

        return productResponses;
    }


    // 사용자의 연령대에 따라, 추천상품을 조회
    @GetMapping("/filter/userAgeGroup")
    public List<ProductResponse> getRecommendedByUserAgeGroup() {

        // userId를 기준으로, 기준을 충족하는 상품들만 저장
        Long userId=3L;
        List<Product> recommendedByUserAgeGroup = recommendedProductService.filterProductsByUserAgeGroup(userId);

        // 상위의 3개만 선택해서 1,2,3위 순위 보여주기
        // filterByUserAgeGroup() : ProductRepository에서 선언한 JPQL 쿼리가 담겨있음
        List<Product> sortedUserAgeGroup = productRepository.filterByUserAgeGroup(recommendedByUserAgeGroup);
        List<Product> rankingUserAgeGroup = new ArrayList<>();
        List<ProductResponse> productResponses = new ArrayList<>();


        // 상위 3개의 상품을 선택
//        int maxRankingCount = Math.max(sortedAssets.size(), 3);
        for (int i = 0; i < 3; i++) {
            if (sortedUserAgeGroup.isEmpty() == false) {
                Product product = sortedUserAgeGroup.get(i);
                rankingUserAgeGroup.add(product);

                // 예외처리1 : 추천상품이 없는 경우
            } else {
                System.out.println("추천상품이 없습니다");
            }


            productResponses = rankingUserAgeGroup.stream()
                    .map(ProductResponse::recommendedFrom)
                    .collect(Collectors.toList());


        }
        return productResponses;
    }
}