package com.privateticket.Base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.privateticket.Model.UserBO;

public class App extends Application{

	// 用户信息
	public static UserBO mUser = null;

	//图片加载类
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions defaultOptions;

	public void onCreate() {
		super.onCreate();
		initImageLoad();
	}
	/**
	 * 初始化图片加载器
	 */
	private void initImageLoad() {
		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new LruMemoryCache(4 * 1024 * 1024))
				.memoryCacheSize(4 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
	}


}
