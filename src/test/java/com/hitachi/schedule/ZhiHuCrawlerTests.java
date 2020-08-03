package com.hitachi.schedule;

import com.hitachi.schedule.crawler.bean.ZhiHuCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZhiHuCrawlerTests {
    @Autowired
    ZhiHuCrawler zhiHuCrawler;

    @Test
    public void test01() {
        try {
            zhiHuCrawler.init();
            zhiHuCrawler.getBrowserInfoAndInsertDB();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zhiHuCrawler.destroy();
        }
    }
}
