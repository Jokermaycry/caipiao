package com.daotian.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.ElevenSortInfo;
import com.daotian.Model.NumInfo;
import com.daotian.Model.ResultBO;
import com.daotian.Model.TicketDetailInfo;
import com.daotian.R;
import com.daotian.Utils.ACache;
import com.daotian.Utils.ToastUtil;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 11选5玩法
 * Created by yzx on 16/11/15.
 */

public class ElevenForFiveActivity extends AppCompatActivity {

    //每注彩票价格
    private final int TicketPrice = 2;
    //普通投注
    private final int N_OPTIONAL2 = 1;//任选二
    private final int N_OPTIONAL3 = 2;//任选三
    private final int N_OPTIONAL4 = 3;
    private final int N_OPTIONAL5 = 4;
    private final int N_OPTIONAL6 = 5;
    private final int N_OPTIONAL7 = 6;
    private final int N_OPTIONAL8 = 7;
    private final int N_TOP_OPTIONAL = 8;//前一
    private final int N_TOP_OPTIONAL2 = 9;//前二直选
    private final int N_TOP_G_OPTIONAL2 = 10;//前二组选
    private final int N_TOP_OPTIONAL3 = 11;//前三直选
    private final int N_TOP_G_OPTIONAL3 = 12;//前三组选
    private final int N_H_OPTIONAL3 = 13;//乐选三
    private final int N_H_OPTIONAL4 = 14;//乐选四
    private final int N_H_OPTIONAL5 = 15;//乐选五
    //拖胆投注
    private final int S_OPTIONAL2 = 21;//任选二
    private final int S_OPTIONAL3 = 22;//任选三
    private final int S_OPTIONAL4 = 23;//任选四
    private final int S_OPTIONAL5 = 24;//任选五
    private final int S_OPTIONAL6 = 25;//任选六
    private final int S_OPTIONAL7 = 26;//任选七
    private final int S_OPTIONAL8 = 27;//任选八
    private final int S_TOP_OPTIONAL2 = 28;//前二直选
    private final int S_TOP_G_OPTIONAL2 = 29;//前二组选
    private final int S_TOP_OPTIONAL3 = 30;//前三直选
    private final int S_TOP_G_OPTIONAL3 = 31;//前三组选

