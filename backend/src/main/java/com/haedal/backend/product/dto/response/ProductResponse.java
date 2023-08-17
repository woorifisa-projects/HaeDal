package com.haedal.backend.product.dto.response;

import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
//TODO: 추후 리팩토링

// 상품 리스트에서 보여 줄 상품정보들
public class ProductResponse {

    private final Long productId;
    private final String productName;
    private final double interestRate;
    private final String shortInfo;
    private final int period;
    private final Tag tag;

    @Builder
    public ProductResponse(Long productId, String productName, double interestRate, String shortInfo, int period, Tag tag) {
        this.productId = productId;
        this.productName = productName;
        this.interestRate = interestRate;
        this.shortInfo = shortInfo;
        this.period = period;
        this.tag = tag;
    }

    public static ProductResponse from(Product product) {
        final Long productId = product.getProductId();
        final String productName = product.getProductName();
        final double interestRate = product.getInterestRate();
        final String shortInfo = product.getShortInfo();
        final int period = product.getPeriod();
        final Tag tag = product.getTag();
        return new ProductResponse(productId , productName , interestRate , shortInfo , period , tag);
    }

}
