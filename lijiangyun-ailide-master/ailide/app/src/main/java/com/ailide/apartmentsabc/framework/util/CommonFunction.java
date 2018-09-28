package com.ailide.apartmentsabc.framework.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ailide.apartmentsabc.R;


/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class CommonFunction {
    static CommonFunction instance = new CommonFunction();
    private int[] table = new int[27]; // 初始化
    private char[] chartable = {'啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'};
    private char[] alphatableb = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private char[] alphatables = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    {
        for (int i = 0; i < 27; ++i) {
            table[i] = gbValue(chartable[i]);
        }
    }

    public static CommonFunction getInstance() {
        return instance;
    }

    /**
     * @方法名: InitPopupWindow
     * @功能: 弹出PopupWindow
     * @参数 mContext, view, view
     * @返回值PopupWindow
     */
    public PopupWindow InitPopupWindow(final Activity mContext, View view, View parent, int bgType, int Location, int AnimotType, float alpha, boolean OutsideCancel) {
        final PopupWindow popup;
        if (bgType == 0) {
            popup = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (bgType == 1) {
            popup = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (bgType == 2) {
            popup = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            popup = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setFocusable(true);
        popup.setOutsideTouchable(OutsideCancel);
        backgroundAlpha(mContext, alpha);
//		popup.update();
        if (AnimotType == 1) {
            popup.setAnimationStyle(R.style.PopupAnimationAlpha);//动画渐变
        }
        if (AnimotType == 2) {
            popup.setAnimationStyle(R.style.PopupAnimationFromTop);//动画从顶部出来
        }
        if (AnimotType == 0) {
            popup.setAnimationStyle(R.style.PopupAnimation);//动画从底部出来
        }
        if (AnimotType == 3) {
            popup.setAnimationStyle(R.style.PopupAnimationRight);//动画从右边出来
        }
        if (parent != null) {
            if (Location == 0) {
                popup.showAtLocation(parent, Gravity.BOTTOM, 0, 0);//最底部
            } else if (Location == 1) {
                if (Build.VERSION.SDK_INT >= 24) {
                    int[] point = new int[2];
                    parent.getLocationInWindow(point);
                    popup.showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.NO_GRAVITY, point[0] - parent.getWidth() / 2, point[1] + parent.getHeight());
                } else {
                    popup.showAsDropDown(parent, 0, 0);
                }
            } else if (Location == 2) {
                popup.setClippingEnabled(false);
                popup.showAtLocation(parent, Gravity.NO_GRAVITY, 0, getStatusBarHeight(mContext));//相对某个控件的位置，有偏移;
            } else {
                popup.setClippingEnabled(false);
                popup.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);//相对某个控件的位置，有偏移;
            }
        }
        view.setFocusable(true); // 这个很重要
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popup.dismiss();
                    return true;
                }

                return false;
            }
        });
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(mContext, 1.0f);
            }
        });
        return popup;
    }

    public PopupWindow InitBottomPopupWindow(final Activity mContext, View view, View parent, boolean OutsideCancel) {
        final PopupWindow popup;
        popup = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setFocusable(true);
        popup.setOutsideTouchable(OutsideCancel);
        backgroundAlpha(mContext, 1f);
        popup.setAnimationStyle(R.style.PopupAnimationAlpha);//动画渐变
        if (parent != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                int[] point = new int[2];
                parent.getLocationInWindow(point);
                popup.showAtLocation(mContext.getWindow().getDecorView(), Gravity.NO_GRAVITY, point[0] - parent.getWidth() / 2, point[1] + parent.getHeight());
            } else {
                int[] point = new int[2];
                parent.getLocationInWindow(point);
                int popupWidth = popup.getContentView().getMeasuredWidth();
                popup.showAtLocation(parent, Gravity.NO_GRAVITY, point[0] - parent.getWidth(), point[1] + parent.getHeight());
//					popup.showAsDropDown(parent, 0, 0);
            }
        }
        view.setFocusable(true); // 这个很重要
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popup.dismiss();
                    return true;
                }

                return false;
            }
        });
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(mContext, 1.0f);
            }
        });
        return popup;
    }

    public void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * @方法名: customDialog
     * @功能: 自定义弹框的属性，以及布局自定义，返回dialog
     * @返回值Dialog 无
     */
    public Dialog customDialog(Context context, int layout) {
        Dialog d = new Dialog(context, R.style.CustomDialog);
        d.setContentView(layout);
        d.setCanceledOnTouchOutside(true);
        d.setCancelable(true);
        return d;
    }

    /**
     * @方法名: String2Alpha
     * @功能:根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
     * @返回值String
     */
    public String String2Alpha(String SourceStr, String type) {
        String Result = "";
        int StrLength = SourceStr.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                Result += Char2Alpha(SourceStr.charAt(i), type);
            }
        } catch (Exception e) {
            Result = "";
        }
        return Result;
    }

    public char Char2Alpha(char ch, String type) {
        if (ch >= 'a' && ch <= 'z')
            // return (char) (ch - 'a' + 'A');
            return ch;
        if (ch >= 'A' && ch <= 'Z')
            return ch;

        int gb = gbValue(ch);
        if (gb < table[0])
            return 'B';

        int i;
        for (i = 0; i < 26; ++i) {
            if (match(i, gb))
                break;
        }

        if (i >= 26) {
            return 'B';
        } else {
            if ("b".equals(type)) {// 大写
                return alphatableb[i];
            } else {// 小写
                return alphatables[i];
            }
        }
    }

    /**
     * @方法名: gbValue
     * @功能: 取出汉字的编码
     * @返回值int 无
     */
    private int gbValue(char ch) {
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GBK");
            if (bytes.length < 2)
                return 0;
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean match(int i, int gb) {
        if (gb < table[i])
            return false;
        int j = i + 1;

        // 字母Z使用了两个标签
        while (j < 26 && (table[j] == table[i]))
            ++j;
        if (j == 26)
            return gb <= table[j];
        else
            return gb < table[j];
    }

    public boolean isAlpha(String alpha) {
        if (alpha == null)
            return false;
        return alpha.matches("[a-zA-Z]+");
    }

}
