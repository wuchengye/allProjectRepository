package com.cs.mis.scheduled;

import com.cs.mis.service.SelectService;
import com.cs.mis.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wcy
 */
@Component
public class ReportCrontab {
    @Autowired
    private SelectService selectService;

    @Scheduled(cron = "00 00 01 * * ?")
    public void autoCreateDayForms(){
        String yesterday = DateUtil.getDateOfYesterday();
        Map<String,String> userMap = new HashMap<>();
        selectService.getDataWithoutPage()
    }
}
