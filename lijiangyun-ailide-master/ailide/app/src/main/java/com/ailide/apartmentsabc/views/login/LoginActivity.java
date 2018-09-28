package com.ailide.apartmentsabc.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.main.MainActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_qq_ly)
    LinearLayout loginQqLy;
    @BindView(R.id.login_weixin_ly)
    LinearLayout loginWeixinLy;
    @BindView(R.id.login_not_ly)
    LinearLayout loginNotLy;
    private int platformType = 2;
    private String uid = "";
    private String nickName = "";
    private String iconurl = "";
    private String gender = "";
    private String qqOrWechat;
    private String expiration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    private void QQLogin() {
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
    }

    private void weChatLogin() {
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            showLoading("授权中...");
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            dismissLoading();
            if (platform.name().equals("SINA")) {
                toast("微博授权成功");
            }
            if (platform.name().equals("WEIXIN")) {
                toast("微信授权成功");
            }
            if (platform.name().equals("QQ")) {
                toast("QQ授权成功");
            }
            nickName = data.get("name");
            gender = data.get("gender");
            iconurl = data.get("iconurl");
            uid = data.get("uid");
            expiration = data.get("expiration");

            isBind();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            dismissLoading();
            if (platform.name().equals("SINA")) {
                toast("微博授权失败");
            }
            if (platform.name().equals("WEIXIN")) {
                Logger.e("tttttttt",t.getMessage());
                toast("微信授权失败");
            }
            if (platform.name().equals("QQ")) {
                toast("QQ授权失败");
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            dismissLoading();
            if (platform.name().equals("SINA")) {
                toast("微博登录取消");
            }
            if (platform.name().equals("WEIXIN")) {
                toast("微信登录取消");
            }
            if (platform.name().equals("QQ")) {
                toast("QQ登录取消");
            }
        }
    };

    private void isBind() {
        showLoading("加载中...");
        int sex = 0;
        if ("男".equals(gender)) {
            sex = 1;
        } else if ("女".equals(gender)) {
            sex = 2;
        } else {
            sex = 0;
        }
        OkGo.<String>post(Urls.LOGIN)//
                .tag(this)//
                .params("screen_name", nickName)//
                .params("profile_image_url", iconurl)//
                .params("unionid", uid)//
                .params("source", qqOrWechat)//
                .params("gender", sex)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("login",response.body());
                            SPUtil.put(LoginActivity.this,"expiration",expiration);
                            SPUtil.put(LoginActivity.this,"user",response.body());
                            jumpToOtherActivity(new Intent(LoginActivity.this, MainActivity.class),true);
                        }
                    }
                });
    }

    //第三方注册
    private void thirdRegistered() {
        int sex = 0;
        if ("男".equals(gender)) {
            sex = 0;
        } else if ("女".equals(gender)) {
            sex = 1;
        } else {
            sex = 2;
        }
        OkGo.<String>post(Urls.LOGIN)//
                .tag(this)//
                .params("uid", uid)//
                .params("nickName", nickName)//
                .params("sex", sex)//
                .params("iconurl", iconurl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {

                        }
                    }
                });
    }

    @OnClick({R.id.login_qq_ly, R.id.login_weixin_ly, R.id.login_not_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_qq_ly:
                QQLogin();
                qqOrWechat = "2";
                break;
            case R.id.login_weixin_ly:
                qqOrWechat = "1";
                weChatLogin();
                break;
            case R.id.login_not_ly:
                jumpToOtherActivity(new Intent(this, MainActivity.class),true);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
