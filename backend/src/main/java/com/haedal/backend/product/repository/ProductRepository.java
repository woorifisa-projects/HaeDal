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
    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN Subscribe s ON p.productId = s.product.productId " +
            "WHERE p.productId IN :productsIdsInAsset " + // Removed the subquery
            "GROUP BY p.productId " +
            "ORDER BY COUNT(s) DESC")
    List<Product> orderByAsset(@Param("productsIdsInAsset") List<Long> productsIdsInAsset);



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
            "WHERE p.productId IN :productIdsInServicePurpose " + // Removed the subquery
            "GROUP BY p.productId " +
            "ORDER BY COUNT(s) DESC")
    List<Product> orderByServicePurpose(@Param("productIdsInServicePurpose") List<Long> productIdsInServicePurpose);


    // 사용자 이용목적별로 조회된 추천상품들을, 사용자가 많은 수에 따라 내림차순
//    @Query("SELECT p " +
//            "FROM Product p " +
//            "JOIN Subscribe s ON p.productId = s.product.productId " +
//            "WHERE p.productId IN (SELECT r.productId FROM Product r WHERE r IN :productIdsInUserAgeGroup) " +
//            "GROUP BY p.productId " +
//            "ORDER BY COUNT(s) DESC")
//    List<Product> orderByUserAgeGroup(@Param("productIdsInUserAgeGroup") List<Long> productIdsInUserAgeGroup);

        @Query("SELECT p " +
            "FROM Product p " +
            "JOIN Subscribe s ON p.productId = s.product.productId " +
            "WHERE p.productId IN :productIdsInUserAgeGroup " +
            "GROUP BY p.productId " +
            "ORDER BY COUNT(s) DESC")
    List<Product> orderByUserAgeGroup(@Param("productIdsInUserAgeGroup") List<Long> productIdsInUserAgeGroup);

}
