package com.jingdong.webmagic.PageProcessor;

import com.jingdong.webmagic.Constant.Constant;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class JDStorePageProcessor implements PageProcessor {
    Site site = Site
            .me()
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0")
            .setRetryTimes(3);

    public void process(Page page) {
        if(page.getUrl().get().equals(Constant.JD_HUAWEI)){
            doHuawei(page);
        }
        if(page.getUrl().get().equals(Constant.JD_MI)){
            doMi(page);
        }
        if(page.getUrl().get().equals(Constant.JD_HONOR)){
            doHonor(page);
        }
        if(page.getUrl().get().equals(Constant.JD_APPLE)){
            doApple(page);
        }
        if(page.getUrl().get().equals(Constant.JD_VIVO)){
            doVivo(page);
        }
        if(page.getUrl().get().equals(Constant.JD_OPPO)){
            doOppo(page);
        }
    }

    private void doOppo(Page page) {
        System.out.println("京东oppo官方旗舰店");
        //Reno4新品
        page.putField("Reno4",page.getHtml().xpath("map [@name='Maprenodaohang']/area/@href").all());
        //Ace玩家
        page.putField("Ace",page.getHtml().xpath("map [@name='MapAcedaohang']/area/@href").all());
        //FindX2系列
        page.putField("FindX2",page.getHtml().xpath("map [@name='MapRdaohang']/area/@href").all());
        //爆款K5
        page.putField("K5",page.getHtml().xpath("map [@name='MapKKdaohang']/area/@href").all());
        //A系列
        page.putField("A",page.getHtml().xpath("map [@name='Mapadaohang']/area/@href").all());
    }

    private void doVivo(Page page) {
        System.out.println("京东vivo官方旗舰店");
        //X系列
        page.putField("X系列",page.getHtml().xpath("div [@class='nav abs']/div[1]//div [@class='rel']//a/@href").all());
        //iQOO
        page.putField("iQOO",page.getHtml().xpath("div [@class='nav abs']/div[2]//div [@class='rel']//a/@href").all());
        //S系列
        page.putField("S系列",page.getHtml().xpath("div [@class='nav abs']/div[3]//div [@class='rel']//a/@href").all());
        //Z/U系列
        page.putField("Z/U系列",page.getHtml().xpath("div [@class='nav abs']/div[4]//div [@class='rel']//a/@href").all());
        //Y系列
        page.putField("Y系列",page.getHtml().xpath("div [@class='nav abs']/div[5]//div [@class='rel']//a/@href").all());
        //NEX系列
        page.putField("NEX系列",page.getHtml().xpath("div [@class='nav abs']/div[6]//div [@class='rel']//a/@href").all());

    }

    private void doApple(Page page) {
        System.out.println("京东苹果旗舰店");
        //iPhone
        page.putField("iPhone",page.getHtml().xpath("div [@class='user-fn-clear sh-head-menu-black-624326']/div/ul/li[4]/div/div/ul//li//a/@href").all());
    }

    private void doHonor(Page page) {
        System.out.println("京东荣耀旗舰店");
        //V系列
        page.putField("V",page.getHtml().xpath("map [@id='V系列']//area/@href").all());
        //HONOR系列
        page.putField("HONOR",page.getHtml().xpath("map [@id='honorxi']//area/@href").all());
        //X系列
        page.putField("X",page.getHtml().xpath("map [@id='X系列']//area/@href").all());
        //Play系列
        page.putField("Play",page.getHtml().xpath("map [@id='Play系列']//area/@href").all());
    }

    private void doMi(Page page) {
        System.out.println("京东小米旗舰店");
        //店内导航条,页面代码重复，结果重复
        //小米手机，导航条第三
        page.putField("小米",page.getHtml().xpath("ul [@class='user-nav-list']//li[3]/div/div/div/a/@href").all());
        //Redmi手机，导航第四
        page.putField("Redmi",page.getHtml().xpath("ul [@class='user-nav-list']//li[4]/div/div/div/div/a/@href").all());

    }

    private void doHuawei(Page page) {
        System.out.println("京东华为官方旗舰店");
        //店内导航条
        //P系列
        page.putField("P", page.getHtml().xpath("map [@id='Mapdhpxl']//area/@href").all());
        //Mate系列
        page.putField("Mate", page.getHtml().xpath("map [@id='Mapzhp']//area/@href").all());
        //nova系列
        page.putField("nova", page.getHtml().xpath("map [@id='Mapdhnova']//area/@href").all());
        //麦芒/畅享系列
        page.putField("麦芒/畅享", page.getHtml().xpath("map [@id='Mapdhcx']//area/@href").all());
    }

    public Site getSite() {
        return site;
    }
}
