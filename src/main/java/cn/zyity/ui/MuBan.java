package cn.zyity.ui;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 创建包含坐标系的模板图片
 */
public class MuBan {
    /*
    创建图片模板：
    纵轴 foF2 单位长度表示0.1(foF2保留一位小数)
    横轴 时间 单位长度表示7.5分钟 ，00:00-24:00
     */
    //    y轴一大分度分成小分度的个数
    public static final int Y_M = 100;
    //    x轴一大分度分成小分度的个数
    public static final int X_M = 24;

    //    y的最大值
    public static final int N_Y = 6;
    //    x的最大值
    public static final int N_X = 31;
    //    y轴的单位长度
    public static final int SMALL_Y = 2;
    //    x轴的单位长度
    public static final int SMALL_X = 3;
    //    坐标轴距离图片边部的像素值
    public static final int PADDING = 100;
    //    y轴最大值距离y轴顶部的像素值
    public static final int LEFT_Y = 25;
    //    x轴最大值距离x轴右部的像素值
    public static final int LEFT_X = 25;
    //    图片宽高
    public static int H = PADDING * 2 + LEFT_Y + SMALL_Y * Y_M * N_Y;
    public static int W = PADDING * 2 + LEFT_X + SMALL_X * X_M * N_X;
    private static final int FONT_SIZE = 22;
    public static final String MUBAN_IMG_DIR = Main.PROJECT_ROOT + "/mdata/muban";//模板图路径
    public static final String MUBAN_IMG_NAME ="dst_muban.jpeg";                  //模板图名称
    @Test
    public  void testMethod(){
            createMuBan();
    }
    /**
     * 创建模板图片
     */

    public static void createMuBan() {
        File muban_dir = new File(MUBAN_IMG_DIR);
        if (!muban_dir.exists()) {
            muban_dir.mkdirs();
        }

//*********************************************创建白色背景图**********************************************
        BufferedImage bufferedImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);// 抗锯齿
        graphics.setPaint(new Color(255, 255, 255));
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
//*********************************************画坐标系***************************************************
//          设置画笔黑色
        graphics.setPaint(new Color(0, 0, 0));
        graphics.setStroke(new BasicStroke(2.0f));
//***************************************画x,y轴******************************************
//              计算y轴长度
        final int Y = N_Y * Y_M * SMALL_Y + LEFT_Y;
//              画出y轴
        graphics.drawLine(PADDING, PADDING, PADDING, Y + PADDING);
        //      计算x轴长度
        final int X = N_X * X_M * SMALL_X + LEFT_X;

//              画出x轴
        graphics.drawLine(PADDING, PADDING + Y, PADDING + X, Y + PADDING);
//              画出y轴箭头
        graphics.drawLine(PADDING, PADDING, PADDING - 5, PADDING + 5);
        graphics.drawLine(PADDING, PADDING, PADDING + 5, PADDING + 5);
//        标注y轴两头的数值
        graphics.setFont(new Font("宋体", Font.BOLD, FONT_SIZE));
        graphics.drawString("Dst/nT", PADDING - 20, PADDING - 5);
        graphics.setFont(new Font("楷体", Font.BOLD, FONT_SIZE));
        graphics.drawString("-500", PADDING - 60, PADDING + Y + 5);
//              画出x轴箭头
        graphics.drawLine(PADDING + X, Y + PADDING, PADDING + X - 5, PADDING + Y - 5);
        graphics.drawLine(PADDING + X, Y + PADDING, PADDING + X - 5, PADDING + Y + 5);
        graphics.setFont(new Font("宋体", Font.BOLD, FONT_SIZE));
//        标注x轴右侧数值
        graphics.drawString("Day/UT", PADDING + X + 5, Y + PADDING + 7);
//***************************************画刻度与等值线**************************************
//***********************画x轴的刻度与x等值线**********************
        graphics.setStroke(new BasicStroke(1.0f));
        graphics.setFont(new Font("楷体", Font.BOLD, FONT_SIZE));
        int y = PADDING + Y;
        int i = 1;
        int offset_x;   //x刻度值偏移量，使其居中
        for (int x = PADDING + X_M * SMALL_X / 2; x <= PADDING + X; x = x + SMALL_X * X_M / 2) {
//              画刻度
            if ((x - PADDING) % ((X_M * SMALL_X / 2) * 2) == 0) {
                //              大刻度位置画等值线
                graphics.setPaint(Color.LIGHT_GRAY);
                graphics.drawLine(x, y, x, y - SMALL_Y * Y_M * N_Y);
                graphics.setPaint(Color.BLACK);
            } else {
                if (i < 10) {
                    offset_x = -FONT_SIZE / 2 / 2;
                } else {
                    offset_x = -FONT_SIZE / 2;
                }
                graphics.drawString(i + "", x+offset_x, y + 20);
                i++;
            }
        }
//***********************画y轴的刻度与y等值线**********************
        graphics.setFont(new Font("楷体", Font.BOLD, FONT_SIZE));
        int j = -450;
        for (y = y - SMALL_Y * Y_M / 2; y >= PADDING + LEFT_Y; y -= SMALL_Y * Y_M / 2) {
            if (j == 0) {
                graphics.drawString(j + "", PADDING - 40, y + 6);
            } else {
                graphics.drawString(j + "", PADDING - 60, y + 6);
            }
//            画等值线
            graphics.setPaint(Color.lightGray);
            graphics.drawLine(PADDING, y, PADDING + X - LEFT_X, y);
            graphics.setPaint(Color.BLACK);
            j += 50;
        }
//*******************************************保存模板图片*************************************************
        try {
            ImageIO.write(bufferedImage, "jpeg", new File(MUBAN_IMG_DIR + "/"+MUBAN_IMG_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
