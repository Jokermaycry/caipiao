package com.daotian.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.daotian.Base.Constant;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.BuyListBO;
import com.daotian.Model.OrderDetailBO;
import com.daotian.Model.ResultBO;
import com.daotian.Model.opennumdetailBO;
import com.daotian.R;
import com.daotian.Utils.ExpandListView;
import com.daotian.Utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

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
    @BindView(R.id.open_list2)
    ExpandListView openList2;


    private String order_id;
    private String log_num;
    private String name;
    private String name2;
    private Activity mActivity;
    private ProgressDialog dialog;
    private OrderDetailBO mInfo;
    private Adapter mAdapter;
    private Adapter2 mAdapter2;
    private String type, ticket_type;
    private String opennumtwo;
    String temp ;
    String temprary ;
    String  openstate;
    //任选
    int[] images = {
            R.drawable.puke1, R.drawable.puke2,
            R.drawable.puke3, R.drawable.puke4,
            R.drawable.puke5, R.drawable.puke6,
            R.drawable.puke7, R.drawable.puke8,
            R.drawable.puke9, R.drawable.puke10,
            R.drawable.puke11, R.drawable.puke12,
            R.drawable.puke13,};

    int[] images_baoxuan = {
            R.drawable.tonghuashunbx,
            R.drawable.tonghuabx,
            R.drawable.duizibx,
            R.drawable.baozibx,
            R.drawable.shunzibx};

    //同花
    int[] images_tonghua = {
            R.drawable.tonghua1,
            R.drawable.tonghua2,
            R.drawable.tonghua3,
            R.drawable.tonghua4,
    };

    //同花顺
    int[] images_tonghuashun = {
            R.drawable.tonghuashun1,
            R.drawable.tonghuashun2,
            R.drawable.tonghuashun3,
            R.drawable.tonghuashun4,
    };

    //豹子
    int[] images_baozi = {
            R.drawable.baozi1,
            R.drawable.baozi2,
            R.drawable.baozi3,
            R.drawable.baozi4,
            R.drawable.baozi5,
            R.drawable.duizi6,
            R.drawable.baozi7,
            R.drawable.baozi8,
            R.drawable.baozi9,
            R.drawable.baozi10,
            R.drawable.baozij,
            R.drawable.baoziq,
            R.drawable.baozik,
    };

    //对子
    int[] images_duizi = {
            R.drawable.duizi1,
            R.drawable.duizi2,
            R.drawable.duizi3,
            R.drawable.duizi4,
            R.drawable.duizi5,
            R.drawable.duizi6,
            R.drawable.duizi7,
            R.drawable.duizi8,
            R.drawable.duizi9,
            R.drawable.duizi10,
            R.drawable.duizi11,
            R.drawable.duizi12,
            R.drawable.duizi13,
    };

    //顺子
    int[] images_shunzi = {
            R.drawable.shunzi1,
            R.drawable.shunzi2,
            R.drawable.shunzi3,
            R.drawable.shunzi4,
            R.drawable.shunzi5,
            R.drawable.shunzi6,
            R.drawable.shunzi7,
            R.drawable.shunzi8,
            R.drawable.shunzi9,
            R.drawable.shunzi10,
            R.drawable.shunzi11,
            R.drawable.shunzi12,

    };

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
        name = getIntent().getStringExtra("name");
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
                openstate=mInfo.getStatus();
                try {
                    name2=mInfo.getName();
                    Log.e("name2", name2);
                }
                catch (Exception e) {
                    Log.e("name2", "error");
                }
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
                openstate=mInfo.getStatus();

                try {
                    name2=mInfo.getName();
                    Log.e("name2", name2);
                }
                catch (Exception e) {
                    Log.e("name2", "error");
                }

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
        /////////
        mAdapter = new Adapter(mActivity, R.layout.list_item_buylist, mInfo.getBuy_detail());
        buyList.setAdapter(mAdapter);
        // opennumtwo = mInfo.getOpen_num_two();
