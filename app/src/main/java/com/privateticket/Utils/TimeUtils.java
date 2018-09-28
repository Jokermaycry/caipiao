package com.privateticket.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by daguye on 16/6/1.
 */

public class TimeUtils {

    /**
     * 精确到秒
     * @param time
     * @return
     */
    public static String formatTimeSecond(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return t1;
    }
    /**
     * 精确到分
     * @param time
     * @return
     */
    public static String formatTimeMinute(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return t1;
    }
    /**
     * 精确到天
     * @param time
     * @return
     */
    public static String formatDay(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return t1;
    }

    /**
     * 只显示时间不显示日期
     * @param time
     * @return
     */
    public static String formatOnlyTime(long time){
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return t1;
    }
}
