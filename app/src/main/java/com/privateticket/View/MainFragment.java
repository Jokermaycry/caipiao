package com.privateticket.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privateticket.Http.ParamUtil;
import com.privateticket.Http.ServiceInterface;
import com.privateticket.Http.TicketService;
import com.privateticket.Model.ResultBO;
import com.privateticket.Model.TicketListInfo;
import com.privateticket.Model.TicketSortBO;
import com.daotian.R;
import com.privateticket.Utils.ExpandGridView;
import com.privateticket.Utils.ExpandListView;
import com.privateticket.Utils.ToastUtil;
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
public class MainFragment extends Fragment {


    @BindView(R.id.title)
    TextView title;
    //    @BindView(R.id.gridview)
//    GridView gridview;
    @BindView(R.id.listview)
    ExpandListView listview;
    @BindView(R.id.refresh_ly)
    SwipeRefreshLayout refreshLy;
    private Activity mActivity;


    private ProgressDialog dialog;
    private List<TicketSortBO> list = new ArrayList<>();
    private ListAdapter mAdapter;
    /**
     * 异步线程
     */
    private AsyncHttpClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        client = new AsyncHttpClient();
        mActivity = getActivity();
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("正在联网下载数据...");
        dialog.setMessage("请稍后...");
        initView();
        return view;

    }

    /**
     * 初始化视图
     */
    private void initView() {
        refreshLy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList("refresh");
            }
        });
        mAdapter = new ListAdapter(mActivity, R.layout.list_item_ticketsort, list);
        listview.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog.show();
        getList("");
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(1);
    }



    /**
     * 获取列表
     */
    private void getList(final String type) {
        //从网络中获取
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.getHomeList, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                dialog.dismiss();
                refreshLy.setRefreshing(false);
                Log.e("post_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    ToastUtil.toast(mActivity, resultBO.getResultMsg());
                    return;
                }
                List<TicketSortBO> result_list = JSON.parseArray(resultBO.getResultData(), TicketSortBO.class);
                list.clear();
                list.addAll(result_list);


                for (int i = 0; i < list.size(); i++) {
                    for(int j=0;j<list.get(i).getDatalist().size();j++){
                        if (list.get(i).getDatalist().get(j).getStatus().equals("1")) {
                            long time = Long.valueOf(list.get(i).getDatalist().get(j).getOpen_time()) - Long.valueOf(list.get(i).getDatalist().get(j).getNow_time())- Long.valueOf(list.get(i).getDatalist().get(j).getAfter_time());
                            if (time < 0) {
                                time = 0;
                            }
                            list.get(i).getDatalist().get(j).setCurrent_time(String.valueOf(time));

                        }
                    }

                }

                mAdapter.notifyDataSetChanged();
                if (type.equals("")) {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                refreshLy.setRefreshing(false);
                ToastUtil.toast(mActivity, "请求失败:" + responseBody);
            }


        });
    }


    /**
     * 列表适配器
     */
    public class ListAdapter extends CommonAdapter<TicketSortBO> {

        public ListAdapter(Context context, int layoutId, List<TicketSortBO> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(final ViewHolder holder, final TicketSortBO info) {
            holder.setText(R.id.name, info.getName());
            ExpandGridView grid=holder.getView(R.id.gridview);

            Adapter adapter = new Adapter(mActivity, R.layout.list_item_ticket, info.getDatalist());
            grid.setAdapter(adapter);


        }
    }


    /**
     * 列表适配器
     */
    public class Adapter extends CommonAdapter<TicketListInfo> {

        public Adapter(Context context, int layoutId, List<TicketListInfo> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(final ViewHolder holder, final TicketListInfo info) {
            holder.setText(R.id.name, info.getName());
            TextView timeTv = holder.getView(R.id.time);
            LinearLayout itemLy = holder.getView(R.id.item_ly);
            Glide.with(mActivity).load(info.getImg()).centerCrop().placeholder(R.drawable.default_user_logo).crossFade().into((ImageView) holder.getView(R.id.logo));
            if (!TextUtils.isEmpty(info.getCurrent_time())) {
                long time = Long.valueOf(info.getCurrent_time());
                timeTv.setTextColor(Color.parseColor("#269ee6"));
                holder.setText(R.id.time, (time / 60 + "分" + time % 60 + "秒"));
                //倒计时结束，重新刷新列表
                if (time <= 1) {
                    timeTv.setTextColor(Color.parseColor("#269ee6"));
                    holder.setText(R.id.time, "等待开奖");

                    if(time==0){
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                getList("refresh");
                            }
                        }, (Long.valueOf(info.getAfter_time())+5) * 1000);
                    }
                } else{
                    itemLy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (info.getStatus().equals("1")) {
                                if (info.getType().equals("1")) {
                                    Intent in = new Intent(mContext, ElevenForFiveActivity.class);
                                    in.putExtra("sh_name", info.getSh_name());
                                    startActivity(in);
                                } else if (info.getType().equals("2")) {
                                    Intent in = new Intent(mContext, FastThreeActivity.class);
                                    in.putExtra("sh_name", info.getSh_name());
                                    startActivity(in);
                                } else {
                                    ToastUtil.toast(mContext, "找不动指定玩法，暂无开放！");
                                }

                            } else {
                                ToastUtil.toast(mContext, "暂停购买，请稍后重试");
                            }
                        }
                    });
                }
            } else {
                timeTv.setTextColor(Color.parseColor("#ff6243"));
                holder.setText(R.id.time, "暂停服务");
            }

        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    for (int index = 0; index < list.size(); index++) {
                        for(int j=0;j<list.get(index).getDatalist().size();j++){
                            if (!TextUtils.isEmpty(list.get(index).getDatalist().get(j).getCurrent_time()) && list.get(index).getDatalist().get(j).getStatus().equals("1")) {
                                list.get(index).getDatalist().get(j).setCurrent_time(String.valueOf(Long.valueOf(list.get(index).getDatalist().get(j).getCurrent_time()) - 1));
                            } else {
                                list.get(index).getDatalist().get(j).setCurrent_time("");
                            }
                        }

                    }
                    mAdapter.notifyDataSetChanged();
                    handler.sendEmptyMessageDelayed(1, 1000);

                    break;
            }
        }

    };
}
