package com.privateticket.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.daotian.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yzx on 16/11/15.
 */

public class OrderActivity extends AppCompatActivity {


    private final int ALL = 0;
    private final int WIN = 2;
    private final int LOSE = 3;
    private final int OVER = 4;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.all_tv)
    TextView allTv;
    @BindView(R.id.all_ly)
    LinearLayout allLy;
    @BindView(R.id.win_tv)
    TextView winTv;
    @BindView(R.id.win_ly)
    LinearLayout winLy;
    @BindView(R.id.lose_tv)
    TextView loseTv;
    @BindView(R.id.lose_ly)
    LinearLayout loseLy;
    @BindView(R.id.over_tv)
    TextView overTv;
    @BindView(R.id.over_ly)
    LinearLayout overLy;
    @BindView(R.id.order_viewpager)
    ViewPager orderViewpager;

    List<Fragment> list_order_process;
    OrderManagerFragmentAdapter mAdapter;
    private int current=-1;
    public static int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initView();
        current=getIntent().getIntExtra("current",0);
        type=getIntent().getIntExtra("type",1);
        if(current==ALL){
            orderViewpager.setCurrentItem(0);
        }else if(current==WIN){
            orderViewpager.setCurrentItem(1);
        }else if(current==LOSE){
            orderViewpager.setCurrentItem(2);
        }else{
            orderViewpager.setCurrentItem(3);
        }
    }

    private void initView() {
        list_order_process = new ArrayList<>();
        OrderFragment allItem = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", ALL);
        allItem.setArguments(bundle);
        list_order_process.add(allItem);

        OrderFragment winItem = new OrderFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("status", WIN);
        winItem.setArguments(bundle1);
        list_order_process.add(winItem);

        OrderFragment loseItem = new OrderFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("status", LOSE);
        loseItem.setArguments(bundle2);
        list_order_process.add(loseItem);

        OrderFragment overItem = new OrderFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("status", OVER);
        overItem.setArguments(bundle3);
        list_order_process.add(overItem);

        mAdapter = new OrderManagerFragmentAdapter(getSupportFragmentManager(), list_order_process);
        orderViewpager.setAdapter(mAdapter);
        orderViewpager.setOffscreenPageLimit(4);
        orderViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        allTv.setSelected(true);
                        winTv.setSelected(false);
                        loseTv.setSelected(false);
                        overTv.setSelected(false);
                        break;
                    case 1:
                        allTv.setSelected(false);
                        winTv.setSelected(true);
                        loseTv.setSelected(false);
                        overTv.setSelected(false);
                        break;
                    case 2:
                        allTv.setSelected(false);
                        winTv.setSelected(false);
                        loseTv.setSelected(true);
                        overTv.setSelected(false);
                        break;
                    case 3:
                        allTv.setSelected(false);
                        winTv.setSelected(false);
                        loseTv.setSelected(false);
                        overTv.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        allTv.setSelected(true);
        winTv.setSelected(false);
        loseTv.setSelected(false);
        overTv.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.back,R.id.all_ly, R.id.win_ly, R.id.lose_ly, R.id.over_ly})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.all_ly:
                orderViewpager.setCurrentItem(0);
                break;
            case R.id.win_ly:
                orderViewpager.setCurrentItem(1);
                break;
            case R.id.lose_ly:
                orderViewpager.setCurrentItem(2);
                break;
            case R.id.over_ly:
                orderViewpager.setCurrentItem(3);
                break;

        }
    }

    public class OrderManagerFragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> mList;

        public OrderManagerFragmentAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            mList = list;
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
}
