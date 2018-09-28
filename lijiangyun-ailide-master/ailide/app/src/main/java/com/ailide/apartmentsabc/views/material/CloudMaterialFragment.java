package com.ailide.apartmentsabc.views.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.MaterialClass;
import com.ailide.apartmentsabc.model.MaterialClasses;
import com.ailide.apartmentsabc.model.MaterialGroup;
import com.ailide.apartmentsabc.model.MaterialGroups;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CloudMaterialFragment extends BaseSimpleFragment {

    @BindView(R.id.sr_common)
    SwipeRefreshLayout mSrCommon;
    @BindView(R.id.rv_class)
    RecyclerView mRvClass;
    @BindView(R.id.rv_common_group)
    RecyclerView mRvCommonGroup;

    private MaterialClassAdapter mClassAdapter;
    private List<MaterialClass> mClasses;
    private MaterialGroupAdapter mCommonAdapter;
    private List<MaterialGroup> commonGroups;
    private int currentPage = 0;
    private int classPosition = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cloud_material;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mClasses = new ArrayList<>();
        mClassAdapter = new MaterialClassAdapter(mClasses);
        mClassAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (MaterialClass clazz :
                        mClasses) {
                    clazz.setSelect(false);
                }
                classPosition = position;
                mClasses.get(classPosition).setSelect(true);
                mClassAdapter.notifyDataSetChanged();
                mCommonAdapter.setEnableLoadMore(true);
                getMaterialTag(0, mClasses.get(classPosition).getId());
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvClass.setLayoutManager(manager);
        mRvClass.setAdapter(mClassAdapter);

        commonGroups = new ArrayList<>();
        mCommonAdapter = new MaterialGroupAdapter(commonGroups, "");
        mCommonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.TO_MATERIAL_DETAIL, commonGroups.get(position).getId()));
            }
        });
        mCommonAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getMaterialTag(currentPage + 1, mClasses.get(classPosition).getId());
            }
        }, mRvCommonGroup);
        mCommonAdapter.disableLoadMoreIfNotFullPage();
        mRvCommonGroup.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvCommonGroup.setAdapter(mCommonAdapter);
        mSrCommon.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (classPosition < mClasses.size()) {
                    OkGo.<String>post(Urls.MATERIA_TAG)
                            .tag(this)
                            .params("page", "0")
                            .params("class_id", mClasses.get(classPosition).getId())
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    mSrCommon.setRefreshing(false);
                                    if (response.isSuccessful()) {
                                        MaterialGroups groups = JSON.parseObject(response.body(), MaterialGroups.class);
                                        currentPage = 0;
                                        commonGroups.clear();
                                        commonGroups.addAll(groups.getData());
                                        mCommonAdapter.setGroupName(mClasses.get(classPosition).getClass_name());
                                        if (groups.getData().size() < 10) {
                                            mCommonAdapter.loadMoreEnd();
                                        } else {
                                            mCommonAdapter.loadMoreComplete();
                                        }
                                        mRvCommonGroup.scrollToPosition(0);
                                    } else {
                                        mCommonAdapter.loadMoreEnd();
                                    }
                                }
                            });
                } else {
                    mSrCommon.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void initData() {
        getMaterialClass();
    }

    @Override
    protected void setListener() {
    }

    public void getMaterialClass() {
        OkGo.<String>post(Urls.MATERIA_CLASS)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            MaterialClasses materialClasses = JSON.parseObject(response.body(), MaterialClasses.class);
                            mClasses.clear();
                            mClasses.addAll(materialClasses.getData());
                            mClassAdapter.notifyDataSetChanged();
                            if (mClasses.size() > classPosition) {
                                for (MaterialClass clazz :
                                        mClasses) {
                                    clazz.setSelect(false);
                                }
                                mClasses.get(classPosition).setSelect(true);
                                mClassAdapter.notifyDataSetChanged();
                                getMaterialTag(0, mClasses.get(classPosition).getId());
                            }
                        }
                    }
                });
    }

    public void getMaterialTag(final int page, int classId) {
        showLoading("请稍后");
        OkGo.<String>post(Urls.MATERIA_TAG)
                .tag(this)
                .params("page", page + "")
                .params("class_id", classId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissLoading();
                        if (response.isSuccessful()) {
                            MaterialGroups groups = JSON.parseObject(response.body(), MaterialGroups.class);
                            currentPage = page;
                            if (page == 0) {
                                commonGroups.clear();
                            }
                            commonGroups.addAll(groups.getData());
                            mCommonAdapter.setGroupName(mClasses.get(classPosition).getClass_name());
                            if (groups.getData().size() < 10) {
                                mCommonAdapter.loadMoreEnd();
                            } else {
                                mCommonAdapter.loadMoreComplete();
                            }
                            if (page == 0 && mRvCommonGroup != null) {
                                mRvCommonGroup.scrollToPosition(0);
                            }
                        } else {
                            mCommonAdapter.loadMoreEnd();
                        }
                    }
                });
    }

    @OnClick({R.id.ll_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                mActivity.jumpToOtherActivity(new Intent(mActivity, SearchMaterialActivity.class), false);
                break;
        }
    }

    public MaterialClass getNowMaterialClass(){
        return mClasses.get(classPosition);
    }
}