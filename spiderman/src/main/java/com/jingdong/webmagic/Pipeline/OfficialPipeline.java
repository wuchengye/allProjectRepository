package com.jingdong.webmagic.Pipeline;

import com.jingdong.webmagic.Scheduled.SpiderScheduled;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfficialPipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if(entry.getKey().equals("HUAWEI")){
                list = (List<String>) entry.getValue();
            }
        }
        SpiderScheduled.OfficialDetailLinkList.addAll(list);
    }
}
