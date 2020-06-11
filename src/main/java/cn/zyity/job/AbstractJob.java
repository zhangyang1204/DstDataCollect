package cn.zyity.job;

import cn.zyity.utils.LogUtils;

/**
 */
public abstract class AbstractJob implements IJob {

    public void beforeRun() {
    }

    public void afterRun() {
    }


    @Override
    public void run() {
        this.beforeRun();


        try {
            this.doFetchPage();
        } catch (Exception e) {
            LogUtils.printLog("访问请求被拒绝");
            LogUtils.printException(e);
        }


        this.afterRun();
    }


    /**
     * 具体的抓去网页的方法， 需要子类来补全实现逻辑
     *
     * @throws Exception
     */
    public abstract void doFetchPage() throws Exception;
}
