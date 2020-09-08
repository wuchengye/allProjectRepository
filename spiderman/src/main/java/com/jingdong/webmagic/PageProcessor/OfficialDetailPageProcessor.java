package com.jingdong.webmagic.PageProcessor;

import com.jingdong.webmagic.Downloader.ChromeDownloader;
import com.jingdong.webmagic.Model.ItemEntity;
import com.jingdong.webmagic.Model.PriceEntity;
import com.jingdong.webmagic.Utils.CommonUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfficialDetailPageProcessor implements PageProcessor {
    private Site site = Site
            .me()
            .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
            .setRetryTimes(3);
    private WebDriver driver;

    private List<ItemEntity> itemEntityList = new ArrayList<>();
    private List<PriceEntity> priceEntityList = new ArrayList<>();

    @Override
    public void process(Page page) {
        //生命周期与这个方法不同，需要每次清空，不然数据重复会报错
        itemEntityList.clear();
        priceEntityList.clear();
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
                case "MI":
                    doMI(page);
                    break;
                case "VIVO":
                    doVIVO(page);
                    break;
                case "OPPO":
                    doOPPO(page);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            driver.close();
        }
        page.putField("item",itemEntityList);
        page.putField("price",priceEntityList);
    }

    private void doOPPO(Page page) {
        List<String> colors = page.getHtml().xpath("section [@id='oc-container']//section [@class='product-details']//section [@class='config-list']/section[1]/section/section/@class").all();
        if(colors != null){
            for (int index = 0; index < colors.size(); index ++){
                if(colors.get(index).contains("disable")){
                    continue;
                }
                try{
                    driver.findElement(By.xpath("//section [@id='oc-container']//section [@class='product-details']//section [@class='config-list']/section[1]/section/section[" + (index+1) + "]")).click();
                    Thread.sleep(3 * 1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                page.clearHtml();
                page.setRawText(driver.getPageSource());
                List<String> specs = page.getHtml().xpath("section [@id='oc-container']//section [@class='product-details']//section [@class='config-list']/section[2]/section/section/@class").all();
                if(specs != null){
                    System.out.println("!" + specs.size());
                    for (int index2 = 0;index2 < specs.size(); index2 ++){
                        if(specs.get(index2).contains("disable")){
                            continue;
                        }
                        try{
                            driver.findElement(By.xpath("//section [@id='oc-container']//section [@class='product-details']//section [@class='config-list']/section[2]/section/section[" + (index2+1) + "]")).click();
                            Thread.sleep(3 * 1000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        page.clearHtml();
                        page.setRawText(driver.getPageSource());
                        getOppoInfo(page);
                    }
                }
            }
        }
    }

    private void doVIVO(Page page) {
        List<String> specs = page.getHtml().xpath("div [@class='sku-info']/dl [@class='specs']/dd[1]/ul [@class='item_list']/li/@class").all();
        if(specs != null){
            for (int index1 = 0 ;index1 < specs.size(); index1 ++){
                if(specs.get(index1).contains("disabled")){
                    continue;
                }
                try{
                    driver.findElement(By.xpath("//div [@class='sku-info']/dl [contains(@class,'specs')]/dd[1]/ul [@class='item_list']/li["+ (index1+1) + "]")).click();
                    Thread.sleep(3 * 1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                page.clearHtml();
                page.setRawText(driver.getPageSource());
                List<String> colors = page.getHtml().xpath("div [@class='sku-info']/dl [@class='specs']/dd[2]/ul [@class='item_list']/li/@class").all();
                if(colors != null){
                    for (int index2 = 0; index2 < colors.size(); index2++){
                        if(colors.get(index2).contains("disabled")){
                            continue;
                        }
                        try{
                            driver.findElement(By.xpath("//div [@class='sku-info']/dl [contains(@class,'specs')]/dd[2]/ul [@class='item_list']/li["+ (index2+1) + "]")).click();
                            Thread.sleep(3 * 1000);
                            driver.findElement(By.xpath("//section [@class='detail-info']//div [@class='detail_tab']/ul/li[2]")).click();
                            Thread.sleep(3 * 1000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        page.clearHtml();
                        page.setRawText(driver.getPageSource());
                        getVivoInfo(page);
                    }
                }
            }
        }
    }

    private void doMI(Page page) {
        List<String> optionBuy = page.getHtml().xpath("div [@class='buy-option']/div").all();
        if(optionBuy != null && optionBuy.size() > 0){
            int li1 = page.getHtml().xpath("div [@class='buy-option']/div[1]/div/ul/li").all().size();
            for (int index1 = 0;index1 < li1; index1++){
                try {
                    driver.findElement(By.xpath("//div [contains(@class,'buy-option')]/div[1]/div/ul/li[" + (index1 + 1) + "]")).click();
                    Thread.sleep(5 * 1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                page.clearHtml();
                page.setRawText(driver.getPageSource());
                int li2 = page.getHtml().xpath("div [@class='buy-option']/div[2]/div/ul/li").all().size();
                for (int index2 = 0 ;index2 < li2;index2 ++){
                    try {
                        driver.findElement(By.xpath("//div [contains(@class,'buy-option')]/div[2]/div/ul/li[" + (index2 + 1) + "]")).click();
                        Thread.sleep(5 * 1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    page.clearHtml();
                    page.setRawText(driver.getPageSource());
                    if(optionBuy.size() == 3){
                        int li3 = page.getHtml().xpath("div [@class='buy-option']/div[3]/div/ul/li").all().size();
                        for (int index3 = 0;index3 < li3;index3++){
                            try {
                                driver.findElement(By.xpath("//div [contains(@class,'buy-option')]/div[3]/div/ul/li[" + (index3 + 1) + "]")).click();
                                Thread.sleep(5 * 1000);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            page.clearHtml();
                            page.setRawText(driver.getPageSource());
                            getMiInfo(true,page);
                        }
                    }else {
                        getMiInfo(false,page);
                    }
                }
            }

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
                    e.printStackTrace();
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
                            e.printStackTrace();
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
                    e.printStackTrace();
                }
                Thread.sleep(5 * 1000);
                page.clearHtml();
                page.setRawText(driver.getPageSource());
                getHuaWeiInfo(page);
            }
        }
    }

    private void getOppoInfo(Page page){
        try {
            ItemEntity itemEntity = new ItemEntity();
            String title = page.getHtml().xpath("section [@id='oc-container']//section [@class='details-title']/p [@class='product-name']/text()").toString();
            String introduce = page.getHtml().xpath("section [@id='oc-container']//section [@class='details-title']/p [@class='product-text']/font/text()").all().toString();
            itemEntity.setItemFullName(title + introduce);
            itemEntity.setColor(page.getHtml().xpath("section [@id='oc-container']//section [@class='product-details']//section [@class='config-list']/section[1]/section/section [@class='current']/a/span/text()").toString());
            itemEntity.setSpecs(page.getHtml().xpath("section [@id='oc-container']//section [@class='product-details']//section [@class='config-list']/section[2]/section/section [@class='current']/a/span/text()").toString());
            String model = title.replace(itemEntity.getColor(),"");
            model = model.replace(itemEntity.getSpecs(),"");
            itemEntity.setModel(model.trim());
            itemEntity.setFormat(CommonUtils.regexFormat(itemEntity.getItemFullName()));
            itemEntity.setItemUrl(page.getUrl().get());
            PriceEntity priceEntity = new PriceEntity();
            priceEntity.setPrice(Double.valueOf(page.getHtml().xpath("section [@id='oc-container']//section [@class='details-title']/div [@class='product-ordinary']/p [@class='product-price']/@data-sersor-price").toString()));
            priceEntity.setReferPrice(priceEntity.getPrice());
            List<String> gifts = page.getHtml().xpath("section [@id='oc-container']//section [@class='product-details']//section [@class='config-list']/section/section [@class='gift-list']/section/p/text()").all();
            if(gifts != null && gifts.size() > 0){
                StringBuffer sb = new StringBuffer();
                for (String s : gifts){
                    sb.append(s + ";");
                }
                priceEntity.setHaveGift("是");
                priceEntity.setPreferentialDetail(sb.toString());
            }else {
                priceEntity.setHaveGift("否");
            }
            itemEntityList.add(itemEntity);
            priceEntityList.add(priceEntity);
            page.putField("brand","OPPO");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getVivoInfo(Page page){
        try{
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setItemFullName(page.getHtml().xpath("div [@class='sku-info']/div [@class='primary']/h1 [@class='name']/text()").toString());
            //itemEntity.setBrand(CommonUtils.regexBrand(itemEntity.getItemFullName()));
            itemEntity.setBrand("VIVO");
            List<String> where = page.getHtml().xpath("section [@class='detail-info']//div [@class='param_list']/div/h1").all();
            for (int i = 0; i < where.size(); i++){
                if(where.get(i).contains("主要参数")){
                    itemEntity.setModel(page.getHtml().xpath("section [@class='detail-info']//div [@class='param_list']/div[" + (i+1) + "]/ul/li[1]/p/text()").toString());
                    break;
                }
            }
            itemEntity.setFormat(CommonUtils.regexFormat(itemEntity.getItemFullName()));
            itemEntity.setSpecs(page.getHtml().xpath("div [@class='sku-info']/dl [@class='specs']/dd[1]/ul [@class='item_list']/li [@class='sku-module_item--checked']/@title").toString());
            itemEntity.setColor(page.getHtml().xpath("div [@class='sku-info']/dl [@class='specs']/dd[2]/ul [@class='item_list']/li [@class='sku-module_item--checked']/@title").toString());
            itemEntity.setItemUrl(page.getUrl().get());
            PriceEntity priceEntity = new PriceEntity();
            priceEntity.setPrice(Double.valueOf(page.getHtml().xpath("div [@class='sku-info']//div [@class='summary']/div [@class='summary_price']/div/p/text()").toString()));
            priceEntity.setReferPrice(priceEntity.getPrice());
            List<String> activity = page.getHtml().xpath("div [@class='summary_activity']//div [@class='content']/span/span/p [@class='name']/text()").all();
            List<String> activity2 = page.getHtml().xpath("div [@class='summary_activity']//div [@class='content']/text()").all();
            activity.addAll(activity2);
            if(activity.size() > 0){
                StringBuffer sb = new StringBuffer();
                List<Map<String,Double>> referList = new ArrayList<>();
                for (String s : activity){
                    sb.append(s + ";");
                    referList.add(CommonUtils.getNumFromString(s));
                }
                priceEntity.setReferPrice(CommonUtils.calcMinNum(priceEntity.getPrice(),referList));
                priceEntity.setHaveGift("是");
                priceEntity.setPreferentialDetail(sb.toString());
                if(priceEntity.getReferPrice() < priceEntity.getPrice()){
                    priceEntity.setPreferentialType("优惠券");
                }
            }else {
                priceEntity.setHaveGift("否");
            }
            itemEntityList.add(itemEntity);
            priceEntityList.add(priceEntity);
            page.putField("brand","MIorVIVO");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getMiInfo(boolean flag , Page page) {
        try{
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setItemFullName(page.getHtml().xpath("div [@class='product-con']/p [@class='sale-desc']/text()").toString());
            if(flag == true){
                itemEntity.setModel(page.getHtml().xpath("div [@class='buy-option']/div[1]/div/ul/li [@class='active']/a/text()").toString());
                itemEntity.setSpecs(page.getHtml().xpath("div [@class='buy-option']/div[2]/div/ul/li [@class='active']/a/text()").toString());
                itemEntity.setColor(page.getHtml().xpath("div [@class='buy-option']/div[3]/div/ul/li [@class='active']/a/text()").toString());
            }else {
                itemEntity.setModel(page.getHtml().xpath("div [@class='product-con']/h2/text()").toString());
                itemEntity.setSpecs(page.getHtml().xpath("div [@class='buy-option']/div[1]/div/ul/li [@class='active']/a/text()").toString());
                itemEntity.setColor(page.getHtml().xpath("div [@class='buy-option']/div[2]/div/ul/li [@class='active']/a/text()").toString());
            }
            itemEntity.setBrand(CommonUtils.regexBrand(itemEntity.getModel()));
            itemEntity.setFormat(CommonUtils.regexFormat(itemEntity.getItemFullName()));
            itemEntity.setItemUrl(page.getUrl().get());
            PriceEntity priceEntity = new PriceEntity();
            priceEntity.setPrice(Double.valueOf(CommonUtils.regexMoney(page.getHtml().xpath("div [@class='product-con']/div [@class='selected-list']//div [@class='total-price']/text()").toString())));
            List<String> gifts = page.getHtml().xpath("div [@class='activity-box']//span [@class='flow-name']/text()").all();
            priceEntity.setReferPrice(priceEntity.getPrice());
            StringBuffer sb = new StringBuffer();
            if(gifts != null && gifts.size() > 0){
                priceEntity.setHaveGift("是");
                for (String s : gifts){
                    sb.append(s + ";");
                }
            }else {
                priceEntity.setHaveGift("否");
            }
            priceEntity.setPreferentialDetail(sb.toString());
            itemEntityList.add(itemEntity);
            priceEntityList.add(priceEntity);
            page.putField("brand","MIorVIVO");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getHuaWeiInfo(Page page) {
        try {
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setItemFullName(page.getHtml().xpath("h1 [@id='pro-name']/text()").toString());
            itemEntity.setItemId(Long.valueOf(page.getHtml().xpath("div [@id='pro-sku-code2']/text()").toString()));
            itemEntity.setBrand(CommonUtils.regexBrand(itemEntity.getItemFullName()));
            itemEntity.setColor(page.getHtml().xpath("div [@id='pro-skus']/dl [@class='product-choosepic']//li [@class='selected']//span/text()").toString());
            itemEntity.setSpecs(page.getHtml().xpath("div [@id='pro-skus']/dl [@class='product-choose clearfix']//li [@data-attrname='版本' and @class='selected']//span/text()").toString());
            itemEntity.setFormat(CommonUtils.regexFormat(itemEntity.getItemFullName()));
            itemEntity.setItemUrl(page.getUrl().get());
            itemEntity.setModel(page.getHtml().xpath("div [@class='product-parameter-main clearfix hide' and @style='display: block;']/ul/li[1]/span/text()").toString());
            PriceEntity priceEntity = new PriceEntity();
            priceEntity.setPrice(Double.valueOf(page.getHtml().xpath("div [@class='product-price-info']/span [@id='pro-price']/text()").toString()));
            List<String> youhuiquan = page.getHtml().xpath("div [@class='product-info']/div [@id='couponBtn']//span/text()").all();
            List<String> gifts = page.getHtml().xpath("div [@id='product-prom-all']/div [@class='product-prom-item clearfix']/div/span/text()").all();
            priceEntity.setReferPrice(priceEntity.getPrice());
            StringBuffer sb = new StringBuffer();
            if(youhuiquan != null && youhuiquan.size() > 0){
                priceEntity.setPreferentialType("优惠券");
                List<Map<String,Double>> referList = new ArrayList<>();
                for (String s : youhuiquan){
                    sb.append(s + ";");
                    referList.add(CommonUtils.getNumFromString(s));
                }
                if(referList.size() > 0){
                    priceEntity.setReferPrice(CommonUtils.calcMinNum(priceEntity.getPrice(),referList));
                }
            }
            if(gifts != null && gifts.size() > 0){
                priceEntity.setHaveGift("是");
                for (String s : gifts){
                    sb.append(s + ";");
                }
            }else {
                priceEntity.setHaveGift("否");
            }
            priceEntity.setPreferentialDetail(sb.toString());
            itemEntityList.add(itemEntity);
            priceEntityList.add(priceEntity);
            page.putField("brand","HUAWEI");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
