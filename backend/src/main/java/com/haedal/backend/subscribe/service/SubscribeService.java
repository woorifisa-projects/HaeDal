package com.haedal.backend.subscribe.service;


import com.haedal.backend.subscribe.dto.response.PortfolioResponse;
import com.haedal.backend.subscribe.model.Subscribe;

import java.util.List;

public interface SubscribeService extends CrudService<Subscribe, Long> {
//    List<Product> filterProductsByAsset(Long userId);
//
//    List<Subscribe> findByUserId(Long userId);
//    // 다른 필터링 로직들에 대한 메서드들도 추가할 수 있음
//
//    List<Product> findByProductId(Long productId);
//
//    // 추가
//    // productId를 기준으로 Subscribe의 StartMoney값 찾기
//    List<Product> findByProductId(Product productId);


    List<PortfolioResponse> findSubscriptionsAndProductsByUser(Long userId);

    public Subscribe save(Subscribe subscribe);
}
