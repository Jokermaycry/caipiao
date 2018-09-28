package com.ailide.apartmentsabc.views.material;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.MaterialGroup;
import com.ailide.apartmentsabc.model.MaterialGroups;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchMaterialActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rv_group)
    RecyclerView mRvGroup;

    private SearchMaterialGroupAdapter mGroupAdapter;
    private List<MaterialGroup> mGroups;
    private int currentPage = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_material);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        mGroups = new ArrayList<>();
        mGroupAdapter = new SearchMaterialGroupAdapter(mGroups);
        mGroupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.TO_MATERIAL_DETAIL, mGroups.get(position).getId()));
            }
        });
        mGroupAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                search(currentPage + 1);
            }
        }, mRvGroup);
        mGroupAdapter.disableLoadMoreIfNotFullPage();
        mRvGroup.setLayoutManager(new LinearLayoutManager(this));
        mRvGroup.setAdapter(mGroupAdapter);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mGroupAdapter.setEnableLoadMore(true);
                if (TextUtils.isEmpty(mEtSearch.getText().toString())) {
                    mGroups.clear();
                    mGroupAdapter.notifyDataSetChanged();
                } else {
                    search(0);
                }
            }
        });
    }

    public void search(final int page) {
        showLoading("请稍后");
        OkGo.<String>post(Urls.MATERIA_TAG)
                .tag(this)
                .params("page", page + "")
                .params("search", mEtSearch.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissLoading();
                        if (response.isSuccessful()) {
                            MaterialGroups groups = JSON.parseObject(response.body(), MaterialGroups.class);
                            currentPage = page;
                            if (page == 0) {
                                mGroups.clear();
                            }
                            mGroups.addAll(groups.getData());
                            mGroupAdapter.notifyDataSetChanged();
                            if (groups.getData().size() < 10) {
                                mGroupAdapter.loadMoreEnd();
                            } else {
                                mGroupAdapter.loadMoreComplete();
                            }
                            if (page == 0) {
                                mRvGroup.scrollToPosition(0);
                            }
                        } else {
                            mGroupAdapter.loadMoreEnd();
                        }
                    }
                });
    }

    @OnClick({R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
        }
    }
}