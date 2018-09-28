package com.ailide.apartmentsabc.views.friendchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.FriendListBean;
import com.ailide.apartmentsabc.model.ShareBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.model.WhisperListBean;
import com.ailide.apartmentsabc.tools.ShareUtil;
import com.ailide.apartmentsabc.tools.TimeUtils;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.tools.view.MakeSureDialog;
import com.ailide.apartmentsabc.views.addequitment.AddEquitmentActivity;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.MainActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WhispersActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.friend_recycle_view)
    RecyclerView friendRecycleView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.friend_qiao_hua)
    ImageView friendQiaoHua;


    private List<WhisperListBean.DataBean> menuList = new ArrayList<>();
    private RecycleViewAdapter recycleViewAdapter;
    private UserBean userBean;
    private int index=0;
    private ShareBean shareBean;
    private PopupWindow popupWindow;
    private MakeSureDialog makeSureDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whispers);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("悄悄话");
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString()))
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        swipeRefreshLayout.setOnRefreshListener(this);
        initRecycleView();
        getList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(popupWindow!=null)
            popupWindow.dismiss();
        if(makeSureDialog!=null)
            makeSureDialog.dismiss();
    }

    private void initRecycleView() {
        friendRecycleView.setLayoutManager(new LinearLayoutManager(this));
        friendRecycleView.setNestedScrollingEnabled(false);
        friendRecycleView.setItemAnimator(new DefaultItemAnimator());
        recycleViewAdapter = new RecycleViewAdapter(R.layout.item_qiaoqiaohua, menuList);
//        recycleViewAdapter.setOnLoadMoreListener(this, appointmentMainRecycleView);
        friendRecycleView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(WhispersActivity.this,ReplyWhisperActivity.class);
                intent.putExtra("wid",menuList.get(position).getId());
                jumpToOtherActivity( intent,false);
            }
        });
        recycleViewAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                shareBean = new ShareBean();
                shareBean.setShare_content(menuList.get(position).getContent());
                shareBean.setShare_url(menuList.get(position).getUrl());
                shareBean.setShare_title(menuList.get(position).getTitle());
                showPopView();
            }
        });
        recycleViewAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showMakeDialog(menuList.get(position).getId()+"",position);
                return true;
            }
        });
//        recycleViewAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//
//            }
//        },friendRecycleView);
    }
    private void showMakeDialog(final String friend_id, final int position){
        makeSureDialog = new MakeSureDialog();
        makeSureDialog.setContent("确定删除该分享？");
        makeSureDialog.setDialogClickListener(new MakeSureDialog.onDialogClickListener() {
            @Override
            public void onSureClick() {
                makeSureDialog.dismiss();
                deleteWhisper(friend_id,position);
            }

            @Override
            public void onCancelClick() {
                makeSureDialog.dismiss();
            }
        });
        makeSureDialog.show(getSupportFragmentManager(),"");
    }
    private void deleteWhisper(String friend_id, final int position){
        showLoading("加载中...");
        OkGo.<String>post(Urls.DELETE_WHISPER)//
                .tag(this)//
                .params("mid", userBean.getData().getId())//
                .params("wid", friend_id)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("body",response.body());
                            WhisperListBean friendListBean = JSON.parseObject(response.body(),WhisperListBean.class);
                            if(friendListBean.getStatus()==1){
                                menuList.remove(position);
                                recycleViewAdapter.setNewData(menuList);
                            }else {
                                toast(friendListBean.getMsg());
                            }

                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        index = 100;
        getList();
    }
    private void getList(){
        if (index ==0)
        showLoading("加载中...");
        OkGo.<String>post(Urls.WHISPERS_LIST)//
                .tag(this)//
                .params("mid", userBean.getData().getId())//
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
                            WhisperListBean friendListBean = JSON.parseObject(response.body(),WhisperListBean.class);
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

    private class RecycleViewAdapter extends BaseQuickAdapter<WhisperListBean.DataBean, BaseViewHolder> {
        public RecycleViewAdapter(@LayoutRes int layoutResId, @Nullable List<WhisperListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WhisperListBean.DataBean item) {
            ImageView imageView = helper.getView(R.id.head_img);
            if(item.getSource().equals("1")){
                imageView.setBackgroundResource(R.drawable.qiaoqiaohua_wx);
            }else {
                imageView.setBackgroundResource(R.drawable.qiaoqiaohua_qq_two);
            }
            if(item.getCreate_time()!=0)
                helper.setText(R.id.name, TimeUtils.formatTimeMinute(item.getCreate_time()*1000));
            if(!TextUtils.isEmpty(item.getContent()))
                helper.setText(R.id.zhitiao_num,item.getContent());
            helper.addOnClickListener(R.id.share_img);

        }
    }

    @OnClick({R.id.iv_include_back, R.id.friend_qiao_hua})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.friend_qiao_hua:
                jumpToOtherActivity(new Intent(this,NewWhisperActivity.class),false);
                break;
        }
    }
    private void showPopView() {
        SPUtil.put(this,"first_equitment","1");
        View layout = getLayoutInflater().inflate(R.layout.pop_whispers, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, tvIncludeTitle, 0, 0, 1, 0.5f, true);
        TextView qq = layout.findViewById(R.id.qq);
        TextView weixin = layout.findViewById(R.id.weixin);
        TextView pengyouquan = layout.findViewById(R.id.pengyouquan);
        TextView kongjian = layout.findViewById(R.id.kongjian);
        TextView cancel = layout.findViewById(R.id.cancel);
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                share(SHARE_MEDIA.QQ);
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                share(SHARE_MEDIA.WEIXIN);
            }
        });
        pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        kongjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                share(SHARE_MEDIA.QZONE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
