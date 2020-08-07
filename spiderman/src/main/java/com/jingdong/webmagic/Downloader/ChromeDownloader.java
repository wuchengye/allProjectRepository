package com.jingdong.webmagic.Downloader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;
import java.io.File;
import java.io.IOException;

public class ChromeDownloader extends AbstractDownloader {
    private static Logger logger = LoggerFactory.getLogger(ChromeDownloader.class);
    private static ChromeDriverService service;

    @Override
    public Page download(Request request, Task task){
        logger.info("page downloading " + request.getUrl());
        Page page;
        try {
            WebDriver driver = getChromeDriver();
            driver.get(request.getUrl());
            Thread.sleep(1000 * 7);

            String html = driver.getPageSource();

            if (html.contains("HTTP request failed")) {
                page = new Page();
                page.setRequest(request);
                return page;
            }

            page = new Page();
            page.setRawText(html);
            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
            page.setStatusCode(200);
            driver.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            page = new Page();
            page.setRequest(request);
            return page;
        }
        return page;
    }

    @Override
    public void setThread(int i) {

    }

    public WebDriver getChromeDriver() throws IOException {
        if(service == null){
            //System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
            //service = new ChromeDriverService.Builder().usingDriverExecutable(new File("D:\\All Downloads\\chromedriver.exe")).usingAnyFreePort().build();
            System.setProperty("webdriver.chrome.driver","/opt/google/chrome/google-chrome");
            service = new ChromeDriverService.Builder().usingDriverExecutable(new File("/home/jingdongpachong/chromedriver")).usingAnyFreePort().build();
        }
        if(!service.isRunning()){
            service.start();
        }
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");//centos7配置
        chromeOptions.addArguments("--headless");
        //chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
        chromeOptions.addArguments("--user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return new RemoteWebDriver(service.getUrl(), capabilities);
    }

}
