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
 * 福建快三
 * Created by wwm on 18/10/6.
 */

public class PukeActivity extends AppCompatActivity {

    //每注彩票价格
    private final int TicketPrice = 2;
    //普通投注
    private final int S_BAOXUAN = 1;//包选
    private final int S_TONGHUA = 2;//同花
    private final int S_SHUNZI = 3;//顺子
    private final int S_TONGHUASHUN = 4;//同花顺
    private final int S_BAOZI = 5;//豹子
    private final int S_DUIZI = 6;//对子

    //拖胆
    private final int SD_ONE = 7;//任选1
    private final int SD_TWO = 8;//任选2
    private final int SD_THREE = 9;//任选3
    private final int SD_FOUR = 10;//任选4
    private final int SD_FIVE = 11;//任选5
    private final int SD_SIX = 12;//任选6


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



    private int mSelecte_Mode = S_BAOXUAN;

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
    //包选
    private List<String> baoxuanNums = new ArrayList<>();
    private TagAdapter<String> baoxuanAdapter;
    private List<NumInfo> baoxuanList = new ArrayList<>();

    //同花
    private List<String> tonghuaNums = new ArrayList<>();
    private TagAdapter<String> tonghuaAdapter;
    private List<NumInfo> tonghuaList = new ArrayList<>();


    private boolean sameThreeAll;
    //顺子
    private List<String> shunziNums = new ArrayList<>();
    private TagAdapter<String> shunziAdapter;
    private List<NumInfo> shunziList = new ArrayList<>();

    //同花顺
    private List<String> tonghuashunNums = new ArrayList<>();
    private TagAdapter<String> tonghuashunAdapter;
    private List<NumInfo> tonghuashunList = new ArrayList<>();
    //豹子
    private List<String> baoziNums = new ArrayList<>();
    private TagAdapter<String> baoziAdapter;
    private List<NumInfo> baoziList = new ArrayList<>();

    //对子
    private List<String> duiziNums = new ArrayList<>();
    private TagAdapter<String> duiziAdapter;
    private List<NumInfo> duiziList = new ArrayList<>();


    //任选1
    private List<String> RONENums = new ArrayList<>();
    private TagAdapter<String> RONEAdapter;
    private List<NumInfo> RONEList = new ArrayList<>();

    private boolean difThreeAll;
    //任选2
    private List<String> RTWONums = new ArrayList<>();
    private TagAdapter<String> RTWOAdapter;
    private List<NumInfo> RTWOList = new ArrayList<>();
    //任选3
    private List<String> RTHREENums = new ArrayList<>();
    private TagAdapter<String> RTHREEAdapter;
    private List<NumInfo> RTHREEList = new ArrayList<>();
    //任选4
    private List<String> RFOURNums = new ArrayList<>();
    private TagAdapter<String> RFOURAdapter;
    private List<NumInfo> RFOURList = new ArrayList<>();
    //任选5
    private List<String> RFIVENums = new ArrayList<>();
    private TagAdapter<String> RFIVEAdapter;
    private List<NumInfo> RFIVEList = new ArrayList<>();
    //任选6
    private List<String> RSIXNums = new ArrayList<>();
    private TagAdapter<String> RSIXAdapter;
    private List<NumInfo> RSIXList = new ArrayList<>();


    private ProgressDialog dialog_test;
    private ProgressDialog dialog;
    private ACache mAcache;

