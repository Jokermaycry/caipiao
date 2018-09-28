package com.ailide.apartmentsabc.tools;

import android.content.Context;
import android.widget.ImageView;

import com.ailide.apartmentsabc.views.MyApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.ailide.apartmentsabc.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by daguye on 16/2/18.
 */
public class NetWorkImageLoader {
    /**
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadNetworkImage(Context context, String url, ImageView imageView){
        loadNetworkImage(MyApplication.context,url,imageView, R.drawable.mrtx,R.drawable.mrtx);
    }

    public static void loadNetworkImage(Context context, int path, ImageView imageView){
        loadNetworkImage(MyApplication.context,path,imageView, R.drawable.mrtx,R.drawable.mrtx);
    }
    /**
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadNetworkImage(Context context, String url, ImageView imageView, int x){
        loadNetworkImage(MyApplication.context,url,imageView,x,R.drawable.mrtx,R.drawable.mrtx);

    }

    /**
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadNetworkImage(Context context, String url, ImageView imageView, int x, int defaultImg, int errorImg){
        Glide.with(MyApplication.context)
                .load(url)
                .placeholder(defaultImg)
                .error(errorImg)
                .transform(new GlideRoundTransform(context, x))
                .into(imageView);
    }

    /**
     * 加载网络图片并设置默认图片和错误图片
     * @param context
     * @param url
     * @param imageView
     * @param defaultImg
     * @param errorImg
     */
    public static void loadNetworkImage(Context context, String url, ImageView imageView, int defaultImg, int errorImg){
        Glide.with(MyApplication.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(defaultImg)
                .dontAnimate()
                .error(errorImg)
                .into(imageView);
    }

    public static void loadNetworkImage(Context context, int path, ImageView imageView, int defaultImg, int errorImg){
        Glide.with(MyApplication.context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(defaultImg)
                .dontAnimate()
                .error(errorImg)
                .into(imageView);

    }

    public static synchronized String getPath(Context mContext, String url){

        String path=null;
        FutureTarget<File> future = Glide.with(MyApplication.context)
                .load(url)
                .downloadOnly(500, 500);
        try {
            File cacheFile = future.get();
            path = cacheFile.getAbsolutePath();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadLocalImage(Context context, String url, ImageView imageView){
        Glide.with(MyApplication.context)
                .load(new File(url))
                .placeholder(R.drawable.mrtx)
                .error(R.drawable.mrtx)
                .into(imageView);
    }
    /**
     * 加载圆形图片
     */
    public static void loadCircularImage(Context context, String url, ImageView imageView){
        Glide.with(MyApplication.context).load(url).placeholder(R.drawable.mrtx)
                .error(R.drawable.mrtx).bitmapTransform(new CropCircleTransformation(context)).crossFade(1000).into(imageView);
    }
    public static void loadCircularImage(Context context, String url, ImageView imageView, int defaultImg, int errorImg){
        Glide.with(MyApplication.context).load(url).placeholder(defaultImg)
                .error(errorImg).bitmapTransform(new CropCircleTransformation(MyApplication.context)).crossFade(1000).into(imageView);
    }
    /**
     * 加载原图高斯模糊
     */
    public static void loadGaussianImage(Context context, String url, ImageView imageView){
        Glide.with(MyApplication.context).load(url).placeholder(R.drawable.haoyou_bg)
                .error(R.drawable.haoyou_bg).bitmapTransform(new BlurTransformation(MyApplication.context, 10,3)).into(imageView);
    }
    public static void loadNetworkImage2(Context context, String url, ImageView imageView){
        Glide.with(MyApplication.context)
                .load(url)
                .into(imageView);
        
    }
}
