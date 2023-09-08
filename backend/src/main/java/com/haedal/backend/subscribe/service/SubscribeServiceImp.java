package com.haedal.backend.subscribe.service;

import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.auth.model.User;
import com.haedal.backend.subscribe.dto.response.PortfolioResponse;
import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.subscribe.repository.SubscribeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@EnableScheduling
@Service
public class SubscribeServiceImp implements SubscribeService{
    private SubscribeRepository subscribeRepository;

    public SubscribeServiceImp(SubscribeRepository subscribeRepository) {
        this.subscribeRepository = subscribeRepository;
    }

    @Override
    public List<PortfolioResponse> findSubscriptionsAndProductsByUserSortedByMoney(String id) {
        List<Subscribe> result = subscribeRepository.findSubscriptionsAndProductsByUser(id);
        List<Subscribe> sortedPortfolio = result.stream()
                .sorted(Comparator.comparing(Subscribe::getStartMoney).reversed()) // 가입금액순으로 정렬해서
                .collect(Collectors.toList()); // 리스트로 반환
        return sortedPortfolio.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<PortfolioResponse> findSubscriptionsAndProductsByUserSortedByDays(String id) {
        List<Subscribe> result = subscribeRepository.findSubscriptionsAndProductsByUser(id);
        List<Subscribe> sortedPortfolio = result.stream()
                .sorted(Comparator.comparing(Subscribe::getSubscribeDate)) // 가입금액순으로 정렬해서
                .collect(Collectors.toList()); // 리스트로 반환
        return sortedPortfolio.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<PortfolioResponse> findDibsAndProductsByUser(String id) {
        List<Dibs> result = subscribeRepository.findDibsAndProductsByUser(id);
        return result.stream()
                .map(PortfolioResponse::dibsProfileFrom)
                .collect(Collectors.toList());
    }

    //해당 user가 같은 상품을 구독하였는지 여부 확인
    @Override
    public List<Subscribe> findSubscriptionsByProductsAndUser(Long userId, Long productId) {
        return subscribeRepository.findSubscriptionsByProductsAndUser(userId,productId);
    }

    @Scheduled(cron = "0 0 9 * * *") // 매일 자정에 실행
    public void updateTodayDate(){
        log.info("스케쥴러실행");
        LocalDate today = LocalDate.now(); // 일일날짜를 위한 today 변수 선언하여 오늘 날짜 받아와서 초기화

        List<Subscribe> subscribes = subscribeRepository.findAll(); // Subscribe모든 리스트 조회하여
        for(Subscribe subscribe: subscribes){
            subscribe.updateTodayDate(today); // update 진행
        }
        subscribeRepository.saveAll(subscribes); // 다시저장
    }

    @Override
    public void deleteByUser(User user){
        subscribeRepository.deleteByUser(user);
    }

    @Override
    public Long deleteBySubscribe(Subscribe subscribe) {
        Long startMoney = subscribe.getStartMoney();
        User user = subscribe.getUser();
        user.updateAsset(user.getAsset()+startMoney);
        subscribeRepository.delete(subscribe);
        return startMoney;
    }

    @Override
    public Subscribe findByUserIdAndProductId(String userId, Long productId) {
        return subscribeRepository.findByUserIdAndProductId(userId,productId);
    }

    public Subscribe save(Subscribe subscribe){
        return subscribeRepository.save(subscribe);
    }

}
