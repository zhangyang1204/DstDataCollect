package cn.zyity.service;

import cn.zyity.entity.CrawlMeta;
import cn.zyity.entity.CrawlResult;
import cn.zyity.job.SimpleCrawlJob;
import cn.zyity.utils.LogUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ddcService")
public class DstDataCrawlService {
    private static final String DST_DATA_URL = "http://wdc.kugi.kyoto-u.ac.jp/dst_realtime/presentmonth/index.html";
    private static final String DST_SELECTOR = "pre[class=data]";
    /**
     * @return 抓取所需数据
     */
    public Map<String, String> crawlData() {
        Set<String> selectRule = new HashSet<>();
        selectRule.add(DST_SELECTOR); // dst数据
        selectRule.add("span"); // 包含日期的数据
        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(DST_DATA_URL);
        crawlMeta.setSelectorRules(selectRule);
        SimpleCrawlJob job = new SimpleCrawlJob();
        job.setCrawlMeta(crawlMeta);
        Thread thread = new Thread(job);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            LogUtils.printLog("发生InterruptedException");
            LogUtils.printException(e);
        }

        CrawlResult result = job.getCrawlResult();
        if (result.getStatus() == CrawlResult.SUCCESS) {
//            抓取成功
            String dstText = result.getResult().get(DST_SELECTOR).get(0);
            String _dstDate = result.getResult().get("span").get(4);
            if (dstText.isEmpty() || _dstDate.isEmpty()) {
                return null;
            }
            String[] split = _dstDate.split(" ");
            String dstDate = split[2].substring(0, 7).replace("-", "_");
            HashMap<String, String> map = new HashMap<>();
            map.put("dstText", dstText);
            map.put("dstDate", dstDate);
            return map;
        }
        return null;
    }


}
