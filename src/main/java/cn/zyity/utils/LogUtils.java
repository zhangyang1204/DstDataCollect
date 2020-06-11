package cn.zyity.utils;

import cn.zyity.ui.Main;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@Aspect
public class LogUtils {
    public static final String LOG_DIR = Main.PROJECT_ROOT + "/mdata/log";
    public static final String LOG_NAME = "mylog.txt";
    public static final String EXCEPTION_NAME = "exception.txt";
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void printException(Exception e) {
        File file = new File(LOG_DIR + "/" + dateFormatter.format(LocalDate.now()));
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            FileWriter fileWriter = new FileWriter(LOG_DIR + "/" + dateFormatter.format(LocalDate.now()) + "/" + EXCEPTION_NAME, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.write("\n" + timeFormatter.format(LocalTime.now()) + "\n");
            e.printStackTrace(printWriter);
            printWriter.flush();
            printWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void printLog(String logMsg) {
        File file = new File(LOG_DIR + "/" + dateFormatter.format(LocalDate.now()));
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            FileWriter fileWriter = new FileWriter((LOG_DIR + "/" + dateFormatter.format(LocalDate.now()) + "/" + LOG_NAME), true);
            fileWriter.write("\n" + timeFormatter.format(LocalTime.now()) + "\n" + logMsg + "\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Around("execution(* cn.zyity.service.*.*(..))")
    public Object logAroung(ProceedingJoinPoint pjp) {
        Object rtValue;
        try {
            rtValue = pjp.proceed(pjp.getArgs());
            return rtValue;
        } catch (Throwable throwable) {
            printLog("发生未知异常(来自service的连接点),请检查异常日志");
            Exception e = (Exception) throwable;
            printException(e);
            throw new RuntimeException();
        }finally {
//            最终通知
        }
    }
}
