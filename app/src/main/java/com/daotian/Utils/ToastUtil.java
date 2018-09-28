package com.daotian.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daotian.R;


/**
 * Created by daguye on 15/6/3.
 */
public class ToastUtil {
    public static boolean isShow = true;
    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message)
    {
        if (isShow){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void zshowLong(Context context, CharSequence message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration)
    {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration)
    {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }
    public static void toast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.common_toast_layout, null);

        TextView tv = (TextView) view.findViewById(R.id.common_toast_textview);
        tv.setText(msg);
        toast.setView(view);
        toast.show();
    }
    public static void toastLong(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.common_toast_layout, null);

        TextView tv = (TextView) view.findViewById(R.id.common_toast_textview);
        tv.setText(msg);
        toast.setView(view);
        toast.show();
    }
    public static void toastDefine(Context context, String msg,int time) {
        Toast toast = Toast.makeText(context, msg, time);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.common_toast_layout, null);

        TextView tv = (TextView) view.findViewById(R.id.common_toast_textview);
        tv.setText(msg);
        toast.setView(view);
        toast.show();
    }
}
