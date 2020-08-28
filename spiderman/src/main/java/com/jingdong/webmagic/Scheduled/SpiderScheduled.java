package com.jingdong.webmagic.Scheduled;

import com.jingdong.webmagic.Constant.Constant;
import com.jingdong.webmagic.Downloader.ChromeDownloader;
import com.jingdong.webmagic.Downloader.FakeDownloader;
import com.jingdong.webmagic.PageProcessor.*;
import com.jingdong.webmagic.Pipeline.JDDetailPipeline;
import com.jingdong.webmagic.Pipeline.JDStorePipeline;
import com.jingdong.webmagic.Pipeline.OfficialDetailPipeline;
import com.jingdong.webmagic.Pipeline.OfficialPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SpiderScheduled {

    @Autowired
    private JDDetailPipeline jdDetailPipeline;
    @Autowired
    private OfficialDetailPipeline officialDetailPipeline;

    public static List<String> JDItemList = new CopyOnWriteArrayList<>();
    public static List<String> OfficialDetailLinkList = new CopyOnWriteArrayList<>();


    @Scheduled(cron = "00 00 02 * * ?")
    public void JDScheduled(){
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

    @Scheduled(cron = "00 00 04 * * ?")
    public void OfficialScheduled(){
        Spider.create(new OfficialPageProcessor())
                .addUrl(Constant.HUAWEI)
                .addUrl(Constant.MI)
                .addUrl(Constant.VIVO)
                .addUrl(Constant.OPPO)
                .setDownloader(new FakeDownloader())
                .addPipeline(new OfficialPipeline())
                .thread(1)
                .run();

        Spider spider = Spider.create(new OfficialDetailPageProcessor());
        for (String url : OfficialDetailLinkList){
            spider.addUrl(url);
        }
        spider.setDownloader(new FakeDownloader());
        spider.addPipeline(officialDetailPipeline);
        spider.thread(1);
        spider.run();
        OfficialDetailLinkList.clear();
    }
}