//
        mAdapter2 = new Adapter2(mActivity, R.layout.list_item_openlist2, mInfo.getOpen_num_two());
        openList2.setAdapter(mAdapter2);
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
        paramMap.put("order_id", order_id);
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
     * 列表适配器2
     */
    public class Adapter2 extends CommonAdapter<opennumdetailBO> {

        public Adapter2(Context context, int layoutId, List<opennumdetailBO> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(final ViewHolder holder, final opennumdetailBO info) {
            List<String> temp3;
            temp3 = JSON.parseArray(info.getOpen_num_detail(), String.class);
            final TagFlowLayout numTag2 = holder.getView(R.id.num_tag2);
            TagAdapter<String> numAdapter2 = new TagAdapter<String>(temp3) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag2, false);
                    Log.e("opennumtwo：", s);

                        try {
                            temp = s.substring(1);
                            temprary = temp.substring(0, 1);
                            try {
                                switch (s.substring(0, 1)) {
                                    case "1":
                                        tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_item_open2, numTag2, false);
                                        tv.setBackgroundResource(R.drawable.tonghua1);

                                        if (temprary.equals("0")) {
                                            tv.setText(temp.substring(1));
                                        } else {
                                            tv.setText(s.substring(1));
                                        }
                                        break;

                                    case "2":
                                        tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_item_open2, numTag2, false);
                                        tv.setBackgroundResource(R.drawable.tonghua2);
                                        if (temprary.equals("0")) {
                                            tv.setText(temp.substring(1));
                                        } else {
                                            tv.setText(s.substring(1));
                                        }
                                        break;
                                    case "3":
                                        tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_item_open2, numTag2, false);
                                        tv.setBackgroundResource(R.drawable.tonghua3);
                                        if (temprary.equals("0")) {
                                            tv.setText(temp.substring(1));
                                        } else {
                                            tv.setText(s.substring(1));
                                        }
                                        break;
                                    case "4":
                                        tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_item_open2, numTag2, false);
                                        tv.setBackgroundResource(R.drawable.tonghua4);
                                        if (temprary.equals("0")) {
                                            tv.setText(temp.substring(1));
                                        } else {
                                            tv.setText(s.substring(1));
                                        }
                                        break;
                                }
                            }
                            catch (Exception e)
                            {

                            }

                        } catch (Exception e) {


                        }



                    return tv;
                }
            };
            numTag2.setAdapter(numAdapter2);
            try {
                if (!name2.equals("扑克3")) {
                    try {
                      numTag2.setVisibility(View.GONE);
                      openNum.setVisibility(View.VISIBLE);

                    } catch (Exception e) {

                    }
                } else {
                    try {
                        if (TextUtils.isEmpty(mInfo.getOpen_num())||openstate.equals("0")) {
                            openNum.setVisibility( View.GONE);
                            openNum.setText("未开奖");
                            numTag2.setVisibility(View.VISIBLE);
                        } else {
                            openNum.setVisibility( View.GONE);
                            numTag2.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {

                    }


                }
            } catch (Exception e) {

            }

        }

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
            List<String> temp;
            List<String> temp2;

            temp = JSON.parseArray(info.getBuy_num_two(), String.class);
            temp2 = JSON.parseArray(temp.get(0), String.class);


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


            final TagFlowLayout numTag = holder.getView(R.id.num_tag);
            TagAdapter<String> numAdapter = new TagAdapter<String>(temp2) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {

                    TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                    switch (info.getBuy_type()) {

                        case "2"://同花单选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_tonghua[Integer.parseInt(s) - 1]);
                            break;
                        case "3"://顺子单选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_shunzi[Integer.parseInt(s) - 1]);
                            break;

                        case "4"://同花顺单选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_tonghuashun[Integer.parseInt(s) - 1]);
                            break;
                        case "5"://豹子单选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_baozi[Integer.parseInt(s) - 1]);
                            break;
                        case "6"://对子单选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_duizi[Integer.parseInt(s) - 1]);
                            break;

                        case "8"://任选2拖胆
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;


                        case "9"://任选3拖胆
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;
                        case "10"://任选4拖胆
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;

                        case "11"://任选5拖胆
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;
                        case "12"://任选6拖胆
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;
                        case "13"://任选1普通
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;
                        case "14"://任选2普通
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;
                        case "15"://任选3普通
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;

                        case "16"://任选4普通
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;
                        case "17"://任选5普通
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;

                        case "18"://任选6普通
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images[Integer.parseInt(s) - 1]);
                            break;

                        case "21"://同花包选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_baoxuan[1]);
                            break;
                        case "22"://顺子包选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_baoxuan[4]);
                            break;
                        case "23"://同花顺包选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_baoxuan[0]);
                            break;
                        case "24"://豹子包选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_baoxuan[3]);
                            break;
                        case "25"://对子包选
                            tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_open2, numTag, false);
                            tv.setBackgroundResource(images_baoxuan[2]);
                            break;

                    }


                    return tv;
                }
            };
            numTag.setAdapter(numAdapter);
            try {
                if (!name2.equals("扑克3")) {
                    try {
                        holder.setText(R.id.numbers, nums);
                        numTag.setVisibility(View.GONE);
                    } catch (Exception e) {

                    }
                } else {
                    try {
                        holder.setVisible(R.id.numbers, false);
                        numTag.setVisibility(View.VISIBLE);
                    } catch (Exception e) {

                    }


                }
            } catch (Exception e) {

            }
        }
    }
}
