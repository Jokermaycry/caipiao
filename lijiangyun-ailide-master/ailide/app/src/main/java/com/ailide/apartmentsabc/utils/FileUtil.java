package com.ailide.apartmentsabc.utils;


import android.os.Environment;

import com.ailide.apartmentsabc.tools.Contants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {

    public static File getStickerFile() {
        File stickerDir = new File(Contants.PATH_STICKER);
        if (!stickerDir.exists()) {
            stickerDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
        String timeStamp = simpleDateFormat.format(new Date());
        String path = Contants.PATH_STICKER + File.separator + timeStamp + ".png";
        return new File(path);
    }

    public static File getLabelFile() {
        File stickerDir = new File(Contants.PATH_LABEL);
        if (!stickerDir.exists()) {
            stickerDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
        String timeStamp = simpleDateFormat.format(new Date());
        String path = Contants.PATH_LABEL + File.separator + timeStamp + ".png";
        return new File(path);
    }

    public static File getStickerDraftFile(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
        String timeStamp = simpleDateFormat.format(time);
        File stickerDir = new File(Contants.PATH_STICKER_DRAFT + File.separator + timeStamp);
        if (!stickerDir.exists()) {
            stickerDir.mkdirs();
        }
        String path = Contants.PATH_STICKER_DRAFT + File.separator + timeStamp + File.separator + timeStamp + ".png";
        return new File(path);
    }

    public static File getLabelDraftFile(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
        String timeStamp = simpleDateFormat.format(time);
        File stickerDir = new File(Contants.PATH_LABEL_DRAFT + File.separator + timeStamp);
        if (!stickerDir.exists()) {
            stickerDir.mkdirs();
        }
        String path = Contants.PATH_LABEL_DRAFT + File.separator + timeStamp + File.separator + timeStamp + ".png";
        return new File(path);
    }

    public static File getStickerDraftStickerFile(long draftTime, long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
        String draftTimeStamp = simpleDateFormat.format(draftTime);
        String timeStamp = simpleDateFormat.format(time);
        File stickerDir = new File(Contants.PATH_STICKER_DRAFT + File.separator + draftTimeStamp);
        if (!stickerDir.exists()) {
            stickerDir.mkdirs();
        }
        String path = Contants.PATH_STICKER_DRAFT + File.separator + draftTimeStamp + File.separator + timeStamp + ".png";
        return new File(path);
    }

    public static File getBillFile() {
        File billDir = new File(Contants.PATH_BILL);
        if (!billDir.exists()) {
            billDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
        String timeStamp = simpleDateFormat.format(new Date());
        String path = Contants.PATH_BILL + File.separator + timeStamp + ".png";
        return new File(path);
    }

    public static File getWebFile() {
        File billDir = new File(Contants.PATH_BILL);
        if (!billDir.exists()) {
            billDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
        String timeStamp = simpleDateFormat.format(new Date());
        String path = Contants.PATH_BILL + File.separator + timeStamp + ".png";
        return new File(path);
    }

    public static void writeLog(String content) {
        System.out.println("+++++++++++++++++++");
        System.out.println(content);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            String file = Environment.getExternalStorageDirectory().getPath() + "/testLog.txt";
            FileWriter writer = new FileWriter(file, true);
            writer.write(sdf.format(System.currentTimeMillis())+"\n");
            writer.write(content+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}