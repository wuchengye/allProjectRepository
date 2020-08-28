package com.jingdong.webmagic.Downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;

//在PageProcessor中进行下载，不在此处
public class FakeDownloader extends AbstractDownloader {
    private static Logger logger = LoggerFactory.getLogger(FakeDownloader.class);

    @Override
    public Page download(Request request, Task task) {
        logger.info("page downloading(fake) " + request.getUrl());
        Page page = new Page();
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        return page;
    }

    @Override
    public void setThread(int i) {

    }
}
