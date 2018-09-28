package com.privateticket.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privateticket.Base.Constant;
import com.privateticket.Http.ParamUtil;
import com.privateticket.Http.ServiceInterface;
import com.privateticket.Http.TicketService;
import com.privateticket.Model.BuyListBO;
import com.privateticket.Model.OpenDetailBO;
import com.privateticket.Model.OrderDetailBO;
import com.privateticket.Model.ResultBO;
import com.daotian.R;
import com.privateticket.Utils.ExpandListView;
import com.privateticket.Utils.ToastUtil;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yzx on 16/11/15.
 */

public class OrderDetailActivity extends AppCompatActivity {


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
    @BindView(R.id.open_num)
    TextView openNum;
    @BindView(R.id.cath_info)
    TextView cathInfo;
    @BindView(R.id.buy_list)
    ExpandListView buyList;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.order_no)
    TextView orderNo;
    @BindView(R.id.wen_set)
    TextView wenSet;
    @BindView(R.id.cancle)
    TextView cancle;

    private String order_id;
    private String log_num;
    private Activity mActivity;
    private ProgressDialog dialog;
    private OrderDetailBO mInfo;
    private Adapter mAdapter;
    private String type, ticket_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        ButterKnife.bind(this);
        mActivity = this;
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("正在联网下载数据...");
        dialog.setMessage("请稍后...");
        order_id = getIntent().getStringExtra("order_id");
        type = getIntent().getStringExtra("type");
        log_num = getIntent().getStringExtra("log_num");
        ticket_type = getIntent().getStringExtra("ticket_type");
        Log.e("ticket_type", ticket_type);
        if (!TextUtils.isEmpty(type) && type.equals("child")) {
            initChildView();
        } else {
            initView();
        }
    }

    private void initChildView() {
        dialog.show();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("log_num", log_num);
        paramMap.put("order_id", order_id);
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.myChildOrderDetail, new AsyncHttpResponseHandler() {
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
                mInfo = JSON.parseObject(resultBO.getResultData(), OrderDetailBO.class);
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

    private void initView() {
        dialog.show();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("order_id", order_id);
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.myOrderDetail, new AsyncHttpResponseHandler() {
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
                mInfo = JSON.parseObject(resultBO.getResultData(), OrderDetailBO.class);
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

    /**
     * 填充数据
     */
    private void initData() {

        Glide.with(mActivity).load(mInfo.getImg()).centerCrop().placeholder(R.drawable.default_user_logo).crossFade().into((ImageView) logo);
        shName.setText(mInfo.getName());
        openQs.setText(mInfo.getLog_num() + "期");
        orderPrice.setText(mInfo.getTotal_money());
        if (mInfo.getStatus().equals("1")) {
            orderStatus.setText("中奖");
            orderStatus.setTextColor(Color.parseColor("#ff6243"));
        } else if (mInfo.getStatus().equals("0")) {
            orderStatus.setText("等待开奖");
            orderStatus.setTextColor(Color.parseColor("#269ee6"));
            cancle.setVisibility(View.VISIBLE);
        } else if (mInfo.getStatus().equals("2")) {
            orderStatus.setText("未中奖");
        } else if (mInfo.getStatus().equals("3")) {
            orderStatus.setText("撤销");
        }
        if (TextUtils.isEmpty(mInfo.getOpen_num())) {
            openNum.setText("未开奖");
        } else {
            openNum.setText(mInfo.getOpen_num());
        }

        orderTime.setText(mInfo.getDay());
        winPrice.setText(mInfo.getGet_fee());
        orderNo.setText(mInfo.getOrder_sn());
        cathInfo.setText(mInfo.getBuy_ts() + "条  " + mInfo.getBuy_bs() + "倍");

        mAdapter = new Adapter(mActivity, R.layout.list_item_buylist, mInfo.getBuy_detail());
        buyList.setAdapter(mAdapter);

        if (Constant.mSetting != null) {
            wenSet.setText(Constant.mSetting.getWel_setting());
        } else {
            wenSet.setText("未设置");
        }

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
    public class Adapter extends CommonAdapter<BuyListBO> {

        public Adapter(Context context, int layoutId, List<BuyListBO> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(final ViewHolder holder, final BuyListBO info) {

            holder.setText(R.id.mode, info.getBuy_name());
            List<String> list = JSON.parseArray(info.getBuy_num(), String.class);
            String nums = "";
            if (!TextUtils.isEmpty(ticket_type) && ticket_type.equals("2")) {
                for (int i = 0; i < list.size(); i++) {
                    if (!TextUtils.isEmpty(list.get(i))) {
                        if (i == 0) {
                            if (!TextUtils.isEmpty(info.getBuy_type()) && (info.getBuy_type().equals("6") || info.getBuy_type().equals("7"))) {
                                nums = "(" + list.get(i) + ")";
                            } else {
                                nums = list.get(i);
                            }
                        } else if (i == 1) {
                            nums = nums + "," + list.get(i);
                        }
                    }
                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (!TextUtils.isEmpty(list.get(i))) {
                        if (i == 0) {
                            if (!TextUtils.isEmpty(info.getBuy_type()) && (info.getBuy_type().equals("21") || info.getBuy_type().equals("22") || info.getBuy_type().equals("23")
                                    || info.getBuy_type().equals("24"))) {
                                nums = "(" + list.get(i) + ")";
                            } else {
                                nums = list.get(i);
                            }
                        } else if (i == 1) {
                            nums = nums + "," + list.get(i);
                        } else if (i == 2) {
                            nums = nums + "," + list.get(i);
                        }
                    }
                }
            }

            holder.setText(R.id.numbers, nums);
        }
    }
}
