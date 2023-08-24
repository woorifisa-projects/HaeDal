package com.haedal.backend.product.repository;

import com.haedal.backend.product.model.Product;
import com.haedal.backend.product.model.Tag;
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
//    @Query("SELECT p " +
//            "FROM Product p " +
//            "JOIN Subscribe s ON p.productId = s.product.productId " +
//            "WHERE p.productId IN (SELECT r.productId FROM Product r WHERE r IN :recommendedProductIds) " +
//            "GROUP BY p.productId " +
//            "ORDER BY COUNT(s) DESC")
    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN Subscribe s ON p.productId = s.product.productId " +
            "WHERE p.productId IN :recommendedProductIds " + // Removed the subquery
            "GROUP BY p.productId " +
            "ORDER BY COUNT(s) DESC")
    List<Product> filterByServicePurpose(@Param("recommendedProductIds") List<Long> recommendedProductIds);


    // 사용자 이용목적별로 조회된 추천상품들을, 사용자가 많은 수에 따라 내림차순
//    @Query("SELECT p " +
//            "FROM Product p " +
//            "JOIN Subscribe s ON p.productId = s.product.productId " +
//            "WHERE p.productId IN (SELECT r.productId FROM Product r WHERE r IN :recommendedServicePurpose) " +
//            "GROUP BY p.productId " +
//            "ORDER BY COUNT(s) DESC")
//    List<Product> filterByUserAgeGroup(@Param("recommendedServicePurpose") List<Long> recommendedUserAgeGroup);
}
