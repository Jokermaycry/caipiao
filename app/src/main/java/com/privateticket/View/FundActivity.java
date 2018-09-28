package com.privateticket.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daotian.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 资金明细
 * Created by yzx on 16/11/15.
 */

public class FundActivity extends AppCompatActivity {


    private final int ALL = 0;
    private final int CATTHE = 1;
    private final int WIN = 2;
    private final int RECHARGE = 3;
    private final int DRAW = 4;

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
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.draw_tv)
    TextView drawTv;
    @BindView(R.id.draw_ly)
    LinearLayout drawLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        list_order_process = new ArrayList<>();
        FundFragment allItem = new FundFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", ALL);
        allItem.setArguments(bundle);
        list_order_process.add(allItem);

        FundFragment winItem = new FundFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("status", CATTHE);
        winItem.setArguments(bundle1);
        list_order_process.add(winItem);

        FundFragment loseItem = new FundFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("status", WIN);
        loseItem.setArguments(bundle2);
        list_order_process.add(loseItem);

        FundFragment overItem = new FundFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("status", RECHARGE);
        overItem.setArguments(bundle3);
        list_order_process.add(overItem);

        FundFragment drawItem = new FundFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putInt("status", DRAW);
        drawItem.setArguments(bundle4);
        list_order_process.add(drawItem);

        mAdapter = new OrderManagerFragmentAdapter(getSupportFragmentManager(), list_order_process);
        orderViewpager.setAdapter(mAdapter);
        orderViewpager.setOffscreenPageLimit(5);
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
                        drawTv.setSelected(false);
                        break;
                    case 1:
                        allTv.setSelected(false);
                        winTv.setSelected(true);
                        loseTv.setSelected(false);
                        overTv.setSelected(false);
                        drawTv.setSelected(false);
                        break;
                    case 2:
                        allTv.setSelected(false);
                        winTv.setSelected(false);
                        loseTv.setSelected(true);
                        overTv.setSelected(false);
                        drawTv.setSelected(false);
                        break;
                    case 3:
                        allTv.setSelected(false);
                        winTv.setSelected(false);
                        loseTv.setSelected(false);
                        overTv.setSelected(true);
                        drawTv.setSelected(false);
                        break;
                    case 4:
                        drawTv.setSelected(true);
                        allTv.setSelected(false);
                        winTv.setSelected(false);
                        loseTv.setSelected(false);
                        overTv.setSelected(false);
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
        drawTv.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.back, R.id.all_ly, R.id.win_ly, R.id.lose_ly, R.id.over_ly,R.id.draw_ly})
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
            case R.id.draw_ly:
                orderViewpager.setCurrentItem(4);
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
