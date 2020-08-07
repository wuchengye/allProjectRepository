package com.jingdong.webmagic.Scheduled;

import com.jingdong.webmagic.Constant.Constant;
import com.jingdong.webmagic.Downloader.ChromeDownloader;
import com.jingdong.webmagic.Downloader.FakeDownloader;
import com.jingdong.webmagic.PageProcessor.*;
import com.jingdong.webmagic.Pipeline.JDDetailPipeline;
import com.jingdong.webmagic.Pipeline.JDStorePipeline;
import com.jingdong.webmagic.Pipeline.OfficialPipeline;
import com.jingdong.webmagic.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.PhantomJSDownloader;
import us.codecraft.webmagic.pipeline.Pipeline;


import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SpiderScheduled {

    @Autowired
    private JDDetailPipeline jdDetailPipeline;
    @Autowired
    private ItemService itemService;

    public static List<String> JDItemList = new CopyOnWriteArrayList<>();
    public static List<String> OfficialDetailLinkList = new CopyOnWriteArrayList<>();

    @Scheduled(cron = "50 33 15 * * ?")
    public void JDScheduled(){

        /*Spider.create(new JDDetailPageProcessor())
                .setDownloader(new ChromeDownloader())
                .addPipeline(jdDetailPipeline)
                .addUrl("https://item.jd.com/100007673355.html")
                .thread(1)
                .run();*/

        /*Spider.create(new JDStorePageProcessor())
                //.addUrl(Constant.JD_MI)
                //.addUrl(Constant.JD_HUAWEI)
                //.addUrl(Constant.JD_HONOR)
                //.addUrl(Constant.JD_APPLE)
                //.addUrl(Constant.JD_OPPO)
                .addUrl(Constant.JD_VIVO)
                .addPipeline(new JDStorePipeline())
                .thread(3)
                .run();*/


        /*Spider spider = Spider.create(new JDDetailPageProcessor());
        for (String url : JDItemList){
            spider.addUrl(url);
        }
        spider.addPipeline(jdDetailPipeline)
                .setDownloader(new ChromeDownloader())
                .thread(2)
                .run();*/
        /*for (String s : JDItemList){
            System.out.println(s);
        }
        JDItemList.clear();*/

        /*Spider.create(new OfficialPageProcessor())
                .addUrl("https://www.vmall.com/list-36")
                .setDownloader(new FakeDownloader())
                .addPipeline(new OfficialPipeline())
                .thread(1)
                .run();*/
        Spider.create(new OfficialDetailPageProcessor())
                .setDownloader(new FakeDownloader())
                .addUrl("https://www.vmall.com/product/10086397382774.html")
                .addPipeline(new Pipeline() {
                    @Override
                    public void process(ResultItems resultItems, Task task) {
                        Map map = resultItems.getAll();
                        System.out.println(map.get("key"));
                    }
                })
                .thread(1)
                .run();
        }
}
