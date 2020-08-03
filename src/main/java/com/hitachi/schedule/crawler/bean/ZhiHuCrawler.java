package com.hitachi.schedule.crawler.bean;

import com.hitachi.schedule.config.common.GXConst;
import com.hitachi.schedule.crawler.param.ZhArticleParam;
import com.hitachi.schedule.crawler.param.ZhTitleParam;
import com.hitachi.schedule.service.GSABScheduleF;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class ZhiHuCrawler {

    @Autowired
    private GSABScheduleF gsabService;

    private Process process;
    private WebDriver driver;

    public void init() throws Exception {
        // browserをオープン
        this.process = doOpenProcess();
        Thread.sleep(5000);
        // seleniumでbrowserを管理
        this.driver = doGetChromeDriver();

    }

    public void destroy() {
        if (null != this.process) {
            process.destroy();
        }
        if (null != this.driver) {
            driver.quit();
        }
    }

    public void getBrowserInfoAndInsertDB() throws Exception {
        // processがない場合、戻す
        if (null == this.process)
            return;

        // article内容を展開
        doOpenAllByClass(GXConst.GSAX_CRAWLER_ZHIHU_ARTICLE_OPENALL_CLASS);
        // browser内容を読み込む
        Document doc = doGetDocument();
        // dbからtitle情報を取得 キー：titleName 値:titleId
        Map<String, Long> titleMap = gsabService.findAllTitleIdAndTitleName();
        // 転換後、browser情報マップ
        Map<String, ZhTitleParam> browserInfo = new HashMap<>();
        // すべてのarticleを取得
        Elements elements = doc.getElementsByClass("Card TopstoryItem TopstoryItem-isRecommend");
        for (Element element : elements) {
            // 未展開のarticleをスキップ
            if (element.getElementsByClass("Button ContentItem-more Button--plain").size() > 0) {
                continue;
            }
            // article内容を編集
            doEditContent(element);

            // article内容を格納
            ZhArticleParam articleParam = doGetZhArticleParam(element);

            // title情報を取得
            Element title = element.getElementsByClass("ContentItem-title").first();
            String titleName = title.text();

            if (browserInfo.containsKey(titleName)) {
                // 既に存在の場合、追加
                browserInfo.get(titleName).getArticleParams().add(articleParam);
            } else {
                // 存在しない場合、新規作成して、格納
                ZhTitleParam titleParam = doGetZhTitleParam(titleMap, title, titleName, articleParam);
                browserInfo.put(titleName, titleParam);
            }
        }

        // browser内容を処理
        gsabService.insertByCrawler(browserInfo.values());
    }

    private Process doOpenProcess() throws Exception {
        Runtime run = Runtime.getRuntime();
        Process process = run.exec(GXConst.ARGS + GXConst.ZHIHU_URL);
        return process;
    }

    private WebDriver doGetChromeDriver() {
        System.setProperty("webdriver.chrome.driver", GXConst.DRIVER);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        return new ChromeDriver(options);
    }

    private void doOpenAllByClass(String className) {
        List<WebElement> readAll = this.driver.findElements(
                By.cssSelector(className)
        );
        for (WebElement webElement : readAll) {
            doElementClick(webElement);
        }
    }


    private Document doGetDocument() {
        Document doc = Jsoup.parse(this.driver.getPageSource());
        return doc;
    }

    private void doEditContent(Element element) {
        // articleのbody
        Element content = element.getElementsByClass("RichContent-inner").first();
        // ビデオを除く
        content.getElementsByClass("ZVideoItem-player").remove();
        // イメージを転換
        for (Element obj : content.getElementsByTag("img")) {
            editImg(obj);
        }
        // リンクを転換
        for (Element obj : content.getElementsByTag("a")) {
            editHref(obj);
        }
        // イメージを転換
        Element image = element.getElementsByClass("ArticleItem-image").first();
        if (null != image) {
            editImg(image);
            content.prependChild(image);
        }
    }

    private ZhArticleParam doGetZhArticleParam(Element element) {
        // articleのbody
        Element content = element.getElementsByClass("RichContent-inner").first();
        ZhArticleParam articleParam = new ZhArticleParam();
        articleParam.setArticleContent(content.outerHtml());
        Element agreeElement = element.getElementsByClass("Button VoteButton VoteButton--up").first();
        articleParam.setArticleAgree(getAgree(agreeElement));
        return articleParam;
    }

    private ZhTitleParam doGetZhTitleParam(
            Map<String, Long> titleMap,
            Element title, String titleName,
            ZhArticleParam articleParam) {
        ZhTitleParam titleParam = new ZhTitleParam();
        Long titleId = titleMap.get(titleName);
        if (null != titleId) {
            titleParam.setTitleId(titleId);
        } else {
            Element titleHref = title.getElementsByTag("a").first();
            String titleContent = null;
            if (null != titleHref) {
                String titleHrefStr = titleHref.attr("href");
                if (!titleHrefStr.contains("zvideo")) {

                    if (titleHrefStr.startsWith("//")) {
                        titleHrefStr = "https:".concat(titleHrefStr);
                    }

                    doOpenUrl(titleHrefStr);
                    doOpenAllByClass(GXConst.GSAX_CRAWLER_ZHIHU_TITLE_OPENALL_CLASS);
                    Document doc = doGetDocument();
                    Element detail = doc.getElementsByClass("QuestionHeader-detail").first();
                    if (null != detail) {
                        titleContent = detail.text();
                    }
                }
            }
            titleParam.setTitleContent(titleContent);
            titleParam.setTitleName(titleName);
        }
        titleParam.getArticleParams().add(articleParam);
        return titleParam;
    }

    private void doOpenUrl(String url) {
        this.driver.get(url);
        this.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    private void doElementClick(WebElement webElement) {
        webElement.click();
        this.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }


    private void editImg(Element element) {
        String imgSrc = element.attr("src");
        removeAllAttr(element);
        element.attr("style", GXConst.IMG_STYLE);
        element.attr("src", imgSrc);
    }

    private void editHref(Element element) {
        String hrefStr = element.attr("href");
        removeAllAttr(element);
        element.attr("target", GXConst.HREF_TARGET);
        element.attr("href", hrefStr);
    }

    private void removeAllAttr(Element element) {
        List<Attribute> attributes = element.attributes().asList();
        for (Attribute attribute : attributes) {
            element.removeAttr(attribute.getKey());
        }
    }

    private long getAgree(Element element) {
        long result = 0;
        if (null == element) {
            return result;
        }
        String[] strArr = element.text().split(" ");
        if (strArr.length < 2) {
            return result;
        }

        double a = Double.parseDouble(strArr[1]);

        int b = 1;
        if (strArr.length > 2) {
            if ("万".equals(strArr[2])) {
                b = 10000;
            } else if ("千".equals(strArr[2])) {
                b = 1000;
            }
        }

        BigDecimal value = BigDecimal.valueOf(a * b);
        return value.longValue();
    }
}
