package com.haedal.backend.product.repository;

import com.haedal.backend.product.dto.response.ProductResponse;
import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
import com.haedal.backend.profile.model.ServicePurpose;
import com.haedal.backend.profile.model.UserAgeGroup;
import com.haedal.backend.subscribe.model.Subscribe;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


//    List<Product> findByProductId(Product productId);

    List<Product> findByTag(Tag tag);


    //상품 명으로 상품 검색
    List<Product> findByProductNameLike(String productName);

    //상품 id로 상품 검색
    Product findByProductId(Long productId);


    // 사용자 자산별로 조회된 추천상품들을, 사용자가 많은 수에 따라 내림차순
    @Query (value = "select p " +
            "from Product p " +
            "join Subscribe s on p.productId = s.product.productId " +
            "group by p.productId " +
            "order by COUNT(s.subscribeId) DESC ")
    List<Product> filterByAsset(List<Product> recommendedAsset);



    // 사용자 이용목적별로 조회된 추천상품들을, 사용자가 많은 수에 따라 내림차순
    @Query (value = "select p " +
            "from Product p " +
            "join Subscribe s on p.productId = s.product.productId " +
            "group by p.productId " +
            "order by COUNT(s.subscribeId) DESC ")
    List<Product> filterByServicePurpose(List<Product> recommendedServicePurpose);



    // 사용자 이용목적별로 조회된 추천상품들을, 사용자가 많은 수에 따라 내림차순
    @Query (value = "select p " +
            "from Product p " +
            "join Subscribe s on p.productId = s.product.productId " +
            "group by p.productId " +
            "order by COUNT(s.subscribeId) DESC ")
    List<Product> filterByUserAgeGroup(List<Product> recommendedUserAgeGroup);
}


