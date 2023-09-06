package com.haedal.backend.subscribe.repository;

import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.subscribe.model.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Query("SELECT s " +
            "FROM Subscribe s " +
            "JOIN FETCH s.product " +
            "WHERE s.user.id = :id")
    List<Subscribe> findSubscriptionsAndProductsByUser(@Param("id") String id);

    //select * from subscribe s join product p on s.product_id = p.product_id where user_id = 4;

    @Query("SELECT d " +
            "FROM Dibs d " +
            "JOIN FETCH d.product " +
            "WHERE d.user.id = :id")
    List<Dibs> findDibsAndProductsByUser(@Param("id") String id);

    @Query("SELECT s " +
            "FROM Subscribe s " +
            "JOIN FETCH s.product p " +
            "JOIN FETCH s.user u " +
            "WHERE u.userId = :userId " +
            "AND p.productId = :productId")
    Subscribe findSubscriptionsByProductsAndUser(@Param("userId") Long userId, @Param("productId") Long productId);
}
