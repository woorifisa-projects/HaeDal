package com.haedal.backend.product.dto.response;

import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
//TODO: 추후 리팩토링

// "전체상품 탭"에서 보일 상품정보들
public class ProductResponse {

    private final Long productId;
    private final String productName;
    private final String interestRate;
    private final String shortInfo;
    private final int period;
    private final Tag tag;

    @Builder
    public ProductResponse(Long productId, String productName, String interestRate, String shortInfo, int period, Tag tag) {
        this.productId = productId;
        this.productName = productName;
        this.interestRate = interestRate;
        this.shortInfo = shortInfo;
        this.period = period;
        this.tag = tag;
    }

}
