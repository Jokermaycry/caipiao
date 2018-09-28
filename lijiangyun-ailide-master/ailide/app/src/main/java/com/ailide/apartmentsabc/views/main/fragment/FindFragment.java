package com.ailide.apartmentsabc.views.main.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.ailide.apartmentsabc.views.find.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends BaseSimpleFragment {


    @BindView(R.id.find_shop_ly)
    LinearLayout findShopLy;
    @BindView(R.id.find_xingqu_ly)
    LinearLayout findXingquLy;
    @BindView(R.id.find_weibo_ly)
    LinearLayout findWeiboLy;
    @BindView(R.id.find_ailide_ly)
    LinearLayout findAilideLy;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }


    @OnClick({R.id.find_shop_ly, R.id.find_xingqu_ly, R.id.find_weibo_ly, R.id.find_ailide_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.find_shop_ly:
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("find","1");
                mActivity.jumpToOtherActivity(intent,false);
                break;
            case R.id.find_xingqu_ly:
                Intent intent1 = new Intent(mActivity, WebViewActivity.class);
                intent1.putExtra("find","2");
                mActivity.jumpToOtherActivity(intent1,false);
                break;
            case R.id.find_weibo_ly:
                Intent intent2 = new Intent(mActivity, WebViewActivity.class);
                intent2.putExtra("find","3");
                mActivity.jumpToOtherActivity(intent2,false);
                break;
            case R.id.find_ailide_ly:
                Intent intent4 = new Intent(mActivity, WebViewActivity.class);
                intent4.putExtra("find","4");
                mActivity.jumpToOtherActivity(intent4,false);
                break;
        }
    }
}
