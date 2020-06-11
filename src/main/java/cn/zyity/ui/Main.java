package cn.zyity.ui;

import cn.zyity.service.DstDataCrawlService;
import cn.zyity.service.ExcelService;
import cn.zyity.service.ImgService;
import cn.zyity.utils.LogUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import static cn.zyity.ui.MyJframe.FRAME_NAME;

/*
 * 程序启动类
 * */
public class Main {
    public static final String PROJECT_ROOT = new File("").getAbsolutePath();//项目根目录
    public static final String DST_IMG_DIR = PROJECT_ROOT + "/数据/img";              //dst保存图片路径
    public static final String DISPLAY_IMG_DIR = PROJECT_ROOT + "/mdata/temp";       //临时图片路径
    public static final String DISPLAY_IMG_NAME = "displayImg.jpeg";                 //临时图片名称

    private static MyJframe myJframe;
    private DstDataCrawlService ddcService;
    private ExcelService excelService;
    private ImgService imgService;

    public static boolean isUpdated = false;//数据是否已更新
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        Main main = new Main();
        main.init();
        main.run();
    }

    public  void init() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        myJframe = context.getBean("myJframe", MyJframe.class);
        excelService = context.getBean("excelService", ExcelService.class);
        imgService = context.getBean("imgService", ImgService.class);
        ddcService = context.getBean("ddcService", DstDataCrawlService.class);
    }

    public void run() {
        String dstDate;            //抓取数据中的日期 （格式：2020_05）
        ArrayList<String[]> lines; //dst数据 （string数组中存放的是一天中的所有数据）

        while (true) {
//*****************************************判断网络状态**********************************************
            boolean netOK = true;
            while (true) {
                try {
                    URL url = new URL("http://baidu.com/");
                    InputStream in = url.openStream();
                    in.close();
                    break;
                } catch (IOException e) {
                    if (netOK) {
                        LogUtils.printLog("网络异常");
                        myJframe.setTitle(FRAME_NAME + "       [网络异常,请检查您的网络]");
                        netOK = false;
                    }
                    System.gc();
                    System.gc();
                    System.gc();
                    System.gc();
                    System.gc();
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException ex) {
                        LogUtils.printException(e);
                    }
                }
            }
            if (!netOK) {
//                网络发生异常后恢复
                LogUtils.printLog("网络恢复，程序正常运行");
            }
//***************************************************************************************************
            myJframe.setTitle(FRAME_NAME + "     数据抓取中..." + getNowTime());
//*****************************************抓取数据并进行处理，保存，绘制*****************************************
            Map<String, String> res = ddcService.crawlData();

            if (res==null) {
//******************************抓取失败则重新尝试******************************
                int i = 0;
                while (i < 15) {
                    i++;
                    LogUtils.printLog("数据抓取失败,重试中...[" + i + "/15]");
                    myJframe.setTitle(FRAME_NAME + "     数据抓取失败,重试中...(" + i + "/15)" + getNowTime());
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        LogUtils.printException(e);
                    }
                    res = ddcService.crawlData();
                    if (res!=null) {
                        LogUtils.printLog("数据抓取成功！");
                        break;
                    }
                }
            }

            if (res == null) {
//******************************数据抓取失败******************************
                LogUtils.printLog("数据抓取失败");
                myJframe.setTitle(FRAME_NAME + "     数据抓取失败" + getNowTime());
            } else {
//******************************数据抓取成功******************************
                String dstText = res.get("dstText");
                lines = excelService.getHandledDstData(dstText);
                dstDate = res.get("dstDate");

                excelService.saveDataToExcel(lines, dstDate);

                myJframe.setTitle(FRAME_NAME + "     Dst数据图像绘制中..." + getNowTime());
                BufferedImage img = imgService.createImg(dstDate, lines);//创建展示图
                imgService.saveImg(img, DST_IMG_DIR, dstDate + "_Dst_RT.jpeg");//保存展示图
                imgService.saveImg(img, DISPLAY_IMG_DIR, DISPLAY_IMG_NAME);//将其另存到一个固定路径，Jpanel通过该路径访问
                isUpdated = true;
                myJframe.repaint();

                myJframe.setTitle(FRAME_NAME + "      图像已更新,程序休眠中..." + getNowTime());
            }
//********************************任务完成，等待下次执行********************************
            System.gc();
            System.gc();
            System.gc();
            System.gc();
            System.gc();
            try {
                Thread.sleep(60 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String getNowTime() {
        return " [ last update at " + dtf.format(LocalTime.now()) + " (local time) ]";
    }

}
