package com.privateticket.Http;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.privateticket.Base.App;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class ParamUtil {
	public static final String INPUT = "input";
	public static final String BEHAVIOR = "behavior";
	public static final String MD5CODE = "sig";
	public static final String SIGN = "cp5645";
	
	public static RequestParams requestParams( Map<String, Object> paramMap) {
		RequestParams params = new RequestParams();
		Gson gson = new Gson();
		HashMap<String, Object> behaviorMap = new HashMap<String, Object>();
		
		if (null != App.mUser && App.mUser.getId()!=null) {
			behaviorMap.put("user_id", App.mUser.getId());
			behaviorMap.put("access_token", App.mUser.getAccess_token());
			Log.e("access_token",App.mUser.getAccess_token());
		}		

		params.put(INPUT, do64Encode(paramMap));	
		System.out.println("input:"+do64Encode(paramMap));

		params.put(BEHAVIOR, do64Encode(behaviorMap));
		System.out.println("behavior:"+do64Encode(behaviorMap));

		params.put(MD5CODE,MD5Util.getMD5WithSalt(SIGN, gson.toJson(paramMap),gson.toJson(behaviorMap)));
		System.out.println("md5:"+MD5Util.getMD5WithSalt(SIGN, gson.toJson(paramMap),gson.toJson(behaviorMap)));
		return params;
	}
	public static Map<String, String> getParamsMap(ServiceInterface serviceInterface, Map<String, Object> paramMap) {
		Map<String, String> params = new HashMap<String, String>();
		Gson gson = new Gson();
		HashMap<String, Object> behaviorMap = new HashMap<String, Object>();
		if (null != App.mUser && !App.mUser.getId().equals("")) {
			behaviorMap.put("user_id", App.mUser.getId());
			behaviorMap.put("access_token", App.mUser.getAccess_token());
		}				
		params.put(INPUT, do64Encode(paramMap));
		System.out.println("input:"+do64Encode(paramMap));
		params.put(BEHAVIOR, do64Encode(behaviorMap));
		System.out.println("behavior:"+do64Encode(behaviorMap));
		params.put(MD5CODE,MD5Util.getMD5WithSalt(SIGN, gson.toJson(paramMap),gson.toJson(behaviorMap)));
		System.out.println("md5::"+MD5Util.getMD5WithSalt(SIGN, gson.toJson(paramMap),gson.toJson(behaviorMap)));
		return params;
	}
	
	//64λ����
	public static String do64Encode(Map<String, Object> paramMap) {
		String retInput = "";
		Gson gson = new Gson();
		if (null != paramMap) {
			try {
				retInput = URLEncoder.encode(Base64Encoder.encode(gson.toJson(paramMap).getBytes()).toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				retInput = "";
			}
		}
		return retInput;
	}
	
	//ת������
	public static String unicodeToChinese(String str) {
		/**
		 * ɾ��Ӣ���е�\,such as "default\
		 */
		if (str.indexOf("\\u") == -1 || str == null || "".equals(str.trim())) {
			return str.replaceAll("\\\\ ", " ");
		}
		StringBuffer sb = new StringBuffer();
		/**
		 * ����ͷ����unicode���硰abc\u4e2d\u56fd��
		 */
		if (!str.startsWith("\\u")) {
			int index = str.indexOf("\\u");
			sb.append(str.substring(0, index));
			str = str.substring(index);
		}
		/**
		 * �硰\u4e2d\u56fd����
		 */
		if (str.endsWith(":")) {
			str = str.substring(0, str.length() - 1);
		}
		String[] chs = str.trim().split("\\\\u");
		for (int i = 0; i < chs.length; i++) {
			String ch = chs[i].trim();
			if (ch != null && !"".equals(ch)) {
				sb.append((char) Integer.parseInt(ch.substring(0, 4), 16));
				if (ch.length() > 4) {
					sb.append(ch.substring(4));
				}
			}
		}
		return sb.toString();
	}
}
