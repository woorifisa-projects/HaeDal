package com.haedal.backend.product.service;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.repository.ProductRepository;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import com.haedal.backend.profile.service.ProfileService;
import com.haedal.backend.subscribe.repository.SubscribeRepository;
import com.haedal.backend.subscribe.service.SubscribeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Service
public class RecommendedProductServiceImpl implements RecommendedProductService {

    private final ProfileService profileService;
    private final ProductRepository productRepository;
    private final SubscribeService subscribeService;
    private final SubscribeRepository subscribeRepository;

    @Autowired
    public RecommendedProductServiceImpl(ProfileService profileService, ProductRepository productRepository, SubscribeService subscribeService, SubscribeRepository subscribeRepository) {
        this.profileService = profileService;
        this.productRepository = productRepository;
        this.subscribeService = subscribeService;
        this.subscribeRepository = subscribeRepository;
    }

    @Override
    public Product findById(Long userId) {
        return productRepository.findById(userId).orElse(null);
    }


    //Long userId = 3L;

    // 사용자의 자산에 따라, 상품을 추천(상위 랭킹 반영 전) -> 추천된 상품 id 값 뽑아내기
    public List<Long> filterProductsByAsset(Long userId) {
        // TODO: 충분한 공부 후, 로그인 로직 구현하기
        // 로그인한 사용자의 자산정보 가져오기
        User user = profileService.findById(userId);
        Long userAsset = user.getAsset();

        // 조건에 맞는, 추천 상품의 ID값 list 만들기
        List<Product> products = productRepository.findAll();
        List<Long> recommendedProductsIdsByAsset = new ArrayList<>();

        // requiredStartMoney <= User의 Asset인 경우에만 추천상품을 나타나도록
        for (Product product : products) {
            int requiredStartMoney = product.getRequiredStartMoney();

            if (requiredStartMoney <= userAsset) {
                recommendedProductsIdsByAsset.add(product.getProductId());
            } else {
                // 프론트에서 alert창 띄우기
                System.out.println("자산이 충분하지 않습니다.");
            }
        }
        return recommendedProductsIdsByAsset;
    }


    // 사용자의 이용목적에 따라, 상품을 추천(상위 랭킹 반영 전) -> 추천된 상품 id 값 뽑아내기
    public List<Long> filterProductsByServicePurpose(Long userId) {

        // 로그인한 사용자의 이용목적 가져오기
        User user = profileService.findById(userId);
        ServicePurpose userServicePurpose = user.getServicePurpose();
        System.out.println("사용자의 이용목적은? " + userServicePurpose);


        // 조건에 맞는, 추천 상품의 ID값 list 만들기
        List<Product> products = productRepository.findAll();
        List<Long> recommendedProductIdsByServicePurpose = new ArrayList<>();

        // 이용목적별 전체 추천상품 가져오기
        for (Product product : products) {
            ServicePurpose productServicePurpose = product.getServicePurpose();

            // 사용자 이용목적과 상품의 이용목적이 같다면, 추천상품 조회
            if (userServicePurpose == productServicePurpose) {
                recommendedProductIdsByServicePurpose.add(product.getProductId());
            }
        }
        return recommendedProductIdsByServicePurpose;
    }



    // 사용자의 연령대에 따라, 상품을 추천(상위 랭킹 반영 전) -> 추천된 상품 id 값 뽑아내기
    public List<Long> filterProductsByUserAgeGroup(Long userId) {


        // 로그인한 사용자의 연령대 정보 가져오기
        User user = profileService.findById(userId);
//        UserAgeGroup userUserAgeGroup = user.getUserAgeGroup();
//        System.out.println("사용자의 연령대는? " + userUserAgeGroup);

        // 조건에 맞는, 추천 상품의 ID값 list 만들기
        List<Product> products = productRepository.findAll();
        List<Long> recommendedProductsIdsByUserAgeGroup = new ArrayList<>();
        System.out.println("어떤 상품이 등장할까? " + products);

        // 사용자의 연령대를 문자열로 치환하여 가져오기
        String userAgeGroupString = String.valueOf(user.getUserAgeGroup());
        System.out.println("사용자 연령대: " + userAgeGroupString);

        // 연령대별 전체 추천상품 가져오기
        // ONE,
        // TWO,
        // THREE,
        // FOUR,
        // FIVE;
        for (Product product : products) {
            String productUserAgeGroup = String.valueOf(product.getUserAgeGroup());
            System.out.println("상품은 어떤 연령대에게 이 제품을 추천해주고 있어? " + productUserAgeGroup);
//            UserAgeGroup productUserAgeGroup = product.getUserAgeGroup();

            // 사용자 연령대와 상품의 추천연령대가 같다면, 추천상품 조회 뒤 해당id값 수집
//            if (userUserAgeGroup == productUserAgeGroup) {
//                recommendedProductsIdsByUserAgeGroup.add(product.getProductId());


                // 상품의 연령대에 사용자의 연령대가 포함되어 있다면 추천상품에 추가
                if (productUserAgeGroup.contains(userAgeGroupString)) {
                    recommendedProductsIdsByUserAgeGroup.add(product.getProductId());
                    System.out.println("추가가 잘 됐을까? " + recommendedProductsIdsByUserAgeGroup.add(product.getProductId()));

            } else {
            }
        }

        return recommendedProductsIdsByUserAgeGroup;
    }


//------------- 상위 랭킹 반영 쿼리 -------
    public List<Product> orderByAsset(List<Long> productIdsInAsset) {
        return productRepository.orderByAsset(productIdsInAsset);
    }

    public List<Product> orderByServicePurpose(List<Long> productIdsInServicePurpose) {
        return productRepository.orderByServicePurpose(productIdsInServicePurpose);
    }

    public List<Product> orderByUserAgeGroup(List<Long> productIdsInUserAgeGroup) {
        return productRepository.orderByUserAgeGroup(productIdsInUserAgeGroup);
    }


    // TODO: Subscribe 테이블에서 productId에 해당하는 구독자 수를 조회하여 반환

    // numberOfSubscribers : 한 상품당 몇 명의 가입자가 있는지
    // numberOfSubscribers 계산
    // Product의 product_id를 기준으로, Subscribe에 subscribe_id가 몇 개인지 세기








}











