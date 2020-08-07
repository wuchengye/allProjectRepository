/*
package com.jingdong.webmagic.PageProcessor;

import com.jingdong.webmagic.Downloader.ChromeDownloader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.IOException;

public class TmallDetailPageProcessor implements PageProcessor {
    private static Logger logger = LoggerFactory.getLogger(TmallDetailPageProcessor.class);
    private Site site = Site
            .me()
            .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
            .setRetryTimes(3);

    private static ChromeDriverService service;

    @Override
    public void process(Page page) {
        try {
            WebDriver driver = getChromeDriver();
            driver.manage().window().maximize();
            driver.get(page.getUrl().get());
            Thread.sleep(7 * 1000);
            //关闭登录弹窗
            try {
                driver.findElement(By.id("sufei-dialog-close")).click();
            }catch (Exception e) {

            }
            Thread.sleep(7 * 1000);
            page.setRawText(driver.getPageSource());
            //初始化页面，将默认已勾选的取消勾选
            //机身颜色
            System.out.println("1" + page.getHtml().xpath("ul [@data-property='机身颜色']/li [@class='tb-selected']"));
            if(page.getHtml().xpath("ul [@data-property='机身颜色']/li [@class='tb-selected']").toString() != null){
                try {
                    driver.findElement(By.xpath("//ul [@data-property='机身颜色']/li [@class='tb-selected']")).click();
                }catch (Exception e){
                    System.out.println(page.getUrl().get());
                    driver.findElement(By.xpath("//ul [@data-property='机身颜色']/li [@class='tb-txt tb-selected']")).click();
                }
            }
            //存储容量
            System.out.println("2" + page.getHtml().xpath("ul [@data-property='存储容量']/li [@class='tb-selected']"));
            if(page.getHtml().xpath("ul [@data-property='存储容量']/li [@class='tb-selected']").toString() != null){
                driver.findElement(By.xpath("//ul [@data-property='存储容量']/li [@class='tb-selected']")).click();
            }
            //套餐类型
            System.out.println("3" + page.getHtml().xpath("ul [@data-property='套餐类型']/li [@class='tb-selected']"));
            if(page.getHtml().xpath("ul [@data-property='套餐类型']/li [@class='tb-selected']").toString() != null){
                driver.findElement(By.xpath("//ul [@data-property='套餐类型']/li [@class='tb-selected']")).click();
            }
            //初始化完成
            //选择网络类型
            if(page.getHtml().xpath("ul [@data-property='网络类型']/li/@class").toString() != null && !page.getHtml().xpath("ul [@data-property='网络类型']/li/@class").toString().equals("tb-selected")){
                driver.findElement(By.xpath("//ul [@data-property='网络类型']/li")).click();
            }
            //选择机身颜色
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public WebDriver getChromeDriver() throws IOException {
        if(service == null){
            System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
            service = new ChromeDriverService.Builder().usingDriverExecutable(new File("D:\\All Downloads\\chromedriver.exe")).usingAnyFreePort().build();
            //System.setProperty("webdriver.chrome.driver","/opt/google/chrome/google-chrome");
            //service = new ChromeDriverService.Builder().usingDriverExecutable(new File("/home/jingdongpachong/chromedriver")).usingAnyFreePort().build();
        }
        if(!service.isRunning()){
            service.start();
        }
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--no-sandbox");//centos7配置
        chromeOptions.addArguments("--headless");
        //chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
        chromeOptions.addArguments("--user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return new RemoteWebDriver(service.getUrl(), capabilities);
    }
}
*/
