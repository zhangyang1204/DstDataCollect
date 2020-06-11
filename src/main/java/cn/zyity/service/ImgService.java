package cn.zyity.service;

import cn.zyity.ui.MuBan;
import cn.zyity.utils.LogUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class ImgService {

    public void saveImg(BufferedImage img, String dir, String name) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            ImageIO.write(img, "jpeg", new File(dir + "/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取dst数据，生成可视化图像
     *
     * @param date
     * @param lines
     * @return
     */
    public static BufferedImage createImg(String date, ArrayList<String[]> lines) {
//        模板图片不存在，创建模板图
        File file = new File(MuBan.MUBAN_IMG_DIR + "/" + MuBan.MUBAN_IMG_NAME);
        if (!file.exists()) {
            MuBan.createMuBan();
        }
//        读取模板图
        BufferedImage muban_img = null;
        try {
            muban_img = ImageIO.read(file);
        } catch (IOException e) {
            LogUtils.printException(e);
        }
        BufferedImage bufferedImage = new BufferedImage(muban_img.getWidth(), muban_img.getHeight(), muban_img.getType());
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(muban_img, 0, 0, muban_img.getWidth(), muban_img.getHeight(), null);
        graphics.setPaint(Color.green);
        // 抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        1.画标题（日期）
        int fontsize = 30;
        graphics.setFont(new Font("宋体", Font.BOLD, fontsize));
        graphics.drawString(date.replace("_", "-"), MuBan.W / 2, 50);
//        2.画数据
        int x;//表示横坐标
        int y;//表示纵坐标
        int[] preZuobiao = null;//表示上一个点坐标
        int size = lines.size();
        for (int i = 0; i < size; i++) {
//            遍历出每天的数据，日期 day = i + 1;
            String[] values = lines.get(i);
            int len = values.length;
            for (int j = 1; j < len; j++) {
//                取出当天的所有值，小时 h = j + 1;
                String string_val = values[j];
                if (string_val == null || "".equals(string_val)) {
                    preZuobiao = null;
                    continue;
                }
                int value = Integer.parseInt(string_val.replace(" ", ""));
//                计算坐标
                x = MuBan.PADDING + (i * MuBan.X_M + j) * MuBan.SMALL_X;
                y = MuBan.PADDING + MuBan.LEFT_Y + MuBan.Y_M * MuBan.SMALL_Y - value * MuBan.SMALL_Y;
//                画出该点
                graphics.fillRect(x - 1, y - 1, 2, 2);
//                与上一个坐标连线
                if (preZuobiao != null) {
                    graphics.drawLine(preZuobiao[0], preZuobiao[1], x, y);
                }
//                更新preZuobiao
                preZuobiao = new int[]{x, y};
            }
        }
        return bufferedImage;
    }
}
