package com.ailide.apartmentsabc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;

import com.ailide.apartmentsabc.tools.DisplayUtil;
import com.ailide.apartmentsabc.views.MyApplication;
import com.ailide.apartmentsabc.views.friendchat.FriendChatActivity;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class WordAndPicture {

    public static Bitmap getWordPicture(Bitmap photo,String str){
        //一行可以显示文字的个数
        int lineTextCount = (int) ((photo.getWidth() - 30) / 40);
        //一共要把文字分为几行
        int line = (int) Math.ceil(Double.valueOf(str.length()) / Double.valueOf(lineTextCount));
        int width = photo.getWidth(), hight = photo.getHeight();
        Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); //建立一个空的BItMap
        Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上
        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些
        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);//创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(40);//字体大小
        Rect bounds = new Rect();

//开启循环直到画完所有行的文字
        for (int i = 0; i < line; i++) {
            String s;
            if (i == line - 1) {//如果是最后一行，则结束位置就是文字的长度，别下标越界哦
                s = str.substring(i * lineTextCount, str.length());
            } else {//不是最后一行
                s = str.substring(i * lineTextCount, (i + 1) * lineTextCount);
            }
            //获取文字的字宽高以便把文字与图片中心对齐
            textPaint.getTextBounds(s, 0, s.length(), bounds);
            //画文字的时候高度需要注意文字大小以及文字行间距
            canvas.drawText(s, photo.getWidth() / 2 - bounds.width() / 2, (photo.getHeight() - line*40 - line*5)/2  + i * 40 + i * 5 + bounds.height() / 2 , textPaint);
        }
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setColor(Color.RED);//采用的颜色
        //textPaint.setShadowLayer(3f, 1, 1,this.getResources().getColor(android.R.color.background_dark));//影音的设置
//        canvas.drawText(str, 0, hight/2, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return icon;
    }

    public static Bitmap getPicturePicture(Bitmap src, Bitmap watermark){
        if( src == null ){
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
//create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );
//创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas( newb );
//draw src into
        cv.drawBitmap( src, 0, 0, null );//在 0，0坐标开始画入src
//draw watermark into
        cv.drawBitmap( watermark, (w - ww)/2 , (h - wh)/2, null );//在src的右下角画入水印
//save all clip
        cv.save( Canvas.ALL_SAVE_FLAG );//保存
//store
        cv.restore();//存储
        return newb;
    }

    //保存图片到data下面
    public void saveMyBitmap(Bitmap bmp) {
//        FileOutputStream fos = null;
//        try {
//            fos = openFileOutput("image1.jpg", Context.MODE_PRIVATE);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//        } catch (FileNotFoundException e) {
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.flush();
//                    fos.close();
//                } catch (IOException e) {
//                }
//            }
//        }
    }

    public static Bitmap loadBitmapFromView(EditText v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        String[] str = v.getText().toString().split("<i");
        Logger.e("text",DisplayUtil.sp2px(MyApplication.context,20.0f)*(v.getText().toString().length()-(str.length-1)*9) +"");
//        if(FriendChatActivity.imgWith*(str.length-1) + DisplayUtil.sp2px(MyApplication.context,20.0f)*((v.getText().toString().length()-(str.length-1)*9)+1) >v.getWidth()){
            screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
//        }else {
//            screenshot = Bitmap.createBitmap(FriendChatActivity.imgWith*(str.length-1) + DisplayUtil.sp2px(MyApplication.context,20.0f)*((v.getText().toString().length()-(str.length-1)*9)+1), v.getHeight(), Bitmap.Config.ARGB_4444);
//        }
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-v.getScrollX(), -v.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        v.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }

    public static Bitmap loadBitmapFromViewTwo(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
            screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-v.getScrollX(), -v.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        v.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }

    public static void saveBitmap( Bitmap btImage){
        FileOutputStream out = null;
        File file = new File(Environment.getExternalStorageDirectory(), "1.jpg");
        try {
            out = new FileOutputStream(file);
            btImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
}
    /**
     * 保存bitmap到SD卡
     * @param bitName 保存的名字
     * @param mBitmap 图片对像
     * return 生成压缩图片后的图片路径
     */
    public static String saveMyBitmap(String bitName,Bitmap mBitmap) {
        File f = new File("/sdcard/" + bitName + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (Exception e) {
            return "create_bitmap_error";
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/sdcard/" + bitName + ".png";
    }

}
