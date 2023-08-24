package com.haedal.backend.subscribe.service;

import com.haedal.backend.product.model.Product;
import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.subscribe.repository.SubscribeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscribeServiceImp implements SubscribeService{
    private SubscribeRepository subscribeRepository;

    public SubscribeServiceImp(SubscribeRepository subscribeRepository) {
        this.subscribeRepository = subscribeRepository;
    }


    @Override
    public Subscribe findById(Long subscribeId) {
        return subscribeRepository.findById(subscribeId).orElse(null);
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
