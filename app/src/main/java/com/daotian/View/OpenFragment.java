package com.daotian.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.OpenListBO;
import com.daotian.Model.ResultBO;
import com.daotian.R;
import com.daotian.Utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 *
 */
public class OpenFragment extends Fragment {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_ly)
    SwipeRefreshLayout refreshLy;
    private Activity mActivity;

    private ProgressDialog dialog;
    private int page = 1;
    private List<OpenListBO> list = new ArrayList<>();
    private MyApdater myApdater;
    private String TAG = "OpenFragment";
    private String openname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open, container, false);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("正在联网下载数据...");
        dialog.setMessage("请稍后...");
        initView();
        Log.e(TAG, "onCreateView");
        return view;

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
     * 初始化视图
     */
    private void initView() {
        refreshLy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList("refresh");
            }
        });
        myApdater = new MyApdater(getActivity(), R.layout.list_item_open_ticket, list);
        refreshListview.setAdapter(myApdater);
        //Utility.setListViewHeightBasedOnChildren(refreshListview);
    }

    private void getList(final String s) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.getOpenList, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                dialog.dismiss();
                refreshLy.setRefreshing(false);
                Log.e("wangweiming_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    ToastUtil.toast(mActivity, resultBO.getResultMsg());
                    return;
                }
                List<OpenListBO> result_list = JSON.parseArray(resultBO.getResultData(), OpenListBO.class);
                if (s.equals("more") && result_list.size() < 1) {
                    ToastUtil.toast(mActivity, "没有更多啦");
                    return;
                }
                list.clear();
                list.addAll(result_list);
                myApdater.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                refreshLy.setRefreshing(false);
                ToastUtil.toast(mActivity, "请求失败:" + responseBody);
            }
        });
    }




    public class MyApdater extends CommonAdapter<OpenListBO> {

        public MyApdater(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, final OpenListBO info) {

            TextView name = holder.getView(R.id.name);
            TextView log_num = holder.getView(R.id.log_num);
            TextView dur_time = holder.getView(R.id.dur_time);
            TextView openname=holder.getView(R.id.openname);
            List<String> nums;
            if (!TextUtils.isEmpty(info.getOpen_num())) {
                nums = JSON.parseArray(info.getOpen_num(), String.class);
            }
            nums = JSON.parseArray(info.getOpen_num(), String.class);

            final TagFlowLayout numTag = holder.getView(R.id.num_tag);
            TagAdapter<String> numAdapter = new TagAdapter<String>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_item_tv2, numTag, false);

                    // Log.e("wangweiming",s.substring(1));
                    tv.setText(s);
                    if (info.getName().equals("山东扑克3")) {
                        Log.e("wangweiming", "enterswitch");
                        String temp = s.substring(1);
                        String temprary = temp.substring(0, 1);
                        switch (s.substring(0, 1)) {
                            case "1":
                                tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_item_open, numTag, false);
                                tv.setBackgroundResource(R.drawable.tonghua1);

                                if (temprary.equals("0")) {
                                    tv.setText(temp.substring(1));
                                } else {
                                    tv.setText(s.substring(1));
                                }
                                break;

                            case "2":
                                tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_item_open, numTag, false);
                                tv.setBackgroundResource(R.drawable.tonghua2);
                                if (temprary.equals("0")) {
                                    tv.setText(temp.substring(1));
                                } else {
                                    tv.setText(s.substring(1));
                                }
                                break;
                            case "3":
                                tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_item_open, numTag, false);
                                tv.setBackgroundResource(R.drawable.tonghua3);
                                if (temprary.equals("0")) {
                                    tv.setText(temp.substring(1));
                                } else {
                                    tv.setText(s.substring(1));
                                }
                                break;
                            case "4":
                                tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_item_open, numTag, false);
                                tv.setBackgroundResource(R.drawable.tonghua4);
                                if (temprary.equals("0")) {
                                    tv.setText(temp.substring(1));
                                } else {
                                    tv.setText(s.substring(1));
                                }
                                break;
                        }
                    }
                    return tv;
                }
            };

            if (info.getStatus().equals("0")) {
                log_num.setVisibility(View.GONE);
                dur_time.setVisibility(View.VISIBLE);
                dur_time.setText("暂无开奖信息");
                openname.setText(info.getOpen_name());
                numTag.setVisibility(View.GONE);
            } else {
                log_num.setVisibility(View.VISIBLE);
                dur_time.setVisibility(View.VISIBLE);
                openname.setText(info.getOpen_name());
                log_num.setText(info.getLast_qs() + "期");
                dur_time.setText("每" + info.getPer_time() + "秒一期");
            }
            Log.e("info.getName()", info.getName());
            name.setText(info.getName());
            numTag.setAdapter(numAdapter);
            RelativeLayout itemLy = holder.getView(R.id.item_ly);
            numTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.getStatus().equals("0")) {
                        ToastUtil.toast(mContext, "暂无开奖信息");
                    } else {
                        Intent in = new Intent(mActivity, OpenDetailActivity.class);
                        in.putExtra("sh_name", info.getSh_name());
                        in.putExtra("name", info.getName());
                        in.putExtra("type", info.getType());
                        startActivity(in);
                    }
                }
            });
            itemLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.getStatus().equals("0")) {
                        ToastUtil.toast(mContext, "暂无开奖信息");
                    } else {
                        Intent in = new Intent(mActivity, OpenDetailActivity.class);
                        in.putExtra("sh_name", info.getSh_name());
                        in.putExtra("name", info.getName());
                        in.putExtra("type", info.getType());
                        startActivity(in);
                    }
                }
            });
        }
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    myApdater.notifyDataSetChanged();
                    handler.sendEmptyMessageDelayed(1, 1000);

                    break;
            }
        }

    };

}
