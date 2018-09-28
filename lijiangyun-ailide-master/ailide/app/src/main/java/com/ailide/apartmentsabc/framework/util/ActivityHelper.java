package com.ailide.apartmentsabc.framework.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.widget.Toast;

/**
 * Activity辅助类
 */
public class ActivityHelper {
    final static String TAG = ActivityHelper.class.getSimpleName();

    /**
     * 对应的Activity
     */
    private Activity mActivity;

    /**
     * 对话框帮助类
     */
    private DialogHelper mDialogHelper;

    public ActivityHelper(Activity activity) {
        mActivity = activity;
        mDialogHelper = new DialogHelper(mActivity);
    }

    public void finish() {
        mDialogHelper.dismissProgressDialog();
    }

    /**
     * 弹对话框
     *
     * @param title            标题
     * @param msg              消息
     * @param positive         确定按钮文字
     * @param positiveListener 确定回调
     * @param negative         否定按钮文字
     * @param negativeListener 否定回调
     */
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener) {
        mDialogHelper.alert(title, msg, positive, positiveListener, negative, negativeListener);
    }


    /**
     * 弹对话框
     *
     * @param title                    标题
     * @param msg                      消息
     * @param positive                 确定按钮文字
     * @param positiveListener         确定回调
     * @param negative                 否定按钮文字
     * @param negativeListener         否定回调
     * @param isCanceledOnTouchOutside 外部是否可点取消
     */
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener,
                      Boolean isCanceledOnTouchOutside) {
        mDialogHelper.alert(title, msg, positive, positiveListener, negative, negativeListener,
                isCanceledOnTouchOutside);
    }

    /**
     * 弹出TOAST
     * @param msg   消息内容
     */
    public void toast(String msg) {
        mDialogHelper.toast(msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示进度对话框
     * @param msg 消息
     */
    public void showProgressDialog(String msg) {
        mDialogHelper.showProgressDialog(msg);
    }

    /**
     * 显示可取消的进度对话框
     * @param msg        消息
     * @param cancelable 是否显示取消按钮
     */
    public void showProgressDialog(final String msg, final boolean cancelable,
                                   final OnCancelListener cancelListener) {
        mDialogHelper.showProgressDialog(msg, cancelable, cancelListener, true);
    }

    public void dismissProgressDialog() {
        mDialogHelper.dismissProgressDialog();
    }

}
