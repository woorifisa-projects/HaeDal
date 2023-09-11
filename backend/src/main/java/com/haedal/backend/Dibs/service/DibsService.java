package com.haedal.backend.Dibs.service;

import com.haedal.backend.Dibs.model.Dibs;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DibsService {
    Dibs save(Dibs newDibs);

    void delete(Dibs deleteDibs);

    List<Dibs> findDibsByUserIDProductID(@Param("userId") Long userId, @Param("productId")Long productId);

    Dibs getFirstDibs(List<Dibs> dibs);
}
