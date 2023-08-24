package com.haedal.backend.product.service;

import com.haedal.backend.product.model.Product;

import java.util.List;


public interface RecommendedProductService extends CrudService<Product, Long> {

//
//    // 자산에 따른 추천상품 전체 조회(상위 랭킹 반영 전)
//    List<Product> filterProductsByAsset(Long userId);

    // 이용목적에 따른 추천상품 전체 조회(상위 랭킹 반영 전)
    List<Long> filterProductsByServicePurpose(Long userId);

//    // 연령대에 따른 추천상품 전체 조회(상위 랭킹 반영 전)
//    List<Long> filterProductsByUserAgeGroup(Long userId);
//



//    List<Product> filterByAsset(List<Product> recommendedByAsset);

    List<Product> filterByServicePurpose(List<Long> recommendedProductIds);

//    List<Product> filterByUserAgeGroup(List<Product> recommendedByUserAgeGroup);
}
