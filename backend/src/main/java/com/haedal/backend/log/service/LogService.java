package com.haedal.backend.log.service;

import com.haedal.backend.log.model.Log;
import org.springframework.stereotype.Service;

@Service
public interface LogService {
    Log save(Log log);
}
