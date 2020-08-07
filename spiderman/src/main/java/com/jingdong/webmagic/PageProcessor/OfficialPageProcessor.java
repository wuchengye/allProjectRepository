package com.jingdong.webmagic.PageProcessor;

import com.jingdong.webmagic.Constant.Constant;
import com.jingdong.webmagic.Downloader.ChromeDownloader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class OfficialPageProcessor implements PageProcessor {
    private static Logger logger = LoggerFactory.getLogger(OfficialPageProcessor.class);
    private Site site = Site
            .me()
            .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
            .setRetryTimes(3);
    private WebDriver driver;

    @Override
    public void process(Page page) {
        try{
            driver = ChromeDownloader.getChromeDriver();
            driver.manage().window().maximize();
            driver.get(page.getUrl().get());
            Thread.sleep(7 * 1000);
            page.setRawText(driver.getPageSource());

            //判断网址
            //华为
            if(page.getUrl().get().equals(Constant.HUAWEI)){
                doHUAWEI(page);
            }
            driver.quit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doHUAWEI(Page page){
        List<String> list = new ArrayList<>();
        list.addAll(page.getHtml().xpath("ul [@id='pro-list']/li/div/a").links().all());
        //后面几页
        List<String> pageNum = new ArrayList<>();
        pageNum.addAll(page.getHtml().xpath("div [@id='search-pager']/ul/span/li [@class='page-number link']/text()").all());
        for (String num : pageNum){
            try {
                driver.findElement(By.xpath("//div [@id='search-pager']/ul/span/li[" + Integer.valueOf(num) + "]")).click();
                Thread.sleep(7 * 1000);
                //重写webmagic-core源码，添加的clearHtml方法
                page.clearHtml();
                page = page.setRawText(driver.getPageSource());
                list.addAll(page.getHtml().xpath("ul [@id='pro-list']/li/div/a").links().all());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        page.putField("HUAWEI",list);
    }






    @Override
    public Site getSite() {
        return site;
    }

}
