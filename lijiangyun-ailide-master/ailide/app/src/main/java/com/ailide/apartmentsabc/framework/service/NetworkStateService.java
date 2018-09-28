package com.ailide.apartmentsabc.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import com.ailide.apartmentsabc.eventbus.NetWorkEvent;
import com.ailide.apartmentsabc.framework.util.NetUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lishaohang on 2015/12/7.
 * 检测网络状态的service
 */
public class NetworkStateService extends Service {
	
	//必须实现方法，返回的IBinder对象，传递给ServiceConnection
	//对象当中的onServiceConnected()方法中，用于通信,就是返回代理对象
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private TimerTask timerTask;
	//是否发出弹出提示框的标识
	private boolean connect = true;
	
	//service创建时回调的方法
	public void onCreate() {
		Timer timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if (!NetUtil.isActive(getApplicationContext())) {
					if (connect) {
						NetWorkEvent trainData = new NetWorkEvent("lost");
						EventBus.getDefault().post(trainData);
						connect = false;
					}
				} else {
					connect = true;
					NetWorkEvent trainData = new NetWorkEvent("connet");
					EventBus.getDefault().post(trainData);
				}
			}
		};
		timer.schedule(timerTask, 2000, 2000);
	}
	
	//service被启动的回调方法
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
   //注销EventBus
		EventBus.getDefault().unregister(this);
	}
	
}
