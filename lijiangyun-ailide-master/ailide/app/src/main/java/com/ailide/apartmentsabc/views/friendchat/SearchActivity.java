package com.ailide.apartmentsabc.views.friendchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.TenFriendBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.friend_search)
    LinearLayout friendSearch;
    @BindView(R.id.friend_add)
    TextView friendAdd;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.edit)
    EditText edit;

    private List<TenFriendBean.DataBean> menuList = new ArrayList<>();
    private RecycleViewAdapter recycleViewAdapter;
    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString()))
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        initRecycleView();
    }

    private void initRecycleView() {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleViewAdapter = new RecycleViewAdapter(R.layout.item_search_friend, menuList);
//        recycleViewAdapter.setOnLoadMoreListener(this, appointmentMainRecycleView);
        recycleView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchActivity.this, FriendChatActivity.class);
                intent.putExtra("fid",menuList.get(position).getId());
                intent.putExtra("title",menuList.get(position).getScreen_name());
                jumpToOtherActivity(intent, false);
            }
        });
    }

    private class RecycleViewAdapter extends BaseQuickAdapter<TenFriendBean.DataBean, BaseViewHolder> {
        public RecycleViewAdapter(@LayoutRes int layoutResId, @Nullable List<TenFriendBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TenFriendBean.DataBean item) {
            ImageView imageView = helper.getView(R.id.head_img);
            if(!TextUtils.isEmpty(item.getProfile_image_url()))
                NetWorkImageLoader.loadCircularImage(null,item.getProfile_image_url(),imageView);
            if(!TextUtils.isEmpty(item.getScreen_name()))
                helper.setText(R.id.name,item.getScreen_name());
            if(!TextUtils.isEmpty(item.getNote_num()))
                helper.setText(R.id.zhitiao_num,"熊号：" + item.getNote_num());
        }
    }

    @OnClick({R.id.iv_include_back, R.id.friend_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.friend_add:
                if(TextUtils.isEmpty(edit.getText().toString().trim())){
                    toast("查找内容不能为空");
                    return;
                }
                addFriend();
                break;
        }
    }

    private void addFriend() {
        showLoading("加载中...");
        OkGo.<String>post(Urls.SEARCH_MY_FRIEND)//
                .tag(this)//
                .params("id", userBean.getData().getId())
                .params("note_num", Long.parseLong(edit.getText().toString().trim()))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("check_friend", response.body());
                            TenFriendBean tenFriendBean = JSON.parseObject(response.body(), TenFriendBean.class);
                            if (tenFriendBean.getStatus() == 1) {
                                if(tenFriendBean.getData()!=null){
                                    menuList.clear();
                                    menuList.addAll(tenFriendBean.getData());
                                    recycleViewAdapter.setNewData(menuList);
                                }
                            } else {
                                toast(tenFriendBean.getMsg());
                            }
                        }
                    }
                });
    }
}
