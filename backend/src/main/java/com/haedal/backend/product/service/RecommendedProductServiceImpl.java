package com.haedal.backend.product.service;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.product.dto.response.ProductResponse;
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
import org.springframework.web.bind.annotation.GetMapping;

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

    // 사용자의 자산에 따라, 상품을 추천(상위 랭킹 반영 전)
    public List<Product> filterProductsByAsset(Long userId) {
        // TODO: 충분한 공부 후, 로그인 로직 구현하기

        // 로그인한 사용자의 자산정보 가져오기
        User user = profileService.findById(userId);
        Long userAsset = user.getAsset();

        // 상품 전체정보 가져오기
        List<Product> products = productRepository.findAll();
        List<Product> recommendedProducts = new ArrayList<>();

        // requiredStartMoney <= User의 Asset인 경우에만 추천상품을 나타나도록
        for (Product product : products) {
            int requiredStartMoney = product.getRequiredStartMoney();

            if (requiredStartMoney <= userAsset) {
                recommendedProducts.add(product);
            } else {
                // 프론트에서 alert창 띄우기
                System.out.println("자산이 충분하지 않습니다.");
            }
        }
        return recommendedProducts;
    }




    // 사용자의 이용목적에 따라, 상품을 추천(상위 랭킹 반영 전)
    public List<Product> filterProductsByServicePurpose(Long userId) {

        // 로그인한 사용자의 이용목적 가져오기
        User user = profileService.findById(userId);
        ServicePurpose userServicePurpose = user.getServicePurpose();

        // 상품 전체정보 가져오기
        List<Product> products = productRepository.findAll();
        List<Product> recommendedProducts = new ArrayList<>();


        // 이용목적별 전체 추천상품 가져오기
        //   MOKDON,
        //    FORCAR,
        //    FORHOUSE,
        //    OTHERS
        for (Product product : products) {
            ServicePurpose productServicePurpose = product.getServicePurpose();

            System.out.println(userServicePurpose);
            System.out.println(productServicePurpose);

            // 사용자 이용목적과 상품의 이용목적이 같다면, 추천상품 조회
            if (userServicePurpose == productServicePurpose) {
                recommendedProducts.add(product);
            }
        }

        for(Product product : recommendedProducts){
            System.out.println("저장된값 : " + product);
        }
        return recommendedProducts;
    }



    // 사용자의 연령대에 따라, 상품을 추천(상위 랭킹 반영 전)
    public List<Product> filterProductsByUserAgeGroup(Long userId) {


        // 로그인한 사용자의 연령대 정보 가져오기
        User user = profileService.findById(userId);
        UserAgeGroup userServiceUserAgeGroup = user.getUserAgeGroup();

        // 상품 전체정보 가져오기
        List<Product> products = productRepository.findAll();
        List<Product> recommendedProducts = new ArrayList<>();


        // 연령대별 전체 추천상품 가져오기
        // ONE,
        // TWO,
        // THREE,
        // FOUR,
        // FIVE;
        for (Product product : products) {
            UserAgeGroup productUserAgeGroup = product.getUserAgeGroup();

            // 사용자 연령대와 상품의 추천연령대가 같다면, 추천상품 조회
            if (userServiceUserAgeGroup == productUserAgeGroup) {
                recommendedProducts.add(product);
            } else {
                return null;
            }
        }

        return recommendedProducts;
    }



//    @Override
//    public List<Product> filterByAsset(List<Product> products) {
//
////        return productRepository.filterByAsset(products);
//        return null;
//    }

//------------- 상위 랭킹 반영 쿼리 -------
    public List<Product> filterByAsset(List<Product> recommendedByAsset) {
        return productRepository.filterByAsset(recommendedByAsset);
    }

    @Override
    public List<Product> filterByServicePurpose(List<Product> recommendedByServicePurpose) {
        return productRepository.filterByServicePurpose(recommendedByServicePurpose);
    }

    @Override
    public List<Product> filterByUserAgeGroup(List<Product> recommendedByUserAgeGroup) {
        return productRepository.filterByUserAgeGroup(recommendedByUserAgeGroup);
    }


    // TODO: Subscribe 테이블에서 productId에 해당하는 구독자 수를 조회하여 반환

    // numberOfSubscribers : 한 상품당 몇 명의 가입자가 있는지
    // numberOfSubscribers 계산
    // Product의 product_id를 기준으로, Subscribe에 subscribe_id가 몇 개인지 세기








}











