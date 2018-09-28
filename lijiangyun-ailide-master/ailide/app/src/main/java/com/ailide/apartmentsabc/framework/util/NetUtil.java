package com.ailide.apartmentsabc.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.Locale;


/**
 * 判断网络连接工具类
 */
public class NetUtil {

	public static boolean isActive(Context context) {
		NetworkInfo info = getActiveNetworkInfo(context);
		return info != null && info.isConnected();
	}

	public static boolean isWifiActive(Context context) {
		NetworkInfo info = getActiveNetworkInfo(context);
		return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
	}

	public static boolean isMobileActive(Context context) {
		NetworkInfo info = getActiveNetworkInfo(context);
		return info != null
				&& info.getType() == ConnectivityManager.TYPE_MOBILE;
	}

	public static int getNetType(Context context) {
		String netType = getNetName(context);
		int type = 0;
		if ("wifi".equalsIgnoreCase(netType)) {
			type = 1;
		} else if ("2G".equals(netType)) {
			type = 2;
		} else if ("3G".equals(netType)) {
			type = 3;
		}
		return type;
	}

	public static String getNetName(Context context) {
		NetworkInfo info = getActiveNetworkInfo(context);
		if (info == null || context == null) {
			return "";
		}
		String type = info.getTypeName();
		if ("mobile".equals(type.toLowerCase(Locale.US))) {
			TelephonyManager teleManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			switch (teleManager.getNetworkType()) {
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
					type = "UNKNOWN"; // 未知网络类型
					break;
				case TelephonyManager.NETWORK_TYPE_GPRS:
					// type = "GPRS"; //GPRS（2G）
					type = "2G";
					break;
				case TelephonyManager.NETWORK_TYPE_EDGE:
					// type = "EDGE"; //通用分组无线服务（2G）
					type = "2G";
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
					// type = "UMTS"; //通用移动通信系统 3G（3G）
					type = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_CDMA:
					// type = "CDMA"; //CDMA网络（电信2G）
					type = "2G";
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					// type = "EVDO_0"; //CDMA2000 1xEV-DO revision 0（3G）
					type = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					// type = "EVDO_A"; //CDMA2000 1xEV-DO revision A（3G）
					type = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					// type = "1xRTT"; //CDMA2000 1xRTT（2G）
					type = "2G";
					break;
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					// type = "HSDPA"; //高速下行分组接入技术（3G）
					type = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					// type = "HSUPA"; //高速上行链路分组接入技术（3G）
					type = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_HSPA:
					// type = "HSPA"; //高速上行行链路分组接入技术（3G）
					type = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_IDEN:
					// type = "IDEN"; //集成数字增强型网络
					type = "3G";
					break;
			}
		}
		return type;
	}

	public static String getImsi(Context context ) {
		String imsi = "";
		if (context != null) {
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			imsi = manager.getSubscriberId();
		}
		return imsi;
	}

	private static NetworkInfo getActiveNetworkInfo(Context context) {
		NetworkInfo info = null;
		if (context != null) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			return manager.getActiveNetworkInfo();
		}
		return info;
	}

	/**
	 * @author qxian 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
	 * @param context
	 * @return
	 */

	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = 3;
			} else {
				netType = 2;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;
		}
		return netType;
	}
	/**
	 * 获取WIFI连接状态是否已连接上
	 */
	public static boolean wifiConected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		return wifi == NetworkInfo.State.CONNECTED ? true : false;
	}

}
