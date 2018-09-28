package com.ailide.apartmentsabc.views.mine;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
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

public class CommonProActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.html_web_view)
    WebView htmlWebView;

    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_pro);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("常见问题");
        content = getIntent().getStringExtra("content");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            htmlWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        htmlWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        htmlWebView.getSettings().setJavaScriptEnabled(true);//启用js
        htmlWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示

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