    @BindView(R.id.clear_btn)
    TextView clearBtn;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.comfirm_btn)
    TextView comfirmBtn;
    @BindView(R.id.bottom_ly)
    LinearLayout bottomLy;
    @BindView(R.id.num_tag_two)
    TagFlowLayout numTagTwo;
    @BindView(R.id.num_tag_ly2)
    LinearLayout numTagLy2;
    @BindView(R.id.num_tag_three)
    TagFlowLayout numTagThree;
    @BindView(R.id.num_tag_ly3)
    LinearLayout numTagLy3;
    @BindView(R.id.random_btn)
    TextView randomBtn;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.last_qs)
    TextView lastQs;
    @BindView(R.id.last_open_num)
    TextView lastOpenNum;
    @BindView(R.id.now_qs)
    TextView nowQs;
    @BindView(R.id.current_time)
    TextView currentTime;
    @BindView(R.id.rule_tv2)
    TextView ruleTv2;

    private int mSelecte_Mode = N_OPTIONAL2;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_ly)
    LinearLayout titleLy;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.num_tag)
    TagFlowLayout numTag;
    @BindView(R.id.titlebar_ly)
    RelativeLayout titlebarLy;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.rule_tv)
    TextView ruleTv;

    private PopupWindow mPopSort;
    private List<String> nums = new ArrayList<>();
    private TagAdapter<String> numAdapter;
    private TagAdapter<String> numAdapter2;
    private TagAdapter<String> numAdapter3;
    //玩法弹窗适配器
    private NoramlAdapter normalAdapter;
    private SpaceAdapter spacelAdapter;

    private List<ElevenSortInfo> elevenNormalList = new ArrayList<>();
    private List<ElevenSortInfo> elevenSpaceList = new ArrayList<>();

    private List<NumInfo> one_normal = new ArrayList<>();
    private List<NumInfo> two_normal = new ArrayList<>();
    private List<NumInfo> three_normal = new ArrayList<>();

    private Activity mActivity;
    private TicketDetailInfo mInfo;
    private String sh_name;
    private boolean mContinue;
    private ProgressDialog dialog;
    private ACache mAcache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevenforfive);
        ButterKnife.bind(this);
        mActivity = this;
        mAcache=ACache.get(this);
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("正在联网下载数据...");
        dialog.setMessage("请稍后...");
        dialog.show();
        sh_name = getIntent().getStringExtra("sh_name");
        mContinue = getIntent().getBooleanExtra("continue", false);
        mSelecte_Mode=getIntent().getIntExtra("mode",1);
        initData();
        initView();
        initView2();
        initRule();
        getDetail();

        initBefore();
    }

    private void initView2() {
        if(mSelecte_Mode==N_TOP_OPTIONAL ||mSelecte_Mode==N_OPTIONAL2||mSelecte_Mode==N_OPTIONAL3||mSelecte_Mode==N_OPTIONAL4||mSelecte_Mode==N_OPTIONAL5
                ||mSelecte_Mode==N_OPTIONAL6||mSelecte_Mode==N_OPTIONAL7||mSelecte_Mode==N_OPTIONAL8||mSelecte_Mode==N_H_OPTIONAL4||mSelecte_Mode==N_H_OPTIONAL5){
            numAdapter = new TagAdapter<String>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(ElevenForFiveActivity.this).inflate(R.layout.list_item_tv, numTag, false);
                    tv.setText(s);
                    return tv;
                }
            };
            numAdapter2 = new TagAdapter<String>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(ElevenForFiveActivity.this).inflate(R.layout.list_item_tv, numTagTwo, false);
                    tv.setText(s);
                    return tv;
                }
            };
            numAdapter3 = new TagAdapter<String>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(ElevenForFiveActivity.this).inflate(R.layout.list_item_tv, numTagThree, false);
                    tv.setText(s);
                    return tv;
                }
            };
        }else{
            numAdapter = new TagAdapter<String>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(ElevenForFiveActivity.this).inflate(R.layout.list_item_tv3, numTag, false);
                    tv.setText(s);
                    return tv;
                }
            };
            numAdapter2 = new TagAdapter<String>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(ElevenForFiveActivity.this).inflate(R.layout.list_item_tv3, numTagTwo, false);
                    tv.setText(s);
                    return tv;
                }
            };
            numAdapter3 = new TagAdapter<String>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(ElevenForFiveActivity.this).inflate(R.layout.list_item_tv3, numTagThree, false);
                    tv.setText(s);
                    return tv;
                }
            };
        }

        numTag.setAdapter(numAdapter);
        numTagTwo.setAdapter(numAdapter2);
        numTagThree.setAdapter(numAdapter3);
    }

    private void initBefore() {
        String mode=mAcache.getAsString("SELECT_MODE");
        if(TextUtils.isEmpty(mode)){
            return;
        }
        mSelecte_Mode=Integer.valueOf(mode);
        initRule();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }

    @Override
    public void onBackPressed() {
        showBackPop();
    }

    public void getDetail() {
        //从网络中获取
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sh_name", sh_name);
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.getInit, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.e("init_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    ToastUtil.toast(mActivity, resultBO.getResultMsg());
                    return;
                }
                mInfo = JSON.parseObject(resultBO.getResultData(), TicketDetailInfo.class);
                name.setText(mInfo.getName());
                lastQs.setText("上期(" + mInfo.getLast_qs() + ")开奖:");
                lastOpenNum.setText(mInfo.getOpen_num());
                nowQs.setText("距本期(" + mInfo.getNow_qs() + ")截止时间:");
                mInfo.setCurrent_time(Long.valueOf(mInfo.getOpen_time()) - Long.valueOf(mInfo.getNow_time())-Long.valueOf(mInfo.getAfter_time())+"");
                handler.sendEmptyMessage(1);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                ToastUtil.toast(mActivity, "请求失败:" + responseBody);

            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mInfo.setCurrent_time(String.valueOf(Long.valueOf(mInfo.getCurrent_time()) - 1));
                    long time = Long.valueOf(mInfo.getCurrent_time());
                    currentTime.setText(time / 60 + "分" + time % 60 + "秒");
                    //停止购买
                    if (time <= 1) {
                        handler.removeMessages(1);
                        showOverTimePop();
                    } else {
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }

                    break;
            }
        }

    };

    private void initView() {
        //第一组选择号码
        numTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Log.e("numTag", position + "");
                if(mSelecte_Mode==S_OPTIONAL2|| mSelecte_Mode==S_TOP_G_OPTIONAL2 || mSelecte_Mode==S_TOP_OPTIONAL2){
                    if(numTag.getSelectedList().size()>1){
                        ToastUtil.toast(ElevenForFiveActivity.this, "至多选择一位");
                        int num = 0;
                        for (int i = 0; i < one_normal.size(); i++) {
                            if (one_normal.get(i).is_checked()) {
                                numAdapter.setSelectedList(i);
                                num++;
                            }
                        }
                        if (num == 0) {
                            numAdapter.notifyDataChanged();
                        }
                        return false;
                    }
                }else if(mSelecte_Mode==S_OPTIONAL3 || mSelecte_Mode==S_TOP_G_OPTIONAL3 || mSelecte_Mode==S_TOP_OPTIONAL3){
                    if(numTag.getSelectedList().size()>2){
                        ToastUtil.toast(ElevenForFiveActivity.this, "至多选择二位");
                        int num = 0;
                        for (int i = 0; i < one_normal.size(); i++) {
                            if (one_normal.get(i).is_checked()) {
                                numAdapter.setSelectedList(i);
                                num++;
                            }
                        }
                        if (num == 0) {
                            numAdapter.notifyDataChanged();
                        }
                        return false;
                    }
                }else if(mSelecte_Mode==S_OPTIONAL4){
                    if(numTag.getSelectedList().size()>3){
                        ToastUtil.toast(ElevenForFiveActivity.this, "至多选择三位");
                        int num = 0;
                        for (int i = 0; i < one_normal.size(); i++) {
                            if (one_normal.get(i).is_checked()) {
                                numAdapter.setSelectedList(i);
                                num++;
                            }
                        }
                        if (num == 0) {
                            numAdapter.notifyDataChanged();
                        }
                        return false;
                    }
                }else if(mSelecte_Mode==S_OPTIONAL5){
                    if(numTag.getSelectedList().size()>4){
                        ToastUtil.toast(ElevenForFiveActivity.this, "至多选择四位");
                        int num = 0;
                        for (int i = 0; i < one_normal.size(); i++) {
                            if (one_normal.get(i).is_checked()) {
                                numAdapter.setSelectedList(i);
                                num++;
                            }
                        }
                        if (num == 0) {
                            numAdapter.notifyDataChanged();
                        }
                        return false;
                    }
                }else if(mSelecte_Mode==S_OPTIONAL6){
                    if(numTag.getSelectedList().size()>5){
                        ToastUtil.toast(ElevenForFiveActivity.this, "至多选择五位");
                        int num = 0;
                        for (int i = 0; i < one_normal.size(); i++) {
                            if (one_normal.get(i).is_checked()) {
                                numAdapter.setSelectedList(i);
                                num++;
                            }
                        }
                        if (num == 0) {
                            numAdapter.notifyDataChanged();
                        }
                        return false;
                    }
                }else if(mSelecte_Mode==S_OPTIONAL7){
                    if(numTag.getSelectedList().size()>6){
                        ToastUtil.toast(ElevenForFiveActivity.this, "至多选择六位");
                        int num = 0;
                        for (int i = 0; i < one_normal.size(); i++) {
                            if (one_normal.get(i).is_checked()) {
                                numAdapter.setSelectedList(i);
                                num++;
                            }
                        }
                        if (num == 0) {
                            numAdapter.notifyDataChanged();
                        }
                        return false;
                    }
                }else if(mSelecte_Mode==S_OPTIONAL8){
                    if(numTag.getSelectedList().size()>7){
                        ToastUtil.toast(ElevenForFiveActivity.this, "至多选择七位");
                        int num = 0;
                        for (int i = 0; i < one_normal.size(); i++) {
                            if (one_normal.get(i).is_checked()) {
                                numAdapter.setSelectedList(i);
                                num++;
                            }
                        }
                        if (num == 0) {
                            numAdapter.notifyDataChanged();
                        }
                        return false;
                    }
                }

                if (two_normal.get(position).is_checked() || three_normal.get(position).is_checked()) {
                    ToastUtil.toast(ElevenForFiveActivity.this, "每一位不能重复");
                    int num = 0;
                    for (int i = 0; i < one_normal.size(); i++) {
                        if (one_normal.get(i).is_checked()) {
                            numAdapter.setSelectedList(i);
                            num++;
                        }
                    }
                    if (num == 0) {
                        numAdapter.notifyDataChanged();
                    }
                    return false;
                }
                if (one_normal.get(position).is_checked()) {
                    one_normal.get(position).setIs_checked(false);
                } else {
                    one_normal.get(position).setIs_checked(true);
                }

                switch (mSelecte_Mode) {
                    case N_OPTIONAL2:
                        if (numTag.getSelectedList().size() < 2) {
                            num.setText("0");
                            price.setText("0");
                        } else if (numTag.getSelectedList().size() == 2) {
                            num.setText("1");
                            price.setText("2");
                        } else {
                            allnum = 0;
                            rank(numTag.getSelectedList().size(), 2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case N_OPTIONAL3:
                        if (numTag.getSelectedList().size() < 3) {
                            num.setText("0");
                            price.setText("0");
                        } else if (numTag.getSelectedList().size() == 3) {
                            num.setText("1");
                            price.setText("2");
                        } else {
                            allnum = 0;
                            rank(numTag.getSelectedList().size(), 3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case N_OPTIONAL4:
                        if (numTag.getSelectedList().size() < 4) {
                            num.setText("0");
                            price.setText("0");
                        } else if (numTag.getSelectedList().size() == 4) {
                            num.setText("1");
                            price.setText("2");
                        } else {
                            allnum = 0;
                            rank(numTag.getSelectedList().size(), 4);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case N_OPTIONAL5:
                        if (numTag.getSelectedList().size() < 5) {
                            num.setText("0");
                            price.setText("0");
                        } else if (numTag.getSelectedList().size() == 5) {
                            num.setText("1");
                            price.setText("2");
                        } else {
                            allnum = 0;
                            rank(numTag.getSelectedList().size(), 5);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case N_OPTIONAL6:
                        if (numTag.getSelectedList().size() < 6) {
                            num.setText("0");
                            price.setText("0");
                        } else if (numTag.getSelectedList().size() == 6) {
                            num.setText("1");
                            price.setText("2");
                        } else {
                            allnum = 0;
                            rank(numTag.getSelectedList().size(), 6);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case N_OPTIONAL7:
                        if (numTag.getSelectedList().size() < 7) {
                            num.setText("0");
                            price.setText("0");
                        } else if (numTag.getSelectedList().size() == 7) {
                            num.setText("1");
                            price.setText("2");
                        } else {
                            allnum = 0;
                            rank(numTag.getSelectedList().size(), 7);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case N_OPTIONAL8:
                        if (numTag.getSelectedList().size() < 8) {
                            num.setText("0");
                            price.setText("0");
                        } else if (numTag.getSelectedList().size() == 8) {
                            num.setText("1");
                            price.setText("2");
                        } else {
                            allnum = 0;
                            rank(numTag.getSelectedList().size(), 8);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    //前一
                    case N_TOP_OPTIONAL:
                        if(numTag.getSelectedList()!=null){
                            num.setText(numTag.getSelectedList().size()+"");
                            price.setText(numTag.getSelectedList().size() * TicketPrice+"");
                        }
                        break;
                    //前二组选
                    case N_TOP_G_OPTIONAL2:
                        if (numTag.getSelectedList().size() < 2) {
                            num.setText("0");
                            price.setText("0");
                        } else if (numTag.getSelectedList().size() == 2) {
                            num.setText("1");
                            price.setText("2");
                        } else {
                            allnum = 0;
                            rank(numTag.getSelectedList().size(), 2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    //前三组选
                    case N_TOP_G_OPTIONAL3:
                        if (numTag.getSelectedList().size() < 3) {
                            num.setText("0");
                            price.setText("0");
                        } else if (numTag.getSelectedList().size() == 3) {
                            num.setText("1");
                            price.setText("2");
                        } else {
                            allnum = 0;
                            rank(numTag.getSelectedList().size(), 3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    //前二直选
                    case N_TOP_OPTIONAL2:
                        if (numTag.getSelectedList().size() < 1 || numTagTwo.getSelectedList().size() < 1) {
                            num.setText("0");
                            price.setText("0");
                        } else {
                            num.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size() + "");
                            price.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }
                        break;
                    //前三直选
                    case N_TOP_OPTIONAL3:
                        if (numTag.getSelectedList().size() < 1 || numTagTwo.getSelectedList().size() < 1 || numTagThree.getSelectedList().size() < 1) {
                            num.setText("0");
                            price.setText("0");
                        } else {
                            num.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size() * numTagThree.getSelectedList().size() + "");
                            price.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size() * numTagThree.getSelectedList().size() * TicketPrice + "");
                        }
                        break;

                    //乐选三
                    case N_H_OPTIONAL3:
                        if(numTag.getSelectedList().size() > 1){
                            ToastUtil.toast(ElevenForFiveActivity.this, "每位只能选一个");
                            int num = 0;
                            for (int i = 0; i < one_normal.size(); i++) {
                                if (one_normal.get(i).is_checked()) {
                                    if(i!=position){
                                        numAdapter.setSelectedList(i);
                                        num++;
                                    }else{
                                        one_normal.get(i).setIs_checked(false);
                                    }
                                }
                            }
                            if (num == 0) {
                                numAdapter.notifyDataChanged();
                            }

                            return false;
                        }
                        if (numTag.getSelectedList().size() == 1 && numTagTwo.getSelectedList().size() == 1 && numTagThree.getSelectedList().size() == 1) {
                            num.setText("1");
                            price.setText("6");
                        } else {
                            num.setText("0");
                            price.setText("0");
                        }
                        break;
                    //乐选四
                    case N_H_OPTIONAL4:
                        if(numTag.getSelectedList().size() > 4){
                            ToastUtil.toast(ElevenForFiveActivity.this, "最多只能选4个号码");
                            int num = 0;
                            for (int i = 0; i < one_normal.size(); i++) {
                                if (one_normal.get(i).is_checked()) {
                                    if(i!=position){
                                        numAdapter.setSelectedList(i);
                                        num++;
                                    }else{
                                        one_normal.get(i).setIs_checked(false);
                                    }
                                }
                            }
                            if (num == 0) {
                                numAdapter.notifyDataChanged();
                            }
                            return false;
                        }
                        if (numTag.getSelectedList().size() == 4 ) {
                            num.setText("1");
                            price.setText("10");
                        } else {
                            num.setText("0");
                            price.setText("0");
                        }
                        break;
                    //乐选五
                    case N_H_OPTIONAL5:
                        if(numTag.getSelectedList().size() > 5){
                            ToastUtil.toast(ElevenForFiveActivity.this, "最多只能选5个号码");
                            int num = 0;
                            for (int i = 0; i < one_normal.size(); i++) {
                                if (one_normal.get(i).is_checked()) {
                                    if(i!=position){
                                        numAdapter.setSelectedList(i);
                                        num++;
                                    }else{
                                        one_normal.get(i).setIs_checked(false);
                                    }
                                }
                            }
                            if (num == 0) {
                                numAdapter.notifyDataChanged();
                            }
                            return false;
                        }
                        if (numTag.getSelectedList().size() == 5 ) {
                            num.setText("1");
                            price.setText("14");
                        } else {
                            num.setText("0");
                            price.setText("0");
                        }
                        break;

                    case S_OPTIONAL2:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<2 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else {
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL3:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<3 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==2){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL4:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<4|| numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==3){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL5:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<5 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==4){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==3){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),4);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL6:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<6 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==5){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==4){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==3){
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),4);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),5);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL7:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<7 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==6){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==5){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==4){
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==3){
                            rank(numTagTwo.getSelectedList().size(),4);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),5);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),6);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL8:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<8 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==7){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }
                        else if(numTag.getSelectedList().size()==6){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==5){
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==4){
                            rank(numTagTwo.getSelectedList().size(),4);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==3){
                            rank(numTagTwo.getSelectedList().size(),5);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),6);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),7);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_TOP_G_OPTIONAL2:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<2 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else {
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }
                        break;
                    case S_TOP_G_OPTIONAL3:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<3 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==2){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_TOP_OPTIONAL2:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<2 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else {
                            num.setText( numTagTwo.getSelectedList().size()*2 + "");
                            price.setText( (numTagTwo.getSelectedList().size()*2) * TicketPrice + "");
                        }
                        break;
                    case S_TOP_OPTIONAL3:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<3 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else{
                            if(numTagTwo.getSelectedList().size()==2 && numTag.getSelectedList().size()==1){
                                num.setText(6 + "");
                                price.setText(6 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==1 && numTag.getSelectedList().size()==2){
                                num.setText(6 + "");
                                price.setText(6 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==2 && numTag.getSelectedList().size()==2){
                                num.setText(12 + "");
                                price.setText(12 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==3 && numTag.getSelectedList().size()==1){
                                num.setText(18 + "");
                                price.setText(18 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==4 && numTag.getSelectedList().size()==1){
                                num.setText(36 + "");
                                price.setText(36 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==5 && numTag.getSelectedList().size()==1){
                                num.setText(60 + "");
                                price.setText(60 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==6 && numTag.getSelectedList().size()==1){
                                num.setText(90 + "");
                                price.setText(90 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==7 && numTag.getSelectedList().size()==1){
                                num.setText(126 + "");
                                price.setText(126 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==8 && numTag.getSelectedList().size()==1){
                                num.setText(168 + "");
                                price.setText(168 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==9 && numTag.getSelectedList().size()==1){
                                num.setText(216 + "");
                                price.setText(216 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==10 && numTag.getSelectedList().size()==1){
                                num.setText(270 + "");
                                price.setText(270 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==3 && numTag.getSelectedList().size()==2){
                                num.setText(18 + "");
                                price.setText(18 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==4 && numTag.getSelectedList().size()==2){
                                num.setText(24 + "");
                                price.setText(24 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==5 && numTag.getSelectedList().size()==2){
                                num.setText(30 + "");
                                price.setText(30 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==6 && numTag.getSelectedList().size()==2){
                                num.setText(36 + "");
                                price.setText(36 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==7 && numTag.getSelectedList().size()==2){
                                num.setText(42 + "");
                                price.setText(42 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==8 && numTag.getSelectedList().size()==2){
                                num.setText(48 + "");
                                price.setText(48 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==9 && numTag.getSelectedList().size()==2){
                                num.setText(54 + "");
                                price.setText(54 * TicketPrice + "");
                            }
                        }
                }
                return true;
            }
        });
        numTagTwo.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Log.e("numTagTwo", position + "");

                if (one_normal.get(position).is_checked() || three_normal.get(position).is_checked()) {
                    ToastUtil.toast(ElevenForFiveActivity.this, "每一位不能重复");
                    int num = 0;
                    for (int i = 0; i < two_normal.size(); i++) {
                        if (two_normal.get(i).is_checked()) {
                            numAdapter2.setSelectedList(i);
                            num++;
                        }
                    }
                    if (num == 0) {
                        numAdapter2.notifyDataChanged();
                    }
                    return false;
                }
                if (two_normal.get(position).is_checked()) {
                    two_normal.get(position).setIs_checked(false);
                } else {
                    two_normal.get(position).setIs_checked(true);
                }
                switch (mSelecte_Mode) {
                    //乐选三
                    case N_H_OPTIONAL3:
                        if(numTagTwo.getSelectedList().size() > 1){
                            ToastUtil.toast(ElevenForFiveActivity.this, "每位只能选一个");
                            int num = 0;
                            for (int i = 0; i < two_normal.size(); i++) {
                                if (two_normal.get(i).is_checked()) {
                                    if(i!=position){
                                        numAdapter2.setSelectedList(i);
                                        num++;
                                    }else{
                                        two_normal.get(i).setIs_checked(false);
                                    }
                                }
                            }
                            if (num == 0) {
                                numAdapter2.notifyDataChanged();
                            }
                            return false;
                        }
                        if (numTag.getSelectedList().size() == 1 && numTagTwo.getSelectedList().size() == 1 && numTagThree.getSelectedList().size() == 1) {
                            num.setText("1");
                            price.setText("6");
                        } else {
                            num.setText("0");
                            price.setText("0");
                        }
                        break;

                    //前二直选
                    case N_TOP_OPTIONAL2:
                        if (numTag.getSelectedList().size() < 1 || numTagTwo.getSelectedList().size() < 1) {
                            num.setText("0");
                            price.setText("0");
                        } else {
                            num.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size() + "");
                            price.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }
                        break;
                    //前三直选
                    case N_TOP_OPTIONAL3:
                        if (numTag.getSelectedList().size() < 1 || numTagTwo.getSelectedList().size() < 1 || numTagThree.getSelectedList().size() < 1) {
                            num.setText("0");
                            price.setText("0");
                        } else {
                            num.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size() * numTagThree.getSelectedList().size() + "");
                            price.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size() * numTagThree.getSelectedList().size() * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL2:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<2 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else {
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL3:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<3 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==2){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL4:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<4 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==3){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL5:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<5 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==4){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==3){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),4);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL6:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<6 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==5){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==4){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==3){
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),4);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),5);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL7:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<7 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==6){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==5){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==4){
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==3){
                            rank(numTagTwo.getSelectedList().size(),4);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),5);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),6);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_OPTIONAL8:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<8 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==7){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }
                        else if(numTag.getSelectedList().size()==6){
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==5){
                            rank(numTagTwo.getSelectedList().size(),3);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==4){
                            rank(numTagTwo.getSelectedList().size(),4);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==3){
                            rank(numTagTwo.getSelectedList().size(),5);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else if(numTag.getSelectedList().size()==2){
                            rank(numTagTwo.getSelectedList().size(),6);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),7);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_TOP_G_OPTIONAL2:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<2 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else {
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }
                        break;
                    case S_TOP_G_OPTIONAL3:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<3 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else if(numTag.getSelectedList().size()==2){
                            num.setText( numTagTwo.getSelectedList().size() + "");
                            price.setText( numTagTwo.getSelectedList().size() * TicketPrice + "");
                        }else{
                            rank(numTagTwo.getSelectedList().size(),2);
                            num.setText(allnum + "");
                            price.setText(allnum * TicketPrice + "");
                        }
                        break;
                    case S_TOP_OPTIONAL2:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<2 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else {
                            num.setText( numTagTwo.getSelectedList().size()*2 + "");
                            price.setText( (numTagTwo.getSelectedList().size()*2) * TicketPrice + "");
                        }
                        break;
                    case S_TOP_OPTIONAL3:
                        if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<3 || numTag.getSelectedList().size()<1
                                ||numTagTwo.getSelectedList().size()<1) {
                            num.setText("0");
                            price.setText("0");
                        }else{
                            if(numTagTwo.getSelectedList().size()==2 && numTag.getSelectedList().size()==1){
                                num.setText(6 + "");
                                price.setText(6 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==1 && numTag.getSelectedList().size()==2){
                                num.setText(6 + "");
                                price.setText(6 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==2 && numTag.getSelectedList().size()==2){
                                num.setText(12 + "");
                                price.setText(12 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==3 && numTag.getSelectedList().size()==1){
                                num.setText(18 + "");
                                price.setText(18 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==4 && numTag.getSelectedList().size()==1){
                                num.setText(36 + "");
                                price.setText(36 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==5 && numTag.getSelectedList().size()==1){
                                num.setText(60 + "");
                                price.setText(60 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==6 && numTag.getSelectedList().size()==1){
                                num.setText(90 + "");
                                price.setText(90 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==7 && numTag.getSelectedList().size()==1){
                                num.setText(126 + "");
                                price.setText(126 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==8 && numTag.getSelectedList().size()==1){
                                num.setText(168 + "");
                                price.setText(168 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==9 && numTag.getSelectedList().size()==1){
                                num.setText(216 + "");
                                price.setText(216 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==10 && numTag.getSelectedList().size()==1){
                                num.setText(270 + "");
                                price.setText(270 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==3 && numTag.getSelectedList().size()==2){
                                num.setText(18 + "");
                                price.setText(18 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==4 && numTag.getSelectedList().size()==2){
                                num.setText(24 + "");
                                price.setText(24 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==5 && numTag.getSelectedList().size()==2){
                                num.setText(30 + "");
                                price.setText(30 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==6 && numTag.getSelectedList().size()==2){
                                num.setText(36 + "");
                                price.setText(36 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==7 && numTag.getSelectedList().size()==2){
                                num.setText(42 + "");
                                price.setText(42 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==8 && numTag.getSelectedList().size()==2){
                                num.setText(48 + "");
                                price.setText(48 * TicketPrice + "");
                            }else if(numTagTwo.getSelectedList().size()==9 && numTag.getSelectedList().size()==2){
                                num.setText(54 + "");
                                price.setText(54 * TicketPrice + "");
                            }
                        }
                        break;
                }
                return true;
            }
        });
        numTagThree.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Log.e("numTagThree", position + "");
                if (two_normal.get(position).is_checked() || one_normal.get(position).is_checked()) {
                    ToastUtil.toast(ElevenForFiveActivity.this, "每一位不能重复");
                    int num = 0;
                    for (int i = 0; i < three_normal.size(); i++) {
                        if (three_normal.get(i).is_checked()) {
                            numAdapter3.setSelectedList(i);

                        }
                    }
                    if (num == 0) {
                        numAdapter3.notifyDataChanged();
                    }
                    return false;
                }
                if (three_normal.get(position).is_checked()) {
                    three_normal.get(position).setIs_checked(false);
                } else {
                    three_normal.get(position).setIs_checked(true);
                }
                switch (mSelecte_Mode){
                    //前三直选
                    case N_TOP_OPTIONAL3:
                        if (numTag.getSelectedList().size() < 1 || numTagTwo.getSelectedList().size() < 1 || numTagThree.getSelectedList().size() < 1) {
                            num.setText("0");
                            price.setText("0");
                        } else {
                            num.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size()*numTagThree.getSelectedList().size() + "");
                            price.setText(numTag.getSelectedList().size() * numTagTwo.getSelectedList().size()*numTagThree.getSelectedList().size() * TicketPrice + "");
                        }
                        break;
                    //乐选三
                    case N_H_OPTIONAL3:
                        if(numTagThree.getSelectedList().size() > 1){
                            ToastUtil.toast(ElevenForFiveActivity.this, "每位只能选一个");
                            int num = 0;
                            for (int i = 0; i < three_normal.size(); i++) {
                                if (three_normal.get(i).is_checked()) {
                                    if(i!=position){
                                        numAdapter3.setSelectedList(i);
                                        num++;
                                    }else{
                                        three_normal.get(i).setIs_checked(false);
                                    }
                                }
                            }
                            if (num == 0) {
                                numAdapter3.notifyDataChanged();
                            }
                            return false;
                        }
                        if (numTag.getSelectedList().size() == 1 && numTagTwo.getSelectedList().size() == 1 && numTagThree.getSelectedList().size() == 1) {
                            num.setText("1");
                            price.setText("6");
                        } else {
                            num.setText("0");
                            price.setText("0");
                        }
                        break;
                }
                return true;
            }
        });

        //玩法选项
        normalAdapter = new NoramlAdapter(ElevenForFiveActivity.this, R.layout.grid_item_tv, elevenNormalList);
        spacelAdapter = new SpaceAdapter(ElevenForFiveActivity.this, R.layout.grid_item_tv, elevenSpaceList);
    }

    /**
     * 初始化规则,
     */
    private void initRule() {
        initView2();
        numTagLy2.setVisibility(View.GONE);
        numTagLy3.setVisibility(View.GONE);
        switch (mSelecte_Mode) {
            case N_OPTIONAL2:
                title.setText("普通-任选二");
                ruleTv.setText("请至少选出二个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_OPTIONAL3:
                title.setText("普通-任选三");
                ruleTv.setText("请至少选出三个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_OPTIONAL4:
                title.setText("普通-任选四");
                ruleTv.setText("请至少选出四个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_OPTIONAL5:
                title.setText("普通-任选五");
                ruleTv.setText("请至少选出五个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_OPTIONAL6:
                title.setText("普通-任选六");
                ruleTv.setText("请至少选出六个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_OPTIONAL7:
                title.setText("普通-任选七");
                ruleTv.setText("请至少选出七个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_OPTIONAL8:
                title.setText("普通-任选八");
                ruleTv.setText("请至少选出八个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_TOP_OPTIONAL:
                title.setText("普通-前一");
                ruleTv.setText("请至少选出一个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_TOP_OPTIONAL2:
                title.setText("普通-前二直选");
                ruleTv.setText("每位至少选择一个号码");
                numTagLy2.setVisibility(View.VISIBLE);
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_TOP_G_OPTIONAL2:
                title.setText("普通-前二组选");
                ruleTv.setText("请至少选出二个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_TOP_OPTIONAL3:
                title.setText("普通-前三直选");
                ruleTv.setText("每位至少选择一个号码");
                numTagLy2.setVisibility(View.VISIBLE);
                numTagLy3.setVisibility(View.VISIBLE);
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_TOP_G_OPTIONAL3:
                title.setText("普通-前三组选");
                ruleTv.setText("请至少选出三个号码");
                ruleTv2.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                break;
            case N_H_OPTIONAL3:
                title.setText("普通-乐选三");
                ruleTv.setText("每位至少选择一个号码");
                numTagLy2.setVisibility(View.VISIBLE);
                numTagLy3.setVisibility(View.VISIBLE);

                break;
            case N_H_OPTIONAL4:
                title.setText("普通-乐选四");
                ruleTv.setText("至少选择4个号码");

                break;
            case N_H_OPTIONAL5:
                title.setText("普通-乐选五");
                ruleTv.setText("至少选择5个号码");

                break;

            case S_OPTIONAL2:
                title.setText("拖胆-任选二");
                ruleTv.setText("胆码 请选择一个号码");
                ruleTv2.setText("拖码 请至少选择二注号码");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_OPTIONAL3:
                title.setText("拖胆-任选三");
                ruleTv.setText("胆码 请至少选择一个号码，至多二个号码");
                ruleTv2.setText("拖码 请至少选择二注号码");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_OPTIONAL4:
                title.setText("拖胆-任选四");
                ruleTv.setText("胆码 请至少选择一个号码，至多三个号码");
                ruleTv2.setText("拖码 请至少选择二注号码");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_OPTIONAL5:
                title.setText("拖胆-任选五");
                ruleTv.setText("胆码 请至少选择一个号码，至多四个号码");
                ruleTv2.setText("拖码 请至少选择二注号码");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_OPTIONAL6:
                title.setText("拖胆-任选六");
                ruleTv.setText("胆码 请至少选择一个号码，至多五个号码");
                ruleTv2.setText("拖码 请至少选择二注号码");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_OPTIONAL7:
                title.setText("拖胆-任选七");
                ruleTv.setText("胆码 请至少选择一个号码，至多六个号码");
                ruleTv2.setText("拖码 请至少选择二注号码");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_OPTIONAL8:
                title.setText("拖胆-任选八");
                ruleTv.setText("胆码 请至少选择一个号码，至多七个号码");
                ruleTv2.setText("拖码 请至少选择二注号码");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_TOP_OPTIONAL2:
                title.setText("拖胆-前二直选");
                ruleTv.setText("胆码 选择1个");
                ruleTv2.setText("拖码 胆+拖不少于3个");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_TOP_OPTIONAL3:
                title.setText("拖胆-前三直选");
                ruleTv.setText("胆码 选择1-2个");
                ruleTv2.setText("拖码 胆+拖不少于3个");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_TOP_G_OPTIONAL2:
                title.setText("拖胆-前二组选");
                ruleTv.setText("胆码 选择1个");
                ruleTv2.setText("拖码 胆+拖不少于3个");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
            case S_TOP_G_OPTIONAL3:
                title.setText("拖胆-前三组选");
                ruleTv.setText("胆码 选择1-2个");
                ruleTv2.setText("拖码 胆+拖不少于4个");
                ruleTv2.setVisibility(View.VISIBLE);
                numTagLy2.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                break;
        }

    }

    /**
     * 初始化数据
     */
    private void initData() {
        //号码初始化
        for (int i = 1; i < 12; i++) {
            if (i > 9) {
                nums.add(i + "");
            } else {
                nums.add("0" + i);
            }
        }
        for (int i = 1; i < 12; i++) {
            NumInfo info = new NumInfo();
            if (i > 9) {
                info.setNum(i + "");
            } else {
                info.setNum("0" + i);
            }
            one_normal.add(info);
        }
        for (int i = 1; i < 12; i++) {
            NumInfo info = new NumInfo();
            if (i > 9) {
                info.setNum(i + "");
            } else {
                info.setNum("0" + i);
            }
            two_normal.add(info);
        }
        for (int i = 1; i < 12; i++) {
            NumInfo info = new NumInfo();
            if (i > 9) {
                info.setNum(i + "");
            } else {
                info.setNum("0" + i);
            }
            three_normal.add(info);
        }


        elevenNormalList.clear();
        //普通投注
        ElevenSortInfo info = new ElevenSortInfo();
        info.setIs_checked(true);
        info.setName("任选二");
        elevenNormalList.add(info);

        ElevenSortInfo info2 = new ElevenSortInfo();
        info2.setName("任选三");
        elevenNormalList.add(info2);

        ElevenSortInfo info3 = new ElevenSortInfo();
        info3.setName("任选四");
        elevenNormalList.add(info3);

        ElevenSortInfo info4 = new ElevenSortInfo();
        info4.setName("任选五");
        elevenNormalList.add(info4);
        ElevenSortInfo info5 = new ElevenSortInfo();
        info5.setName("任选六");
        elevenNormalList.add(info5);
        ElevenSortInfo info6 = new ElevenSortInfo();
        info6.setName("任选七");
        elevenNormalList.add(info6);
        ElevenSortInfo info7 = new ElevenSortInfo();
        info7.setName("任选八");
        elevenNormalList.add(info7);
        ElevenSortInfo info8 = new ElevenSortInfo();
        info8.setName("前一");
        elevenNormalList.add(info8);
        ElevenSortInfo info9 = new ElevenSortInfo();
        info9.setName("前二直选");
        elevenNormalList.add(info9);
        ElevenSortInfo info10 = new ElevenSortInfo();
        info10.setName("前二组选");
        elevenNormalList.add(info10);
        ElevenSortInfo info11 = new ElevenSortInfo();
        info11.setName("前三直选");
        elevenNormalList.add(info11);
        ElevenSortInfo info12 = new ElevenSortInfo();
        info12.setName("前三组选");
        elevenNormalList.add(info12);
        ElevenSortInfo info13 = new ElevenSortInfo();
        info13.setName("乐选三");
        elevenNormalList.add(info13);
        ElevenSortInfo info14 = new ElevenSortInfo();
        info14.setName("乐选四");
        elevenNormalList.add(info14);
        ElevenSortInfo info15 = new ElevenSortInfo();
        info15.setName("乐选五");
        elevenNormalList.add(info15);

        //拖胆投注
        ElevenSortInfo info22 = new ElevenSortInfo();
        info22.setName("任选二");
        elevenSpaceList.add(info22);
        ElevenSortInfo info23 = new ElevenSortInfo();
        info23.setName("任选三");
        elevenSpaceList.add(info23);
        ElevenSortInfo info24 = new ElevenSortInfo();
        info24.setName("任选四");
        elevenSpaceList.add(info24);
        ElevenSortInfo info25 = new ElevenSortInfo();
        info25.setName("任选五");
        elevenSpaceList.add(info25);
        ElevenSortInfo info26 = new ElevenSortInfo();
        info26.setName("任选六");
        elevenSpaceList.add(info26);
        ElevenSortInfo info27 = new ElevenSortInfo();
        info27.setName("任选七");
        elevenSpaceList.add(info27);
        ElevenSortInfo info28 = new ElevenSortInfo();
        info28.setName("任选八");
        elevenSpaceList.add(info28);
//        ElevenSortInfo info29 = new ElevenSortInfo();
//        info29.setName("前二直选");
//        elevenSpaceList.add(info29);
        ElevenSortInfo info30 = new ElevenSortInfo();
        info30.setName("前二组选");
        elevenSpaceList.add(info30);
//        ElevenSortInfo info31 = new ElevenSortInfo();
//        info31.setName("前三直选");
//        elevenSpaceList.add(info31);
        ElevenSortInfo info32 = new ElevenSortInfo();
        info32.setName("前三组选");
        elevenSpaceList.add(info32);


    }


    @OnClick({R.id.back, R.id.title_ly, R.id.more, R.id.clear_btn, R.id.comfirm_btn, R.id.random_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            //机选
            case R.id.random_btn:
                clearNumber();
                getRandow();
                break;
            case R.id.back:
                showBackPop();
                break;
            case R.id.title_ly:
                icon.setImageResource(R.drawable.icon_up);
                sort_pop();
                break;
            case R.id.more:
                Intent in=new Intent(mActivity,PlayIntroductionActivity.class);
                startActivity(in);
                break;
            case R.id.clear_btn:
                clearNumber();
                break;
            case R.id.comfirm_btn:
                numbersComfirm();
                break;
        }
    }

    /**
     * 设置机选号码
     */
    private void getRandow() {
        int[] result;
        num.setText("1");
        price.setText(TicketPrice + "");
        switch (mSelecte_Mode) {
            case N_OPTIONAL2:
                result = numberRandom(2);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_OPTIONAL3:
                result = numberRandom(3);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_OPTIONAL4:
                result = numberRandom(4);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_OPTIONAL5:
                result = numberRandom(5);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_OPTIONAL6:
                result = numberRandom(6);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_OPTIONAL7:
                result = numberRandom(7);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_OPTIONAL8:
                result = numberRandom(8);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_TOP_OPTIONAL:
                result = numberRandom(1);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_TOP_G_OPTIONAL2:
                result = numberRandom(2);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_TOP_G_OPTIONAL3:
                result = numberRandom(3);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                break;
            case N_TOP_OPTIONAL2:
                result = numberRandom(2);
                for (int i = 0; i < result.length; i++) {
                    if (i == 0) {
                        one_normal.get(result[i]).setIs_checked(true);
                        numAdapter.setSelectedList(result[i]);
                    } else if (i == 1) {
                        two_normal.get(result[i]).setIs_checked(true);
                        numAdapter2.setSelectedList(result[i]);
                    }
                }
                break;
            case N_TOP_OPTIONAL3:
                result = numberRandom(3);
                for (int i = 0; i < result.length; i++) {
                    if (i == 0) {
                        one_normal.get(result[i]).setIs_checked(true);
                        numAdapter.setSelectedList(result[i]);
                    } else if (i == 1) {
                        two_normal.get(result[i]).setIs_checked(true);
                        numAdapter2.setSelectedList(result[i]);
                    } else if (i == 2) {
                        three_normal.get(result[i]).setIs_checked(true);
                        numAdapter3.setSelectedList(result[i]);
                    }
                }
                break;
            case N_H_OPTIONAL3:
                result = numberRandom(3);
                for (int i = 0; i < result.length; i++) {
                    if (i == 0) {
                        one_normal.get(result[i]).setIs_checked(true);
                        numAdapter.setSelectedList(result[i]);
                    } else if (i == 1) {
                        two_normal.get(result[i]).setIs_checked(true);
                        numAdapter2.setSelectedList(result[i]);
                    } else if (i == 2) {
                        three_normal.get(result[i]).setIs_checked(true);
                        numAdapter3.setSelectedList(result[i]);
                    }
                }
                num.setText("1");
                price.setText("6");
                break;
            case N_H_OPTIONAL4:
                result = numberRandom(4);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                num.setText("1");
                price.setText("10");
                break;
            case N_H_OPTIONAL5:
                result = numberRandom(5);
                for (int i = 0; i < result.length; i++) {
                    one_normal.get(result[i]).setIs_checked(true);
                    numAdapter.setSelectedList(result[i]);
                }
                num.setText("1");
                price.setText("14");
                break;
        }

    }

    /**
     * 机选号码
     */
    private int[] numberRandom(int select_num) {
        int suit[] = new int[select_num]; //存储select_num个随机数
        boolean sw[] = new boolean[nums.size()]; //随机数存在。则为真，否则为假

        int key = 0;
        for (int i = 0; i < suit.length; i++) {
            while (true) {
                key = (int) (Math.random() * nums.size());
                if (sw[key] == false) {
                    break;
                }
            }
            suit[i] = key;
            sw[key] = true;
        }
        return suit;
    }

    /**
     * 选中号码
     */
    private void numbersComfirm() {
//        if(App.mUser==null || App.mUser.getAccess_token()==null){
//            ToastUtil.toast(mActivity,"请先登录！");
//            Intent in=new Intent(this,LoginActivity.class);
//            startActivity(in);
//            return;
//        }
        switch (mSelecte_Mode) {
            case N_OPTIONAL2:
                if (numTag.getSelectedList().size() < 2) {
                    ToastUtil.toast(this, "请至少选择二个号码");
                    return;
                }
                break;
            case N_OPTIONAL3:
                if (numTag.getSelectedList().size() < 3) {
                    ToastUtil.toast(this, "请至少选择三个号码");
                    return;
                }
                break;
            case N_OPTIONAL4:
                if (numTag.getSelectedList().size() < 4) {
                    ToastUtil.toast(this, "请至少选择四个号码");
                    return;
                }
                break;
            case N_OPTIONAL5:
                if (numTag.getSelectedList().size() < 5) {
                    ToastUtil.toast(this, "请至少选择五个号码");
                    return;
                }
                break;
            case N_OPTIONAL6:
                if (numTag.getSelectedList().size() < 6) {
                    ToastUtil.toast(this, "请至少选择六个号码");
                    return;
                }
                break;
            case N_OPTIONAL7:
                if (numTag.getSelectedList().size() < 7) {
                    ToastUtil.toast(this, "请至少选择七个号码");
                    return;
                }
                break;
            case N_OPTIONAL8:
                if (numTag.getSelectedList().size() < 8) {
                    ToastUtil.toast(this, "请至少选择八个号码");
                    return;
                }
                break;
            case N_TOP_OPTIONAL:
                if (numTag.getSelectedList().size() < 1) {
                    ToastUtil.toast(this, "请至少选择一个号码");
                    return;
                }
                break;
            case N_TOP_OPTIONAL2:
                if (numTag.getSelectedList().size() < 1 || numTagTwo.getSelectedList().size() < 1) {
                    ToastUtil.toast(this, "每位至少选择一个号码");
                    return;
                }
                break;
            case N_TOP_OPTIONAL3:
                if (numTag.getSelectedList().size() < 1 || numTagTwo.getSelectedList().size() < 1 || numTagThree.getSelectedList().size() < 1) {
                    ToastUtil.toast(this, "每位至少选择一个号码");
                    return;
                }
                break;
            case N_TOP_G_OPTIONAL2:
                if (numTag.getSelectedList().size() < 2) {
                    ToastUtil.toast(this, "请至少选择二个号码");
                    return;
                }
                break;
            case N_TOP_G_OPTIONAL3:
                if (numTag.getSelectedList().size() < 3) {
                    ToastUtil.toast(this, "请至少选择三个号码");
                    return;
                }
                break;
            case N_H_OPTIONAL3:
                if (numTag.getSelectedList().size() != 1 || numTagTwo.getSelectedList().size() != 1 || numTagThree.getSelectedList().size() != 1) {
                    ToastUtil.toast(this, "每位至少选择一个号码");
                    return;
                }
                break;
            case N_H_OPTIONAL4:
                if (numTag.getSelectedList().size() !=4 ) {
                    ToastUtil.toast(this, "请至少选择4个号码");
                    return;
                }
                break;
            case N_H_OPTIONAL5:
                if (numTag.getSelectedList().size()!=5 ) {
                    ToastUtil.toast(this, "请至少选择5个号码");
                    return;
                }
                break;
            case S_OPTIONAL2:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<1 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
                break;
            case S_OPTIONAL3:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<2 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
                break;
            case S_OPTIONAL4:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<3 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
                break;
            case S_OPTIONAL5:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<4 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
                break;
            case S_OPTIONAL6:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<5 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
            case S_OPTIONAL7:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<6 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
            case S_OPTIONAL8:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<7 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
                break;
            case S_TOP_OPTIONAL2:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<2 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
                break;
            case S_TOP_OPTIONAL3:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<3 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
                break;
            case S_TOP_G_OPTIONAL2:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<2 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
                break;
            case S_TOP_G_OPTIONAL3:
                if ((numTag.getSelectedList().size()+numTagTwo.getSelectedList().size())<3 || numTag.getSelectedList().size()<1
                        ||numTagTwo.getSelectedList().size()<1) {
                    ToastUtil.toast(this, "请至少选择一注");
                    return;
                }
                break;
        }


        if (mContinue) {
            Intent in = new Intent(ElevenForFiveActivity.this, ElevenForFiveDetailActivity.class);
            in.putExtra("result_one", (Serializable) one_normal);
            in.putExtra("result_two", (Serializable) two_normal);
            in.putExtra("result_three", (Serializable) three_normal);
            in.putExtra("selecte_mode", mSelecte_Mode);
            in.putExtra("num", num.getText().toString());
            in.putExtra("price", price.getText().toString());
            in.putExtra("title", mInfo.getName());
            in.putExtra("sh_name", mInfo.getSh_name());
            in.putExtra("type", mInfo.getType());
            in.putExtra("now_qs", mInfo.getNow_qs());
            setResult(RESULT_OK, in);
            finish();
        } else {
            Intent in = new Intent(ElevenForFiveActivity.this, ElevenForFiveDetailActivity.class);
            in.putExtra("result_one", (Serializable) one_normal);
            in.putExtra("result_two", (Serializable) two_normal);
            in.putExtra("result_three", (Serializable) three_normal);
            in.putExtra("selecte_mode", mSelecte_Mode);
            in.putExtra("num", num.getText().toString());
            in.putExtra("price", price.getText().toString());
            in.putExtra("title", mInfo.getName());
            in.putExtra("sh_name", mInfo.getSh_name());
            in.putExtra("type", mInfo.getType());
            in.putExtra("now_qs", mInfo.getNow_qs());
            startActivity(in);
            finish();
        }
    }

    /**
     * 清空号码
     */
    private void clearNumber() {
        initView2();
        num.setText("0");
        price.setText("0");
        for (int i = 0; i < one_normal.size(); i++) {
            one_normal.get(i).setIs_checked(false);
        }
        for (int i = 0; i < two_normal.size(); i++) {
            two_normal.get(i).setIs_checked(false);
        }
        for (int i = 0; i < three_normal.size(); i++) {
            three_normal.get(i).setIs_checked(false);
        }
    }


    //分类弹窗
    public void sort_pop() {
        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.pop_goodsort, null, false);

        GridView normalTicket = (GridView) view.findViewById(R.id.normal_ticket);
        GridView spaceTicket = (GridView) view.findViewById(R.id.space_ticket);
        normalTicket.setAdapter(normalAdapter);
        spaceTicket.setAdapter(spacelAdapter);
        // 创建PopupWindow对象
        mPopSort = new PopupWindow(view, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, false);
        LinearLayout other_ly = (LinearLayout) view.findViewById(R.id.other_ly);
        other_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopSort != null) {
                    icon.setImageResource(R.drawable.icon_down);
                    mPopSort.dismiss();
                }

            }
        });


        // 需要设置一下此参数，点击外边可消失
        mPopSort.setBackgroundDrawable(new ColorDrawable(0));
        //设置点击窗口外边窗口消失
        mPopSort.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        mPopSort.setFocusable(true);
        mPopSort.showAsDropDown(titlebarLy);
    }


    /**
     * 普通投注列表适配器
     */
    public class NoramlAdapter extends CommonAdapter<ElevenSortInfo> {

        public NoramlAdapter(Context context, int layoutId, List<ElevenSortInfo> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, final ElevenSortInfo info) {
            TextView sortname = holder.getView(R.id.sort_name);
            sortname.setText(info.getName());
            if (info.is_checked()) {
                sortname.setTextColor(Color.parseColor("#269ee6"));
            } else {
                sortname.setTextColor(Color.parseColor("#4a4a4a"));
            }


            LinearLayout itemLy = holder.getView(R.id.item_ly);
            itemLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < elevenNormalList.size(); i++) {
                        elevenNormalList.get(i).setIs_checked(false);
                    }
                    for (int i = 0; i < elevenSpaceList.size(); i++) {
                        elevenSpaceList.get(i).setIs_checked(false);
                    }
                    info.setIs_checked(true);
                    notifyDataSetChanged();
                    if (mPopSort != null) {
                        icon.setImageResource(R.drawable.icon_down);
                        mPopSort.dismiss();
                    }
                    clearNumber();
                    switch (info.getName().toString()) {
                        case "任选二":
                            mSelecte_Mode = N_OPTIONAL2;
                            break;
                        case "任选三":
                            mSelecte_Mode = N_OPTIONAL3;
                            break;
                        case "任选四":
                            mSelecte_Mode = N_OPTIONAL4;
                            break;
                        case "任选五":
                            mSelecte_Mode = N_OPTIONAL5;
                            break;
                        case "任选六":
                            mSelecte_Mode = N_OPTIONAL6;
                            break;
                        case "任选七":
                            mSelecte_Mode = N_OPTIONAL7;
                            break;
                        case "任选八":
                            mSelecte_Mode = N_OPTIONAL8;
                            break;
                        case "前一":
                            mSelecte_Mode = N_TOP_OPTIONAL;
                            break;
                        case "前二直选":
                            mSelecte_Mode = N_TOP_OPTIONAL2;
                            break;
                        case "前二组选":
                            mSelecte_Mode = N_TOP_G_OPTIONAL2;
                            break;
                        case "前三直选":
                            mSelecte_Mode = N_TOP_OPTIONAL3;
                            break;
                        case "前三组选":
                            mSelecte_Mode = N_TOP_G_OPTIONAL3;
                            break;
                        case "乐选三":
                            mSelecte_Mode = N_H_OPTIONAL3;
                            break;
                        case "乐选四":
                            mSelecte_Mode = N_H_OPTIONAL4;
                            break;
                        case "乐选五":
                            mSelecte_Mode = N_H_OPTIONAL5;
                            break;
                    }
                    mAcache.put("SELECT_MODE",mSelecte_Mode+"");
                    initRule();
                }
            });
        }
    }

    /**
     * 拖胆投注列表适配器
     */
    public class SpaceAdapter extends CommonAdapter<ElevenSortInfo> {

        public SpaceAdapter(Context context, int layoutId, List<ElevenSortInfo> datas) {
            super(context, layoutId, datas);
        }

        @Override
        public void convert(ViewHolder holder, final ElevenSortInfo info) {
            TextView sortname = holder.getView(R.id.sort_name);
            sortname.setText(info.getName());
            if (info.is_checked()) {
                sortname.setTextColor(Color.parseColor("#269ee6"));
            } else {
                sortname.setTextColor(Color.parseColor("#4a4a4a"));
            }
            LinearLayout itemLy = holder.getView(R.id.item_ly);
            itemLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < elevenNormalList.size(); i++) {
                        elevenNormalList.get(i).setIs_checked(false);
                    }
                    for (int i = 0; i < elevenSpaceList.size(); i++) {
                        elevenSpaceList.get(i).setIs_checked(false);
                    }
                    info.setIs_checked(true);
                    notifyDataSetChanged();
                    if (mPopSort != null) {
                        icon.setImageResource(R.drawable.icon_down);
                        mPopSort.dismiss();
                    }
                    clearNumber();
                    switch (info.getName().toString()) {
                        case "任选二":
                            mSelecte_Mode = S_OPTIONAL2;
                            break;
                        case "任选三":
                            mSelecte_Mode = S_OPTIONAL3;
                            break;
                        case "任选四":
                            mSelecte_Mode = S_OPTIONAL4;
                            break;
                        case "任选五":
                            mSelecte_Mode = S_OPTIONAL5;
                            break;
                        case "任选六":
                            mSelecte_Mode = S_OPTIONAL6;
                            break;
                        case "任选七":
                            mSelecte_Mode = S_OPTIONAL7;
                            break;
                        case "任选八":
                            mSelecte_Mode = S_OPTIONAL8;
                            break;
                        case "前二直选":
                            mSelecte_Mode = S_TOP_OPTIONAL2;
                            break;
                        case "前二组选":
                            mSelecte_Mode = S_TOP_G_OPTIONAL2;
                            break;
                        case "前三直选":
                            mSelecte_Mode = S_TOP_OPTIONAL3;
                            break;
                        case "前三组选":
                            mSelecte_Mode = S_TOP_G_OPTIONAL3;
                            break;
                    }
                    mAcache.put("SELECT_MODE",mSelecte_Mode+"");
                    initRule();
                }
            });
        }
    }

    public void showBackPop() {
        // 一个自定义的布局，作为显示的内容
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_close, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView comfirm = (TextView) view.findViewById(R.id.comfirm);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearNumber();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                finish();
            }
        });
        popupWindow.setTouchable(true);
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void showOverTimePop() {
        // 一个自定义的布局，作为显示的内容
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_overtime, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView comfirm = (TextView) view.findViewById(R.id.comfirm);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearNumber();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                finish();
            }
        });
        popupWindow.setTouchable(true);
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    /**
     * 排列组合算法
     */
    int allnum = 0;

    public void rank(int n, int m) {
        allnum=0;
        int min = n - m + 1;
        int all_n;
        int all_m;
        n_num = n;
        m_num = m;
        all_n = nrank(n, min);
        all_m = mrank(m);
        allnum = all_n / all_m;
        return;
    }

    int n_num = 1;

    public int nrank(int n, int min) {
        if (min <= n - 1) {
            n_num = n_num * (n - 1);
            nrank(n - 1, min);
        }
        return n_num;
    }

    int m_num = 1;

    public int mrank(int m) {
        if (m > 1) {
            m_num = m_num * (m - 1);
            mrank(m - 1);
        }
        return m_num;
    }


}
