package com.haedal.backend.subscribe.dto.response;

import com.haedal.backend.subscribe.model.Subscribe;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SubscribeResponse {
    private Long subscribeId;
    private Long userId;
    private Long productId;
    private Long startMoney;
    private Long presentMoney;
    private LocalDate subscribeDate;

    @Builder
    public SubscribeResponse(Long subscribeId, Long userId, Long productId, Long startMoney, Long presentMoney, LocalDate subscribeDate) {
        this.subscribeId = subscribeId;
        this.userId = userId;
        this.productId = productId;
        this.startMoney = startMoney;
        this.presentMoney = presentMoney;
        this.subscribeDate = subscribeDate;
    }

    // Subscribe 엔티티로부터 SubscribeResponse로 변환하는 메서드
    public static SubscribeResponse from(Subscribe subscribe) {
        final Long subscribeId = subscribe.getSubscribeId();
        final Long userId = subscribe.getUser().getUserId();
        final Long productId = subscribe.getProduct().getProductId();
        final Long startMoney = subscribe.getStartMoney();
        final Long presentMoney = subscribe.getPresentMoney();
        final LocalDate subscribeDate = subscribe.getSubscribeDate();
        return new SubscribeResponse(subscribeId, userId , productId , startMoney, presentMoney , subscribeDate);
    }

}


