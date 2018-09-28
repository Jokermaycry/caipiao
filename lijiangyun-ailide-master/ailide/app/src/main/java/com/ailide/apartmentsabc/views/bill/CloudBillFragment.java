package com.ailide.apartmentsabc.views.bill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Bill;
import com.ailide.apartmentsabc.model.Bills;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CloudBillFragment extends BaseSimpleFragment {

    @BindView(R.id.rv_more)
    RecyclerView mRvMore;
    @BindView(R.id.sr_more)
    SwipeRefreshLayout mSrMore;

    private List<Bill> mMores;
    private BillAdapter mMoreAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cloud_bill;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mMores = new ArrayList<>();
        mMoreAdapter = new BillAdapter(mMores);
        mMoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, EditBillActivity.class);
                intent.putExtra("data", mMores.get(position));
                intent.putExtra("local", ((BillActivity)getActivity()).isLocal(mMores.get(position)));
                startActivity(intent);
            }
        });
        mRvMore.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mRvMore.setAdapter(mMoreAdapter);
        mSrMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMoreBills();
            }
        });
    }

    @Override
    protected void initData() {
        getMoreBills();
    }

    @Override
    protected void setListener() {
    }

    public void getMoreBills() {
        OkGo.<String>post(Urls.STICKY)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mSrMore.setRefreshing(false);
                        if (response.isSuccessful()) {
                            Bills bills = JSON.parseObject(response.body(), Bills.class);
                            mMores.clear();
                            mMores.addAll(bills.getData());
                            mMoreAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}