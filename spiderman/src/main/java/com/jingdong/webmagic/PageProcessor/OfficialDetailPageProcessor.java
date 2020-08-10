package com.jingdong.webmagic.PageProcessor;

import com.jingdong.webmagic.Downloader.ChromeDownloader;
import com.jingdong.webmagic.Model.ItemEntity;
import com.jingdong.webmagic.Model.PriceEntity;
import com.jingdong.webmagic.Utils.CommonUtils;
import org.openqa.selenium.By;
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
    List<ItemEntity> itemEntityList = new ArrayList<>();
    List<PriceEntity> priceEntityList = new ArrayList<>();

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

    private void doHUAWEI(Page page) throws InterruptedException {
        List<String > colors = page.getHtml().xpath("div [@id='pro-skus']/dl [@class='product-choosepic']//li/@class").all();
        List<String> specs = page.getHtml().xpath("div [@id='pro-skus']/dl [@class='product-choose clearfix']//li [@data-attrname='版本']/@class").all();
        if(colors != null && colors.size() > 0){
            for(int index = 0; index < colors.size(); index ++){
                if(colors.get(index).contains("selected")){
                    colors.set(index,colors.get(index).replace(" selected",""));
                }
                try {
                    driver.findElement(By.xpath("//div [@id='pro-skus']/dl [contains(@class,'product-choosepic')]//li [contains(@class,'" + colors.get(index)+ "')]")).click();
                }catch (Exception e){
                    System.out.println("yanse");
                }
                Thread.sleep(5 * 1000);
                if(specs != null && specs.size() > 0){
                    for(int index2 = 0;index2 < specs.size();index2 ++){
                        if(specs.get(index2).contains("selected")){
                            specs.set(index2,specs.get(index2).replace(" selected",""));
                        }
                        try {
                            driver.findElement(By.xpath("//div [@id='pro-skus']/dl [contains(@class,'product-choose')]//li [@data-attrname='版本' and contains(@class,'" + specs.get(index2) + "')]")).click();
                        }catch (Exception e){
                            System.out.println("banben");
                        }
                        Thread.sleep(5 * 1000);
                        page.clearHtml();
                        page.setRawText(driver.getPageSource());
                        getHuaWeiInfo(page);
                    }
                }else {
                    page.clearHtml();
                    page.setRawText(driver.getPageSource());
                    getHuaWeiInfo(page);
                }
            }
        }else if(specs != null && specs.size() > 0){
            for(int index2 = 0;index2 < specs.size();index2 ++) {
                if (specs.get(index2).contains("selected")) {
                    specs.set(index2, specs.get(index2).replace(" selected", ""));
                }
                try {
                    driver.findElement(By.xpath("//div [@id='pro-skus']/dl [@class='product-choose clearfix']//li [@data-attrname='版本' and @class='" + specs.get(index2) + "']"));
                }catch (Exception e){

                }
                Thread.sleep(5 * 1000);
                page.clearHtml();
                page.setRawText(driver.getPageSource());
                getHuaWeiInfo(page);
            }
        }
    }

    private void getHuaWeiInfo(Page page) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setItemFullName(page.getHtml().xpath("h1 [@id='pro-name']/text()").toString());
        itemEntity.setItemId(Long.valueOf(page.getHtml().xpath("div [@id='pro-sku-code2']/text()").toString()));
        itemEntity.setBrand(CommonUtils.regexBrand(itemEntity.getItemFullName()));
        itemEntity.setColor(page.getHtml().xpath("div [@id='pro-skus']/dl [@class='product-choosepic']//li [@class='selected']//span/text()").toString());
        itemEntity.setSpecs(page.getHtml().xpath("div [@id='pro-skus']/dl [@class='product-choose clearfix']//li [@data-attrname='版本' and @class='selected']//span/text()").toString());
        itemEntity.setFormat(CommonUtils.regexFormat(itemEntity.getItemFullName()));
        itemEntity.setItemUrl(page.getUrl().get());
        itemEntity.setModel(page.getHtml().xpath("div [@class='product-parameter-main clearfix hide' and @style='display: block;']/ul/li[1]/span/text()").toString());
        itemEntityList.add(itemEntity);
        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setPrice(Double.valueOf(page.getHtml().xpath("div [@class='product-price-info']/span [@id='pro-price']/text()").toString()));

        List<String> gifts = page.getHtml().xpath("div [@id='product-prom-all']/div [@class='product-prom-item clearfix']/div/span/text()").all();
        if(gifts != null && gifts.size() > 0){
            StringBuffer sb = new StringBuffer();
            for (String s : gifts){
                sb.append(s);
            }
            priceEntity.setHaveGift("是");
        }else {
            priceEntity.setHaveGift("否");
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
