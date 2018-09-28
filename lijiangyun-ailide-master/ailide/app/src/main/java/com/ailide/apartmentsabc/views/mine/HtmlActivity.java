package com.ailide.apartmentsabc.views.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.HtmlBean;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HtmlActivity extends BaseActivity {

    @BindView(R.id.html_web_view)
    WebView htmlWebView;
    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;

    private String witch_html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        ButterKnife.bind(this);
        witch_html = getIntent().getStringExtra("witch_html");
        if(!TextUtils.isEmpty(witch_html)){
            tvIncludeTitle.setText("客服");
        }else {
            tvIncludeTitle.setText("使用说明");
        }
        post();
    }

    private void post() {
        showLoading("加载中...");
        OkGo.<String>post(Urls.SETTING)//
                .tag(this)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("body",response.body());
                            HtmlBean htmlBean = JSON.parseObject(response.body(), HtmlBean.class);
                            if (!TextUtils.isEmpty(witch_html)) {
                                htmlWebView.loadDataWithBaseURL(null, htmlBean.getData().getKefu(), "text/html", "utf-8", null);
                            } else {
                                htmlWebView.loadDataWithBaseURL(null, htmlBean.getData().getInstructions(), "text/html", "utf-8", null);
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.iv_include_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
        }
    }
}
