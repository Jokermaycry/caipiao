package com.ailide.apartmentsabc.views.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.ComonListBean;
import com.ailide.apartmentsabc.model.PrintNameBean;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.FriendFragment;
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

public class CommonProListActivity extends BaseActivity {

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

    private RecycleViewAdapter recycleViewAdapter;
    private List<ComonListBean.DataBean> menuList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_pro_list);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("常见问题");
        post();
    }
    private void post() {
        showLoading("加载中...");
        OkGo.<String>post(Urls.COMMON_PROBEMLE)//
                .tag(this)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            ComonListBean comonListBean = JSON.parseObject(response.body(),ComonListBean.class);
                            menuList.addAll(comonListBean.getData());
                            initRecycleView();
                        }
                    }
                });
    }
    private void initRecycleView() {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleViewAdapter = new RecycleViewAdapter(R.layout.item_common_list, menuList);
//        recycleViewAdapter.setOnLoadMoreListener(this, appointmentMainRecycleView);
        recycleView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent =new Intent(CommonProListActivity.this,CommonProActivity.class);
                intent.putExtra("content",menuList.get(position).getContent());
                jumpToOtherActivity(intent,false);
            }
        });
    }

    private class RecycleViewAdapter extends BaseQuickAdapter<ComonListBean.DataBean, BaseViewHolder> {
        public RecycleViewAdapter(@LayoutRes int layoutResId, @Nullable List<ComonListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ComonListBean.DataBean item) {
            if(!TextUtils.isEmpty(item.getQuestion()))
                helper.setText(R.id.item_main_child_left,item.getQuestion());
            if(helper.getLayoutPosition() == menuList.size() -1){
                helper.setVisible(R.id.item_main_child_line,false);
            }else {
                helper.setVisible(R.id.item_main_child_line,true);
            }
        }
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
