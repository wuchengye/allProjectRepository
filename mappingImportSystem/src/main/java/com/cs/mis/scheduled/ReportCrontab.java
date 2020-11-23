package com.cs.mis.scheduled;

import com.cs.mis.service.SelectService;
import com.cs.mis.service.UserService;
import com.cs.mis.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wcy
 */
@Component
public class ReportCrontab {
    @Autowired
    private SelectService selectService;
    @Autowired
    private UserService userService;

    @Scheduled(cron = "00 00 01 * * ?")
    public void autoCreateDayForms(){
        String yesterday = DateUtil.getDateOfYesterday();
        List<Map<String,String>> oneToOneList = userService.getUserIdAndCenterByType(0);
        //中心一对多用户id
        Map<String,Object> oneToManyMap = new HashMap<>();
        for (Map<String,String> map : oneToOneList){
            if (map.get("id") != null && map.get("center") != null){
                if(oneToManyMap.get(map.get("center")) == null){
                    List<String> list = new ArrayList<>();
                    list.add(map.get("id"));
                    oneToManyMap.put(map.get("center"),list);
                }else {
                    List<String> list = (List<String>) oneToManyMap.get(map.get("center"));
                    list.add(map.get("id"));
                    oneToManyMap.put(map.get("center"),list);
                }
            }
        }

        for (Map.Entry<String,Object> entry : oneToManyMap.entrySet()){

        }



    }
}
