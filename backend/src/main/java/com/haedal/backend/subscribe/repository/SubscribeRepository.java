package com.haedal.backend.subscribe.repository;

import com.haedal.backend.subscribe.model.Subscribe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends CrudRepository<Subscribe, Long> {
}
