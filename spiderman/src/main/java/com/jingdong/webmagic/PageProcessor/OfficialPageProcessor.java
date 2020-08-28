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
            switch (page.getUrl().get()){
                case Constant.HUAWEI:
                    doHUAWEI(page);
                    break;
                case Constant.MI:
                    doMI(page);
                    break;
                case Constant.VIVO:
                    doVIVO(page);
                    break;
                case Constant.OPPO:
                    doOPPO(page);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            driver.close();
        }
    }

    private void doOPPO(Page page) {
        List<String> list = new ArrayList<>();
        //FindX
        list.addAll(page.getHtml().xpath("ul [@class='oh-nav-list']/li[1]//ul [@class='oc-row']/li/a/@href").all());
        //Reno
        list.addAll(page.getHtml().xpath("ul [@class='oh-nav-list']/li[2]//ul [@class='oc-row']/li/a/@href").all());
        //Ace
        list.addAll(page.getHtml().xpath("ul [@class='oh-nav-list']/li[3]//ul [@class='oc-row']/li/a/@href").all());
        //K/A
        list.addAll(page.getHtml().xpath("ul [@class='oh-nav-list']/li[4]//ul [@class='oc-row']/li/a/@href").all());

        page.putField("OPPO",list);
    }

    private void doVIVO(Page page) {
        List<String> list = new ArrayList<>();
        boolean flag = true;
        while (flag){
            list.addAll(page.getHtml().xpath("div [@id='content']//ul [@class='spu-item-list']/li/a").links().all());
            flag = false;
            try {
                driver.findElement(By.xpath("//div [@id='content']//div [@class='pagination']//span [@class='pagination_numbers']/span[@class='pagination_btn' and contains(text(),'下一页')]")).click();
                Thread.sleep(5 * 1000);
                page.clearHtml();
                page = page.setRawText(driver.getPageSource());
                flag = true;
            }
            catch (Exception e) {}
        }
        page.putField("VIVO",list);
    }

    private void doMI(Page page) throws InterruptedException {
        List<String> list = new ArrayList<>();
        //小米
        list.addAll(page.getHtml().xpath("div [@class='header-nav']/ul/li[2]/div/div/ul/li/a").links().all());
        //红米
        list.addAll(page.getHtml().xpath("div [@class='header-nav']/ul/li[3]/div/div/ul/li/a").links().all());
        //遍历：有些链接不是直达购买页面
        for (int index = 0; index < list.size(); index++){
            if( !list.get(index).contains("product_id") ){
                driver.get(list.get(index));
                Thread.sleep(7 * 1000);
                page.clearHtml();
                page.setRawText(driver.getPageSource());
                String url = page.getHtml().xpath("div [@class='xm-product-box']//div [@class='con']/div [@class='right']/a [@class='J_nav_buy']").links().toString();
                if(url != null && !url.equals("")){
                    list.set(index,url);
                }else {
                    list.remove(index);
                }
            }
        }
        page.putField("MI",list);
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
