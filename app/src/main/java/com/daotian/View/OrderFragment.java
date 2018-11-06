package com.daotian.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.MyOrderBO;
import com.daotian.Model.ResultBO;
import com.daotian.R;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 *
 */
public class OrderFragment extends Fragment {

    private final int ALL = 0;
    private final int WIN = 2;
    private final int LOSE = 3;
    private final int OVER = 4;
    @BindView(R.id.refresh_listview)
    PullToRefreshListView refreshListview;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.empty_ly)
    RelativeLayout emptyLy;

    private Activity mActivity;
    private int orderStatus;
    private int page = 1;
    private List<MyOrderBO> list = new ArrayList<>();
    private Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        intentData();
        initView();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                refreshListview.setRefreshing();
            }
        }, 200);
        return view;

    }

    @Override
    public void onResume(){
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                refreshListview.setRefreshing();
            }
        }, 200);
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

        mAdapter = new Adapter(mActivity, R.layout.list_item_order, list);
        refreshListview.setAdapter(mAdapter);
    }

    private void getList(final String s) {
        Log.e("status", orderStatus + "");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("page", page);
        paramMap.put("type", OrderActivity.type);
        if (orderStatus == OVER) {
            paramMap.put("is_zq", "1");
        } else {
            paramMap.put("is_zq", "0");
            paramMap.put("status", orderStatus);
        }
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.myOrder, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                refreshListview.onRefreshComplete();
                Log.e("post_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    refreshListview.onRefreshComplete();
                    if(s.equals("")){
                        emptyLy.setVisibility(View.VISIBLE);
                    }
                    return;
                }
                List<MyOrderBO> result_list = JSON.parseArray(resultBO.getResultData(), MyOrderBO.class);
                if (s.equals("")) {
                    list.clear();
                }
                list.addAll(result_list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                refreshListview.onRefreshComplete();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 列表适配器
     */
    public class Adapter extends CommonAdapter<MyOrderBO> {

        public Adapter(Context context, int layoutId, List<MyOrderBO> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(final ViewHolder holder, final MyOrderBO info) {
//            holder.setText(R.id.time, info.getYmd());
//            holder.setText(R.id.detail_time, info.getHis());
            holder.setText(R.id.sh_name, info.getName());
            holder.setText(R.id.order_no, "第"+info.getFirst_lognum()+"期");
            holder.setText(R.id.price, "彩豆：" + info.getTotal_fee()+"元");

            TextView order_type = holder.getView(R.id.order_type);
            if (info.getSery_num().equals("1")) {
                order_type.setText("普通订单");
            } else {
                order_type.setText("追号订单");
            }
            TextView status = holder.getView(R.id.order_status);
            if (info.getStatus().equals("1")) {
                status.setText("等待开奖");
                status.setTextColor(Color.parseColor("#269ee6"));
            } else if (info.getStatus().equals("2")) {
                status.setText("中奖"+info.getGet_fee()+"元");
                status.setTextColor(Color.parseColor("#ff6243"));
            } else if (info.getStatus().equals("3")) {
                status.setText("未中奖");
                status.setTextColor(Color.parseColor("#444444"));
            }
            LinearLayout item_ly = holder.getView(R.id.item_ly);
            item_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.getSery_num().equals("1")) {
                        Intent in = new Intent(mContext, OrderDetailActivity.class);
                        in.putExtra("order_id", info.getId());
                        in.putExtra("name",info.getName());
                        in.putExtra("ticket_type", OrderActivity.type+"");
                        startActivity(in);
                    } else {
                        Intent in = new Intent(mContext, ZqOrderDetailActivity.class);
                        in.putExtra("order_id", info.getId());
                        in.putExtra("name",info.getName());
                        in.putExtra("ticket_type",OrderActivity.type+"");
                        startActivity(in);
                    }

                }
            });

        }
    }

}