    //任选
    int[] images = {
            R.drawable.puke1, R.drawable.puke2,
            R.drawable.puke3, R.drawable.puke4,
            R.drawable.puke5, R.drawable.puke6,
            R.drawable.puke7, R.drawable.puke8,
            R.drawable.puke9, R.drawable.puke10,
            R.drawable.puke11, R.drawable.puke12,
            R.drawable.puke13,};
    int[] images_selected = {
            R.drawable.puke1_selected, R.drawable.puke2_selected,
            R.drawable.puke3_selected, R.drawable.puke4_selected,
            R.drawable.puke5_selected, R.drawable.puke6_selected,
            R.drawable.puke7_selected, R.drawable.puke8_selected,
            R.drawable.puke9_selected, R.drawable.puke10_selected,
            R.drawable.puke11_selected, R.drawable.puke12_selected,
            R.drawable.puke13_selected};
    //包选
    int[] baoxuan = {
            R.drawable.tonghuashunbx,
            R.drawable.tonghuabx,
            R.drawable.duizibx,
            R.drawable.baozibx,
            R.drawable.shunzibx};
    int[] baoxuan_selected = {
            R.drawable.tonghuashunbx_selected,
            R.drawable.tonghuabx_selected,
            R.drawable.duizibx_selected,
            R.drawable.baozibx_selected,
            R.drawable.shunzibx_selected
    };
    //同花
    int[] tonghua = {
            R.drawable.tonghua1,
            R.drawable.tonghua2,
            R.drawable.tonghua3,
            R.drawable.tonghua4,
    };
    int[] tonghua_selected = {
            R.drawable.tonghua1_selected,
            R.drawable.tonghua2_selected,
            R.drawable.tonghua3_selected,
            R.drawable.tonghua4_selected,

    };
    //同花顺
    int[] tonghuashun = {
            R.drawable.tonghuashun1,
            R.drawable.tonghuashun2,
            R.drawable.tonghuashun3,
            R.drawable.tonghuashun4,
    };
    int[] tonghuashun_selected = {
            R.drawable.tonghuashun1_2,
            R.drawable.tonghuashun2_2,
            R.drawable.tonghuashun3_2,
            R.drawable.tonghuashun4_2,

    };
    //豹子
    int[] baozi = {
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
    int[] baozi_selected = {
            R.drawable.baozi1_2,
            R.drawable.baozi2_2,
            R.drawable.baozi3_2,
            R.drawable.baozi4_2,
            R.drawable.baozi5_2,
            R.drawable.baozi6_2,
            R.drawable.baozi7_2,
            R.drawable.baozi8_2,
            R.drawable.baozi9_2,
            R.drawable.baozi10_2,
            R.drawable.baozij_2,
            R.drawable.baoziq_2,
            R.drawable.baozik_2,

    };
    //对子
    int[] duizi = {
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
    int[] duizi_selected = {
            R.drawable.duizi1_2,
            R.drawable.duizi2_2,
            R.drawable.duizi3_2,
            R.drawable.duizi4_2,
            R.drawable.duizi5_2,
            R.drawable.duizi6_2,
            R.drawable.duizi7_2,
            R.drawable.duizi8_2,
            R.drawable.duizi9_2,
            R.drawable.duizi10_2,
            R.drawable.duizi11_2,
            R.drawable.duizi12_2,
            R.drawable.duizi13_2,

    };
    //顺子
    int[] shunzi = {
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
    int[] shunzi_selected = {
            R.drawable.shunzi1_2,
            R.drawable.shunzi2_2,
            R.drawable.shunzi3_2,
            R.drawable.shunzi4_2,
            R.drawable.shunzi5_2,
            R.drawable.shunzi6_2,
            R.drawable.shunzi7_2,
            R.drawable.shunzi8_2,
            R.drawable.shunzi9_2,
            R.drawable.shunzi10_2,
            R.drawable.shunzi11_2,
            R.drawable.shunzi12_2,


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puke);
        ButterKnife.bind(this);
        mActivity = this;
//        dialog = new ProgressDialog(mActivity);
//        dialog.setTitle("正在联网下载数据...");
//        dialog.setMessage("请稍后...");
//        dialog.show();
        sh_name = getIntent().getStringExtra("sh_name");
        mContinue = getIntent().getBooleanExtra("continue", false);
        mSelecte_Mode = S_BAOXUAN;
        mAcache = ACache.get(this);

        initData();
        initRule();

        initBAOXUAN();
//        initTONGHUA();
//        initTONGHUASHUN();
//        initDUIZI();
//        initSHUNZI();
//        initBAOZI();
//        initONE();
//        initTWO();
//        initTHREE();
//        initFOUR();
//        initFIVE();
//        initSIX();
        initSameThreeAll();
        initDifThreeAll();
        // getDetail();

        // initBefore();


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
                mInfo.setCurrent_time(Long.valueOf(mInfo.getOpen_time()) - Long.valueOf(mInfo.getNow_time()) - Long.valueOf(mInfo.getAfter_time()) + "");
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
        //包选 yes
        for (int i = 0; i < 5; i++) {
            baoxuanNums.add("");
        }
        for (int i = 0; i < 5; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i + "");
            baoxuanList.add(info);
        }
        //同花 yes
        for (int i = 0; i < 4; i++) {
            tonghuaNums.add("");
        }
        for (int i = 0; i < 4; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            tonghuaList.add(info);
        }
        //顺子 yes
        for (int i = 0; i <12; i++) {
            shunziNums.add(i + "");
        }
        for (int i = 0; i <12; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i + "");
            shunziList.add(info);
        }
//        //同花顺 yes
        for (int i = 0; i < 4; i++) {
            tonghuashunNums.add(i * 1 + "");
        }
        for (int i = 0; i < 4; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            tonghuashunList.add(info);
        }
        //豹子 yes
        for (int i = 0; i < 13; i++) {
            baoziNums.add(i + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i + "");
            baoziList.add(info);
        }
        //对子 yes
        for (int i = 0; i < 13; i++) {
            duiziNums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            duiziList.add(info);
        }
        //任选1
        for (int i = 0; i < 13; i++) {
            RONENums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            RONEList.add(info);
        }
        //任选2
        for (int i = 0; i < 13; i++) {
            RTWONums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            RTWOList.add(info);
        }
        //任选3
        for (int i = 0; i < 13; i++) {
            RTHREENums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            RTHREEList.add(info);
        }
        //任选4
        for (int i = 0; i < 13; i++) {
            RFOURNums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            RFOURList.add(info);
        }
        //任选5
        for (int i = 0; i < 13; i++) {
            RFIVENums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            RFIVEList.add(info);
        }
        //任选6
        for (int i = 0; i < 13; i++) {
            RSIXNums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            info.setNum(i * 111 + "");
            RSIXList.add(info);
        }


        //玩法初始化，普通投注
        ElevenSortInfo info1 = new ElevenSortInfo();
        info1.setIs_checked(true);
        info1.setName("包选");
        elevenNormalList.add(info1);

        ElevenSortInfo info2 = new ElevenSortInfo();
        info2.setName("同花");
        elevenNormalList.add(info2);

        ElevenSortInfo info3 = new ElevenSortInfo();
        info3.setName("顺子");
        elevenNormalList.add(info3);

        ElevenSortInfo info4 = new ElevenSortInfo();
        info4.setName("同花顺");
        elevenNormalList.add(info4);

        ElevenSortInfo info5 = new ElevenSortInfo();
        info5.setName("豹子");
        elevenNormalList.add(info5);

        ElevenSortInfo info6 = new ElevenSortInfo();
        info6.setName("对子");
        elevenNormalList.add(info6);

        //拖胆投注
        ElevenSortInfo info7 = new ElevenSortInfo();
        info7.setName("任选一");
        elevenSpaceList.add(info7);

        ElevenSortInfo info8 = new ElevenSortInfo();
        info8.setName("任选二");
        elevenSpaceList.add(info8);

        ElevenSortInfo info9 = new ElevenSortInfo();
        info9.setName("任选三");
        elevenSpaceList.add(info9);

        ElevenSortInfo info10 = new ElevenSortInfo();
        info10.setName("任选四");
        elevenSpaceList.add(info10);

        ElevenSortInfo info11 = new ElevenSortInfo();
        info11.setName("任选五");
        elevenSpaceList.add(info11);

        ElevenSortInfo info12 = new ElevenSortInfo();
        info12.setName("任选六");
        elevenSpaceList.add(info12);

        //玩法选项
        normalAdapter = new NoramlAdapter(mActivity, R.layout.grid_item_tv, elevenNormalList);
        spacelAdapter = new SpaceAdapter(mActivity, R.layout.grid_item_tv, elevenSpaceList);
    }

