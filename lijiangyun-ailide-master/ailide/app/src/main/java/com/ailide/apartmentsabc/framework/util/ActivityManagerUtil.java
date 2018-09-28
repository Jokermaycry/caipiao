package com.ailide.apartmentsabc.framework.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Activity 管理类，每次创建一个activity总会放入管理类的栈中
 */
public class ActivityManagerUtil {
	
	private static ActivityManagerUtil activityManagerUtil = null;
	
	public static ActivityManagerUtil getInstance() {
		if (activityManagerUtil == null) {
			activityManagerUtil = new ActivityManagerUtil();
		}
		return activityManagerUtil;
	}
	
	private ArrayList<Activity> stacks = new ArrayList<>();
	private ArrayList<Activity> littleStacks = new ArrayList<>();
	
	public void addActivity(Activity activity) {
		if (stacks != null) {
			stacks.add(activity);
		}
	}
	
	public void addLittleActivity(Activity activity) {
		if (littleStacks != null) {
			littleStacks.add(activity);
		}
	}
	
	public void removeActivity(Activity activity) {
		if (stacks != null) {
			stacks.remove(activity);
		}
		if (littleStacks != null) {
			littleStacks.remove(activity);
		}
	}
	
	public void closeLittleAll() {
		for (Activity a : littleStacks) {
			a.finish();
		}
	}
	
	public void closeAll() {
		for (Activity a : stacks) {
			a.finish();
		}
	}
	
	public void exit() {
		closeAll();
		// 杀死当前的进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
