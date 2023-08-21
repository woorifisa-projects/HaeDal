package com.haedal.backend.product.model;

import javax.persistence.*;

//TODO : 상속 관련 설정
@DiscriminatorValue("I")
@Entity
@Table(name = "InstallmentSavings")
public class InstallmentSavings extends Product {

    @Column(name = "subscription")
    private int Subscription; // 월 납입금액(구독료)

    //상품의 예, 적금 구분을 위한 메서드
    @Transient // 엔티티의 컬럼으로 매핑하지 않음
    @Override
    public String getProductType() {
        return "I";
    }
}
