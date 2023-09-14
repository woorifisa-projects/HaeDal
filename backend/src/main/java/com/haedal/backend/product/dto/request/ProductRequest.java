package com.haedal.backend.product.dto.request;


import com.haedal.backend.product.model.Tag;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ProductRequest {

        private final Long productId; // 상품 id
        //    private final Long productAsset; // 추천을 위한 상품 자산
        private ServicePurpose servicePurpose; // 서비스 이용 목적
        private UserAgeGroup userAgeGroup; // 연령대
        private Tag tag;// 어떤 태그를 가지고 있는지
        private final String productName; // 상품 이름
        private final String shortInfo; // 상품 한 줄 설명
        private final int period; // 저축 기간
        private int requiredStartMoney; // 시작 금액
        private final double interestRate; // 금리
        //    private List<Subscribe> subscribers; // 한 개의 상품에 몇 명의 고객들이 가입했는지 list
        private boolean isDeposit; // 예금, 적금 타입 확인용 컬럼(0=예금, 1=적금)
        private boolean productStatus;
        private Long maxProductMoney;


    }
