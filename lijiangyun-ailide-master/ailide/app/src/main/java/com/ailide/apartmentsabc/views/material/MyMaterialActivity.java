package com.ailide.apartmentsabc.views.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.CollectMaterialGroup;
import com.ailide.apartmentsabc.model.CollectMaterialGroups;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.ToastUtil;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyMaterialActivity extends BaseActivity {

    @BindView(R.id.rv_group)
    RecyclerView mRvGroup;

    private MyMaterialGroupAdapter mCollectAdapter;
    private List<CollectMaterialGroup> collectGroups;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_material);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    public void initView() {
        collectGroups = new ArrayList<>();
        mCollectAdapter = new MyMaterialGroupAdapter(collectGroups);
        mCollectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.TO_MY_MATERIAL_DETAIL, collectGroups.get(position)));

            }
        });
        mRvGroup.setLayoutManager(new LinearLayoutManager(this));
        mRvGroup.setAdapter(mCollectAdapter);
    }

    public void getData() {
        UserBean userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        if (userBean == null) {
            ToastUtil.toast("您还未登录");
        } else {
            OkGo.<String>post(Urls.MATERIA_COLLECT_LIST)
                    .tag(this)
                    .params("mid", userBean.getData().getId()+"")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (response.isSuccessful()) {
                                CollectMaterialGroups groups = JSON.parseObject(response.body(), CollectMaterialGroups.class);
                                collectGroups.clear();
                                collectGroups.addAll(groups.getData());
                                mCollectAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEntity event) {
        switch (event.getId()) {
            case EventConstant.TO_MY_MATERIAL_DETAIL:
                Intent collectIntent = new Intent(MyMaterialActivity.this, MyMaterialDetailActivity.class);
                collectIntent.putExtra("data", (CollectMaterialGroup) event.getData());
                jumpToOtherActivity(collectIntent, false);
                break;
            case EventConstant.MY_MATERIAL:
                finish();
                break;
        }
    }
}