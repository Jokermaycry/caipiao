package com.ailide.apartmentsabc.views.Web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.AddWebTag;
import com.ailide.apartmentsabc.model.StatusResponse;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.model.Web;
import com.ailide.apartmentsabc.model.WebTag;
import com.ailide.apartmentsabc.model.WebTags;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.tools.view.spinner.NiceSpinner;
import com.ailide.apartmentsabc.utils.FileUtil;
import com.ailide.apartmentsabc.views.MyApplication;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.ailide.apartmentsabc.views.mine.MyEquitmentActivity;
import com.ailide.apartmentsabc.views.sticker.BillPrePrintActivity;
import com.ailide.apartmentsabc.views.sticker.StickerActivity;
import com.ailide.apartmentsabc.views.sticker.WebPrePrintActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ailide.apartmentsabc.framework.util.KeyBoardUtils.hideInputForce;

public class WebActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @BindView(R.id.tv_manage)
    TextView mTvManage;
    @BindView(R.id.iv_collect)
    ImageView mIvCollect;
    @BindView(R.id.rv_web_tag)
    RecyclerView mRvWebTag;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.iv_reduce)
    ImageView mIvReduce;
    @BindView(R.id.iv_add)
    ImageView mIvAdd;
    @BindView(R.id.iv_refresh)
    ImageView mIvRefresh;
    @BindView(R.id.et_web)
    EditText mEtWeb;

    private WebTagAdapter mTagAdapter;
    private List<WebTag> mWebTags;
    private Bitmap mWebBitmp;

    private int scale = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSdkVersion();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView() {
        mWebTags = new ArrayList<>();
        WebTag tag = new WebTag("添加分组");
        mWebTags.add(tag);
        mTagAdapter = new WebTagAdapter(mWebTags);
        mTagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == mWebTags.size() - 1) {
                    final UserBean userBean = JSON.parseObject(SPUtil.get(WebActivity.this, "user", "").toString(), UserBean.class);
                    if (userBean == null) {
                        toast("您还未登录");
                    } else {
                        new MaterialDialog.Builder(WebActivity.this)
                                .title(R.string.create_group)
                                .titleGravity(GravityEnum.CENTER)
                                .input(R.string.hint_input_group_name, 0, false, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        OkGo.<String>post(Urls.WEB_TAG_ADD)
                                                .tag(this)
                                                .params("mid", userBean.getData().getId())
                                                .params("tag_name", input.toString())
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(Response<String> response) {
                                                        if (response.isSuccessful()) {
                                                            AddWebTag addWebTag = JSON.parseObject(response.body(), AddWebTag.class);
                                                            if (addWebTag.getStatus() == 1) {
                                                                toast("添加成功");
                                                                getData();
                                                            } else {
                                                                toast("添加分组失败");
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .negativeText(R.string.cancel)
                                .show();
                    }
                }
            }
        });
        mTagAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final UserBean userBean = JSON.parseObject(SPUtil.get(WebActivity.this, "user", "").toString(), UserBean.class);
                if (userBean == null) {
                    toast("您还未登录");
                } else {
                    OkGo.<String>post(Urls.WEB_TAG_DELETE)
                            .tag(this)
                            .params("mid", userBean.getData().getId())
                            .params("tag_id", mWebTags.get(position).getId())
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    if (response.isSuccessful()) {
                                        AddWebTag addWebTag = JSON.parseObject(response.body(), AddWebTag.class);
                                        if (addWebTag.getStatus() == 1) {
                                            toast(addWebTag.getMsg());
                                            getData();
                                        } else {
                                            toast(addWebTag.getMsg());
                                        }
                                    }
                                }
                            });
                }
            }
        });
        mRvWebTag.setLayoutManager(new LinearLayoutManager(this));
        mRvWebTag.setAdapter(mTagAdapter);
        mRvWebTag.setNestedScrollingEnabled(false);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        mWebview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mWebview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mWebview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebview.getSettings().setAppCacheEnabled(false);//是否使用缓存
        mWebview.getSettings().setDomStorageEnabled(false);//DOM Storage
        mWebview.getSettings().setDisplayZoomControls(false);
        mWebview.setDrawingCacheEnabled(true);
        mWebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                updateCollect();
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        mEtWeb.setOnEditorActionListener(this);
        mIvAdd.setColorFilter(getResources().getColor(R.color.btn_web_gray));
        mIvReduce.setColorFilter(getResources().getColor(R.color.btn_web_gray));
        mIvRefresh.setColorFilter(getResources().getColor(R.color.btn_web_gray));
    }

    private void getData() {
        final UserBean userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        if (userBean == null) {
            toast("您还未登录");
        } else {
            OkGo.<String>post(Urls.WEB)
                    .tag(this)
                    .params("mid", userBean.getData().getId())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (response.isSuccessful()) {
                                WebTags webTags = JSON.parseObject(response.body(), WebTags.class);
                                mWebTags.clear();
                                mWebTags.addAll(webTags.getData());
                                WebTag tag = new WebTag("添加分组");
                                mWebTags.add(tag);
                                mTagAdapter.notifyDataSetChanged();
                                updateCollect();
                            }
                        }
                    });
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_manage, R.id.iv_collect, R.id.iv_left, R.id.iv_right,R.id.iv_print, R.id.iv_add, R.id.iv_reduce, R.id.iv_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_manage:
                mTagAdapter.setManage(!mTagAdapter.isManage());
                if (mTagAdapter.isManage()) {
                    mTvManage.setText(R.string.complete);
                } else {
                    mTvManage.setText(R.string.manage);
                }
                break;
            case R.id.iv_collect:
                final UserBean userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
                if (userBean == null) {
                    toast("您还未登录");
                } else if(!TextUtils.isEmpty(mWebview.getUrl())){
                    Web nowWeb = null;
                    for (WebTag webTag :
                            mWebTags) {
                        for (Web web :
                                webTag.getWeb()) {
                            if (mWebview.getUrl().equals(web.getWebsite())) {
                                nowWeb = web;
                                break;
                            }
                        }
                        if (nowWeb != null) {
                            break;
                        }
                    }
                    if (nowWeb != null) {
                        OkGo.<String>post(Urls.WEB_DELETE)
                                .tag(this)
                                .params("mid", userBean.getData().getId())
                                .params("web_id", nowWeb.getId())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        if (response.isSuccessful()) {
                                            StatusResponse statusResponse = JSON.parseObject(response.body(), StatusResponse.class);
                                            if (statusResponse.getStatus() == 1) {
                                                toast("取消收藏");
                                                getData();
                                            } else {
                                                toast(statusResponse.getMsg());
                                            }
                                        }
                                    }
                                });
                    } else {
                        showCollectDialog();
                    }
                }
                break;
            case R.id.iv_left:
                goWebBack();
                break;
            case R.id.iv_right:
                if (mWebview.getVisibility() == View.VISIBLE && mWebview.canGoForward()) {
                    mWebview.goForward();
                    updateStatus();
                }
                break;
            case R.id.iv_add:
                if (mWebview.getVisibility() == View.VISIBLE) {
//                    mWebview.zoomIn();
                    scale +=30;
                    mWebview.getSettings().setTextZoom(scale);
                }
                break;
            case R.id.iv_reduce:
                if (mWebview.getVisibility() == View.VISIBLE) {
//                    mWebview.zoomOut();
                    scale -=30;
                    mWebview.getSettings().setTextZoom(scale);
                }
                break;
            case R.id.iv_refresh:
                if (mWebview.getVisibility() == View.VISIBLE) {
                    mWebview.reload();
                }
                break;
            case R.id.iv_print:
                List<String> items = new ArrayList<>();
                items.add("当前页");
                items.add("全部页");
                new MaterialDialog.Builder(this)
                        .title("网页打印")
                        .items(items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if(which == 0){
                                    if(PrintFragment.printPP!=null && PrintFragment.printPP.isConnected()){
                                        getNowSnapshot();
                                    }else {
                                        showPopView();
                                    }
                                }else if(which == 1){
                                    if(PrintFragment.printPP!=null && PrintFragment.printPP.isConnected()){
                                        getAllSnapshot();
                                    }else {
                                        showPopView();
                                    }
                                }
                            }
                        })
                        .show();
                break;
        }
    }
    private PopupWindow popupWindow;
    private void showPopView() {
        View layout = getLayoutInflater().inflate(R.layout.pop_add_equitment, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, mIvCollect, 0, 0, 1, 0.5f, true);
        TextView pop_equitment_add = layout.findViewById(R.id.pop_equitment_add);
        TextView pop_equitment_not = layout.findViewById(R.id.pop_equitment_not);
        TextView equipment_tv = layout.findViewById(R.id.equipment_tv);
        pop_equitment_add.setText("连 接 设 备");
        pop_equitment_not.setText("取 消 打 印");
        equipment_tv.setText("还没有连接到设备哦");
        pop_equitment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                jumpToOtherActivity(new Intent(WebActivity.this, MyEquitmentActivity.class),false);
            }
        });
        pop_equitment_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    private void getNowSnapshot() {
        mWebBitmp = mWebview.getDrawingCache();
        try {
            File file = FileUtil.getWebFile();
            FileOutputStream fos = new FileOutputStream(file);
            //压缩bitmap到输出流中
            mWebBitmp.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.close();
            Intent intent = new Intent(this, WebPrePrintActivity.class);
            intent.putExtra("path", file.getAbsolutePath());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllSnapshot() {
        float scale = mWebview.getScale();
        int webViewHeight = (int) (mWebview.getContentHeight()*scale+0.5);
        Bitmap bitmap = Bitmap.createBitmap(mWebview.getWidth(),webViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mWebview.draw(canvas);
        try {
            File file = FileUtil.getWebFile();
            FileOutputStream fos = new FileOutputStream(file);
            //压缩bitmap到输出流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.close();
            bitmap.recycle();
            Intent intent = new Intent(this, WebPrePrintActivity.class);
            intent.putExtra("path", file.getAbsolutePath());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 当系统版本大于5.0时 开启enableSlowWholeDocumentDraw 获取整个html文档内容
     */
    private void checkSdkVersion() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEntity event) {
        switch (event.getId()) {
            case EventConstant.SHOW_WEB:
                if (mTagAdapter.isManage()) {
                    return;
                }
                mRvWebTag.setVisibility(View.GONE);
                mWebview.setVisibility(View.VISIBLE);
                scale = 100;
                mWebview.getSettings().setTextZoom(scale);
//                mWebview.loadUrl("http://www.jiankeyan.com");
                mWebview.loadUrl((String) event.getData());
                mWebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebview.clearHistory();
                    }
                }, 1000);
                updateStatus();
                break;
            case EventConstant.DELETE_WEB:
                final UserBean userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
                if (userBean == null) {
                    toast("您还未登录");
                } else {
                    OkGo.<String>post(Urls.WEB_DELETE)
                            .tag(this)
                            .params("mid", userBean.getData().getId())
                            .params("web_id", ((Web) event.getData()).getId())
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    if (response.isSuccessful()) {
                                        StatusResponse statusResponse = JSON.parseObject(response.body(), StatusResponse.class);
                                        if (statusResponse.getStatus() == 1) {
                                            toast(statusResponse.getMsg());
                                            getData();
                                        } else {
                                            toast(statusResponse.getMsg());
                                        }
                                    }
                                }
                            });
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mRvWebTag.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        }
        goWebBack();
    }

    public void goWebBack() {
        if (mWebview.canGoBack()) {
            mWebview.goBack();
        } else {
            mRvWebTag.setVisibility(View.VISIBLE);
            mWebview.setVisibility(View.GONE);
        }
        updateStatus();
    }

    public void updateStatus() {
        if (mRvWebTag.getVisibility() == View.VISIBLE) {
            mIvAdd.setColorFilter(getResources().getColor(R.color.btn_web_gray));
            mIvReduce.setColorFilter(getResources().getColor(R.color.btn_web_gray));
            mIvRefresh.setColorFilter(getResources().getColor(R.color.btn_web_gray));
            mIvLeft.setImageResource(R.drawable.icon_turn_left_n);
            mIvRight.setImageResource(R.drawable.icon_turn_right_n);
            mTvManage.setVisibility(View.VISIBLE);
            mIvCollect.setVisibility(View.GONE);
//            mWebview.clearHistory();
           /* mWebview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebview.clearHistory();
                }
            }, 1000);*/
            mEtWeb.setText("");
        } else {
            mIvAdd.clearColorFilter();
            mIvReduce.clearColorFilter();
            mIvRefresh.clearColorFilter();
            mTvManage.setVisibility(View.GONE);
            mIvCollect.setVisibility(View.VISIBLE);
            mIvLeft.setImageResource(R.drawable.icon_turn_left_p);
            if (mWebview.canGoForward()) {
                mIvRight.setImageResource(R.drawable.icon_turn_right_p);
            } else {
                mIvRight.setImageResource(R.drawable.icon_turn_right_n);
            }
        }
    }

    public void showCollectDialog() {
        final UserBean userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        if (userBean == null) {
            toast("您还未登录");
        } else {
            View layout = getLayoutInflater().inflate(R.layout.dialog_collect_web, null);
            final NiceSpinner spinner = layout.findViewById(R.id.nice_spinner);
            spinner.setGravity(Gravity.CENTER);
            final TextView tvCancel = layout.findViewById(R.id.tv_cancel);
            TextView tvConfirm = layout.findViewById(R.id.tv_confirm);
            final EditText etName = layout.findViewById(R.id.et_name);
            List<String> dataset = new ArrayList<>();
            for (int i = 0; i < mWebTags.size() - 1; i++) {
                dataset.add(mWebTags.get(i).getTag_name());
            }
            spinner.attachDataSource(dataset);
            spinner.setHint(R.string.hint_choose_group);
            final MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .customView(layout, false)
                    .show();
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(etName.getText().toString())) {
                        toast("您还未输入标签");
                    } else {
                        dialog.dismiss();
                        spinner.getSelectedIndex();
                        OkGo.<String>post(Urls.WEB_ADD)
                                .tag(this)
                                .params("mid", userBean.getData().getId())
                                .params("tag_id", mWebTags.get(spinner.getSelectedIndex()).getId())
                                .params("web_name", etName.getText().toString())
                                .params("website", mWebview.getUrl())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        if (response.isSuccessful()) {
                                            StatusResponse statusResponse = JSON.parseObject(response.body(), StatusResponse.class);
                                            if (statusResponse.getStatus() == 1) {
                                                toast("已经收藏");
                                                getData();
                                            } else {
                                                toast(statusResponse.getMsg());
                                            }
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }

    public void updateCollect() {
        if (mRvWebTag.getVisibility() == View.VISIBLE) {
            mIvCollect.setVisibility(View.GONE);
        } else {
            mIvCollect.setVisibility(View.VISIBLE);
            boolean collect = false;
            if(!TextUtils.isEmpty(mWebview.getUrl())){
                for (WebTag webTag :
                        mWebTags) {
                    for (Web web :
                            webTag.getWeb()) {
                        if (mWebview.getUrl().equals(web.getWebsite())) {
                            collect = true;
                            break;
                        }
                    }
                    if (collect) {
                        break;
                    }
                }
            }
            if (collect) {
                mIvCollect.setImageResource(R.drawable.icon_web_collect_p);
            } else {
                mIvCollect.setImageResource(R.drawable.icon_web_collect_n);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        if(mWebBitmp != null){
            mWebBitmp.recycle();
            mWebBitmp = null;
        }
        if(popupWindow!=null)
            popupWindow.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String contentStr = mEtWeb.getText().toString();
        if (!TextUtils.isEmpty(contentStr)) {
            String url;
            if (Patterns.WEB_URL.matcher(contentStr).matches()) {
                url = contentStr;
            } else {
                url = "http://www.baidu.com/s?wd=" + contentStr + "&rsv_bp=0&ch=&tn=baidu&bar=&rsv_spt=3&ie=utf-8&rsv_sug3=3&rsv_sug=0&rsv_sug4=95&rsv_sug1=1&inputT=1001";
            }
            mRvWebTag.setVisibility(View.GONE);
            mWebview.setVisibility(View.VISIBLE);
            scale = 100;
            mWebview.getSettings().setTextZoom(scale);
            mWebview.loadUrl(url);
            mWebview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebview.clearHistory();
                }
            }, 1000);
            hideInputForce(this);
            updateStatus();
        }
        return true;
    }
}