package com.ailide.apartmentsabc.tools;

import android.os.Environment;

import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class ReadUtil {

    public static String readSDFile(String fileName){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return null;
        }
        String SDPATH = Environment.getExternalStorageDirectory().getPath();
        String res=null;
        File file = new File(SDPATH + "/BXW/" + fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            /* 准备一个字节数组用户装即将读取的数据 */
            byte[] buffer = new byte[fis.available()];

            /* 开始进行文件的读取 */
            fis.read(buffer);
            fis.close();
            /* 将字节数组转换成字符创， 并转换编码的格式 */
            res = EncodingUtils.getString(buffer, "UTF-8");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
    public static String getNewContent(String htmltext){
        try {
            Document doc= Jsoup.parse(htmltext);
            Elements elements=doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width","100%").attr("height","auto");
            }

            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }
}
