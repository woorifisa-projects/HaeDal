package com.haedal.backend.product.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//TODO : 상속 관련 설정
@Entity
@Table(name = "InstallmentSavings")
public class InstallmentSavings extends Product {

    @Column(name = "subscription")
    private int Subscription; // 월 납입금액(구독료)
}
