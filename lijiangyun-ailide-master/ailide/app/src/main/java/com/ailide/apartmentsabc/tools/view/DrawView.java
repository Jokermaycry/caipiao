package com.ailide.apartmentsabc.tools.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.DrawEvent;
import com.ailide.apartmentsabc.framework.util.ActivityManagerUtil;
import com.ailide.apartmentsabc.tools.ToastUtil;
import com.ailide.apartmentsabc.views.main.MainActivity;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/1/12 0012.
 */

public class DrawView extends View {
    // 定义记录前一个拖动事件发生点的坐标
    float preX;
    float preY;
    private Path path;
    public Paint paint = null;
    // 定义一个内存中的图片，该图片将作为缓冲区
    Bitmap cacheBitmap = null;
    // 定义cacheBitmap上的Canvas对象
    Canvas cacheCanvas = null;
    private long exitTime = 0;
    private long upTime = 0;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, int width , int height) {
        super(context);
        // 创建一个与该View相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();
        // 设置cacheCanvas将会绘制到内存中的cacheBitmap上
        cacheCanvas.setBitmap(cacheBitmap);
        // 设置画笔的颜色
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.BLACK);
        // 设置画笔风格
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(25);
        // 反锯齿
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // 获取拖动事件的发生位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                // 从前一个点绘制到当前点之后，把当前点定义成下次绘制的前一个点
                path.moveTo(x, y);
                preX = x;
                preY = y;
                handler.removeMessages(1);
                break;
            case MotionEvent.ACTION_MOVE:
                // 从前一个点绘制到当前点之后，把当前点定义成下次绘制的前一个点
                path.quadTo(preX, preY, x, y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path, paint); // ①
                path.reset();
                Message message =new Message();
                message.what = 1;
                handler.sendMessageDelayed(message,1000);
                break;
        }
        invalidate();
        // 返回true表明处理方法已经处理该事件
        return true;
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DrawEvent drawEvent = new DrawEvent();
            EventBus.getDefault().post(drawEvent);
        }
    };
    @Override
    public void onDraw(Canvas canvas)
    {
        Paint bmpPaint = new Paint();
        // 将cacheBitmap绘制到该View组件上
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint); // ②
        // 沿着path绘制
        canvas.drawPath(path, paint);
    }

    //获取我们绘制成功后的图片

    public Bitmap getPaintBitmap() {
        return resizeImage(cacheBitmap, 300, 100);
    }



    // 缩放
    public static Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        //获取图片的宽高
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        Logger.e("dddddd",originWidth + " &&" + originHeight);

        //这里缩放我们的尺寸，缩放多少自己去定义
        float scaleWidth = ((float) width) / originWidth;
        float scaleHeight = ((float) height) / originHeight;

        //进行缩放
        Matrix matrix = new Matrix();
        matrix.postScale(0.1f, 0.08f);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, originWidth,
                originHeight, matrix, true);
        return resizedBitmap;
    }


    //清除画板
    public void clear() {
        if (cacheBitmap != null) {
            path.reset();
            cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }
}