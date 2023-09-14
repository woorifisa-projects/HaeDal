package com.haedal.backend.product.model;

import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import com.haedal.backend.subscribe.model.Subscribe;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Getter
@Table
@Entity
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId; // 상품 id

    @Column(name = "max_product_money")
    private Long maxProductMoney; // 상품 신청 최대 금액

    @Enumerated(EnumType.STRING)
    @Column(name = "service_purpose")
    private ServicePurpose servicePurpose; // 서비스 이용 목적

    @Enumerated(EnumType.STRING)
    @Column(name = "user_age_group")
    private UserAgeGroup userAgeGroup; // 연령대

    @Enumerated(EnumType.STRING)
    private Tag tag;// 어떤 태그를 가지고 있는지

    @Column(name = "product_name")
    private String productName; // 상품 이름

    @Column(name = "short_info")
    private String shortInfo; // 상품 한 줄 설명
    @Column(name = "long_info")
    private String longInfo; // 상품 전체 설명
    @Column(name = "period")
    private int period; // 저축 기간
    @Column(name = "required_start_money")
    private int requiredStartMoney; // 시작 금액
    @Column(name = "interest_rate")
    private double interestRate; // 금리

    @OneToMany(mappedBy = "product")
    private List<Subscribe> subscribers; // 한 개의 상품에 몇 명의 고객들이 가입했는지 list

    @Column(name = "is_deposit")
    private boolean isDeposit; // 예금, 적금 타입 확인용 컬럼(0:적금 1:예금)


    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Dibs> dibs;

    //구매 가능 상품인지 여부
    @Column(name="product_status",columnDefinition = "BOOLEAN DEFAULT true")
    private boolean productStatus;

    public Product(Long productId) {
        this.productId=productId;
    }

    public boolean getProductStatus() {
        return this.productStatus;
    }

    @Builder
    public Product(Long productId, Long maxProductMoney, ServicePurpose servicePurpose, UserAgeGroup userAgeGroup, Tag tag, String productName, String shortInfo, String longInfo, int period, int requiredStartMoney, double interestRate, List<Subscribe> subscribers, boolean isDeposit, List<Dibs> dibs, boolean productStatus) {
        this.productId = productId;
        this.maxProductMoney = maxProductMoney;
        this.servicePurpose = servicePurpose;
        this.userAgeGroup = userAgeGroup;
        this.tag = tag;
        this.productName = productName;
        this.shortInfo = shortInfo;
        this.longInfo = longInfo;
        this.period = period;
        this.requiredStartMoney = requiredStartMoney;
        this.interestRate = interestRate;
        this.subscribers = subscribers;
        this.isDeposit = isDeposit;
        this.dibs = dibs;
        this.productStatus = productStatus;
    }
}
