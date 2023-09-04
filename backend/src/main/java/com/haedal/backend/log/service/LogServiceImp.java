package com.haedal.backend.log.service;

import com.haedal.backend.log.model.Log;
import com.haedal.backend.log.repository.LogRepository;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImp implements LogService{
    private LogRepository logRepository;

    public LogServiceImp(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
    @Override
    public Log save(Log log) {
        return logRepository.save(log);
    }
}
