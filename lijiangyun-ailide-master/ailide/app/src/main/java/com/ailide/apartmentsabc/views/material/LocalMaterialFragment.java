package com.ailide.apartmentsabc.views.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.CollectMaterialGroup;
import com.ailide.apartmentsabc.model.CollectMaterialGroups;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
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

public class LocalMaterialFragment extends BaseSimpleFragment {

    @BindView(R.id.sr_collect)
    SwipeRefreshLayout mSrCollect;
    @BindView(R.id.rv_collect_group)
    RecyclerView mRvCollectGroup;

    private List<CollectMaterialGroup> collectGroups;
    private CollectMaterialGroupAdapter mCollectAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_material;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        collectGroups = new ArrayList<>();
        mCollectAdapter = new CollectMaterialGroupAdapter(collectGroups);
        mCollectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.TO_COLLECT_MATERIAL_DETAIL, collectGroups.get(position)));
            }
        });
        mRvCollectGroup.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvCollectGroup.setAdapter(mCollectAdapter);
        mSrCollect.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCollectMaterials();
            }
        });
    }

    @Override
    protected void initData() {
        getCollectMaterials();
    }

    @Override
    protected void setListener() {
    }

    public void getCollectMaterials() {
        UserBean userBean = JSON.parseObject(SPUtil.get(mActivity, "user", "").toString(), UserBean.class);
        if (userBean != null) {
            OkGo.<String>post(Urls.MATERIA_COLLECT_LIST)
                    .tag(this)
                    .params("mid", userBean.getData().getId() + "")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            mSrCollect.setRefreshing(false);
                            if (response.isSuccessful()) {
                                CollectMaterialGroups groups = JSON.parseObject(response.body(), CollectMaterialGroups.class);
                                collectGroups.clear();
                                collectGroups.addAll(groups.getData());
                                mCollectAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        } else {
            mSrCollect.setRefreshing(false);
        }
    }

    public List<CollectMaterialGroup> getCollectGroups() {
        return collectGroups;
    }
}