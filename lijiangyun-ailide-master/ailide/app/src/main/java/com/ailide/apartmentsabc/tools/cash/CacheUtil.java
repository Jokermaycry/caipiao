package com.ailide.apartmentsabc.tools.cash;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liwenguo on 2017/6/1 0001.
 */

public class CacheUtil {
	private static CacheUtil cacheUtil = null;
	
	public static CacheUtil getInstance() {
		if (cacheUtil == null) {
			cacheUtil = new CacheUtil();
		}
		return cacheUtil;
	}
	/**
	 * 保存对象
	 * @param ser 缓存对象
	 * @param file 缓存路径
	 */
	public boolean saveObject(Context context, Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
				fos.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 读取缓存对象
	 * @param file 读取的缓存路径
	 * @return 返回缓存对象
	 */
	public Serializable readObject(Context context, String file) {
		if (!isExistDataCache(context,file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		}  catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = context.getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
				fis.close();
			} catch (Exception e) {
				
			}
		}
		return null;
	}
	
	/**
	 * 判断缓存是否存在
	 * @param cachefile 缓存路径
	 * @return true为存在
	 */
	private boolean isExistDataCache(Context context, String cachefile) {
		boolean exist = false;
		File data = context.getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}
}
