package com.jingdong.webmagic.Pipeline;

import com.jingdong.webmagic.Scheduled.SpiderScheduled;
import com.jingdong.webmagic.WebmagicApplication;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JDStorePipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {

        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            List<String> urls = (List<String>) entry.getValue();
            for (String url : urls){
                url = url.replace(" ","");
                list.add("https:" + url);
            }
        }
        SpiderScheduled.JDItemList.addAll(list);
    }
}
