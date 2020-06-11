package cn.zyity.test;

import cn.zyity.entity.CrawlMeta;
import cn.zyity.entity.CrawlResult;
import cn.zyity.job.SimpleCrawlJob;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 */
public class BaseCrawlerTest {


    /**
     * 测试我们写的最简单的一个爬虫,
     *
     * 目标是爬取一篇博客
     */
    @Test
    public void testFetch() throws InterruptedException {
        String url = "http://wdc.kugi.kyoto-u.ac.jp/dst_realtime/presentmonth/index.html";
        Set<String> selectRule = new HashSet<>();
        selectRule.add("pre[class=data]"); // dst数据

        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url);
        crawlMeta.setSelectorRules(selectRule);


        SimpleCrawlJob job = new SimpleCrawlJob();
        job.setCrawlMeta(crawlMeta);
        Thread thread = new Thread(job, "crawler-test");
        thread.start();

        thread.join();


        CrawlResult result = job.getCrawlResult();
        System.out.println(result);
    }

}
