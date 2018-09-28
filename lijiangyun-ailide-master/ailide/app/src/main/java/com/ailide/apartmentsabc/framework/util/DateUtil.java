package com.ailide.apartmentsabc.framework.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by liwenguo on 2016/12/12.
 */

public class DateUtil {
	/**
	 * 计算某个日期与当前日期相差天数
	 *
	 * @param rentEndTime
	 * @return
	 */
	public static double getDateDifferent(String rentEndTime) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");//输入日期的格式
		String str1 = simpleDateFormat.format(new Date());
		String str2 = rentEndTime;
		if (rentEndTime.length() > 10) {
			str2 = rentEndTime.substring(0, 10).replace("-", "");  //"yyyyMMdd"格式 如 20131022
		}
		Date date1 = null;
		try {
			date1 = simpleDateFormat.parse(str1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date2 = null;
		try {
			date2 = simpleDateFormat.parse(str2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);//从间隔毫秒变成间隔天数
	}
	
	public static double getTimeAndDateDifferent(String rentBeginTime) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//输入日期的格式
		String str1 = rentBeginTime;
		String str2 = simpleDateFormat.format(new Date());
		Date date1 = null;
		try {
			date1 = simpleDateFormat.parse(str1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date2 = null;
		try {
			date2 = simpleDateFormat.parse(str2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		long l1 = cal2.getTimeInMillis() - cal1.getTimeInMillis();//日期间的毫秒差
		int i = 1000 * 3600 * 24;//一天的毫秒
		double div = div(l1, i, 2);//相差天数
		return div;
	}
	
	/**
	 * @param v1    被除数
	 * @param v2    除数
	 * @param scale 小数点有效数
	 * @return
	 */
	private static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
}
