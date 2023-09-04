package com.haedal.backend.log.repository;

import com.haedal.backend.log.model.Log;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends CrudRepository<Log, Long> {
    Log save(Log log);
}
