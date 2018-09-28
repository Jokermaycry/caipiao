package com.ailide.apartmentsabc.views.bill;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.Bill;
import com.ailide.apartmentsabc.views.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillActivity extends BaseActivity {

    @BindView(R.id.tv_common_template)
    TextView mTvCommonTemplete;
    @BindView(R.id.line_common_template)
    View mLineCommonTemplete;
    @BindView(R.id.tv_more_template)
    TextView mTvMoreTemplete;
    @BindView(R.id.line_more_template)
    View mLineMoreTemplete;
    @BindView(R.id.tv_manage)
    TextView mTvManage;
    @BindView(R.id.vp)
    ViewPager mVp;


    private CloudBillFragment cloudBillFragment;
    private LocalBillFragment localBillFragment;
    private BillTypeAdapter billTypeAdapter;
    private List<Fragment> mFragments;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        mFragments = new ArrayList<>();
        cloudBillFragment = new CloudBillFragment();
        localBillFragment = new LocalBillFragment();
        mFragments.add(cloudBillFragment);
        mFragments.add(localBillFragment);
        billTypeAdapter = new BillTypeAdapter(getSupportFragmentManager(), mFragments);
        mVp.setAdapter(billTypeAdapter);
        mVp.setOffscreenPageLimit(2);
        mVp.setCurrentItem(0);// 设置起始位置
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (mVp.getCurrentItem()){
                    case 0:
                        onClickCloudBill();
                        break;
                    case 1:
                        onClickLocalBill();
                        break;
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.rl_common_template, R.id.rl_more_template, R.id.tv_manage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_common_template:
                onClickLocalBill();
                break;
            case R.id.rl_more_template:
                onClickCloudBill();
                break;
            case R.id.tv_manage:
                localBillFragment.setManage(!localBillFragment.getMange());
                if (localBillFragment.getMange()) {
                    mTvManage.setText(R.string.complete);
                } else {
                    mTvManage.setText(R.string.manage);
                }
                break;
        }
    }

    public boolean isLocal(Bill data) {
        return localBillFragment.isLocal(data);
    }

    public void onClickLocalBill(){
        mTvManage.setVisibility(View.VISIBLE);
        mTvCommonTemplete.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTvMoreTemplete.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mLineCommonTemplete.setVisibility(View.VISIBLE);
        mLineMoreTemplete.setVisibility(View.INVISIBLE);
        mVp.setCurrentItem(1);
    }

    public void onClickCloudBill(){
        mTvManage.setVisibility(View.GONE);
        localBillFragment.setManage(false);
        mTvManage.setText(R.string.manage);
        mTvCommonTemplete.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mTvMoreTemplete.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mLineCommonTemplete.setVisibility(View.INVISIBLE);
        mLineMoreTemplete.setVisibility(View.VISIBLE);
        mVp.setCurrentItem(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEntity event) {
        switch (event.getId()) {
            case EventConstant.EDIT_BILL:
                localBillFragment.getCommonBills();
                break;
        }
    }
}