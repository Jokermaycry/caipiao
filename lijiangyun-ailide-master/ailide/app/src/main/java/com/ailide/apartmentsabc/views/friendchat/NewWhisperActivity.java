package com.ailide.apartmentsabc.views.friendchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.NewWhisperBean;
import com.ailide.apartmentsabc.model.ShareBean;
import com.ailide.apartmentsabc.model.ShareTwoBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.ShareUtil;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewWhisperActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.fenlei_tv)
    TextView fenleiTv;
    @BindView(R.id.qiaoqiaohua_tv)
    TextView qiaoqiaohuaTv;
    @BindView(R.id.qq)
    TextView qq;
    @BindView(R.id.weixin)
    TextView weixin;
    @BindView(R.id.pengyouquan)
    TextView pengyouquan;
    @BindView(R.id.qq_kongjian)
    TextView qqKongjian;
    @BindView(R.id.qiaoqiaohua_edit)
    EditText qiaoqiaohuaEdit;
    @BindView(R.id.niming_img)
    ImageView nimingImg;
    @BindView(R.id.niming_ly)
    LinearLayout nimingLy;

    private ShareBean shareBean;
    private UserBean userBean;
    private NewWhisperBean baseDateBean;
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<String> options1ItemsTwo = new ArrayList<>();
    private OptionsPickerView pvOptions, pvOptionsTwo;
    private String content;
    private int isni=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_whisper);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString()))
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        getNew();
    }

    private void initOptionPicker() {
        //条件选择器初始化
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                fenleiTv.setText(options1Items.get(options1));
                options1ItemsTwo.clear();
                if (options1Items.get(options1).equals("其他")) {
                    qiaoqiaohuaEdit.setVisibility(View.VISIBLE);
                    qiaoqiaohuaTv.setVisibility(View.GONE);
                } else {
                    qiaoqiaohuaEdit.setVisibility(View.GONE);
                    qiaoqiaohuaTv.setVisibility(View.VISIBLE);
                    for (int i = 0; i < baseDateBean.getData().get(options1).getData().size(); i++) {
                        options1ItemsTwo.add(baseDateBean.getData().get(options1).getData().get(i).getContent());
                    }
                    qiaoqiaohuaTv.setText("选择悄悄话");
                    initOptionPickerTwo();
                }
            }
        })
                .setCancelColor(getResources().getColor(R.color._3d99ff))
                .setSubmitColor(getResources().getColor(R.color._3a3a3a))
                .setTextColorCenter(getResources().getColor(R.color._888888))
                .setTextColorOut(getResources().getColor(R.color.E7E7E7))
                .build();
        pvOptions.setPicker(options1Items);//二级选择器
    }

    private void initOptionPickerTwo() {
        //条件选择器初始化
        pvOptionsTwo = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                qiaoqiaohuaTv.setText(options1ItemsTwo.get(options1));
                content = options1ItemsTwo.get(options1);
            }
        })
                .setCancelColor(getResources().getColor(R.color._3d99ff))
                .setSubmitColor(getResources().getColor(R.color._3a3a3a))
                .setTextColorCenter(getResources().getColor(R.color._888888))
                .setTextColorOut(getResources().getColor(R.color.E7E7E7))
                .build();
        pvOptionsTwo.setPicker(options1ItemsTwo);//二级选择器
    }

    private void getNew() {
        showLoading("加载中...");
        OkGo.<String>post(Urls.WHISPERS_NEW)//
                .tag(this)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("body", response.body());
                            baseDateBean = JSON.parseObject(response.body(), NewWhisperBean.class);
                            if (baseDateBean.getStatus() == 1) {
                                for (int i = 0; i < baseDateBean.getData().size(); i++) {
                                    options1Items.add(baseDateBean.getData().get(i).getTag_name());
                                }
                                options1Items.add("其他");
                                initOptionPicker();
                            } else {
                                toast(baseDateBean.getMsg());
                            }

                        }
                    }
                });
    }

    private void getAddNew(final SHARE_MEDIA share, int qqOrwx, String content) {
        showLoading("加载中...");
        OkGo.<String>post(Urls.WHISPERS_NEW_ADD)//
                .params("mid", userBean.getData().getId())//
                .params("content", content)//
                .params("source", qqOrwx)//
                .params("is_ni", isni)//
                .tag(this)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("body", response.body());
                            baseDateBean = JSON.parseObject(response.body(), NewWhisperBean.class);
                            if (baseDateBean.getStatus() == 1) {
                                ShareTwoBean shareTwoBean = JSON.parseObject(response.body(), ShareTwoBean.class);
                                shareBean = new ShareBean();
                                shareBean.setShare_content(shareTwoBean.getData().getContent());
                                shareBean.setShare_url(shareTwoBean.getData().getUrl());
                                shareBean.setShare_title(shareTwoBean.getData().getTitle());
                                share(share);
                            } else {
                                toast(baseDateBean.getMsg());
                            }

                        }
                    }
                });
    }

    @OnClick({R.id.iv_include_back, R.id.fenlei_tv, R.id.qiaoqiaohua_tv, R.id.qq, R.id.weixin, R.id.pengyouquan, R.id.qq_kongjian,R.id.niming_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.fenlei_tv:
                pvOptions.show();
                break;
            case R.id.qiaoqiaohua_tv:
                if (fenleiTv.getText().toString().equals("选择分类")) {
                    toast("请先选择分类");
                    return;
                }
                pvOptionsTwo.show();
                break;
            case R.id.qq:
                if (fenleiTv.getText().toString().equals("选择分类")) {
                    toast("请先选择分类");
                    return;
                }
                if (fenleiTv.getText().toString().equals("其他")) {
                    if (TextUtils.isEmpty(qiaoqiaohuaEdit.getText().toString().trim())) {
                        toast("输入内容不能为空");
                        return;
                    }
                    content = qiaoqiaohuaEdit.getText().toString().trim();
                    getAddNew(SHARE_MEDIA.QQ, 2, content);
                } else {
                    if (qiaoqiaohuaTv.getText().toString().equals("选择悄悄话")) {
                        toast("请选择悄悄话");
                        return;
                    }
                    getAddNew(SHARE_MEDIA.QQ, 2, content);
                }
                break;
            case R.id.weixin:
                if (fenleiTv.getText().toString().equals("选择分类")) {
                    toast("请先选择分类");
                    return;
                }
                if (fenleiTv.getText().toString().equals("其他")) {
                    if (TextUtils.isEmpty(qiaoqiaohuaEdit.getText().toString().trim())) {
                        toast("输入内容不能为空");
                        return;
                    }
                    content = qiaoqiaohuaEdit.getText().toString().trim();
                    getAddNew(SHARE_MEDIA.WEIXIN, 1, content);
                } else {
                    if (qiaoqiaohuaTv.getText().toString().equals("选择悄悄话")) {
                        toast("请选择悄悄话");
                        return;
                    }
                    getAddNew(SHARE_MEDIA.WEIXIN, 1, content);
                }
                break;
            case R.id.pengyouquan:
                if (fenleiTv.getText().toString().equals("选择分类")) {
                    toast("请先选择分类");
                    return;
                }
                if (fenleiTv.getText().toString().equals("其他")) {
                    if (TextUtils.isEmpty(qiaoqiaohuaEdit.getText().toString().trim())) {
                        toast("输入内容不能为空");
                        return;
                    }
                    content = qiaoqiaohuaEdit.getText().toString().trim();
                    getAddNew(SHARE_MEDIA.WEIXIN_CIRCLE, 3, content);
                } else {
                    if (qiaoqiaohuaTv.getText().toString().equals("选择悄悄话")) {
                        toast("请选择悄悄话");
                        return;
                    }
                    getAddNew(SHARE_MEDIA.WEIXIN_CIRCLE, 3, content);
                }
                break;
            case R.id.qq_kongjian:
                if (fenleiTv.getText().toString().equals("选择分类")) {
                    toast("请先选择分类");
                    return;
                }
                if (fenleiTv.getText().toString().equals("其他")) {
                    if (TextUtils.isEmpty(qiaoqiaohuaEdit.getText().toString().trim())) {
                        toast("输入内容不能为空");
                        return;
                    }
                    content = qiaoqiaohuaEdit.getText().toString().trim();
                    getAddNew(SHARE_MEDIA.QZONE, 4, content);
                } else {
                    if (qiaoqiaohuaTv.getText().toString().equals("选择悄悄话")) {
                        toast("请选择悄悄话");
                        return;
                    }
                    getAddNew(SHARE_MEDIA.QZONE, 4, content);
                }
                break;
            case R.id.niming_ly:
                if(isni==2){
                    isni =1;
                    nimingImg.setBackgroundResource(R.drawable.fxqqh_nimin_pre);
                }else {
                    isni =2;
                    nimingImg.setBackgroundResource(R.drawable.fxqqh_nimin_default);
                }
                break;
        }
    }

    private void share(final SHARE_MEDIA share) {
        ShareUtil.toShare(this, share, shareBean, false, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                if (platform.name().equals("WEIXIN_CIRCLE")) {
                    toast("朋友圈分享成功");
                }
                if (platform.name().equals("WEIXIN")) {
                    toast("微信分享成功");
                }
                if (platform.name().equals("QQ")) {
                    toast("QQ分享成功");
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                if (platform.name().equals("WEIXIN_CIRCLE")) {
                    toast("朋友圈分享失败");
                }
                if (platform.name().equals("WEIXIN")) {
                    toast("微信分享失败");
                }
                if (platform.name().equals("QQ")) {
                    toast("QQ分享失败");
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                if (platform.name().equals("WEIXIN_CIRCLE")) {
                    toast("朋友圈分享取消");
                }
                if (platform.name().equals("WEIXIN")) {
                    toast("微信分享取消");
                }
                if (platform.name().equals("QQ")) {
                    toast("QQ分享取消");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
