package com.haedal.backend.product.model;

import com.haedal.backend.profile.model.Account;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId; // 상품 id

    @OneToMany
    @JoinColumn(name = "subcribe_id")
    private List<Subscribe> subscribers; // 한 개의 추천상품에 어떤 고객들이 가입했는지 list


    @Column(name = "asset")
    private int asset; // 자산

    @Enumerated(EnumType.STRING)
    @Column(name = "service_surpose")
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
    @Column(name = "all_info")
    private String allInfo; // 상품 전체 설명
    @Column(name = "period")
    private int period; // 저축 기간
    @Column(name = "start_money")
    private int startMoney; // 시작 금액

    @Column(name = "interest_rate")
    private double interestRate; // 금리

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;





}
