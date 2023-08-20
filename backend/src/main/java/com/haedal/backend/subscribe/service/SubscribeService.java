package com.haedal.backend.subscribe.service;


import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.subscribe.repository.SubscribeRepository;

public interface SubscribeService extends CrudService<Subscribe, Long> {
    public Subscribe findById(Long subscribeId);

    public Subscribe save(Subscribe subscribe);
}
