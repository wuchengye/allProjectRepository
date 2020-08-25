package com.jingdong.webmagic.Service;

import com.jingdong.webmagic.Model.LogEntity;
import com.jingdong.webmagic.Repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private LogRepository logRepository;
    @Autowired
    public void setLogRepository(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    public void insertLog(LogEntity logEntity){
        logRepository.save(logEntity);
    }
}
