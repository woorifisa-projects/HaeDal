package com.haedal.backend.Dibs.repository;

import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.subscribe.model.Subscribe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DibsRepository extends CrudRepository<Dibs,Long> {
    Dibs save(Dibs newDibs);

    void delete(Dibs deleteDibs);

    @Query("SELECT d " +
            "FROM Dibs d " +
            "WHERE d.user.userId = :userId "+
            "AND d.product.productId = :productId ")
    Dibs findDibsByUserIDProductID(@Param("userId") Long userId,@Param("productId")Long productId);
}