    /**
     * 初始化规则,
     */
    private void initRule() {
        switch (mSelecte_Mode) {
            case S_BAOXUAN:
                title.setText("包选");
                ruleTv.setText("包选");
                fastBig.setVisibility(View.GONE);
                fastDual.setVisibility(View.GONE);
                fastSig.setVisibility(View.GONE);
                fastSmall.setVisibility(View.GONE);
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
            case S_TONGHUA:
                title.setText("同花");
                ruleTv.setText("同花");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
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
            case S_TONGHUASHUN:
                title.setText("同花顺");
                ruleTv.setText("同花顺");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case S_BAOZI:
                title.setText("豹子");
                ruleTv.setText("豹子");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case S_SHUNZI:
                title.setText("顺子");
                ruleTv.setText("顺子");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case S_DUIZI:
                title.setText("对子");
                ruleTv.setText("对子");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case SD_ONE:
                title.setText("任选一");
                ruleTv.setText("任选一");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case SD_TWO:
                title.setText("任选二");
                ruleTv.setText("任选二");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case SD_THREE:
                title.setText("任选三");
                ruleTv.setText("任选三");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case SD_FOUR:
                title.setText("任选四");
                ruleTv.setText("任选四");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case SD_FIVE:
                title.setText("任选五");
                ruleTv.setText("任选五");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
            case SD_SIX:
                title.setText("任选六");
                ruleTv.setText("任选六");
                ruleTv.setVisibility(View.VISIBLE);
                sumvalueLy.setVisibility(View.VISIBLE);
                sameThreeLy.setVisibility(View.GONE);
                sameTwoLy.setVisibility(View.GONE);
                sameTwoSLy.setVisibility(View.GONE);
                difThreeLy.setVisibility(View.GONE);
                difTwoLy.setVisibility(View.GONE);
                sDifThreeLy.setVisibility(View.GONE);
                sDifTwoLy.setVisibility(View.GONE);
                randomBtn.setVisibility(View.VISIBLE);
                sameThreeAllLy.setVisibility(View.GONE);
                difThreeAllLy.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 包选   视图初始化
     */
    private void initBAOXUAN() {
        Log.e("initBAOXUAN","initBAOXUAN");
        for (int i = 0; i < baoxuanList.size(); i++) {
            baoxuanList.get(i).setIs_checked(false);
        }
        baoxuanAdapter = new TagAdapter<String>(baoxuanNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, sumvalueTag, false);
                if (baoxuanList.get(position).is_checked()) {
                    tv.setBackgroundResource(baoxuan_selected[position]);
                } else {
                    tv.setBackgroundResource(baoxuan[position]);
                }

                return tv;
            }
        };
        sumvalueTag.setAdapter(baoxuanAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                if (baoxuanList.get(position).is_checked()) {
                    baoxuanList.get(position).setIs_checked(false);
                    baoxuanAdapter.notifyDataChanged();

                } else {
                    baoxuanList.get(position).setIs_checked(true);
                    baoxuanAdapter.notifyDataChanged();

                }


                calculate_baoxuan();

                return false;
            }
        });
    }

    /**
     * 同花   视图初始化
     */
    private void initTONGHUA() {

        Log.e("inittonghua","inittonghua");

        for (int i = 0; i < tonghuaList.size(); i++) {
            tonghuaList.get(i).setIs_checked(false);
        }
        tonghuaAdapter = new TagAdapter<String>(tonghuaNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, sumvalueTag, false);
                if (tonghuaList.get(position).is_checked()) {
                    tv.setBackgroundResource(tonghua_selected[position]);
                } else {
                    tv.setBackgroundResource(tonghua[position]);
                }


                return tv;
            }
        };
        sumvalueTag.setAdapter(tonghuaAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                if (tonghuaList.get(position).is_checked()) {
                    tonghuaList.get(position).setIs_checked(false);
                    tonghuaAdapter.notifyDataChanged();
                } else {
                    tonghuaList.get(position).setIs_checked(true);
                    tonghuaAdapter.notifyDataChanged();
                }
                calculate_tonghua();

                return false;
            }
        });


    }

    /**
     * 顺子   视图初始化
     */
    private void initSHUNZI() {
        for (int i = 0; i < shunziList.size(); i++) {
            shunziList.get(i).setIs_checked(false);
        }
        shunziAdapter = new TagAdapter<String>(shunziNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_baozi, sumvalueTag, false);

                if (shunziList.get(position).is_checked()) {
                    tv.setBackgroundResource(shunzi_selected[position]);
                } else {
                    tv.setBackgroundResource(shunzi[position]);
                }
                return tv;
            }
        };
        sumvalueTag.setAdapter(shunziAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (shunziList.get(position).is_checked()) {
                    shunziList.get(position).setIs_checked(false);
                    shunziAdapter.notifyDataChanged();
                } else {
                    shunziList.get(position).setIs_checked(true);
                    shunziAdapter.notifyDataChanged();
                }
                calculate_shunzi();
                return false;
            }
        });
    }

    /**
     * 同花顺   视图初始化
     */
    private void initTONGHUASHUN() {
        Log.e("tonghuashunlist",String.valueOf(tonghuashunList.size()));
        for (int i = 0; i < tonghuashunList.size(); i++) {
            tonghuashunList.get(i).setIs_checked(false);
        }
        tonghuashunAdapter = new TagAdapter<String>(tonghuashunNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, sumvalueTag, false);

                if (tonghuashunList.get(position).is_checked()) {
                    tv.setBackgroundResource(tonghuashun_selected[position]);
                } else {
                    tv.setBackgroundResource(tonghuashun[position]);
                }
                return tv;
            }
        };
        sumvalueTag.setAdapter(tonghuashunAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (tonghuashunList.get(position).is_checked()) {
                    tonghuashunList.get(position).setIs_checked(false);
                    tonghuashunAdapter.notifyDataChanged();
                } else {
                    tonghuashunList.get(position).setIs_checked(true);
                    tonghuashunAdapter.notifyDataChanged();
                }
                calculate_tonghuashun();
                return false;
            }
        });
    }

    /**
     * 豹子
     */
    private void initBAOZI() {
        Log.e("initbaozi",String.valueOf(baoziList.size()));

        for (int i = 0; i < baoziList.size(); i++) {
            baoziList.get(i).setIs_checked(false);
        }
        baoziAdapter = new TagAdapter<String>(baoziNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_baozi, sumvalueTag, false);
                if (baoziList.get(position).is_checked()) {
                    tv.setBackgroundResource(baozi_selected[position]);
                } else {
                    tv.setBackgroundResource(baozi[position]);
                }

                return tv;
            }
        };
        sumvalueTag.setAdapter(baoziAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                if (baoziList.get(position).is_checked()) {
                    baoziList.get(position).setIs_checked(false);
                    baoziAdapter.notifyDataChanged();

                } else {
                    baoziList.get(position).setIs_checked(true);
                    baoziAdapter.notifyDataChanged();

                }


                calculate_baozi();

                return false;
            }
        });
    }

    /**
     * 对子
     */
    private void initDUIZI() {
        for (int i = 0; i < duiziList.size(); i++) {
            duiziList.get(i).setIs_checked(false);
        }
        duiziAdapter = new TagAdapter<String>(duiziNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_baozi, sumvalueTag, false);
                if (duiziList.get(position).is_checked()) {
                    tv.setBackgroundResource(duizi_selected[position]);
                } else {
                    tv.setBackgroundResource(duizi[position]);
                }
                return tv;
            }
        };
        sumvalueTag.setAdapter(duiziAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (duiziList.get(position).is_checked()) {
                    duiziList.get(position).setIs_checked(false);
                    duiziAdapter.notifyDataChanged();
                } else {
                    duiziList.get(position).setIs_checked(true);
                    duiziAdapter.notifyDataChanged();
                }
                calculate_duizi();
                return false;
            }
        });
    }

    /**
     * 任选1
     */
    private void initONE() {
        for (int i = 0; i < RONEList.size(); i++) {
            RONEList.get(i).setIs_checked(false);
        }
        RONEAdapter = new TagAdapter<String>(RONENums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, sumvalueTag, false);

                if (RONEList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        sumvalueTag.setAdapter(RONEAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (RONEList.get(position).is_checked()) {
                    RONEList.get(position).setIs_checked(false);
                    RONEAdapter.notifyDataChanged();
                } else {
                    RONEList.get(position).setIs_checked(true);
                    RONEAdapter.notifyDataChanged();
                }
                calculate_one();
                return false;
            }
        });
    }

    /**
     * 任选2
     */
    private void initTWO() {
        for (int i = 0; i < RTWOList.size(); i++) {
            RTWOList.get(i).setIs_checked(false);
        }
        RTWOAdapter = new TagAdapter<String>(RTWONums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, sumvalueTag, false);

                if (RTWOList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }

                return tv;
            }
        };
        sumvalueTag.setAdapter(RTWOAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (RTWOList.get(position).is_checked()) {
                    RTWOList.get(position).setIs_checked(false);
                    RTHREEAdapter.notifyDataChanged();
                } else {
                    RTWOList.get(position).setIs_checked(true);
                    RTWOAdapter.notifyDataChanged();
                }
                calculate_two();
                return false;
            }
        });
    }

    /**
     * 任选3
     */
    private void initTHREE() {
        for (int i = 0; i < RTHREEList.size(); i++) {
            RTHREEList.get(i).setIs_checked(false);
        }
        RTHREEAdapter = new TagAdapter<String>(RTHREENums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, sumvalueTag, false);

                if (RTHREEList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        sumvalueTag.setAdapter(RTHREEAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (RTHREEList.get(position).is_checked()) {
                    RTHREEList.get(position).setIs_checked(false);
                    RTHREEAdapter.notifyDataChanged();
                } else {
                    RTHREEList.get(position).setIs_checked(true);
                    RTHREEAdapter.notifyDataChanged();
                }
                calculate_three();
                return false;
            }
        });
    }

    /**
     * 任选4
     */
    private void initFOUR() {
        for (int i = 0; i < RFOURList.size(); i++) {
            RFOURList.get(i).setIs_checked(false);
        }
        RFOURAdapter = new TagAdapter<String>(RFOURNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, sumvalueTag, false);

                if (RFOURList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        sumvalueTag.setAdapter(RFOURAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (RFOURList.get(position).is_checked()) {
                    RFOURList.get(position).setIs_checked(false);
                    RFOURAdapter.notifyDataChanged();
                } else {
                    RFOURList.get(position).setIs_checked(true);
                    RFOURAdapter.notifyDataChanged();
                }
                calculate_four();
                return false;
            }
        });
    }

    /**
     * 任选5
     */
    private void initFIVE() {
        for (int i = 0; i < RFIVEList.size(); i++) {
            RFIVEList.get(i).setIs_checked(false);
        }
        RFIVEAdapter = new TagAdapter<String>(RFIVENums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, sumvalueTag, false);

                if (RFIVEList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        sumvalueTag.setAdapter(RFIVEAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (RFIVEList.get(position).is_checked()) {
                    RFIVEList.get(position).setIs_checked(false);
                    RFIVEAdapter.notifyDataChanged();
                } else {
                    RFIVEList.get(position).setIs_checked(true);
                    RFIVEAdapter.notifyDataChanged();
                }
               calculate_five();
                return false;
            }
        });
    }

    /**
     * 任选6
     */
    private void initSIX() {
        for (int i = 0; i < RSIXList.size(); i++) {
            RSIXList.get(i).setIs_checked(false);
        }
        RSIXAdapter = new TagAdapter<String>(RSIXNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, sumvalueTag, false);

                if (RSIXList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        sumvalueTag.setAdapter(RSIXAdapter);
        sumvalueTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (RSIXList.get(position).is_checked()) {
                    RSIXList.get(position).setIs_checked(false);
                    RSIXAdapter.notifyDataChanged();
                } else {
                    RSIXList.get(position).setIs_checked(true);
                    RSIXAdapter.notifyDataChanged();
                }
                calculate_six();
                return false;
            }
        });
    }

    public void initSameThreeAll() {
        sameThreeAll = false;
        threeSameBtn.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
        threeSameBtn.setTextColor(Color.parseColor("#269ee6"));
    }

    public void initDifThreeAll() {
        difThreeAll = false;
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
                //showBackPop();
                Intent i=new Intent(this,BaseActivity.class);
                startActivity(i);
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
                    case S_BAOXUAN:
                        initBAOXUAN();
                        break;
                    case S_TONGHUA:
                        initTONGHUA();
                        break;
                    case S_TONGHUASHUN:
                        initTONGHUASHUN();
                        break;
                    case S_BAOZI:
                        initBAOZI();
                        break;
                    case S_DUIZI:
                        initDUIZI();
                        break;
                    case S_SHUNZI:
                        initSHUNZI();
                        break;
                    case SD_ONE:
                        initONE();
                        break;
                    case SD_TWO:
                        initTWO();
                        break;
                    case SD_THREE:
                        initTHREE();
                        break;
                    case SD_FOUR:
                        initFOUR();
                        break;
                    case SD_FIVE:
                        initFIVE();
                        break;
                    case SD_SIX:
                        initSIX();
                        break;

                }
                getRandow();
                break;
            case R.id.fast_big:
