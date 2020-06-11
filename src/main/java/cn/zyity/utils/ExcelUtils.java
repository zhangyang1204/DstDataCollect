package cn.zyity.utils;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;


public class ExcelUtils {
    public static void createExcel(String path, String name, String[] keys) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(path + "/" + name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
//            创建一行
        XSSFRow row = sheet.createRow(0);
//            创建所有列
        for (int i = 0; i < keys.length; i++) {
            if (i==0){
//                将第一个留空
                row.createCell(0);
            }else {
                row.createCell(i).setCellValue(Double.parseDouble(keys[i]));
            }
        }
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void appendExcel(String path, String name, String[] line) {
        FileOutputStream os = null;
        FileInputStream is = null;
        XSSFWorkbook wb = null;
        try {
            is = new FileInputStream(path + "/" + name);
            wb = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = wb.getSheetAt(0);
        int totalRow = sheet.getPhysicalNumberOfRows();
        XSSFRow row = sheet.createRow(totalRow);
        for (int i = 0; i < line.length; i++) {
            String value = line[i];
            if (value == null || "".equals(value)) {
                value="9999";
            }
            row.createCell(i).setCellValue(Double.parseDouble(value));
        }
        try {
            os=new FileOutputStream(path+"/"+name);
            wb.write(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
