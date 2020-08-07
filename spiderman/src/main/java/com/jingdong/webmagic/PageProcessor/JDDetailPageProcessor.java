package com.jingdong.webmagic.PageProcessor;

import com.jingdong.webmagic.Utils.CommonUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;


public class JDDetailPageProcessor implements PageProcessor {
    private Site site = Site
            .me()
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")
            .setRetryTimes(3);

    public void process(Page page) {

        String ItemId = CommonUtils.splitUrl(page.getUrl().get());
        page.putField("id",ItemId);
        page.putField("title",page.getHtml().xpath("div [@class='sku-name']//text()"));
        page.putField("color",page.getHtml().xpath("div [@id='choose-attr-1']//div [@class='item  selected']//a//i/text()"));
        page.putField("specs",page.getHtml().xpath("div [@id='choose-attr-2']//div [@class='item  selected']//a/text()"));
        page.putField("brand",page.getHtml().xpath("ul [@id='parameter-brand']/li/@title"));
        page.putField("model",page.getHtml().xpath("ul [@class='parameter2 p-parameter-list']/li[1]/@title"));
        page.putField("salesVolume",page.getHtml().xpath("div [@id='comment-count']/a/text()"));

        page.putField("price",page.getHtml().xpath("span [@class='price J-p-" + ItemId + "']/text()"));
        page.putField("originPrice",page.getHtml().xpath("span [@class='pricing']"));
        page.putField("youhuiquan",page.getHtml().xpath("div [@id='summary-quan']/div [@class='dd']//span [@class='quan-item']/span/text()").all());
        page.putField("gift",page.getHtml().xpath("div [@id='J-summary-top']//div [@class='dd J-prom-wrap p-promotions-wrap']//ins [@id='prom-gift']//a/@title").all());



        page.addTargetRequests(CommonUtils.idToUrl(page.getHtml().xpath("div [@id='choose-attr-1']//div [@class='item']/@data-sku").all()));
        page.addTargetRequests(CommonUtils.idToUrl(page.getHtml().xpath("div [@id='choose-attr-2']//div [@class='item']/@data-sku").all()));
    }

    public Site getSite() {
        return site;
    }

}
