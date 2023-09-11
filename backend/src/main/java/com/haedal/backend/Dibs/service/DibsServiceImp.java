package com.haedal.backend.Dibs.service;

import com.haedal.backend.Dibs.model.Dibs;
import com.haedal.backend.Dibs.repository.DibsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DibsServiceImp implements DibsService{
    private DibsRepository dibsRepository;

    public DibsServiceImp(DibsRepository dibsRepository) {
        this.dibsRepository = dibsRepository;
    }


    @Override
    public Dibs save(Dibs newDibs) {
        return dibsRepository.save(newDibs);
    }

    @Override
    public void delete(Dibs deleteDibs) {
       dibsRepository.delete(deleteDibs);
    }

    @Override
    public List<Dibs> findDibsByUserIDProductID(Long userId, Long productId) {
        return dibsRepository.findDibsByUserIDProductID(userId,productId);
    }

    @Override
    public Dibs getFirstDibs(List<Dibs> dibs) {
        return dibs.get(0);
    }
}
