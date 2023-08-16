package com.haedal.backend.product.model;

import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId; // 상품 id

    @Column(name = "product_asset")
    private Long productAsset; // 추천을 위한 상품 자산




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




}
