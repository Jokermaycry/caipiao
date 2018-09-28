package com.ailide.apartmentsabc.tools;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MatcherUtils {
	// 判断是否为手机号
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	// 判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 判断number参数是否是整型数表示方式
	 * @param number
	 * @return
	 */
	public static boolean isIntegerNumber(String number){
		number=number.trim();
		String intNumRegex="\\-{0,1}\\d+";//整数的正则表达式
		if(number.matches(intNumRegex))
			return true;
		else
			return false;
	}

	/**
	 * 判断number参数是否是浮点数表示方式
	 * @param number
	 * @return
	 */
	public static boolean isFloatPointNumber(String number){
		number=number.trim();
		String pointPrefix="(\\-|\\+){0,1}\\d*\\.\\d+";//浮点数的正则表达式-小数点在中间与前面
		String pointSuffix="(\\-|\\+){0,1}\\d+\\.";//浮点数的正则表达式-小数点在后面
		if(number.matches(pointPrefix)||number.matches(pointSuffix))
			return true;
		else
			return false;
	}

	public static boolean isStartDateTimeBeforeEndDateTime(String starttime, String endtime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date startdate = new Date();
		Date enddate = new Date();
		try {
			startdate = formatter.parse(starttime);
			enddate = formatter.parse(endtime);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (startdate.compareTo(enddate) < 0) {
			return true;
		}
		return false;
	}
	public static String getDoubleVaule(String vaule){
		BigDecimal bd = new BigDecimal(vaule);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd.toString();
	}
	public static String getDoubleOneVaule(String vaule){
		BigDecimal bd = new BigDecimal(vaule);
		bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		return bd.toString();
	}
	public static String getNextDateString(String time, int days) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		try {
			date = formatter.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		date.setDate(date.getDate() + days);
		return formatter.format(date);
	}
	public static String getNowDateTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		return formatter.format(curDate);
	}
	public static String getNowDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		return formatter.format(curDate);
	}
	public static Long getNowLongDate(){
//		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
//		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		return System.currentTimeMillis();
	}
}
