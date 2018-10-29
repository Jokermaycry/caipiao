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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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
 * 快三
 * Created by yzx on 17/1/16.
 */

public class FastThreeActivity extends AppCompatActivity {

    //每注彩票价格
    private final int TicketPrice = 2;
    //普通投注
    private final int SUM_VALUE = 1;//和值
    private final int S_THREE = 2;//三同号单选
    private final int S_THREE_ALL = 9;//三同号通选
    private final int S_TWO = 8;//二同号复选
    private final int S_TWO_S = 3;//二同号单选
    private final int D_THREE = 4;//三不同号
    private final int D_TWO = 5;//二不同号
    private final int D_THREE_ALL = 10;//三连号通选
    //拖胆
    private final int SD_THREE = 6;//三不同号
    private final int SD_TWO = 7;//二不同号

    @BindView(R.id.s_diftwo_tag1)
    TagFlowLayout sDiftwoTag1;
    @BindView(R.id.s_diftwo_tag2)
    TagFlowLayout sDiftwoTag2;
    @BindView(R.id.s_dif_two_ly)
    LinearLayout sDifTwoLy;
    @BindView(R.id.same_two_s_ly)
    LinearLayout sameTwoSLy;
    @BindView(R.id.same_three_all_ly)
    LinearLayout sameThreeAllLy;
    @BindView(R.id.dif_three_all_ly)
    LinearLayout difThreeAllLy;
    private int mSelecte_Mode = SUM_VALUE;

