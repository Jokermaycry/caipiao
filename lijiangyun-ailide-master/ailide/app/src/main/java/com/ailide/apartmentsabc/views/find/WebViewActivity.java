package com.ailide.apartmentsabc.views.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.webview)
    WebView webview;

    private String find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        find = getIntent().getStringExtra("find");
        if(find.equals("1")){
            tvIncludeTitle.setText("商城");
        }else if(find.equals("2")){
            tvIncludeTitle.setText("兴趣部落");
        }else if(find.equals("3")){
            tvIncludeTitle.setText("新浪微博");
        }else {
            tvIncludeTitle.setText("爱立熊官网");
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
                            Logger.e("tag",response.body());
                            HtmlBean htmlBean = JSON.parseObject(response.body(), HtmlBean.class);
                            if(find.equals("1")){
                                setWebView(htmlBean.getData().getMall());
                            }else if(find.equals("2")){
                                setWebView(htmlBean.getData().getWechat());
                            }else if(find.equals("3")){
                                setWebView(htmlBean.getData().getWeibo());
                            }else {
                                setWebView(htmlBean.getData().getOfficial_link());
                            }
                        }
                    }
                });
    }

    private void setWebView(String mUrl) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setCacheMode((WebSettings.LOAD_NO_CACHE));
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);  //就是这句
        webview.requestFocus();
        webview.setWebChromeClient(new WebViewClient());
        if (!TextUtils.isEmpty(mUrl)) {
            webview.loadUrl(mUrl);
        }
        webview.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                return true;
            }
        });

    }

    private class WebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        } else {
            doBack();
        }
        return true;
    }
    @OnClick({R.id.iv_include_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                if (webview.canGoBack()) {
                    // goBack()表示返回WebView的上一页面
                    webview.goBack();
                    return;
                }
                doBack();
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (webview != null) {
            webview.destroy();
        }
        super.onDestroy();
    }
}
