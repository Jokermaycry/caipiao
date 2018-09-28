package com.ailide.apartmentsabc.tools;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 */

public class TimeUtils {

    public static String formatTime(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return t1;
    }
    public static String formatTimeMinute(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return t1;
    }
    public static String formatDate(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return t1;
    }
    
    public static String formatOnlyTime(long time){
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return t1;
    }
    public static String formatShiFen(long time){
        SimpleDateFormat format=new SimpleDateFormat("MM-dd HH:mm");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return t1;
    }
    public static String getCurrentTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        Date d = new Date();
        return sdf.format(d);
    }
    public static String getCurrentTimeDay(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//24小时制
        Date d = new Date();
        return sdf.format(d);
    }
    public static Long getNowLongDate(){//获取当前时间戳
//		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
//		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return System.currentTimeMillis();
    }
    /**
     * 判断当前系统时间是否在指定时间的范围内
     *
     * @param beginHour
     *            开始小时，例如22
     * @param beginMin
     *            开始小时的分钟数，例如30
     * @param endHour
     *            结束小时，例如 8
     * @param endMin
     *            结束小时的分钟数，例如0
     * @return true表示在范围内，否则false
     */public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin)
    {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if(!startTime.before(endTime)) // 跨天的特殊情况（比如22:00-8:00）
        {
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if(!now.before(startTimeInThisDay))
            {
                result = true;
            }
        }else//普通情况(比如 8:00 - 14:00)
        {
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }

    //年月日转成时间戳
    public static Long timeToLong(String timeDate){
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd" );
        String time=timeDate;
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String timeToLongDateTime(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14 16:09:00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static long timeToLongDateTime2(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date = null;
        try {
            date = sdr.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    public static String formatDate2(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd");
        Date d1=new Date(time);
        String t1=format.format(d1);
        
        return t1;
    }
    public static Integer getNowYear(){
        long time= System.currentTimeMillis();
        final Calendar mCalendar= Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int year = t.year;
//        int month = t.month;
//        int date = t.monthDay;
//        int hour = t.hour;    // 0-23
        return year;
    }

	public static String getCurrentTime2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");//24小时制
		Date d = new Date();
		return sdf.format(d);
	}
	
	public static String formatTime2(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date d1 = new Date(time);
		String t1 = format.format(d1);
		return t1;
	}
	
	public static String formatTime3(long time) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		Date d1 = new Date(time);
		String t1 = format.format(d1);
		return t1;
	}
	
	public static String stringGetWeek(String time) {
		Calendar cal = Calendar.getInstance();
		String week="";
		int i = -1;
// 对 calendar 设置时间的方法
// 设置传入的时间格式
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
// 指定一个日期
			Date date;
			date = dateFormat.parse(time);
			cal.setTime(date);
			i = cal.get(Calendar.DAY_OF_WEEK);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		switch (i){
			case 1:
				week="周日";
				break;
			case 2:
				week="周一";
				break;
			case 3:
				week="周二";
				break;
			case 4:
				week="周三";
				break;
			case 5:
				week="周四";
				break;
			case 6:
				week="周五";
				break;
			case 7:
				week="周六";
				break;
			
		}
		return week;
	}
    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            if(year1<year2){
                int timeDistance = 0 ;
                for(int i = year1 ; i < year2 ; i ++)
                {
                    if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                    {
                        timeDistance += 366;
                    }
                    else    //不是闰年
                    {
                        timeDistance += 365;
                    }
                }

                return timeDistance + (day2-day1) ;
            }else {
                int timeDistance = 0 ;
                for(int i = year2 ; i < year1 ; i ++)
                {
                    if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                    {
                        timeDistance += 366;
                    }
                    else    //不是闰年
                    {
                        timeDistance += 365;
                    }
                }

                return -(timeDistance + (day1-day2)) ;
            }

        }
        else    //不同年
        {
            return day2-day1;
        }
    }
    public static Date getNowDate(){
        Date curDate =  new Date(System.currentTimeMillis());
        return curDate;
    }
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
}