    @BindView(R.id.samethree_tag)
    TagFlowLayout samethreeTag;
    @BindView(R.id.three_same_btn)
    TextView threeSameBtn;
    @BindView(R.id.same_three_ly)
    LinearLayout sameThreeLy;
    @BindView(R.id.sametow_s_tag)
    TagFlowLayout sametowSTag;
    @BindView(R.id.sametow_d_tag)
    TagFlowLayout sametowDTag;
    @BindView(R.id.sametow_spe_tag)
    TagFlowLayout sametowSpeTag;
    @BindView(R.id.same_two_ly)
    LinearLayout sameTwoLy;
    @BindView(R.id.difthree_tag)
    TagFlowLayout difthreeTag;
    @BindView(R.id.three_dif_btn)
    TextView threeDifBtn;
    @BindView(R.id.dif_three_ly)
    LinearLayout difThreeLy;
    @BindView(R.id.diftwo_tag)
    TagFlowLayout diftwoTag;
    @BindView(R.id.dif_two_ly)
    LinearLayout difTwoLy;
    @BindView(R.id.rule_tv)
    TextView ruleTv;
    @BindView(R.id.s_difthree_tag2)
    TagFlowLayout sDifthreeTag2;
    @BindView(R.id.s_difthree_tag1)
    TagFlowLayout sDifthreeTag1;
    @BindView(R.id.s_dif_three_ly)
    LinearLayout sDifThreeLy;
    @BindView(R.id.top_ly)
    LinearLayout topLy;

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
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.title_ly)
    LinearLayout titleLy;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.titlebar_ly)
    RelativeLayout titlebarLy;
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
    @BindView(R.id.random_btn)
    TextView randomBtn;
    @BindView(R.id.sumvalue_tag)
    TagFlowLayout sumvalueTag;
    @BindView(R.id.fast_big)
    TextView fastBig;
    @BindView(R.id.fast_small)
    TextView fastSmall;
    @BindView(R.id.fast_sig)
    TextView fastSig;
    @BindView(R.id.fast_dual)
    TextView fastDual;
    @BindView(R.id.sumvalue_ly)
    LinearLayout sumvalueLy;

    private TicketDetailInfo mInfo;
    private String sh_name;
    private boolean mContinue;

    private PopupWindow mPopSort;
    private Activity mActivity;
    //玩法弹窗适配器
    private NoramlAdapter normalAdapter;
    private SpaceAdapter spacelAdapter;
    private List<ElevenSortInfo> elevenNormalList = new ArrayList<>();
    private List<ElevenSortInfo> elevenSpaceList = new ArrayList<>();

    //和值快速选择判断
    private boolean sumBig;
    private boolean sumSmall;
    private boolean sumSig;
    private boolean sumDual;
    //和值号码和适配器
    private List<String> sumValueNums = new ArrayList<>();
    private TagAdapter<String> sumNumAdapter;
    //记录和值选中号码列表
    private List<NumInfo> sumList = new ArrayList<>();

    //三同号号码和适配器
    private List<String> sameThreeNums = new ArrayList<>();
    private TagAdapter<String> sameThreeAdapter;
    //记录三同号选中号码列表
    private List<NumInfo> sameThreeList = new ArrayList<>();
    private boolean sameThreeAll;

    //二同号号码和适配器
    //单选同号
    private List<String> sameTwo_s_Nums = new ArrayList<>();
    private TagAdapter<String> sameTwoSAdapter;
    //不同号
    private List<String> sameTwo_d_Nums = new ArrayList<>();
    private TagAdapter<String> sameTwoDAdapter;
    //复选
    private List<String> sameTwo_m_Nums = new ArrayList<>();
    private TagAdapter<String> sameTwoMAdapter;
    //记录二同号选中号码列表
    private List<NumInfo> sameTwo_S_List = new ArrayList<>();
    private List<NumInfo> sameTwo_D_List = new ArrayList<>();
    private List<NumInfo> sameTwo_M_List = new ArrayList<>();

    //三不同
    private List<String> difThreeNums = new ArrayList<>();
    private TagAdapter<String> difThreeAdapter;
    private List<NumInfo> difThreeList = new ArrayList<>();
    private boolean difThreeAll;
    //二不同
    private List<String> difTwoNums = new ArrayList<>();
    private TagAdapter<String> difTwoAdapter;
    private List<NumInfo> difTwoList = new ArrayList<>();

    //拖胆
    private List<String> SdifThreeNums = new ArrayList<>();
    private TagAdapter<String> SdifThreeAdapter;
    private List<String> SSdifThreeNums = new ArrayList<>();
    private TagAdapter<String> SSdifThreeAdapter;

    private List<String> SSdifTwoNums = new ArrayList<>();
    private TagAdapter<String> SSdifTwoAdapter;
    private List<String> SdifTwoNums = new ArrayList<>();
    private TagAdapter<String> SdifTwoAdapter;

    private List<NumInfo> sDifTwoList = new ArrayList<>();
    private List<NumInfo> sSDifTwoList = new ArrayList<>();
    private List<NumInfo> sSDifThreeList = new ArrayList<>();
    private List<NumInfo> sDifThreeList = new ArrayList<>();

    private ProgressDialog dialog;
    private ACache mAcache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fastthree);
        ButterKnife.bind(this);
        mActivity = this;
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("正在联网下载数据...");
        dialog.setMessage("请稍后...");
        dialog.show();
        sh_name = getIntent().getStringExtra("sh_name");
        mContinue = getIntent().getBooleanExtra("continue", false);
        mSelecte_Mode = SUM_VALUE;
        mAcache=ACache.get(this);
        initData();
        initRule();
        initSumValueView();
        initSameThree();
        initSameTwo();
        initDifThree();
        initDifTwo();
        initSDifThree();
        initSDifTwo();
        initSameThreeAll();
        initDifThreeAll();
        getDetail();

        initBefore();
    }
    private void initBefore() {
        String mode = mAcache.getAsString("FAST_SELECT_MODE");
        if (TextUtils.isEmpty(mode)) {
            return;
        }
        clearNum();
        mSelecte_Mode = Integer.valueOf(mode);
        initRule();
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

    private void initData() {
        //和值初始化
        for (int i = 3; i <= 18; i++) {
            sumValueNums.add(i + "");
        }
        for (int i = 3; i <= 18; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i + "");
            sumList.add(info);
        }
        //三同号初始化
        for (int i = 1; i < 7; i++) {
            sameThreeNums.add(i * 111 + "");
        }
        for (int i = 1; i < 7; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            sameThreeList.add(info);
        }
        //二同号
        for (int i = 1; i < 7; i++) {
            sameTwo_s_Nums.add(i * 11 + "");
            sameTwo_d_Nums.add(i + "");
            sameTwo_m_Nums.add(i * 11 + "*");
        }
        for (int i = 1; i < 7; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 11 + "");
            sameTwo_S_List.add(info);
            NumInfo info1 = new NumInfo();
            info1.setNum(i + "");
            sameTwo_D_List.add(info1);
            NumInfo info2 = new NumInfo();
            info2.setNum(i * 11 + "*");
            sameTwo_M_List.add(info2);
        }
        //三不同号
        for (int i = 1; i < 7; i++) {
            difThreeNums.add(i + "");
        }
        for (int i = 1; i < 7; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i + "");
            difThreeList.add(info);
        }

        //二不同号
        for (int i = 1; i < 7; i++) {
            difTwoNums.add(i + "");
        }
        for (int i = 1; i < 7; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i + "");
            difTwoList.add(info);
        }
        //拖胆
        for (int i = 1; i < 7; i++) {
            SdifThreeNums.add(i + "");
            SSdifThreeNums.add(i + "");
        }
        for (int i = 1; i < 7; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i + "");
            sDifThreeList.add(info);
            NumInfo info2 = new NumInfo();
            info2.setNum(i + "");
            sSDifThreeList.add(info2);
        }
        for (int i = 1; i < 7; i++) {
            SdifTwoNums.add(i + "");
            SSdifTwoNums.add(i + "");
        }
        for (int i = 1; i < 7; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i + "");
            sDifTwoList.add(info);
            NumInfo info2 = new NumInfo();
            info2.setNum(i + "");
            sSDifTwoList.add(info2);
        }


        //玩法初始化，普通投注
        ElevenSortInfo info = new ElevenSortInfo();
        info.setIs_checked(true);
        info.setName("和值");
        elevenNormalList.add(info);

        ElevenSortInfo info2 = new ElevenSortInfo();
        info2.setName("三同号");
        elevenNormalList.add(info2);
        ElevenSortInfo info9 = new ElevenSortInfo();
        info9.setName("三同号通选");
        elevenNormalList.add(info9);
        ElevenSortInfo info3 = new ElevenSortInfo();
        info3.setName("二同号-单选");
        elevenNormalList.add(info3);
        ElevenSortInfo info8 = new ElevenSortInfo();
        info8.setName("二同号-复选");
        elevenNormalList.add(info8);
        ElevenSortInfo info4 = new ElevenSortInfo();
        info4.setName("三不同号");
        elevenNormalList.add(info4);
        ElevenSortInfo info10 = new ElevenSortInfo();
        info10.setName("三连号通选");
        elevenNormalList.add(info10);
        ElevenSortInfo info5 = new ElevenSortInfo();
        info5.setName("二不同号");
        elevenNormalList.add(info5);

        //拖胆投注
        ElevenSortInfo info6 = new ElevenSortInfo();
        info6.setName("三不同号");
        elevenSpaceList.add(info6);
        ElevenSortInfo info7 = new ElevenSortInfo();
        info7.setName("二不同号");
        elevenSpaceList.add(info7);

        //玩法选项
        normalAdapter = new NoramlAdapter(mActivity, R.layout.grid_item_tv, elevenNormalList);
        spacelAdapter = new SpaceAdapter(mActivity, R.layout.grid_item_tv, elevenSpaceList);
    }

    /**
     * 初始化规则,
     */
    private void initRule() {
        switch (mSelecte_Mode) {
            case SUM_VALUE:
                title.setText("和值");
                ruleTv.setText("猜开奖号码相加的和");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sDifTwoLy.setVisibility(View.GONE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case S_THREE:
                title.setText("三同号");
                ruleTv.setText("猜豹子号（3个号相同）");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.GONE);
                sameThreeLy.setVisibility(View.VISIBLE);
                sameTwoLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameTwoSLy.setVisibility(View.GONE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case S_TWO:
                title.setText("二同号-单选");
                ruleTv.setText("猜对子号（2个号相同）");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.GONE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.VISIBLE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case S_TWO_S:
                title.setText("二同号-复选");
                ruleTv.setText("猜对子号（2个号相同）");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.GONE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.VISIBLE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case D_THREE:
                title.setText("三不同号");
                ruleTv.setText("选出三个不同号码");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.GONE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.VISIBLE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case D_TWO:
                title.setText("二不同号");
                ruleTv.setText("选出两个不同号码");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.GONE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.VISIBLE);
                sDifThreeLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case SD_THREE:
                title.setText("拖胆-三不同号");
                ruleTv.setVisibility(View.GONE);
                sumvalueLy.setVisibility(View.GONE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.VISIBLE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.GONE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case SD_TWO:
                title.setText("拖胆-二不同号");
                ruleTv.setVisibility(View.GONE);
                sumvalueLy.setVisibility(View.GONE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case S_THREE_ALL:
                title.setText("三同号通选");
                ruleTv.setVisibility(View.GONE);
                sumvalueLy.setVisibility(View.GONE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.GONE);
                sameThreeAllLy.setVisibility(View.VISIBLE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case D_THREE_ALL:
                title.setText("三连号通选");
                ruleTv.setVisibility(View.GONE);
                sumvalueLy.setVisibility(View.GONE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.GONE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 和值视图初始化
     */
    private void initSumValueView() {
        for (int i = 0; i < sumList.size(); i++) {
            sumList.get(i).setIs_checked(false);
        }
        sumNumAdapter = new TagAdapter<String>(sumValueNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv, sumvalueTag, false);
                tv.setText(s);
                return tv;
            }
        };
        sumvalueTag.setAdapter(sumNumAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (sumList.get(position).is_checked()) {
                    sumList.get(position).setIs_checked(false);
                } else {
                    sumList.get(position).setIs_checked(true);
                }
                sumValueConsult();
                return false;
            }
        });
    }

    /**
     * 三同号初始化
     */
    private void initSameThree() {
        sameThreeAll = false;
        threeSameBtn.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
        threeSameBtn.setTextColor(Color.parseColor("#269ee6"));

        for (int i = 0; i < sameThreeList.size(); i++) {
            sameThreeList.get(i).setIs_checked(false);
        }
        sameThreeAdapter = new TagAdapter<String>(sameThreeNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv3, samethreeTag, false);
                tv.setText(s);
                return tv;
            }
        };
        samethreeTag.setAdapter(sameThreeAdapter);
        samethreeTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (sameThreeList.get(position).is_checked()) {
                    sameThreeList.get(position).setIs_checked(false);
                } else {
                    sameThreeList.get(position).setIs_checked(true);
                }
                sameThreeConsult();
                return false;
            }
        });
    }

    /**
     * 二同号初始化
     */
    private void initSameTwo() {
        for (int i = 0; i < sameTwo_S_List.size(); i++) {
            sameTwo_S_List.get(i).setIs_checked(false);
        }
        for (int i = 0; i < sameTwo_D_List.size(); i++) {
            sameTwo_D_List.get(i).setIs_checked(false);
        }

        //单选 同号
        sameTwoSAdapter = new TagAdapter<String>(sameTwo_s_Nums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv2, sametowSTag, false);
                tv.setText(s);
                return tv;
            }
        };
        sametowSTag.setAdapter(sameTwoSAdapter);
        sametowSTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (sameTwo_D_List.get(position).is_checked()) {
                    ToastUtil.toast(mActivity, "单选不能重复");
                    int num = 0;
                    for (int i = 0; i < sameTwo_S_List.size(); i++) {
                        if (sameTwo_S_List.get(i).is_checked()) {
                            sameTwoSAdapter.setSelectedList(i);
                            num++;
                        }
                    }
                    if (num == 0) {
                        sameTwoSAdapter.notifyDataChanged();
                    }
                    return false;
                }

                if (sameTwo_S_List.get(position).is_checked()) {
                    sameTwo_S_List.get(position).setIs_checked(false);
                } else {
                    sameTwo_S_List.get(position).setIs_checked(true);
                }
                sameTwoConsult();
                return false;
            }
        });
        //单选 不同号
        sameTwoDAdapter = new TagAdapter<String>(sameTwo_d_Nums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv2, sametowDTag, false);
                tv.setText(s);
                return tv;
            }
        };
        sametowDTag.setAdapter(sameTwoDAdapter);
        sametowDTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (sameTwo_S_List.get(position).is_checked()) {
                    ToastUtil.toast(mActivity, "单选不能重复");
                    int num = 0;
                    for (int i = 0; i < sameTwo_D_List.size(); i++) {
                        if (sameTwo_D_List.get(i).is_checked()) {
                            sameTwoDAdapter.setSelectedList(i);
                            num++;
                        }
                    }
                    if (num == 0) {
                        sameTwoDAdapter.notifyDataChanged();
                    }
                    return false;
                }
                if (sameTwo_D_List.get(position).is_checked()) {
                    sameTwo_D_List.get(position).setIs_checked(false);
                } else {
                    sameTwo_D_List.get(position).setIs_checked(true);
                }
                sameTwoConsult();
                return false;
            }
        });
        for (int i = 0; i < sameTwo_M_List.size(); i++) {
            sameTwo_M_List.get(i).setIs_checked(false);
        }
        //复选
        sameTwoMAdapter = new TagAdapter<String>(sameTwo_m_Nums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv2, sametowSpeTag, false);
                tv.setText(s);
                return tv;
            }
        };
        sametowSpeTag.setAdapter(sameTwoMAdapter);
        sametowSpeTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (sameTwo_M_List.get(position).is_checked()) {
                    sameTwo_M_List.get(position).setIs_checked(false);
                } else {
                    sameTwo_M_List.get(position).setIs_checked(true);
                }
                sameTwoMConsult();
                return false;
            }
        });
    }

    /**
     * 三不同号初始化
     */
    private void initDifThree() {
        difThreeAll = false;
        threeDifBtn.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
        threeDifBtn.setTextColor(Color.parseColor("#269ee6"));

        for (int i = 0; i < difThreeList.size(); i++) {
            difThreeList.get(i).setIs_checked(false);
        }
        difThreeAdapter = new TagAdapter<String>(difThreeNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv2, difthreeTag, false);
                tv.setText(s);
                return tv;
            }
        };
        difthreeTag.setAdapter(difThreeAdapter);
        difthreeTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (difThreeList.get(position).is_checked()) {
                    difThreeList.get(position).setIs_checked(false);
                } else {
                    difThreeList.get(position).setIs_checked(true);
                }

                if (difthreeTag.getSelectedList().size() == 3) {
                    if (difThreeAll) {
                        num.setText("2");
                        price.setText("4");
                    } else {
                        num.setText("1");
                        price.setText("2");
                    }
                } else if (difthreeTag.getSelectedList().size() > 3) {
                    allnum = 0;
                    rank(difthreeTag.getSelectedList().size(), 3);
                    if (difThreeAll) {
                        allnum++;
                    }
                    num.setText(allnum + "");
                    price.setText(allnum * TicketPrice + "");
                } else if (difthreeTag.getSelectedList().size() < 3) {
                    if (difThreeAll) {
                        num.setText("1");
                        price.setText("2");
                    } else {
                        num.setText("0");
                        price.setText("0");
                    }
                }

                return false;
            }
        });
    }

    /**
     * 二不同号
     */
    private void initDifTwo() {
        for (int i = 0; i < difTwoList.size(); i++) {
            difTwoList.get(i).setIs_checked(false);
        }
        difTwoAdapter = new TagAdapter<String>(difTwoNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv2, diftwoTag, false);
                tv.setText(s);
                return tv;
            }
        };
        diftwoTag.setAdapter(difTwoAdapter);
        diftwoTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (difTwoList.get(position).is_checked()) {
                    difTwoList.get(position).setIs_checked(false);
                } else {
                    difTwoList.get(position).setIs_checked(true);
                }
                if (diftwoTag.getSelectedList().size() == 2) {
                    num.setText("1");
                    price.setText("2");
                } else if (diftwoTag.getSelectedList().size() > 2) {
                    allnum = 0;
                    rank(diftwoTag.getSelectedList().size(), 2);
                    num.setText(allnum + "");
                    price.setText(allnum * TicketPrice + "");
                } else if (diftwoTag.getSelectedList().size() < 2) {
                    num.setText("0");
                    price.setText("0");
                }

                return false;
            }
        });
    }

    /**
     * 拖胆三不同号初始化
     */
    private void initSDifThree() {
        for (int i = 0; i < sDifThreeList.size(); i++) {
            sDifThreeList.get(i).setIs_checked(false);
        }
        for (int i = 0; i < sDifThreeList.size(); i++) {
            sSDifThreeList.get(i).setIs_checked(false);
        }

        //胆码
        SdifThreeAdapter = new TagAdapter<String>(SdifThreeNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv2, sDifthreeTag1, false);
                tv.setText(s);
                return tv;
            }
        };
        sDifthreeTag1.setAdapter(SdifThreeAdapter);
        sDifthreeTag1.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (sDifthreeTag1.getSelectedList().size() > 2) {
                    ToastUtil.toast(mActivity, "胆码至多选择一个");
                    int num = 0;
                    for (int i = 0; i < sDifThreeList.size(); i++) {
                        if (sDifThreeList.get(i).is_checked()) {
                            SdifThreeAdapter.setSelectedList(i);
                            num++;
                        }
                    }
                    if (num == 0) {
                        SdifThreeAdapter.notifyDataChanged();
                    }
                    return false;
                }
                if (sSDifThreeList.get(position).is_checked()) {
                    ToastUtil.toast(mActivity, "不能重复");
                    int num = 0;
                    for (int i = 0; i < sDifThreeList.size(); i++) {
                        if (sDifThreeList.get(i).is_checked()) {
                            SdifThreeAdapter.setSelectedList(i);
                            num++;
                        }
                    }
                    if (num == 0) {
                        SdifThreeAdapter.notifyDataChanged();
                    }
                    return false;
                }

                if (sDifThreeList.get(position).is_checked()) {
                    sDifThreeList.get(position).setIs_checked(false);
                } else {
                    sDifThreeList.get(position).setIs_checked(true);
                }
                sDifThreeConsult();
                return false;
            }
        });
        //拖胆
        SSdifThreeAdapter = new TagAdapter<String>(SSdifThreeNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv2, sDifthreeTag2, false);
                tv.setText(s);
                return tv;
            }
        };
        sDifthreeTag2.setAdapter(SSdifThreeAdapter);
        sDifthreeTag2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (sDifThreeList.get(position).is_checked()) {
                    ToastUtil.toast(mActivity, "单选不能重复");
                    int num = 0;
                    for (int i = 0; i < sSDifThreeList.size(); i++) {
                        if (sSDifThreeList.get(i).is_checked()) {
                            SSdifThreeAdapter.setSelectedList(i);
                            num++;
                        }
                    }
                    if (num == 0) {
                        SSdifThreeAdapter.notifyDataChanged();
                    }
                    return false;
                }

                if (sSDifThreeList.get(position).is_checked()) {
                    sSDifThreeList.get(position).setIs_checked(false);
                } else {
                    sSDifThreeList.get(position).setIs_checked(true);
                }
                sDifThreeConsult();
                return false;
            }
        });
    }

    private void initSDifTwo() {
        for (int i = 0; i < sDifTwoList.size(); i++) {
            sDifTwoList.get(i).setIs_checked(false);
        }
        for (int i = 0; i < sDifTwoList.size(); i++) {
            sSDifTwoList.get(i).setIs_checked(false);
        }

        //胆码
        SdifTwoAdapter = new TagAdapter<String>(SdifTwoNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv2, sDiftwoTag1, false);
                tv.setText(s);
                return tv;
            }
        };
        sDiftwoTag1.setAdapter(SdifTwoAdapter);
        sDiftwoTag1.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                int number = 0;
                for (int i = 0; i < sDifTwoList.size(); i++) {
                    if (sDifTwoList.get(i).is_checked()) {
                        number++;
                    }
                }
                if (number > 0) {
                    ToastUtil.toast(mActivity, "胆码只能选择一个");
                    int num = 0;
                    for (int i = 0; i < sDifTwoList.size(); i++) {
                        if (sDifTwoList.get(i).is_checked()) {
                            SdifTwoAdapter.setSelectedList(i);
                        }
                    }
                    if (num == 0) {
                        SdifTwoAdapter.notifyDataChanged();
                    }
                    return false;
                }
                if (sSDifTwoList.get(position).is_checked()) {
                    ToastUtil.toast(mActivity, "不能重复");
                    int num = 0;
                    for (int i = 0; i < sDifTwoList.size(); i++) {
                        if (sDifTwoList.get(i).is_checked()) {
                            SdifTwoAdapter.setSelectedList(i);
                            num++;
                        }
                    }
                    if (num == 0) {
                        SdifTwoAdapter.notifyDataChanged();
                    }
                    return false;
                }

                if (sDifTwoList.get(position).is_checked()) {
                    sDifTwoList.get(position).setIs_checked(false);
                } else {
                    sDifTwoList.get(position).setIs_checked(true);
                }
                sDifTwoConsult();
                return false;
            }
        });
        //拖胆
        SSdifTwoAdapter = new TagAdapter<String>(SSdifTwoNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_fast_tv2, sDiftwoTag2, false);
                tv.setText(s);
                return tv;
            }
        };
        sDiftwoTag2.setAdapter(SSdifTwoAdapter);
        sDiftwoTag2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (sDifTwoList.get(position).is_checked()) {
                    ToastUtil.toast(mActivity, "单选不能重复");
                    int num = 0;
                    for (int i = 0; i < sSDifTwoList.size(); i++) {
                        if (sSDifTwoList.get(i).is_checked()) {
                            SSdifTwoAdapter.setSelectedList(i);
                            num++;
                        }
                    }
                    if (num == 0) {
                        SSdifTwoAdapter.notifyDataChanged();
                    }
                    return false;
                }

                if (sSDifTwoList.get(position).is_checked()) {
                    sSDifTwoList.get(position).setIs_checked(false);
                } else {
                    sSDifTwoList.get(position).setIs_checked(true);
                }
                sDifTwoConsult();
                return false;
            }
        });
    }

    public void initSameThreeAll(){
        sameThreeAll = false;
        threeSameBtn.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
        threeSameBtn.setTextColor(Color.parseColor("#269ee6"));
    }

    public void initDifThreeAll(){
        difThreeAll=false;
        threeDifBtn.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
        threeDifBtn.setTextColor(Color.parseColor("#269ee6"));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(1);
        super.onDestroy();
    }

    @OnClick({R.id.comfirm_btn, R.id.clear_btn, R.id.back, R.id.title_ly, R.id.more, R.id.random_btn, R.id.fast_big, R.id.fast_small, R.id.fast_sig, R.id.fast_dual, R.id.three_same_btn, R.id.three_dif_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comfirm_btn:
                numbersComfirm();
                break;
            case R.id.clear_btn:
                clearNum();
                break;
            case R.id.back:
                showBackPop();
                break;
            case R.id.title_ly:
                icon.setImageResource(R.drawable.icon_up);
                sort_pop();
                break;
            case R.id.more:
                Intent in = new Intent(mActivity, PlayIntroductionActivity.class);
                startActivity(in);
                break;
            //机选
            case R.id.random_btn:
                switch (mSelecte_Mode) {
                    case SUM_VALUE:
                        initSumValueView();
                        break;
                    case S_THREE:
                        initSameThree();
                        break;
                    case S_TWO:
                        initSameTwo();
                        break;
                    case S_TWO_S:
                        initSameTwo();
                        break;
                    case D_THREE:
                        initDifThree();
                        break;
                    case D_TWO:
                        initDifTwo();
                        break;
                    case SD_THREE:
                        initSDifThree();
                        break;
                    case SD_TWO:
                        initSDifTwo();
                        break;

                }
                getRandow();
                break;
            case R.id.fast_big:
                if (sumBig) {
                    sumBig = false;
                    fastBig.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    fastBig.setTextColor(Color.parseColor("#269ee6"));
                } else {
                    sumBig = true;
                    sumSmall = false;
                    fastBig.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
                    fastSmall.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    fastBig.setTextColor(Color.parseColor("#ffffff"));
                    fastSmall.setTextColor(Color.parseColor("#269ee6"));
                }
                fastSeleteSumValue();

                break;
            case R.id.fast_small:
                if (sumSmall) {
                    sumSmall = false;
                    fastSmall.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    fastSmall.setTextColor(Color.parseColor("#269ee6"));
                } else {
                    sumSmall = true;
                    sumBig = false;
                    fastBig.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    fastSmall.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
                    fastBig.setTextColor(Color.parseColor("#269ee6"));
                    fastSmall.setTextColor(Color.parseColor("#ffffff"));
                }
                fastSeleteSumValue();

                break;
            case R.id.fast_sig:
                if (sumSig) {
                    sumSig = false;
                    fastSig.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    fastSig.setTextColor(Color.parseColor("#269ee6"));
                } else {
                    sumSig = true;
                    sumDual = false;
                    fastSig.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
                    fastDual.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    fastSig.setTextColor(Color.parseColor("#ffffff"));
                    fastDual.setTextColor(Color.parseColor("#269ee6"));
                }
                fastSeleteSumValue();
                break;
            case R.id.fast_dual:
                if (sumDual) {
                    sumDual = false;
                    fastDual.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    fastDual.setTextColor(Color.parseColor("#269ee6"));
                } else {
                    sumSig = false;
                    sumDual = true;
                    fastSig.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    fastDual.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
                    fastSig.setTextColor(Color.parseColor("#269ee6"));
                    fastDual.setTextColor(Color.parseColor("#ffffff"));
                }
                fastSeleteSumValue();
                break;
            //三同号通选
            case R.id.three_same_btn:
                if (sameThreeAll) {
                    sameThreeAll = false;
                    threeSameBtn.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    threeSameBtn.setTextColor(Color.parseColor("#269ee6"));
                    num.setText("0");
                    price.setText("0");
                } else {
                    sameThreeAll = true;
                    threeSameBtn.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
                    threeSameBtn.setTextColor(Color.parseColor("#ffffff"));
                    num.setText("1");
                    price.setText(1 * TicketPrice + "");
                }
                break;
            //三连号
            case R.id.three_dif_btn:
                if (difThreeAll) {
                    difThreeAll = false;
                    threeDifBtn.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
                    threeDifBtn.setTextColor(Color.parseColor("#269ee6"));
                    num.setText("0");
                    price.setText("0");
                } else {
                    difThreeAll = true;
                    threeDifBtn.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
                    threeDifBtn.setTextColor(Color.parseColor("#ffffff"));
                    num.setText("1");
                    price.setText(1 * TicketPrice + "");
                }

                break;
        }
    }

    /**
     * 选中号码
     */
    private void numbersComfirm() {
        if (num.getText().toString().equals("0")) {
            ToastUtil.toast(mActivity, "至少购买一注");
            return;
        }
        if (mContinue) {
            Log.e("mContinue", "true");
            Intent in = new Intent(FastThreeActivity.this, FastThreeDetailActivity.class);
            //和值
            in.putExtra("sumList", (Serializable) sumList);
            //三同号
            in.putExtra("sameThreeList", (Serializable) sameThreeList);
            //二同号单选
            in.putExtra("sameTwo_D_List", (Serializable) sameTwo_D_List);
            in.putExtra("sameTwo_S_List", (Serializable) sameTwo_S_List);
            //二同号复选
            in.putExtra("sameTwo_M_List", (Serializable) sameTwo_M_List);
            //三不同号
            in.putExtra("difThreeList", (Serializable) difThreeList);
            //二不同号
            in.putExtra("difTwoList", (Serializable) difTwoList);
            //拖胆——三不同号
            in.putExtra("sDifThreeList", (Serializable) sDifThreeList);
            in.putExtra("sSDifThreeList", (Serializable) sSDifThreeList);
            //拖胆——二不同号
            in.putExtra("sDifTwoList", (Serializable) sDifTwoList);
            in.putExtra("sSDifTwoList", (Serializable) sSDifTwoList);

            //三同号通选
            in.putExtra("sameThreeAll", sameThreeAll);
            //三连号通选
            in.putExtra("difThreeAll",difThreeAll);

            in.putExtra("selecte_mode", mSelecte_Mode);
            in.putExtra("num", num.getText().toString());
            in.putExtra("price", price.getText().toString());
            in.putExtra("title", mInfo.getName());
            //Log.e("shname",mInfo.getName());
            in.putExtra("sh_name", mInfo.getSh_name());
            in.putExtra("type", mInfo.getType());
            in.putExtra("now_qs", mInfo.getNow_qs());
            setResult(RESULT_OK, in);
            finish();
            clearNum();
        } else {
            Log.e("mContinue", "false");
            Intent in = new Intent(FastThreeActivity.this, FastThreeDetailActivity.class);
            //和值
            in.putExtra("sumList", (Serializable) sumList);
            //三同号
            in.putExtra("sameThreeList", (Serializable) sameThreeList);
            //二同号单选
            in.putExtra("sameTwo_D_List", (Serializable) sameTwo_D_List);
            in.putExtra("sameTwo_S_List", (Serializable) sameTwo_S_List);
            //二同号复选
            in.putExtra("sameTwo_M_List", (Serializable) sameTwo_M_List);
            //三不同号
            in.putExtra("difThreeList", (Serializable) difThreeList);
            //二不同号
            in.putExtra("difTwoList", (Serializable) difTwoList);
            //拖胆——三不同号
            in.putExtra("sDifThreeList", (Serializable) sDifThreeList);
            in.putExtra("sSDifThreeList", (Serializable) sSDifThreeList);
            //拖胆——二不同号
            in.putExtra("sDifTwoList", (Serializable) sDifTwoList);
            in.putExtra("sSDifTwoList", (Serializable) sSDifTwoList);

            //三同号通选
            in.putExtra("sameThreeAll", sameThreeAll);
            //三连号通选
            in.putExtra("difThreeAll",difThreeAll);

            in.putExtra("selecte_mode", mSelecte_Mode);
            in.putExtra("num", num.getText().toString());
            in.putExtra("price", price.getText().toString());
            in.putExtra("title", mInfo.getName());
            in.putExtra("sh_name", mInfo.getSh_name());
            in.putExtra("type", mInfo.getType());
            in.putExtra("now_qs", mInfo.getNow_qs());
            startActivity(in);
            clearNum();
            finish();
        }


    }

    /*
       和值快速选择
     */
    private void fastSeleteSumValue() {
        initSumValueView();
        Log.e("sum", sumBig + "," + sumSmall + "," + sumSig + "," + sumDual);
        if (sumBig && !sumSig && !sumDual) {
            for (int i = sumList.size() / 2; i < sumList.size(); i++) {
                sumList.get(i).setIs_checked(true);
                sumNumAdapter.setSelectedList(i);
            }
        } else if (sumSmall && !sumSig && !sumDual) {
            for (int i = 0; i < sumList.size() / 2; i++) {
                sumList.get(i).setIs_checked(true);
                sumNumAdapter.setSelectedList(i);
            }
        } else if (sumSig && !sumBig && !sumSmall) {
            for (int i = 0; i < sumList.size(); i++) {
                if (i % 2 == 0) {
                    sumList.get(i).setIs_checked(true);
                    sumNumAdapter.setSelectedList(i);
                }
            }
        } else if (sumDual && !sumBig && !sumSmall) {
            for (int i = 0; i < sumList.size(); i++) {
                if (i % 2 == 1) {
                    sumList.get(i).setIs_checked(true);
                    sumNumAdapter.setSelectedList(i);
                }
            }
        } else if (sumBig && (sumSig || sumDual)) {
            if (sumSig) {
                for (int i = sumList.size() / 2; i < sumList.size(); i++) {
                    if (i % 2 == 0) {
                        sumList.get(i).setIs_checked(true);
                        sumNumAdapter.setSelectedList(i);
                    }
                }
            } else if (sumDual) {
                for (int i = sumList.size() / 2; i < sumList.size(); i++) {
                    if (i % 2 == 1) {
                        sumList.get(i).setIs_checked(true);
                        sumNumAdapter.setSelectedList(i);
                    }
                }
            }
        } else if (sumSmall && (sumSig || sumDual)) {
            if (sumSig) {
                for (int i = 0; i < sumList.size() / 2; i++) {
                    if (i % 2 == 0) {
                        sumList.get(i).setIs_checked(true);
                        sumNumAdapter.setSelectedList(i);
                    }
                }
            } else if (sumDual) {
                for (int i = 0; i < sumList.size() / 2; i++) {
                    if (i % 2 == 1) {
                        sumList.get(i).setIs_checked(true);
                        sumNumAdapter.setSelectedList(i);
                    }
                }
            }
        } else if (sumSig && (sumBig || sumSmall)) {
            if (sumBig) {
                for (int i = sumList.size() / 2; i < sumList.size(); i++) {
                    if (i % 2 == 0) {
                        sumList.get(i).setIs_checked(true);
                        sumNumAdapter.setSelectedList(i);
                    }
                }
            } else if (sumSmall) {
                for (int i = 0; i < sumList.size() / 2; i++) {
                    if (i % 2 == 0) {
                        sumList.get(i).setIs_checked(true);
                        sumNumAdapter.setSelectedList(i);
                    }
                }
            }
        } else if (sumDual && (sumBig || sumSmall)) {
            if (sumBig) {
                for (int i = sumList.size() / 2; i < sumList.size(); i++) {
                    if (i % 2 == 1) {
                        sumList.get(i).setIs_checked(true);
                        sumNumAdapter.setSelectedList(i);
                    }
                }
            } else if (sumSmall) {
                for (int i = 0; i < sumList.size() / 2; i++) {
                    if (i % 2 == 1) {
                        sumList.get(i).setIs_checked(true);
                        sumNumAdapter.setSelectedList(i);
                    }
                }
            }
        }
        sumValueConsult();
    }

    /**
     * 计算和值购买数量和金额
     */
    public void sumValueConsult() {
        //计算选中的注数和金额
        int buy_acount = 0;
        for (int i = 0; i < sumList.size(); i++) {
            if (sumList.get(i).is_checked()) {
                buy_acount++;
                Log.e("sumValueSelecte", sumList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");

    }

    /**
     * 计算三同号购买数量和金额
     */
    public void sameThreeConsult() {
        //计算选中的注数和金额
        int but_acount = 0;
        for (int i = 0; i < sameThreeList.size(); i++) {
            if (sameThreeList.get(i).is_checked()) {
                but_acount++;
                Log.e("sumValueSelecte", sameThreeList.get(i).getNum());
            }
        }

        num.setText(but_acount + "");
        price.setText(but_acount * TicketPrice + "");
    }

    /**
     * 计算二同号购买数量和金额
     */
    public void sameTwoConsult() {
        //计算选中的注数和金额
        int same_acount = 0;
        int dif_acount = 0;
        for (int i = 0; i < sameTwo_S_List.size(); i++) {
            if (sameTwo_S_List.get(i).is_checked()) {
                same_acount++;
                Log.e("sameTwo_s_Selecte", sameTwo_S_List.get(i).getNum());
            }
        }
        for (int i = 0; i < sameTwo_D_List.size(); i++) {
            if (sameTwo_D_List.get(i).is_checked()) {
                dif_acount++;
                Log.e("sameTwo_d_Selecte", sameTwo_D_List.get(i).getNum());
            }
        }

        num.setText(same_acount * dif_acount + "");
        price.setText(same_acount * dif_acount * TicketPrice + "");

    }

    public void sameTwoMConsult() {
        //计算选中的注数和金额
        int muti_acount = 0;
        for (int i = 0; i < sameTwo_M_List.size(); i++) {
            if (sameTwo_M_List.get(i).is_checked()) {
                muti_acount++;
                Log.e("sameTwo_m_Selecte", sameTwo_M_List.get(i).getNum());
            }
        }


        num.setText(muti_acount + "");
        price.setText(muti_acount * TicketPrice + "");

    }

    /**
     * 计算三不同号购买数量和金额
     */
    public void difThreeConsult() {
        //计算选中的注数和金额
        int but_acount = 0;
        for (int i = 0; i < difThreeList.size(); i++) {
            if (difThreeList.get(i).is_checked()) {
                but_acount++;
                Log.e("sumValueSelecte", difThreeList.get(i).getNum());
            }
        }
        if (sameThreeAll) {
            but_acount++;
        }
        num.setText(but_acount + "");
        price.setText(but_acount * TicketPrice + "");
    }

    /**
     * 计算拖胆三不同号购买数量和金额
     */
    public void sDifThreeConsult() {
        //计算选中的注数和金额
        int same_acount = 0;
        int dif_acount = 0;
        for (int i = 0; i < sDifThreeList.size(); i++) {
            if (sDifThreeList.get(i).is_checked()) {
                same_acount++;
                Log.e("sameTwo_s_Selecte", sDifThreeList.get(i).getNum());
            }
        }
        for (int i = 0; i < sSDifThreeList.size(); i++) {
            if (sSDifThreeList.get(i).is_checked()) {
                dif_acount++;
                Log.e("sameTwo_d_Selecte", sSDifThreeList.get(i).getNum());
            }
        }
        if (same_acount + dif_acount < 3) {
            num.setText(0 + "");
            price.setText(0 * TicketPrice + "");
        } else {
            if (same_acount == 2) {
                num.setText(1 * dif_acount + "");
                price.setText(1 * dif_acount * TicketPrice + "");
            } else if (same_acount == 1 && dif_acount == 2) {
                num.setText(1 + "");
                price.setText(1 * TicketPrice + "");
            } else {
                if (dif_acount == 4) {
                    num.setText(6 + "");
                    price.setText(6 * TicketPrice + "");
                } else if (dif_acount == 5) {
                    num.setText(10 + "");
                    price.setText(10 * TicketPrice + "");
                } else {
                    num.setText(same_acount * dif_acount + "");
                    price.setText(same_acount * dif_acount * TicketPrice + "");
                }
            }

        }
    }

    /**
     * 计算拖胆二不同号购买数量和金额
     */
    public void sDifTwoConsult() {
        //计算选中的注数和金额
        int same_acount = 0;
        int dif_acount = 0;
        for (int i = 0; i < sDifTwoList.size(); i++) {
            if (sDifTwoList.get(i).is_checked()) {
                same_acount++;
                Log.e("sameTwo_s_Selecte", sDifTwoList.get(i).getNum());
            }
        }
        for (int i = 0; i < sSDifTwoList.size(); i++) {
            if (sSDifTwoList.get(i).is_checked()) {
                dif_acount++;
                Log.e("sameTwo_d_Selecte", sSDifTwoList.get(i).getNum());
            }
        }


        num.setText(same_acount * dif_acount + "");
        price.setText(same_acount * dif_acount * TicketPrice + "");

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
                    switch (info.getName().toString()) {
                        case "和值":
                            clearNum();
                            mSelecte_Mode = SUM_VALUE;
                            break;
                        case "三同号":
                            clearNum();
                            mSelecte_Mode = S_THREE;
                            break;
                        case "三同号通选":
                            clearNum();
                            mSelecte_Mode = S_THREE_ALL;
                            break;
                        case "二同号-单选":
                            clearNum();
                            mSelecte_Mode = S_TWO;
                            break;
                        case "二同号-复选":
                            clearNum();
                            mSelecte_Mode = S_TWO_S;
                            break;
                        case "三不同号":
                            clearNum();
                            mSelecte_Mode = D_THREE;
                            break;
                        case "三连号通选":
                            clearNum();
                            mSelecte_Mode = D_THREE_ALL;
                            break;
                        case "二不同号":
                            clearNum();
                            mSelecte_Mode = D_TWO;
                            break;
                    }
                    mAcache.put("FAST_SELECT_MODE",mSelecte_Mode+"");
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
                    switch (info.getName().toString()) {
                        case "三不同号":
                            clearNum();
                            mSelecte_Mode = SD_THREE;
                            break;
                        case "二不同号":
                            clearNum();
                            mSelecte_Mode = SD_TWO;
                            break;
                    }
                    mAcache.put("FAST_SELECT_MODE",mSelecte_Mode+"");
                    initRule();
                }
            });
        }
    }

    /**
     * 清空和值号码
     */
    private void clearNum() {
        switch (mSelecte_Mode) {
            case SUM_VALUE:
                initSumValueView();
                break;
            case S_THREE:
                initSameThree();
                break;
            case S_TWO_S:
            case S_TWO:
                initSameTwo();
                break;
            case D_THREE:
                initDifThree();
                break;
            case D_TWO:
                initDifTwo();
                break;
            case SD_THREE:
                initSDifThree();
                break;
            case SD_TWO:
                initSDifTwo();
                break;
            case S_THREE_ALL:
                initSameThreeAll();
                break;
            case D_THREE_ALL:
                initDifThreeAll();
                break;
        }
        num.setText("0");
        price.setText("0");
    }


    /**
     * 获取随机数
     */
    private void getRandow() {
        int[] result;
        int[] result2;
        num.setText("1");
        price.setText(TicketPrice + "");
        switch (mSelecte_Mode) {
            case SUM_VALUE:
                result = numberRandom(1, sumValueNums);
                for (int i = 0; i < result.length; i++) {
                    sumList.get(result[i]).setIs_checked(true);
                    sumNumAdapter.setSelectedList(result[i]);
                }
                break;
            case S_THREE:
                result = numberRandom(1, sameThreeNums);
                for (int i = 0; i < result.length; i++) {
                    sameThreeList.get(result[i]).setIs_checked(true);
                    sameThreeAdapter.setSelectedList(result[i]);
                }
                break;
            case S_TWO:
                result = numberRandom(1, sameTwo_s_Nums);
                while (true) {
                    result2 = numberRandom(1, sameTwo_d_Nums);
                    if (result2[0] * 11 != result[0]) {
                        break;
                    }
                }
                sameTwo_S_List.get(result[0]).setIs_checked(true);
                sameTwoSAdapter.setSelectedList(result[0]);
                sameTwo_D_List.get(result2[0]).setIs_checked(true);
                sameTwoDAdapter.setSelectedList(result2[0]);
                break;
            case S_TWO_S:
                result = numberRandom(1, sameTwo_m_Nums);
                while (true) {
                    result2 = numberRandom(1, sameTwo_m_Nums);
                    if (result2[0] * 11 != result[0]) {
                        break;
                    }
                }
                sameTwo_M_List.get(result[0]).setIs_checked(true);
                sameTwoMAdapter.setSelectedList(result[0]);

                break;
            case D_THREE:
                result = numberRandom(3, difThreeNums);
                for (int i = 0; i < result.length; i++) {
                    difThreeList.get(result[i]).setIs_checked(true);
                    difThreeAdapter.setSelectedList(result[i]);
                }
                break;
            case D_TWO:
                result = numberRandom(2, difTwoNums);
                for (int i = 0; i < result.length; i++) {
                    difTwoList.get(result[i]).setIs_checked(true);
                    difTwoAdapter.setSelectedList(result[i]);
                }
                break;
        }
    }

    /**
     * 机选号码
     */
    private int[] numberRandom(int select_num, List<String> nums) {
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

    public void showOverTimePop() {
        // 一个自定义的布局，作为显示的内容
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_overtime, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView comfirm = (TextView) view.findViewById(R.id.comfirm);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearNum();
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
}
