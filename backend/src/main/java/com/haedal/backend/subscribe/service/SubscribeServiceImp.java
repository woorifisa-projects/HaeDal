package com.haedal.backend.subscribe.service;

import com.haedal.backend.subscribe.dto.response.PortfolioResponse;
import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.subscribe.repository.SubscribeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class SubscribeServiceImp implements SubscribeService{
    private SubscribeRepository subscribeRepository;

    public SubscribeServiceImp(SubscribeRepository subscribeRepository) {
        this.subscribeRepository = subscribeRepository;
    }

    @Override
    public List<PortfolioResponse> findSubscriptionsAndProductsByUser(Long userId) {
        List<Subscribe> result = subscribeRepository.findSubscriptionsAndProductsByUser(userId);
        return result.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    //    @Override
//    public List<Product> filterProductsByAsset(Long userId) {
//        return null;
//    }

//    @Override
//    public List<Subscribe> findByUserId(Long userId) {
//        return null;
//    }
//
//    @Override
//    public List<Product> findByProductId(Long productId) {
//        return null;
//    }
//
//    @Override
//    public List<Product> findByProductId(Product productId) {
//        return null;
//    }


    public Subscribe save(Subscribe subscribe){
        return subscribeRepository.save(subscribe);
    }

}
