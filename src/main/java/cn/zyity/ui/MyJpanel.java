package cn.zyity.ui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyJpanel extends JPanel {
    private BufferedImage im;
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        int width = getWidth();
        int height = getHeight();
        g.fillRect(0,0,width, height);
        updateImg();
        if (im==null){
            return;
        }
        BufferedImage temp = getScaledInstance(im, width, height);
        g.drawImage(temp,0,0,width,height,null);
    }

    private void updateImg() {
        if (im!=null&& !Main.isUpdated){
            return;
        }
        try {
            im= ImageIO.read(new File(Main.DISPLAY_IMG_DIR+"/"+ Main.DISPLAY_IMG_NAME));
        } catch (IOException e) {
            return;
        }
        Main.isUpdated=false;
    }

    public static BufferedImage getScaledInstance(BufferedImage var0, int var1, int var2) {
        int var3 = var0.getTransparency() == 1 ? 1 : 2;
        BufferedImage var4 = new BufferedImage(var1, var2, var3);
        Graphics2D var5 = var4.createGraphics();
        var5.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        var5.drawImage(var0, 0, 0, var1, var2, null);
        var5.dispose();
        return var4;
    }
}
