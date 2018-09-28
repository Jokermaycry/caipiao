package com.daotian.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.ResultBO;
import com.daotian.Model.ZqOrderDetailBO;
import com.daotian.R;
import com.daotian.Utils.ExpandListView;
import com.daotian.Utils.ToastUtil;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yzx on 16/11/15.
 */

public class ZqOrderDetailActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.sh_name)
    TextView shName;
    @BindView(R.id.open_qs)
    TextView openQs;
    @BindView(R.id.order_price)
    TextView orderPrice;
    @BindView(R.id.order_status)
    TextView orderStatus;
    @BindView(R.id.win_price)
    TextView winPrice;
    @BindView(R.id.finish_list)
    ExpandListView finishList;
    @BindView(R.id.no_finish_list)
    ExpandListView noFinishList;
    @BindView(R.id.finish_num)
    TextView finishNum;
    @BindView(R.id.no_finish_num)
    TextView noFinishNum;
    @BindView(R.id.finish_ly)
    LinearLayout finishLy;
    @BindView(R.id.no_finish_ly)
    LinearLayout noFinishLy;
    @BindView(R.id.cancle)
    TextView cancle;
    private String order_id;
    private Activity mActivity;
    private ProgressDialog dialog;
    private Adapter mFinishAdapter;
    private Adapter mNoFinishAdapter;

    private ZqOrderDetailBO mInfo;
    private String ticket_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qzorderdetail);
        ButterKnife.bind(this);
        mActivity = this;
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("正在联网下载数据...");
        dialog.setMessage("请稍后...");
        order_id = getIntent().getStringExtra("order_id");
        ticket_type = getIntent().getStringExtra("ticket_type");
        initView();
    }

    private void initView() {
        dialog.show();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", order_id);
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.myZqOrderDetail, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                dialog.dismiss();
                Log.e("orderdetail_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    ToastUtil.toast(mActivity, resultBO.getResultMsg());
                    return;
                }
                mInfo = JSON.parseObject(resultBO.getResultData(), ZqOrderDetailBO.class);
                initData();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                dialog.dismiss();
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
            }
        });
    }

    private void initData() {
        Glide.with(mActivity).load(mInfo.getImg()).centerCrop().placeholder(R.drawable.default_user_logo).crossFade().into((ImageView) logo);
        shName.setText(mInfo.getName());
        openQs.setText("共" + mInfo.getSery_num() + "期");
        orderPrice.setText(mInfo.getTotal_fee());
        winPrice.setText(mInfo.getGet_fee());
        if (mInfo.getStatus().equals("1")) {
            orderStatus.setText("等待开奖");
            orderStatus.setTextColor(Color.parseColor("#269ee6"));
            cancle.setVisibility(View.VISIBLE);
        } else if (mInfo.getStatus().equals("2")) {
            orderStatus.setText("中奖");
            orderStatus.setTextColor(Color.parseColor("#ff6243"));
        } else if (mInfo.getStatus().equals("3")) {
            orderStatus.setText("未中奖");
            orderStatus.setTextColor(Color.parseColor("#444444"));
        }
        List<ZqOrderDetailBO.ChildOrder> finish_list = new ArrayList<>();
        List<ZqOrderDetailBO.ChildOrder> nofinish_list = new ArrayList<>();
        for (int i = 0; i < mInfo.getChild_order().size(); i++) {
            if (mInfo.getChild_order().get(i).getStatus().equals("0")) {
                nofinish_list.add(mInfo.getChild_order().get(i));
            } else {
                finish_list.add(mInfo.getChild_order().get(i));
            }
        }

        finishNum.setText("已完成" + mInfo.getSery_now() + "期");
        noFinishNum.setText("未完成" + nofinish_list.size() + "期");

        mFinishAdapter = new Adapter(mActivity, R.layout.list_item_zq_child_list, finish_list);
        finishList.setAdapter(mFinishAdapter);

        mNoFinishAdapter = new Adapter(mActivity, R.layout.list_item_zq_child_list, nofinish_list);
        noFinishList.setAdapter(mNoFinishAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back, R.id.cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.cancle:
                cancleOrder();
                break;
        }
    }

    /**
     *
     */
    private void cancleOrder() {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("order_id",order_id);
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.cancleOrder, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.e("result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    ToastUtil.toast(mActivity, resultBO.getResultMsg());
                    return;
                }
                ToastUtil.toast(mActivity, "撤单成功");
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
            }
        });
    }


    /**
     * 列表适配器
     */
    public class Adapter extends CommonAdapter<ZqOrderDetailBO.ChildOrder> {

        public Adapter(Context context, int layoutId, List<ZqOrderDetailBO.ChildOrder> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(final ViewHolder holder, final ZqOrderDetailBO.ChildOrder info) {

            LinearLayout itemLy = holder.getView(R.id.item_ly);

            holder.setText(R.id.log_num, info.getLog_num() + "期");
            holder.setText(R.id.price, info.getTotal_money() + "元");

            TextView status = holder.getView(R.id.status);
            if (info.getStatus().equals("0")) {
                status.setText("未开奖");
                status.setTextColor(Color.parseColor("#269ee6"));
            } else if (info.getStatus().equals("1")) {
                status.setText("中奖" + info.getGet_fee() + "元");
                status.setTextColor(Color.parseColor("#ff6243"));
            } else if (info.getStatus().equals("2")) {
                status.setText("未中奖");
                status.setTextColor(Color.parseColor("#444444"));
            } else if (info.getStatus().equals("3")) {
                status.setText("撤单");
                status.setTextColor(Color.parseColor("#444444"));
            }
            itemLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(mContext, OrderDetailActivity.class);
                    in.putExtra("type", "child");
                    in.putExtra("log_num", info.getLog_num());
                    in.putExtra("order_id", mInfo.getId());
                    in.putExtra("ticket_type", ticket_type);
                    startActivity(in);
                }
            });

        }
    }
}