//                if (sumBig) {
//                    sumBig = false;
//                    fastBig.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    fastBig.setTextColor(Color.parseColor("#269ee6"));
//                } else {
//                    sumBig = true;
//                    sumSmall = false;
//                    fastBig.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
//                    fastSmall.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    fastBig.setTextColor(Color.parseColor("#ffffff"));
//                    fastSmall.setTextColor(Color.parseColor("#269ee6"));
//                }
//                fastSeleteSumValue();

                break;
            case R.id.fast_small:
//                if (sumSmall) {
//                    sumSmall = false;
//                    fastSmall.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    fastSmall.setTextColor(Color.parseColor("#269ee6"));
//                } else {
//                    sumSmall = true;
//                    sumBig = false;
//                    fastBig.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    fastSmall.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
//                    fastBig.setTextColor(Color.parseColor("#269ee6"));
//                    fastSmall.setTextColor(Color.parseColor("#ffffff"));
//                }
//                fastSeleteSumValue();

                break;
            case R.id.fast_sig:
//                if (sumSig) {
//                    sumSig = false;
//                    fastSig.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    fastSig.setTextColor(Color.parseColor("#269ee6"));
//                } else {
//                    sumSig = true;
//                    sumDual = false;
//                    fastSig.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
//                    fastDual.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    fastSig.setTextColor(Color.parseColor("#ffffff"));
//                    fastDual.setTextColor(Color.parseColor("#269ee6"));
//                }
//                fastSeleteSumValue();
                break;
            case R.id.fast_dual:
//                if (sumDual) {
//                    sumDual = false;
//                    fastDual.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    fastDual.setTextColor(Color.parseColor("#269ee6"));
//                } else {
//                    sumSig = false;
//                    sumDual = true;
//                    fastSig.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    fastDual.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
//                    fastSig.setTextColor(Color.parseColor("#269ee6"));
//                    fastDual.setTextColor(Color.parseColor("#ffffff"));
//                }
//                fastSeleteSumValue();
                break;
            //三同号通选
            case R.id.three_same_btn:
//                if (sameThreeAll) {
//                    sameThreeAll = false;
//                    threeSameBtn.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    threeSameBtn.setTextColor(Color.parseColor("#269ee6"));
//                    num.setText("0");
//                    price.setText("0");
//                } else {
//                    sameThreeAll = true;
//                    threeSameBtn.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
//                    threeSameBtn.setTextColor(Color.parseColor("#ffffff"));
//                    num.setText("1");
//                    price.setText(1 * TicketPrice + "");
//                }
                break;
            //三连号
            case R.id.three_dif_btn:
//                if (difThreeAll) {
//                    difThreeAll = false;
//                    threeDifBtn.setBackgroundResource(R.drawable.btn_bg_blue_s_fill);
//                    threeDifBtn.setTextColor(Color.parseColor("#269ee6"));
//                    num.setText("0");
//                    price.setText("0");
//                } else {
//                    difThreeAll = true;
//                    threeDifBtn.setBackgroundResource(R.drawable.btn_bg_blues_s_fill_s);
//                    threeDifBtn.setTextColor(Color.parseColor("#ffffff"));
//                    num.setText("1");
//                    price.setText(1 * TicketPrice + "");
//                }

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
            Intent in = new Intent(PukeActivity.this, PukeDetailActivity.class);
            //baoxuan
            in.putExtra("baoxuanList", (Serializable) baoxuanList);
            //tonghua
            in.putExtra("tonghuaList", (Serializable) tonghuaList);
            //shunzi
            in.putExtra("sameTwo_D_List", (Serializable) shunziList);

            //tonghuashun
            in.putExtra("sameTwo_M_List", (Serializable) tonghuashunList);
            //baozi
            in.putExtra("difThreeList", (Serializable) baoziList);
            //duizi
            in.putExtra("difTwoList", (Serializable) duiziList);
            //one
            in.putExtra("sDifThreeList", (Serializable) RONEList);

            //two
            in.putExtra("sDifTwoList", (Serializable) RTWOList);


            //three
            in.putExtra("sameThreeAll",  (Serializable) RTHREEList);
            //four
            in.putExtra("difThreeAll",  (Serializable) RFOURList);
            //five
            in.putExtra("difThreeAll",  (Serializable) RFIVEList);
            //six
            in.putExtra("difThreeAll",  (Serializable) RSIXList);

            in.putExtra("selecte_mode", mSelecte_Mode);
            in.putExtra("num", num.getText().toString());
            in.putExtra("price", price.getText().toString());
            in.putExtra("title", mInfo.getName());
            in.putExtra("sh_name", mInfo.getSh_name());
            in.putExtra("type", mInfo.getType());
            in.putExtra("now_qs", mInfo.getNow_qs());
            setResult(RESULT_OK, in);
            finish();
            clearNum();
        } else {
            Log.e("mContinue", "false");
            Intent in = new Intent(PukeActivity.this, PukeDetailActivity.class);
            //和值
            in.putExtra("baoxuanList", (Serializable) baoxuanList);
            //三同号
            in.putExtra("tonghuaList", (Serializable) baoziList);
            //二同号单选
            in.putExtra("sameTwo_D_List", (Serializable) baoziList);
            in.putExtra("sameTwo_S_List", (Serializable) baoziList);
            //二同号复选
            in.putExtra("sameTwo_M_List", (Serializable) baoziList);
            //三不同号
            in.putExtra("difThreeList", (Serializable) baoziList);
            //二不同号
            in.putExtra("difTwoList", (Serializable) baoziList);
            //拖胆——三不同号
            in.putExtra("sDifThreeList", (Serializable) baoziList);
            in.putExtra("sSDifThreeList", (Serializable) baoziList);
            //拖胆——二不同号
            in.putExtra("sDifTwoList", (Serializable) baoziList);
            in.putExtra("sSDifTwoList", (Serializable) baoziList);

            //三同号通选
            in.putExtra("sameThreeAll", sameThreeAll);
            //三连号通选
            in.putExtra("difThreeAll", difThreeAll);

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
        initBAOXUAN();
        Log.e("sum", sumBig + "," + sumSmall + "," + sumSig + "," + sumDual);
        if (sumBig && !sumSig && !sumDual) {
            for (int i = baoxuanList.size() / 2; i < baoxuanList.size(); i++) {
                baoxuanList.get(i).setIs_checked(true);
                baoxuanAdapter.setSelectedList(i);
            }
        } else if (sumSmall && !sumSig && !sumDual) {
            for (int i = 0; i < baoxuanList.size() / 2; i++) {
                baoxuanList.get(i).setIs_checked(true);
                baoxuanAdapter.setSelectedList(i);
            }
        } else if (sumSig && !sumBig && !sumSmall) {
            for (int i = 0; i < baoxuanList.size(); i++) {
                if (i % 2 == 0) {
                    baoxuanList.get(i).setIs_checked(true);
                    baoxuanAdapter.setSelectedList(i);
                }
            }
        } else if (sumDual && !sumBig && !sumSmall) {
            for (int i = 0; i < baoxuanList.size(); i++) {
                if (i % 2 == 1) {
                    baoxuanList.get(i).setIs_checked(true);
                    baoxuanAdapter.setSelectedList(i);
                }
            }
        } else if (sumBig && (sumSig || sumDual)) {
            if (sumSig) {
                for (int i = baoxuanList.size() / 2; i < baoxuanList.size(); i++) {
                    if (i % 2 == 0) {
                        baoxuanList.get(i).setIs_checked(true);
                        baoxuanAdapter.setSelectedList(i);
                    }
                }
            } else if (sumDual) {
                for (int i = baoxuanList.size() / 2; i < baoxuanList.size(); i++) {
                    if (i % 2 == 1) {
                        baoxuanList.get(i).setIs_checked(true);
                        baoxuanAdapter.setSelectedList(i);
                    }
                }
            }
        } else if (sumSmall && (sumSig || sumDual)) {
            if (sumSig) {
                for (int i = 0; i < baoxuanList.size() / 2; i++) {
                    if (i % 2 == 0) {
                        baoxuanList.get(i).setIs_checked(true);
                        baoxuanAdapter.setSelectedList(i);
                    }
                }
            } else if (sumDual) {
                for (int i = 0; i < baoxuanList.size() / 2; i++) {
                    if (i % 2 == 1) {
                        baoxuanList.get(i).setIs_checked(true);
                        baoxuanAdapter.setSelectedList(i);
                    }
                }
            }
        } else if (sumSig && (sumBig || sumSmall)) {
            if (sumBig) {
                for (int i = baoxuanList.size() / 2; i < baoxuanList.size(); i++) {
                    if (i % 2 == 0) {
                        baoxuanList.get(i).setIs_checked(true);
                        baoxuanAdapter.setSelectedList(i);
                    }
                }
            } else if (sumSmall) {
                for (int i = 0; i < baoxuanList.size() / 2; i++) {
                    if (i % 2 == 0) {
                        baoxuanList.get(i).setIs_checked(true);
                        baoxuanAdapter.setSelectedList(i);
                    }
                }
            }
        } else if (sumDual && (sumBig || sumSmall)) {
            if (sumBig) {
                for (int i = baoxuanList.size() / 2; i < baoxuanList.size(); i++) {
                    if (i % 2 == 1) {
                        baoxuanList.get(i).setIs_checked(true);
                        baoxuanAdapter.setSelectedList(i);
                    }
                }
            } else if (sumSmall) {
                for (int i = 0; i < baoxuanList.size() / 2; i++) {
                    if (i % 2 == 1) {
                        baoxuanList.get(i).setIs_checked(true);
                        baoxuanAdapter.setSelectedList(i);
                    }
                }
            }
        }
      // sumValueConsult();
    }

    /**
     * 计算baoxuan
     */
    public void calculate_baoxuan() {
        //计算选中的注数和金额
        int buy_acount = 0;

            for (int i = 0; i < baoxuanList.size(); i++) {
                if (baoxuanList.get(i).is_checked()) {
                    buy_acount++;
                    Log.e("sumValueSelecte", baoxuanList.get(i).getNum());
                }
            }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算tonghua
     */
    public void calculate_tonghua() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < tonghuaList.size(); i++) {
            if (tonghuaList.get(i).is_checked()) {
                buy_acount++;
                Log.e("sumValueSelecte", tonghuaList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算shunzi
     */
    public void calculate_shunzi() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < shunziList.size(); i++) {
            if (shunziList.get(i).is_checked()) {
                buy_acount++;
                Log.e("sumValueSelecte", shunziList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算tonghuashun
     */
    public void calculate_tonghuashun() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < tonghuashunList.size(); i++) {
            if (tonghuashunList.get(i).is_checked()) {
                buy_acount++;
                Log.e("sumValueSelecte", tonghuashunList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算baozi
     */
    public void calculate_baozi() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < baoziList.size(); i++) {
            if (baoziList.get(i).is_checked()) {
                buy_acount++;
                Log.e("sumValueSelecte", baoziList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算duizi
     */
    public void calculate_duizi() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < duiziList.size(); i++) {
            if (duiziList.get(i).is_checked()) {
                buy_acount++;
                Log.e("sumValueSelecte", duiziList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }

    /**
     * 计算one
     */
    public void calculate_one() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < RONEList.size(); i++) {
            if (RONEList.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算two
     */
    public void calculate_two() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < RTWOList.size(); i++) {
            if (RTWOList.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算three
     */
    public void calculate_three() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < RTHREEList.size(); i++) {
            if (RTHREEList.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算four
     */
    public void calculate_four() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < RFOURList.size(); i++) {
            if (RFOURList.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算five
     */
    public void calculate_five() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < RFIVEList.size(); i++) {
            if (RFIVEList.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }
    /**
     * 计算six
     */
    public void calculate_six() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < RSIXList.size(); i++) {
            if (RSIXList.get(i).is_checked()) {
                buy_acount++;
                Log.e("sumValueSelecte", duiziList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
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
                   // ToastUtil.toast(mActivity, " click"+info.getName().toString());
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
                        case "包选":

                            mSelecte_Mode = S_BAOXUAN;
                            break;
                        case "同花":

                            mSelecte_Mode = S_TONGHUA;
                            break;
                        case "顺子":

                            mSelecte_Mode = S_SHUNZI;
                            break;
                        case "同花顺":

                            mSelecte_Mode = S_TONGHUASHUN;
                            break;
                        case "豹子":

                            mSelecte_Mode = S_BAOZI;

                            break;
                        case "对子":

                            mSelecte_Mode = S_DUIZI;
                            break;
                        case "任选一":

                            mSelecte_Mode = SD_ONE;
                            break;
                        case "任选二":

                            mSelecte_Mode = SD_TWO;
                            break;
                        case "任选三":

                            mSelecte_Mode = SD_THREE;
                            break;
                        case "任选四":

                            mSelecte_Mode = SD_FOUR;
                            break;
                        case "任选五":

                            mSelecte_Mode = SD_FIVE;
                            break;
                        case "任选六":

                            mSelecte_Mode = SD_SIX;
                            break;

                    }
                    mAcache.put("FAST_SELECT_MODE", mSelecte_Mode + "");
                    initRule();
                    clearNum();

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
                   // ToastUtil.toast(mActivity, " click"+info.getName().toString());
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
                        case "任选一":

                            mSelecte_Mode = SD_ONE;
                            break;
                        case "任选二":

                            mSelecte_Mode = SD_TWO;
                            break;
                        case "任选三":

                            mSelecte_Mode = SD_THREE;
                            break;
                        case "任选四":

                            mSelecte_Mode = SD_FOUR;
                            break;
                        case "任选五":

                            mSelecte_Mode = SD_FIVE;
                            break;
                        case "任选六":

                            mSelecte_Mode = SD_SIX;
                            break;
                    }
                    mAcache.put("FAST_SELECT_MODE", mSelecte_Mode + "");
                    initRule();
                    clearNum();
                }
            });
        }
    }

    /**
     * 清空和值号码
     */
    private void clearNum() {
        switch (mSelecte_Mode) {
            case S_BAOXUAN:
                initBAOXUAN();
                break;
            case S_TONGHUA:
                initTONGHUA();
                break;
            case S_BAOZI:
                initBAOZI();
                break;
            case S_TONGHUASHUN:
                initTONGHUASHUN();
                break;
            case S_DUIZI:
                initDUIZI();
                break;
            case S_SHUNZI:
                initSHUNZI();
                break;
            case SD_ONE:
                initONE();
                break;
            case SD_TWO:
                initTWO();
                break;
            case SD_THREE:
                initTHREE();
                break;
            case SD_FOUR:
                initFOUR();
                break;
            case SD_FIVE:
                initFIVE();
                break;
            case SD_SIX:
                initSIX();
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
            //
            case S_BAOXUAN:
                result = numberRandom(1, baoxuanNums);
                for (int i = 0; i < result.length; i++) {
                    baoxuanList.get(result[i]).setIs_checked(true);
                    baoxuanAdapter.setSelectedList(result[i]);
                }
                break;
                //
            case S_TONGHUA:
                result = numberRandom(1, tonghuaNums);
                for (int i = 0; i < result.length; i++) {
                    tonghuaList.get(result[i]).setIs_checked(true);
                    tonghuaAdapter.setSelectedList(result[i]);
                }
                break;
            case S_TONGHUASHUN:
                result = numberRandom(1, tonghuashunNums);
                for (int i = 0; i < result.length; i++) {
                    tonghuashunList.get(result[i]).setIs_checked(true);
                    tonghuashunAdapter.setSelectedList(result[i]);
                }
                break;
            case S_BAOZI:
                result = numberRandom(1, baoziNums);
                for (int i = 0; i < result.length; i++) {
                    baoziList.get(result[i]).setIs_checked(true);
                    baoziAdapter.setSelectedList(result[i]);
                }

                break;
            case S_DUIZI:
                result = numberRandom(1, duiziNums);
                for (int i = 0; i < result.length; i++) {
                    duiziList.get(result[i]).setIs_checked(true);
                    duiziAdapter.setSelectedList(result[i]);
                }
                break;
            case SD_ONE:
                result = numberRandom(1, RONENums);
                for (int i = 0; i < result.length; i++) {
                    RONEList.get(result[i]).setIs_checked(true);
                    RONEAdapter.setSelectedList(result[i]);
                }
                break;
            case SD_TWO:
                result = numberRandom(2, RTWONums);
                for (int i = 0; i < result.length; i++) {
                    RTWOList.get(result[i]).setIs_checked(true);
                    RTWOAdapter.setSelectedList(result[i]);
                }
                break;
            case SD_THREE:
                result = numberRandom(3, RTHREENums);
                for (int i = 0; i < result.length; i++) {
                    RTHREEList.get(result[i]).setIs_checked(true);
                    RTHREEAdapter.setSelectedList(result[i]);
                }
                break;
            case SD_FOUR:
                result = numberRandom(4, RFOURNums);
                for (int i = 0; i < result.length; i++) {
                    RFOURList.get(result[i]).setIs_checked(true);
                    RFOURAdapter.setSelectedList(result[i]);
                }
                break;
            case SD_FIVE:
                result = numberRandom(5, RFIVENums);
                for (int i = 0; i < result.length; i++) {
                    RFIVEList.get(result[i]).setIs_checked(true);
                    RFIVEAdapter.setSelectedList(result[i]);
                }
                break;

            case SD_SIX:
                result = numberRandom(6, RSIXNums);
                for (int i = 0; i < result.length; i++) {
                    RSIXList.get(result[i]).setIs_checked(true);
                    RSIXAdapter.setSelectedList(result[i]);
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
