package com.ailide.apartmentsabc.views.sticker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.utils.GrayScaleUtil;
import com.ailide.apartmentsabc.views.MyApplication;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillPrePrintActivity extends BaseActivity {

    @BindView(R.id.iv_print)
    SubsamplingScaleImageView mIvPrint;

    private Bitmap printBitmap;

    /**
     * 对图片进行压缩（去除透明度）
     */
    public static Bitmap compressPic(Bitmap bitmap) {
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 指定调整后的宽度和高度
        int newWidth;
        int newHeight;
        if (width > 384) {
            newWidth = 384;
            newHeight = height * 384 / width;
        } else {
            newWidth = width;
            newHeight = height;
        }
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmap, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_preprint);
        ButterKnife.bind(this);
        mIvPrint.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        mIvPrint.setMinScale(1.0F);
        Glide.with(this)
                .load(getIntent().getStringExtra("path"))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        if(bitmap.getHeight()< mIvPrint.getHeight()){
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvPrint.getLayoutParams();
                            params.height = bitmap.getHeight();
                        }
                        printBitmap = bitmap;
                        new AsyncTask<Integer, Integer, Bitmap>() {

                            @Override
                            protected void onPreExecute() {
                                showLoading("请稍后");
                            }

                            @Override
                            protected Bitmap doInBackground(Integer... integers) {
                                Bitmap bitmap1 = printBitmap;
                                Bitmap luminosity = bitmap2Gray(compressPic(printBitmap));
                                printBitmap = luminosity;
                                return bitmap2Gray(bitmap1);
                            }

                            @Override
                            protected void onPostExecute(Bitmap bitmap) {
                                mIvPrint.setImage(ImageSource.bitmap(bitmap), new ImageViewState(0.5F, new PointF(0, 0), 0));
                                dismissLoading();
                            }
                        }.execute();
                    }
                });
    }

    public Bitmap grayScale(final Bitmap bitmap, GrayScaleUtil.GrayScale grayScale) {
        if (null == bitmap || null == grayScale) {
            return null;
        }
        Bitmap rs = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rs);
        Paint paint = new Paint();
        for (int x = 0, w = bitmap.getWidth(); x < w; x++) {
            for (int y = 0, h = bitmap.getHeight(); y < h; y++) {
                int c = bitmap.getPixel(x, y);
                int a = Color.alpha(c);
                int r = Color.red(c);
                int g = Color.red(c);
                int b = Color.blue(c);
                int gc = grayScale.grayScale(r, g, b);
                paint.setColor(Color.argb(a, gc, gc, gc));
                canvas.drawPoint(x, y, paint);
            }
        }
        return rs;
    }

    //打印添加抖动和灰色图片处理
    public Bitmap convertGreyImgByFloyd(Bitmap img) {
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高
        int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] gray = new int[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                gray[width * i + j] = red;
            }
        }
        int e = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int g = gray[width * i + j];
                if (g >= 128) {
                    pixels[width * i + j] = 0xffffffff;
                    e = g - 255;


                } else {
                    pixels[width * i + j] = 0xff000000;
                    e = g - 0;
                }
                if (j < width - 1 && i < height - 1) {
                    //右边像素处理
                    gray[width * i + j + 1] += 3 * e / 8;
                    //下
                    gray[width * (i + 1) + j] += 3 * e / 8;
                    //右下
                    gray[width * (i + 1) + j + 1] += e / 4;
                } else if (j == width - 1 && i < height - 1) {//靠右或靠下边的像素的情况
                    //下方像素处理
                    gray[width * (i + 1) + j] += 3 * e / 8;
                } else if (j < width - 1 && i == height - 1) {
                    //右边像素处理
                    gray[width * (i) + j + 1] += e / 4;
                }
            }


        }
        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return mBitmap;
    }

    public Bitmap bitmap2Gray(Bitmap bmSrc) {
        // 得到图片的长和宽
        int width = bmSrc.getWidth();
        int height = bmSrc.getHeight();
        // 创建目标灰度图像
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 创建画布
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0, 0, paint);
        return bmpGray;
    }

    @OnClick({R.id.iv_back, R.id.btn_print})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_print:
                if (PrintFragment.printPP==null || !PrintFragment.printPP.isConnected()) {
                    toast("未连接设备");
                    return;
                }
//                int width = printBitmap.getWidth();
//                int height = printBitmap.getHeight();
//                // 设置想要的大小
//                int newWidth = 384;
//                // 计算缩放比例
//                float scaleWidth = ((float) newWidth) / width;
////                float scaleHeight = ((float) newHeight) / height;
//                // 取得想要缩放的matrix参数
//                Matrix matrix = new Matrix();
//                matrix.postScale(scaleWidth, scaleWidth);
////                matrix.postScale(scaleWidth, scaleHeight);
//                // 得到新的图片
//                printBitmap = Bitmap.createBitmap(printBitmap, 0, 0, width, height, matrix, true);
//                bitmap
                toast("打印中");
                PrintFragment.printPP.enablePrinter();
                PrintFragment.printPP.printerWakeup();
                PrintFragment.printPP.printLinedots(16);
                PrintFragment.printPP.printBitmap(printBitmap, 0);
                PrintFragment.printPP.printLinedots(96);
                PrintFragment.printPP.stopPrintJob();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (printBitmap != null) {
            printBitmap.recycle();
            printBitmap = null;
        }
    }
}
