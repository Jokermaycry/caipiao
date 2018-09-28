package com.privateticket.Http;


import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privateticket.Base.Constant;

/**
 * ��ا�ӿ�
 * @author YZX 2015.12.9
 *
 */
public class TicketService {

	private static AsyncHttpClient client = new AsyncHttpClient();

	
	public static void post(RequestParams params,ServiceInterface serviceInterface,AsyncHttpResponseHandler  responseHandler) {
		client.post(getAbsoluteUrl(serviceInterface), params, responseHandler);
		Log.e("TicketService-url",getAbsoluteUrl(serviceInterface));
		Log.e("TicketService-params",params+"");
	}
	
	
	public static String getAbsoluteUrl(ServiceInterface serviceInterface) {

		StringBuffer url = new StringBuffer();
		url.append(Constant.BASE_URL + "/" + serviceInterface.getModel() + "/"+ serviceInterface.getAction());
		if (!TextUtils.isEmpty(serviceInterface.getParam())) {
			url.append("?" + serviceInterface.getParam());
		}
//		DevLog.e("request for:", url.toString());

		return url.toString();

	}
}
