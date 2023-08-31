package com.haedal.backend.subscribe.dto.response;

import com.haedal.backend.product.model.Tag;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import com.haedal.backend.subscribe.model.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
@AllArgsConstructor
public class PortfolioResponse {

    private Long subscribeId; //0
    private Long presentMoney; //0
    private Long startMoney; //0
    private LocalDate subscribeDate; //0
    private Long productId;//0
    private Long userId;
//    private Long productId; 하나더조회되는데 일단 주석처리
    private double interestRate;//0
    private boolean isDeposit; // 예금, 적금 타입 확인용 컬럼(0=예금, 1=적금)//0
    private String longInfo; // 상품 전체 설명//0
    private Long maxProductMoney;//0
    private int period; // 저축 기간//0
    private String productName; // 상품 이름//0
    private int requiredStartMoney; // 시작 금액//0
    private ServicePurpose servicePurpose; // 서비스 이용 목적//0
    private String shortInfo; // 상품 한 줄 설명//0
    private int subscription; // 월 납입금액(구독료)//0
    private Tag tag;// 어떤 태그를 가지고 있는지//0
    private UserAgeGroup userAgeGroup; // 연령대//0

    private Long progressdate; //현재 날짜 - 시작날짜 long값
    private LocalDate endSubscribeDate; //구독 만료 날짜

    public static PortfolioResponse from(Subscribe subscribe){
        return PortfolioResponse.builder()
        .subscribeId(subscribe.getSubscribeId())
        .presentMoney(subscribe.getPresentMoney())
        .startMoney(subscribe.getStartMoney())
                .subscribeDate(subscribe.getSubscribeDate())
                .productId(subscribe.getProduct().getProductId())
                .userId(subscribe.getUser().getUserId())
                .interestRate(subscribe.getProduct().getInterestRate())
                .isDeposit(subscribe.getProduct().isDeposit())
                .longInfo(subscribe.getProduct().getLongInfo())
                .maxProductMoney(subscribe.getProduct().getMaxProductMoney())
                .period(subscribe.getProduct().getPeriod())
                .productName(subscribe.getProduct().getProductName())
                .requiredStartMoney(subscribe.getProduct().getRequiredStartMoney())
                .servicePurpose(subscribe.getProduct().getServicePurpose())
                .shortInfo(subscribe.getProduct().getShortInfo())
                .subscription(subscribe.getProduct().getSubscription())
                .tag(subscribe.getProduct().getTag())
                .userAgeGroup(subscribe.getProduct().getUserAgeGroup())
                .progressdate(ChronoUnit.DAYS.between((subscribe.getTodayDate()),(subscribe.getSubscribeDate())))
                .endSubscribeDate(subscribe.getSubscribeDate().plusMonths(subscribe.getProduct().getPeriod()))
                .build();

    }
    // Getters and setters...
}