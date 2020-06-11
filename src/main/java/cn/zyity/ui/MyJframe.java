package cn.zyity.ui;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import static cn.zyity.ui.Main.PROJECT_ROOT;

@Component
public class MyJframe extends JFrame {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int SCREEN_WIDTH = (int) SCREEN_SIZE.getWidth();//屏幕宽
    private static final int SCREEN_HEIGHT = (int) SCREEN_SIZE.getHeight();//屏幕高
    private static final int FRAME_WIDTH = (int) (SCREEN_WIDTH * 0.7); //窗口宽
    private static final int FRAME_HEIGHT = (int) (SCREEN_HEIGHT * 0.7); //窗口高

    public static final String FRAME_NAME = "Dst数据实时采集与显示软件";
    private static final String ICON_DIR = PROJECT_ROOT + "/mdata";
    private static final String ICON_NAME = "myIcon.png";


    public MyJframe() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        MyJpanel myJpanel = new MyJpanel();
        add(myJpanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(ICON_DIR + "/" + ICON_NAME).getImage());
        setVisible(true);
    }

}