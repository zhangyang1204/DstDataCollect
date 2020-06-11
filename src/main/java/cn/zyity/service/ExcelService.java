package cn.zyity.service;

import cn.zyity.ui.Main;
import cn.zyity.ui.MyJframe;
import cn.zyity.utils.ExcelUtils;
import cn.zyity.utils.LogUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class ExcelService {

    public static final String DST_EXCEL_DIR = Main.PROJECT_ROOT + "/数据/excel";
    public static final String EXCEL_LAST_NAME = "_Dst_RT.xlsx";//表格后缀名
    private static final String[] keys = new String[25];

    static {
        for (int i = 1; i < 25; i++) {
//            给excel表格头赋值
            keys[i] = String.valueOf(i);
        }
    }


    /**
     * 将数据保存至excel
     *
     * @param date：数据的日期，作为文件名的组成部分
     */
    public void saveDataToExcel( ArrayList<String[]> lines, String date) {
        String name = date + EXCEL_LAST_NAME;
        File file = new File(DST_EXCEL_DIR + "/" + name);
        if (file.exists()) {
//            表格存在，删除原表格
            file.delete();
        }
        ExcelUtils.createExcel(DST_EXCEL_DIR, name, keys);
        int size = lines.size();
        for (int i = 0; i < size; i++) {
            String[] line = lines.get(i);
            ExcelUtils.appendExcel(DST_EXCEL_DIR, name, line);
        }
    }
    /**
     *处理抓取的原始数据
     * @return list,其装载excel表的每一行数据
     */
    public  ArrayList<String[]> getHandledDstData(String dstText) {
        ArrayList<String[]> res = new ArrayList<>();
        String[] lines = dstText.split("(\n)+");
        int len = lines.length;
        for (int i = 6; i < len; i++) {
            String line = lines[i];
            String[] data = new String[25];
            data[0] = String.valueOf(i - 5);
            for (int j = 1; j < 25; j++) {
                int start = j * 4 - 1 + (j - 1) / 8;
                String dst_val = line.substring(start, start + 4);
                if (dst_val.equals("9999")) {
                    continue;
                }
                data[j] = dst_val;
            }
            res.add(data);
        }
        return res;
    }


}
