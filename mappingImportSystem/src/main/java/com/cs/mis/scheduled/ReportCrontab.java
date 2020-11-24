package com.cs.mis.scheduled;

import com.cs.mis.controller.ExcelController;
import com.cs.mis.entity.ExcelDataEntity;
import com.cs.mis.mapper.ExcelMapper;
import com.cs.mis.service.SelectService;
import com.cs.mis.service.UserService;
import com.cs.mis.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    @Autowired
    private ExcelMapper excelMapper;

    @Scheduled(cron = "00 00 01 * * ?")
    public void autoCreateDayForms() throws IOException {
        String yesterday = DateUtil.getDateOfYesterday();
        List<Map<String,Object>> oneToOneList = userService.getUserIdAndCenterByType(0);
        //中心一对多用户id
        Map<String,Object> oneToManyMap = new HashMap<>();
        for (Map<String,Object> map : oneToOneList){
            if (map.get("id") != null && map.get("center") != null){
                if(oneToManyMap.get(map.get("center")) == null){
                    List<String> list = new ArrayList<>();
                    list.add(map.get("id").toString());
                    oneToManyMap.put((String)map.get("center"),list);
                }else {
                    List<String> list = (List<String>) oneToManyMap.get(map.get("center"));
                    list.add(map.get("id").toString());
                    oneToManyMap.put((String)map.get("center"),list);
                }
            }
        }
        for (Map.Entry<String,Object> entry : oneToManyMap.entrySet()){
            ExcelDataEntity yesterdayData = selectService.getOneDataByDateAndId(yesterday, (List<String>) entry.getValue());
            if(yesterdayData == null){
                ExcelDataEntity beforeYesterdayData = selectService.getOneDataBeforeDateByIdDesc(yesterday, (List<String>) entry.getValue());
                if(beforeYesterdayData == null){
                    continue;
                }else {
                    doBeforeToYesterday(beforeYesterdayData,entry,yesterday);
                }
            }else {
                continue;
            }
        }
    }

    /**
     * 把之前的数据最新的数据提取写入昨天的
     * @date 2020-11-24 16:37
     */
    private void doBeforeToYesterday(ExcelDataEntity beforeYesterdayData, Map.Entry<String, Object> entry, String yesterday) throws IOException {
        String parentPath = ExcelController.TXT_DAT_PATH + yesterday + "\\";
        String fileName = "staffinformation_" + entry.getKey() + "_day_" + yesterday.replace("-","") + ".txt";
        File folder = new File(parentPath);
        folder.mkdirs();
        FileWriter fw = new FileWriter(parentPath + fileName);
        try {
            List<String> idList = (List<String>) entry.getValue();
            for (String id : idList) {
                List<ExcelDataEntity> entityList = selectService.getDataByDateAndId(beforeYesterdayData.getImportTime(), id);
                if (entityList != null && entityList.size() > 0) {
                    Map<String, Object> map = new HashMap<>(3);
                    map.put("id", id);
                    map.put("date", yesterday);
                    map.put("entityList", entityList);
                    excelMapper.insertListWithDateAndId(map);
                    for (ExcelDataEntity entity : entityList) {
                        fw.write(entity.toString() + "\n");
                    }
                    fw.flush();
                    break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            fw.close();
        }
    }
}
