package com.haedal.backend.log.repository;

import com.haedal.backend.log.model.Log;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends CrudRepository<Log, Long> {
    Log save(Log log);

    @Query("SELECT l " +
            "FROM Log l " +
            "WHERE l.logTime BETWEEN ?1 AND ?2")
    List<Log> findByLogTimeBetween(LocalDateTime sixHoursAgo, LocalDateTime currentTime);
}