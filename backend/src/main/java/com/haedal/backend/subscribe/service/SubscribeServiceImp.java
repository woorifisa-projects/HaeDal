package com.haedal.backend.subscribe.service;

import com.haedal.backend.subscribe.dto.response.PortfolioResponse;
import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.subscribe.repository.SubscribeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void updateTodayDate(){
        LocalDate today = LocalDate.now(); // 일일날짜를 위한 today 변수 선언하여 오늘 날짜 받아와서 초기화

        List<Subscribe> subscribes = subscribeRepository.findAll(); // Subscribe모든 리스트 조회하여
        for(Subscribe subscribe: subscribes){
            subscribe.updateTodayDate(today); // update 진행
        }
        subscribeRepository.saveAll(subscribes); // 다시저장
    }


    public Subscribe save(Subscribe subscribe){
        return subscribeRepository.save(subscribe);
    }

}
