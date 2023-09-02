package com.haedal.backend.Dibs.service;

import com.haedal.backend.Dibs.model.Dibs;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public interface DibsService {
    Dibs save(Dibs newDibs);

    void delete(Dibs deleteDibs);

    Dibs findDibsByUserIDProductID(@Param("userId") Long userId, @Param("productId")Long productId);
}
