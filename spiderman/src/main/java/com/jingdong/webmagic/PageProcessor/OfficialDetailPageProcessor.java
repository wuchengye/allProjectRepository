package com.jingdong.webmagic.PageProcessor;

import com.jingdong.webmagic.Downloader.ChromeDownloader;
import com.jingdong.webmagic.Model.ItemEntity;
import com.jingdong.webmagic.Model.PriceEntity;
import com.jingdong.webmagic.Utils.CommonUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OfficialDetailPageProcessor implements PageProcessor {
    private static Logger logger = LoggerFactory.getLogger(OfficialPageProcessor.class);
    private Site site = Site
            .me()
            .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
            .setRetryTimes(3);
    private WebDriver driver;

    @Override
    public void process(Page page) {
        try {
            driver = ChromeDownloader.getChromeDriver();
            driver.manage().window().maximize();
            driver.get(page.getUrl().get());
            Thread.sleep(7 * 1000);
            page.setRawText(driver.getPageSource());
            //判断是什么详情页面
            switch (CommonUtils.brandOfUrl(page.getUrl().get())){
                case "HUAWEI":
                    doHUAWEI(page);
                    break;
                default:

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doHUAWEI(Page page) {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        List<PriceEntity> priceEntityList = new ArrayList<>();

        List<String > colors = page.getHtml().xpath("div [@id='pro-skus']/dl [@class='product-choosepic']//li/@class").all();
        for(int index = 0; index < colors.size(); index ++){
            if(colors.get(index).contains("selected")){
                colors.set(index,colors.get(index).replace(" selected",""));
            }
        }
        page.putField("key",page.getHtml().xpath("div [@id='choose-attr-2']//div [@class='item  selected']//a/text()"));
        System.out.println("xxxx" + page.getHtml().xpath("div [@id='choose-attr-2']//div [@class='item  selected']//a/text()").toString());
        for (String c: colors){
            System.out.println(c);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
