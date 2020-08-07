package com.jingdong.webmagic.Scheduled;

import com.jingdong.webmagic.Constant.Constant;
import com.jingdong.webmagic.Downloader.ChromeDownloader;
import com.jingdong.webmagic.PageProcessor.JDDetailPageProcessor;
import com.jingdong.webmagic.PageProcessor.JDStorePageProcessor;
import com.jingdong.webmagic.PageProcessor.TestPageProcessor;
import com.jingdong.webmagic.Pipeline.JDDetailPipeline;
import com.jingdong.webmagic.Pipeline.JDStorePipeline;
import com.jingdong.webmagic.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.PhantomJSDownloader;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SpiderScheduled {

    @Autowired
    private JDDetailPipeline jdDetailPipeline;
    @Autowired
    private ItemService itemService;

    public static List<String> JDItemList = new CopyOnWriteArrayList<>();

    @Scheduled(cron = "00 00 02 * * ?")
    public void JDScheduled(){

        /*Spider.create(new JDDetailPageProcessor())
                .setDownloader(new ChromeDownloader())
                .addPipeline(jdDetailPipeline)
                .addUrl("https://item.jd.com/100007673355.html")
                .thread(1)
                .run();*/

        Spider.create(new JDStorePageProcessor())
                .addUrl(Constant.JD_MI)
                .addUrl(Constant.JD_HUAWEI)
                .addUrl(Constant.JD_HONOR)
                .addUrl(Constant.JD_APPLE)
                .addUrl(Constant.JD_OPPO)
                .addUrl(Constant.JD_VIVO)
                .addPipeline(new JDStorePipeline())
                .thread(3)
                .run();


        Spider spider = Spider.create(new JDDetailPageProcessor());
        for (String url : JDItemList){
            spider.addUrl(url);
        }
        spider.addPipeline(jdDetailPipeline)
                .setDownloader(new ChromeDownloader())
                .thread(2)
                .run();

        JDItemList.clear();

    }
}
