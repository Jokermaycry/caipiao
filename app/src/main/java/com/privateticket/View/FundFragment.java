package com.privateticket.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.privateticket.Model.UserBO;
import com.daotian.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class FundFragment extends Fragment {

    private final int ALL = 0;
    private final int WIN = 1;
    private final int LOSE = 2;
    private final int OVER = 3;
    @BindView(R.id.refresh_listview)
    PullToRefreshListView refreshListview;

    private Activity mActivity;
    private int orderStatus;
    private int page = 1;
    private List<UserBO> list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fund, container, false);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        intentData();
        initView();
        return view;

    }

    private void intentData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderStatus = bundle.getInt("status");
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        refreshListview.setMode(PullToRefreshBase.Mode.BOTH);
        refreshListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            // 下拉Pulling Down
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                list.clear();
                getList("");
            }

            // 上拉Pulling Up
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getList("more");
            }
        });
    }

    private void getList(String s) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

}
