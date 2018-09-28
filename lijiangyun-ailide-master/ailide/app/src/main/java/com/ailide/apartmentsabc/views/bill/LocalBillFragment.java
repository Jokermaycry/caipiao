package com.ailide.apartmentsabc.views.bill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Bill;
import com.ailide.apartmentsabc.tools.Contants;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LocalBillFragment extends BaseSimpleFragment {

    @BindView(R.id.sr_common)
    SwipeRefreshLayout mSrCommon;
    @BindView(R.id.rv_common)
    RecyclerView mRvCommon;

    private List<Bill> mCommons;
    private BillAdapter mCommonAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_bill;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mCommons = new ArrayList<>();
        mCommonAdapter = new BillAdapter(mCommons);
        mCommonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!mCommonAdapter.isManage()) {
                    Intent intent = new Intent(mActivity, EditBillActivity.class);
                    intent.putExtra("data", mCommons.get(position));
                    intent.putExtra("local", true);
                    startActivity(intent);
                }
            }
        });
        mCommonAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mCommons.remove(position);
                SPUtil.saveObject(Contants.SP_BILL, mCommons);
                mCommonAdapter.notifyDataSetChanged();
            }
        });
        mRvCommon.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mRvCommon.setAdapter(mCommonAdapter);
        mSrCommon.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getCommonBills();
            }
        });
    }

    @Override
    protected void initData() {
        getCommonBills();
    }

    @Override
    protected void setListener() {
    }

    public void getCommonBills() {
        List<Bill> bills = (List<Bill>) SPUtil.readObject(Contants.SP_BILL);
        if (bills == null) {
            bills = new ArrayList<>();
        }
        mCommons.clear();
        mCommons.addAll(bills);
        mCommonAdapter.notifyDataSetChanged();
        mSrCommon.setRefreshing(false);
    }

    public boolean isLocal(Bill data) {
        for (Bill bill :
                mCommons) {
            if (data.getId() == bill.getId()) {
                return true;
            }
        }
        return false;
    }

    public void setManage(boolean manage) {
        mCommonAdapter.setManage(manage);
    }

    public boolean getMange() {
        return mCommonAdapter.isManage();
    }
}