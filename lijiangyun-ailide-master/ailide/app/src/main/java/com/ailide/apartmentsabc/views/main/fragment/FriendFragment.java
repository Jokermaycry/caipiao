package com.ailide.apartmentsabc.views.main.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.DrawEvent;
import com.ailide.apartmentsabc.eventbus.RefreshEvent;
import com.ailide.apartmentsabc.eventbus.RefreshFriendEvent;
import com.ailide.apartmentsabc.model.BaseDateBean;
import com.ailide.apartmentsabc.model.FriendListBean;
import com.ailide.apartmentsabc.model.PrintNameBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.TimeUtils;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.tools.view.MakeSureDialog;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.ailide.apartmentsabc.views.friendchat.AddFriendActivity;
import com.ailide.apartmentsabc.views.friendchat.FriendChatActivity;
import com.ailide.apartmentsabc.views.friendchat.FriendDetailActivity;
import com.ailide.apartmentsabc.views.friendchat.SearchActivity;
import com.ailide.apartmentsabc.views.friendchat.WhispersActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends BaseSimpleFragment implements SwipeRefreshLayout.OnRefreshListener{


    @BindView(R.id.friend_add)
    ImageView friendAdd;
    @BindView(R.id.friend_recycle_view)
    RecyclerView friendRecycleView;
    @BindView(R.id.friend_qiao_hua)
    ImageView friendQiaoHua;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<FriendListBean.DataBean> menuList = new ArrayList<>();
    private RecycleViewAdapter recycleViewAdapter;
    private UserBean userBean;
    private MakeSureDialog makeSureDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        swipeRefreshLayout.setOnRefreshListener(this);
        EventBus.getDefault().register(this);
        initRecycleView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(makeSureDialog!=null)
            makeSureDialog.dismiss();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        if(!TextUtils.isEmpty(SPUtil.get(mActivity,"user","").toString())){
            userBean = JSON.parseObject(SPUtil.get(mActivity,"user","").toString(),UserBean.class);
            getList();
        }
    }

    private void getList(){
        OkGo.<String>post(Urls.FRIEND_LIST)//
                .tag(this)//
                .params("id", userBean.getData().getId())//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!isAdded())
                            return;
                        if(swipeRefreshLayout!=null)
                            swipeRefreshLayout.setRefreshing(false);
                        if (response.isSuccessful()) {
                            Logger.e("eeeeeee",response.body());
                            FriendListBean friendListBean = JSON.parseObject(response.body(),FriendListBean.class);
                            if(friendListBean.getStatus()==1){
                                menuList.clear();
                                menuList.addAll(friendListBean.getData());
                                recycleViewAdapter.setNewData(menuList);
                                if(menuList.size()<=0){
                                    View view = mActivity.getLayoutInflater().inflate(R.layout.empty_view_two, null);
                                    recycleViewAdapter.setEmptyView(view);
                                }
                            }else {
                                mActivity.toast(friendListBean.getMsg());
                            }
                        }
                    }
                });
    }

    private void initRecycleView() {
        friendRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        friendRecycleView.setNestedScrollingEnabled(false);
        friendRecycleView.setItemAnimator(new DefaultItemAnimator());
        recycleViewAdapter = new RecycleViewAdapter(R.layout.item_friend, menuList);
//        recycleViewAdapter.setOnLoadMoreListener(this, appointmentMainRecycleView);
        friendRecycleView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.confirm:
                        passFriend(menuList.get(position).getId()+"");
                        break;
                    case R.id.head_img:
                        Intent intent=new Intent(mActivity, FriendDetailActivity.class);
                        intent.putExtra("note_num",menuList.get(position).getNote_num());
                        mActivity.jumpToOtherActivity(intent,false);
                        break;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getList();
    }


    private class RecycleViewAdapter extends BaseQuickAdapter<FriendListBean.DataBean, BaseViewHolder> {
        public RecycleViewAdapter(@LayoutRes int layoutResId, @Nullable List<FriendListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FriendListBean.DataBean item) {
            ImageView imageView = helper.getView(R.id.head_img);
            if(!TextUtils.isEmpty(item.getProfile_image_url()))
                NetWorkImageLoader.loadCircularImage(null,item.getProfile_image_url(),imageView,R.drawable.icon_circle_mrtx,R.drawable.icon_circle_mrtx);
            if(!TextUtils.isEmpty(item.getScreen_name()))
                helper.setText(R.id.name,item.getScreen_name());
            if(!TextUtils.isEmpty(item.getNote_num()))
                helper.setText(R.id.zhitiao_num,item.getNote_num());
            if(item.getIs_via()==1){
                helper.setVisible(R.id.time,true);
                helper.setText(R.id.time, TimeUtils.formatDate(item.getCreate_time()*1000));
                helper.setVisible(R.id.confirm,false);
                helper.addOnClickListener(R.id.head_img);
            }else if(item.getIs_via() == 0){
                helper.setVisible(R.id.time,false);
                helper.setVisible(R.id.confirm,true);
                helper.addOnClickListener(R.id.confirm);
            }
        }
    }

    @Override
    protected void setListener() {
        recycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(menuList.get(position).getIs_via()==0){
                    mActivity.toast("还不是好友");
                    return;
                }
                Intent intent = new Intent(mActivity, FriendChatActivity.class);
                intent.putExtra("fid",menuList.get(position).getId());
                intent.putExtra("title",menuList.get(position).getScreen_name());
                mActivity.jumpToOtherActivity(intent, false);
            }
        });
        recycleViewAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showMakeDialog(menuList.get(position).getId()+"");
                return true;
            }
        });
    }

    private void showMakeDialog(final String friend_id){
        makeSureDialog = new MakeSureDialog();
        makeSureDialog.setContent("确实删除好友吗？");
        makeSureDialog.setDialogClickListener(new MakeSureDialog.onDialogClickListener() {
            @Override
            public void onSureClick() {
                makeSureDialog.dismiss();
                deleteFriend(friend_id);
            }

            @Override
            public void onCancelClick() {
                makeSureDialog.dismiss();
            }
        });
        makeSureDialog.show(getFragmentManager(),"");
    }

    private void deleteFriend(String friend_id){
        showLoading("加载中...");
        OkGo.<String>post(Urls.DELETE_FRIEND)//
                .tag(this)//
                .params("id", userBean.getData().getId())//
                .params("friend_id",friend_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!isAdded())
                            return;
                        dismissLoading();
                        Logger.e("eeeeeee",response.body());
                        if (response.isSuccessful()) {
                            BaseDateBean baseDateBean = JSON.parseObject(response.body(),BaseDateBean.class);
                            if(baseDateBean.getStatus()==1){
                                mActivity.toast("删除成功");
                                getList();
                            }else {
                                mActivity.toast(baseDateBean.getMsg());
                            }
                        }
                    }
                });
    }
    private void passFriend(String friend_id){
        showLoading("加载中...");
        OkGo.<String>post(Urls.ADOPT_FRIEND)//
                .tag(this)//
                .params("mid", userBean.getData().getId())//
                .params("friend_id",friend_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!isAdded())
                            return;
                        dismissLoading();
                        Logger.e("eeeeeee",response.body());
                        if (response.isSuccessful()) {
                            BaseDateBean baseDateBean = JSON.parseObject(response.body(),BaseDateBean.class);
                            if(baseDateBean.getStatus()==1){
                                mActivity.toast("添加成功");
                                getList();
                            }else {
                                mActivity.toast(baseDateBean.getMsg());
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.friend_add, R.id.friend_qiao_hua,R.id.friend_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.friend_add:
                if(TextUtils.isEmpty(SPUtil.get(mActivity,"user","").toString())){
                    mActivity.toast("没有登录");
                    return;
                }
                mActivity.jumpToOtherActivity(new Intent(mActivity, AddFriendActivity.class),false);
                break;
            case R.id.friend_qiao_hua:
                if(TextUtils.isEmpty(SPUtil.get(mActivity,"user","").toString())){
                    mActivity.toast("没有登录");
                    return;
                }
                mActivity.jumpToOtherActivity(new Intent(mActivity, WhispersActivity.class),false);
                break;
            case R.id.friend_search:
                if(TextUtils.isEmpty(SPUtil.get(mActivity,"user","").toString())){
                    mActivity.toast("没有登录");
                    return;
                }
                mActivity.jumpToOtherActivity(new Intent(mActivity, SearchActivity.class),false);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        getList();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshFriendEvent event) {
        getList();
    }
}
