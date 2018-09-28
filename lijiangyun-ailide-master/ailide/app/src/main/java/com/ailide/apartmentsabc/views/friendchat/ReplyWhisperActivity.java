package com.ailide.apartmentsabc.views.friendchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.ReplyWhisperBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.model.WhisperListBean;
import com.ailide.apartmentsabc.tools.ReadUtil;
import com.ailide.apartmentsabc.tools.TimeUtils;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.utils.FileUtil;
import com.ailide.apartmentsabc.views.MyApplication;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.ailide.apartmentsabc.views.mine.MyEquitmentActivity;
import com.ailide.apartmentsabc.views.sticker.PrePrintActivity;
import com.ailide.apartmentsabc.views.sticker.StickerActivity;
import com.ailide.apartmentsabc.views.sticker.WebPrePrintActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReplyWhisperActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private UserBean userBean;
    private int index=0;
    private List<ReplyWhisperBean.DataBean> menuList = new ArrayList<>();
    private RecycleViewAdapter recycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_whisper);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("悄悄话详情");
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString()))
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        swipeRefreshLayout.setOnRefreshListener(this);
        initRecycleView();
        getList();
    }
    private void getAllSnapshot(WebView mWebview) {
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
            Intent intent = new Intent(this, PrePrintActivity.class);
            intent.putExtra("path", file.getAbsolutePath());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initRecycleView() {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleViewAdapter = new RecycleViewAdapter(R.layout.item_reply_whisper, menuList);
//        recycleViewAdapter.setOnLoadMoreListener(this, appointmentMainRecycleView);
        recycleView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }
    private class RecycleViewAdapter extends BaseQuickAdapter<ReplyWhisperBean.DataBean, BaseViewHolder> {
        public RecycleViewAdapter(@LayoutRes int layoutResId, @Nullable List<ReplyWhisperBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ReplyWhisperBean.DataBean item) {
            if(item.getCreate_time()!=0)
                helper.setText(R.id.time,TimeUtils.formatTimeMinute(item.getCreate_time()*1000));
            if(!TextUtils.isEmpty(item.getFriend_name()))
                helper.setText(R.id.name,"FROM："+item.getFriend_name());
            final WebView htmlWebView = helper.getView(R.id.webView);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                htmlWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            htmlWebView.loadDataWithBaseURL(null, ReadUtil.getNewContent(item.getContent()), "text/html", "utf-8", null);
            htmlWebView.getSettings().setJavaScriptEnabled(true);//启用js
            htmlWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
            Logger.e("item",item.getContent());
            helper.getView(R.id.tv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(PrintFragment.printPP != null&&PrintFragment.printPP.isConnected()){
                        getAllSnapshot(htmlWebView);
                    }else {
                        showPopView();
                    }
                }
            });
        }
    }
    private PopupWindow popupWindow;
    private void showPopView() {
        View layout = getLayoutInflater().inflate(R.layout.pop_add_equitment, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, tvIncludeTitle, 0, 0, 1, 0.5f, true);
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
                jumpToOtherActivity(new Intent(ReplyWhisperActivity.this, MyEquitmentActivity.class),false);
            }
        });
        pop_equitment_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(popupWindow!=null)
            popupWindow.dismiss();
    }

    private void getList(){
        if (index ==0)
            showLoading("加载中...");
        OkGo.<String>post(Urls.REPLY_WHISPER)//
                .tag(this)//
                .params("mid", userBean.getData().getId())//
                .params("wid", getIntent().getIntExtra("wid",0))//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if(swipeRefreshLayout!=null)
                            swipeRefreshLayout.setRefreshing(false);
                        if (response.isSuccessful()) {
                            Logger.e("body",response.body());
                            ReplyWhisperBean friendListBean = JSON.parseObject(response.body(),ReplyWhisperBean.class);
                            if(friendListBean.getStatus()==1){
                                menuList.clear();
                                menuList.addAll(friendListBean.getData());
                                recycleViewAdapter.setNewData(menuList);
                            }else {
                                toast(friendListBean.getMsg());
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

    @Override
    public void onRefresh() {
        index = 100;
        getList();
    }
}
