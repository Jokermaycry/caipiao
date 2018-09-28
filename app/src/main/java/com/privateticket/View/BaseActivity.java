package com.privateticket.View;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privateticket.Base.App;
import com.privateticket.Base.Constant;
import com.privateticket.Http.ParamUtil;
import com.privateticket.Http.ServiceInterface;
import com.privateticket.Http.TicketService;
import com.privateticket.Model.ResultBO;
import com.privateticket.Model.SettingBO;
import com.privateticket.Model.UserBO;
import com.daotian.R;
import com.privateticket.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    @BindView(R.id.textView12)
    TextView textView12;
    @BindView(R.id.main_tab_shop)
    LinearLayout mainTabShop;
    @BindView(R.id.main_tab_sort)
    LinearLayout mainTabSort;
    @BindView(R.id.main_tab_my)
    LinearLayout mainTabMy;

    private Boolean isExit = false;
    private List<Fragment> list_fm;
    private MainFragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        initView();
//        getBase();

    }


    private void initView() {
        list_fm = new ArrayList<>();
        MainFragment shop = new MainFragment();
        OpenFragment sort = new OpenFragment();
        MyFragment my = new MyFragment();

        list_fm.add(shop);
        list_fm.add(sort);
        list_fm.add(my);
        mAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), list_fm);
        mainViewpager.setAdapter(mAdapter);
        mainViewpager.setCurrentItem(0);

        mainViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //页面滑动
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mainTabShop.setSelected(true);
                        mainTabSort.setSelected(false);
                        mainTabMy.setSelected(false);
                        break;
                    case 1:
                        mainTabShop.setSelected(false);
                        mainTabSort.setSelected(true);
                        mainTabMy.setSelected(false);
                        break;
                    case 2:
                        mainTabShop.setSelected(false);
                        mainTabSort.setSelected(false);
                        mainTabMy.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mainTabShop.setSelected(true);
        mainTabSort.setSelected(false);
        mainTabMy.setSelected(false);
    }

//    private void getBase() {
//        HashMap<String, Object> paramMap = new HashMap<String, Object>();
//        RequestParams params = ParamUtil.requestParams(paramMap);
//        TicketService.post(params, ServiceInterface.getSetting, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String result = new String(responseBody);
//                Log.e("result", result);
//                if (!TextUtils.isEmpty(result)) {
//                    result = ParamUtil.unicodeToChinese(result);
//                }
//                try{
//                    ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
//                    if (resultBO==null || resultBO.getResultId() != 0) {
//                        return;
//                    }
//                    Constant.mSetting = JSON.parseObject(resultBO.getResultData(), SettingBO.class);
//                }catch (Exception e){
//
//                }
//
//            }
//
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                ToastUtil.toast(BaseActivity.this, "请求失败:" + responseBody);
//            }
//        });
//    }


    @OnClick({R.id.main_tab_shop, R.id.main_tab_sort, R.id.main_tab_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab_shop:
                mainViewpager.setCurrentItem(0);
                break;
            case R.id.main_tab_sort:
                mainViewpager.setCurrentItem(1);
                break;
            case R.id.main_tab_my:
                mainViewpager.setCurrentItem(2);
                break;
        }
    }

    public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> mList;

        public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> list){
            super(fm);
            mList=list;
        }
        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }

    /**
     * 退出程序.
     */
    @Override
    public void onBackPressed() {
        if (isExit == false) {
            isExit = true;
            ToastUtil.toast(BaseActivity.this, "再按一次退出程序");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
           finish();
        }
    }
}
