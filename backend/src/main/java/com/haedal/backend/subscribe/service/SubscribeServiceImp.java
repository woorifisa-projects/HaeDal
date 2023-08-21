package com.haedal.backend.subscribe.service;

import com.haedal.backend.subscribe.controller.SubscribeController;
import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.subscribe.repository.SubscribeRepository;
import org.springframework.stereotype.Service;

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

    public Subscribe save(Subscribe subscribe){
        return subscribeRepository.save(subscribe);
    }
}
