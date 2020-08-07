package com.jingdong.webmagic.PageProcessor;

import com.jingdong.webmagic.Utils.CommonUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestPageProcessor implements PageProcessor {
    Site site = Site
            .me()
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0")
            .setRetryTimes(3);

    @Override
    public void process(Page page) {
        WebDriver driver = null;
        try {
            driver = getChromeDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.get(page.getRequest().getUrl());

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String ItemId = CommonUtils.splitUrl(page.getUrl().get());
        System.out.println("price=============="+page.getHtml().xpath("span [@class='price J-p-" + ItemId + "']/text()"));
        if (page.getHtml().xpath("span [@class='pricing']").toString()==null){
            System.out.println("初始价格null");
        }

        List<String> list = page.getHtml().xpath("div [@id='summary-quan']/div [@class='dd']//span [@class='quan-item']/span/text()").all();
        System.out.print("youhuiquan=========");
        int i = 1;
        for (String s : list){
            System.out.print(i + s);
            i++;
        }
        i=1;
        System.out.println(" ");
        List<String> list1 = page.getHtml().xpath("div [@id='J-summary-top']//div [@class='dd J-prom-wrap p-promotions-wrap']//ins [@id='prom-gift']//a/@title").all();
        System.out.print("gift===============");
        for (String s: list1){
            System.out.print(i + s);
            i++;
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public WebDriver getChromeDriver() throws IOException {
        System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(new File("D:\\All Downloads\\chromedriver.exe")).usingAnyFreePort().build();
        service.start();
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return new ChromeDriver(service, capabilities);
    }
}
