package com.haedal.backend.subscribe.repository;

import com.haedal.backend.auth.model.User;
import com.haedal.backend.subscribe.model.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Query("SELECT s " +
            "FROM Subscribe s " +
            "JOIN FETCH s.product " +
            "WHERE s.user.userId = :userId")
    List<Subscribe> findSubscriptionsAndProductsByUser(@Param("userId") Long userId);

    //select * from subscribe s join product p on s.product_id = p.product_id where user_id = 4;
}
