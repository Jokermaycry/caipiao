package com.ailide.apartmentsabc.views;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.BaseDateBean;
import com.ailide.apartmentsabc.model.GetLoadingPictureBean;
import com.ailide.apartmentsabc.model.WhisperListBean;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.TimeUtils;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.login.LoginActivity;
import com.ailide.apartmentsabc.views.main.MainActivity;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Administrator on 2017/10/19 0019.
 */

public class LoadingActivity extends BaseActivity {
    @BindView(R.id.ivLoading)
    ImageView ivLoading;

    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PermissionGen.needPermission(this, 101, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
            bitmap = null;
        }
    }

    private void loading() {
        ivLoading.setVisibility(View.VISIBLE);
        AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);
        animation.setDuration(2000);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                jump();
            }
        });
        ivLoading.startAnimation(animation);
    }
    @PermissionSuccess(requestCode = 101)
    private void success() {
        getPicture();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(bitmap!=null)
                ivLoading.setImageBitmap(bitmap);
                loading();
            }
        },1000);
    }

    @PermissionFail(requestCode = 101)
    private void fail() {
        toast("授权失败，请手动添加权限");
        loading();
    }
    //获取本地token数据
    private void jump() {
        if (TextUtils.isEmpty(SPUtil.get(this, "expiration", "").toString())) {
            jumpToOtherActivity(new Intent(this, LoginActivity.class), true);
        } else if (!TextUtils.isEmpty(SPUtil.get(this, "expiration", "").toString()) && TimeUtils.getNowLongDate() + 259200 > Long.parseLong(SPUtil.get(this, "expiration", "").toString())) {
            jumpToOtherActivity(new Intent(this, LoginActivity.class), true);
        } else {
            jumpToOtherActivity(new Intent(this, MainActivity.class), true);
        }
//            String user_bean = SPUtil.get(LoadingActivity.this, "user_bean", "").toString();
//            if (TextUtils.isEmpty(user_bean)) {
//                jumpToOtherActivity(new Intent(LoadingActivity.this, LoginActivity.class), true);
//            } else {
//                jumpToOtherActivity(new Intent(LoadingActivity.this, MainActivity.class), true);
//            }
    }
    private void getPicture(){
        OkGo.<String>post(Urls.LOADING_PICTURE)//
                .tag(this)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        if (response.isSuccessful()) {
                            Logger.e("body",response.body());
                            GetLoadingPictureBean baseDateBean = JSON.parseObject(response.body(),GetLoadingPictureBean.class);
                            if(baseDateBean.getStatus()==1){
                                if(baseDateBean.getData().getIs_show()==1){
                                    if(!TextUtils.isEmpty(baseDateBean.getData().getImage_an())){
                                        Glide.with(MyApplication.context)
                                                .load(Urls.BASE_IMG +baseDateBean.getData().getImage_an())
                                                .asBitmap()
                                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                                                    @Override
                                                    public void onResourceReady(Bitmap bitmap1, GlideAnimation glideAnimation) {
                                                        bitmap = bitmap1;
                                                    }
                                                });
                                    }
                                }

                            }else {
                                toast(baseDateBean.getMsg());
                            }

                        }
                    }
                });
    }
}
