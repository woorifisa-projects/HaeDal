package com.haedal.backend.product.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

//TODO : 상속 관련 설정
@DiscriminatorValue("D")
@Entity
@Table(name = "Deposit")
public class Deposit extends Product {

    //상품의 예, 적금 구분을 위한 메서드
    @Transient // 엔티티의 컬럼으로 매핑하지 않음
    @Override
    public String getProductType() {
        return "D";
    }
}
