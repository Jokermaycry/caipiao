package com.ailide.apartmentsabc.tools;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;


import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by admin on 2016/6/30.
 */
public class SetJpushUtil {

    public static boolean setTagSucess;
    Context mContext;

    public SetJpushUtil(Context onctext){
        mContext=onctext;
    }
    /**
     * 设置别名
     */
    public  void setAlias(String alias){
        if (TextUtils.isEmpty(alias)) {
            ToastUtil.toast(mContext,"别名为空，推送异常");
            return;
        }
        if (!NetWorkUtil.isValidTagAndAlias(alias)) {
            ToastUtil.toast(mContext,"别名格式错误，推送异常");
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }
    /**
     * 设置Tags
     */
    public  void setTag(String tag){
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            ToastUtil.toast(mContext,"tag为空，推送异常");
            Log.e("jpush","tag为空，推送异常");
            return;
        }
        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!NetWorkUtil.isValidTagAndAlias(sTagItme)) {
                ToastUtil.toast(mContext,"tag格式错误，推送异常");
                Log.e("jpush","tag格式错误，推送异常");
                return;
            }
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tag));

    }

    /**
     *
     */
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("JpushSet", "Set alias in handler.");
                    JPushInterface.setAliasAndTags(mContext, (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d("JpushSet", "Set tags in handler.");
                    //设置tag
//                    JPushInterface.setAliasAndTags(mContext, null, (Set<String>) msg.obj, mTagsCallback);
                    //设置alias
                    JPushInterface.setAliasAndTags(mContext, String.valueOf(msg.obj), null, mTagsCallback);
                    break;

                default:
                    Log.i("JpushSet", "Unhandled msg - " + msg.what);
            }
        }
    };

    /**
     * 设置别名和tag接口回掉
     */
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set alias success";
                    ToastUtil.toast(mContext,"Set  alias success");
                    Log.e("Alias", logs);
                    break;
                case 6002:
                    logs = "Failed to set alias  due to timeout. Try again after 60s.";
                    ToastUtil.toast(mContext,"Failed to set alias and tags due to timeout. Try again after 60s.");
                    Log.e("Alias", logs);
                    if (NetWorkUtil.isConnect(mContext)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.e("Alias", "No network");
                    }
                    break;
                case 1011:
                    if (NetWorkUtil.isConnect(mContext)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.e("Alias", "No network");
                    }
                    break;

                default:
                    ToastUtil.toast(mContext, "Failed with errorCode = " + code);
                    logs = "Failed with errorCode = " + code;
                    Log.e("Alias", logs);
            }
        }

    };
    /**
     * 设置tag
     */
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag success";
                    Log.e("Tag", logs);
                    setTagSucess=true;
                    break;
                case 6002:
                    logs = "Failed to set  tags due to timeout. Try again after 60s.";
                    Log.e("Tag", logs);
                    if (NetWorkUtil.isConnect(mContext)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.e("Tag", "No network");
                    }
                    break;
                case 1011:
                    if (NetWorkUtil.isConnect(mContext)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.e("Tag", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("Tag", logs);
            }
        }

    };

}
