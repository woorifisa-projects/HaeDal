package com.haedal.backend.product.dto.response;


import com.haedal.backend.auth.model.User;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.subscribe.model.Subscribe;
import com.haedal.backend.product.model.Tag;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import javax.persistence.Column;
import java.util.List;


//TODO: 추후 리팩토링
//TODO: 추후 리팩토링2 - 프론트에서는 상품 id를 띄우지 않고, 백엔드에서 조회할 때만 사용하는 방법 고안

// 상품 리스트에서 보여 줄 상품정보들
@AllArgsConstructor
@Getter
@Builder
public class ProductResponse {


    private final Long productId; // 상품 id
//    private final Long productAsset; // 추천을 위한 상품 자산
    private ServicePurpose servicePurpose; // 서비스 이용 목적
    private UserAgeGroup userAgeGroup; // 연령대
    private Tag tag;// 어떤 태그를 가지고 있는지
    private final String productName; // 상품 이름
    private final String shortInfo; // 상품 한 줄 설명
    private String longInfo; // 상품 전체 설명
    private final int period; // 저축 기간
    private int requiredStartMoney; // 시작 금액
    private final double interestRate; // 금리
//    private List<Subscribe> subscribers; // 한 개의 상품에 몇 명의 고객들이 가입했는지 list
    private boolean productStatus; // 상품 활성 상태
    private boolean isDeposit; // 예금, 적금 타입 확인용 컬럼(0=예금, 1=적금)
    private Long maxProductMoney;

    private String accountNumber; //유저 계좌번호를 넘겨주기 위함

    //사용자 계좌 잔액
    private Long asset;




    public static ProductResponse from(Product product)  {
        return ProductResponse.builder()
                .productId(product.getProductId())
//                .productAsset(product.getMaxProductMoney())
                .tag(product.getTag())
                .productName(product.getProductName())
                .shortInfo(product.getShortInfo())
                .longInfo(product.getLongInfo())
                .period(product.getPeriod())
                .interestRate(product.getInterestRate())
                .isDeposit(product.isDeposit())
                .productStatus(product.getProductStatus())
                .requiredStartMoney(product.getRequiredStartMoney())
                .maxProductMoney(product.getMaxProductMoney())
                .build();
    }


    public static ProductResponse expectedResponse(
            Long productId,
            Tag tag,
            String productName,
            String shortInfo,
            String longInfo,
            int period,
            int requiredStartMoney,
            double interestRate,
            boolean productStatus,
            boolean isDeposit,
            Long maxProductMoney) {
        return ProductResponse.builder()
                .productId(productId)
//            .productAsset(product.getMaxProductMoney())
                .tag(tag)
                .productName(productName)
                .shortInfo(shortInfo)
                .longInfo(longInfo)
                .period(period)
                .interestRate(interestRate)
                .isDeposit(isDeposit)
                .productStatus(productStatus)
                .requiredStartMoney(requiredStartMoney)
                .maxProductMoney(maxProductMoney)
                .build();
    }






    public static ProductResponse recommendedFrom(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .productId(product.getProductId())
//                .productAsset(product.getMaxProductMoney())
                .servicePurpose(product.getServicePurpose())
                .userAgeGroup(product.getUserAgeGroup())
                .tag(product.getTag())
                .productName(product.getProductName())
                .longInfo(product.getLongInfo())
                .interestRate(product.getInterestRate())
                .isDeposit(product.isDeposit())
                .build();
        return productResponse;
    }


    public static ProductResponse mapProductToResponse(Product foundProduct, User user) {
        ProductResponse productResponse = ProductResponse.builder()
        .productId(foundProduct.getProductId())
//            .productAsset(foundProduct.getMaxProductMoney())
            .tag(foundProduct.getTag())
            .productName(foundProduct.getProductName())
            .shortInfo(foundProduct.getShortInfo())
            .longInfo(foundProduct.getLongInfo())
            .period(foundProduct.getPeriod())
            .interestRate(foundProduct.getInterestRate())
            .requiredStartMoney(foundProduct.getRequiredStartMoney())
            .maxProductMoney(foundProduct.getMaxProductMoney())
            .isDeposit(foundProduct.isDeposit())
            .accountNumber(user.getAccountNumber()).asset(user.getAsset())
            .build();
        return productResponse;
    }

}

