package com.ailide.apartmentsabc.views;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends MultiDexApplication  {
	{
		PlatformConfig.setWeixin("wxf7e78c8d88e66208", "14dfff4c269f97445192807bf048556c");
		PlatformConfig.setQQZone("1106615109", "hARKbqugDCmXreWS");
	}
	public int appCount = 0;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
		OkGo.getInstance().init(this);
	}

	private static final String TAG = "Init";
	/** The context. */
	public static Application context;

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
		context = this;
		//极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		UMShareAPI.get(this);
		ZXingLibrary.initDisplayOpinion(this);
		ISNav.getInstance().init(new ImageLoader() {
			@Override
			public void displayImage(Context context, String path, ImageView imageView) {
				NetWorkImageLoader.loadNetworkImage(null, path,imageView);
			}
		});

		registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			}

			@Override
			public void onActivityStarted(Activity activity) {
				appCount++;
			}

			@Override
			public void onActivityResumed(Activity activity) {
			}

			@Override
			public void onActivityPaused(Activity activity) {
			}

			@Override
			public void onActivityStopped(Activity activity) {
				appCount--;
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {

			}
		});
	}
	public int getAppCount() {
		return appCount;
	}

}