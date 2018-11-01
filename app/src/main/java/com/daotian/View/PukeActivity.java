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
    private final int danxuan_tonghua = 2;//同花单选
    private final int danxuan_shunzi = 3;//顺子单选
    private final int danxuan_tonghuashun =4;//同花顺单选
    private final int danxuan_baozi = 5;//豹子单选
    private final int danxuan_duizi = 6;//对子单选



    //拖胆
    /*private final int SD_ONE = 7;//任选1*/
    private final int tuodan_two = 8;//任选2拖胆
    private final int tuodan_three = 9;//任选3拖胆
    private final int tuodan_four = 10;//任选4拖胆
    private final int tuodan_five = 11;//任选5拖胆
    private final int tuodan_six = 12;//任选6拖胆

    //普通任选
    private final int putong_one = 13;//任选1普通
    private final int putong_two = 14;//任选2普通
    private final int putong_three = 15;//任选3普通
    private final int putong_four = 16;//任选4普通
    private final int putong_five = 17;//任选5普通
    private final int putong_six = 18;//任选6普通

    private final int type_TONGHUA = 21;//同花包选
    private final int type_SHUNZI = 22;//顺子包选
    private final int type_TONGHUASHUN = 23;//同花顺包选
    private final int type_BAOZI = 24;//豹子包选
    private final int type_DUIZI = 25;//对子包选
    int temp_position;


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
    @BindView(R.id.rule_tv)
    TextView ruleTv;
    @BindView(R.id.random_btn)
    TextView randomBtn;
    @BindView(R.id.baoxuan)
    TagFlowLayout baoxuan;
    @BindView(R.id.layout_baoxuan)
    LinearLayout layoutBaoxuan;
    @BindView(R.id.tonghua)
    TagFlowLayout tonghua;
    @BindView(R.id.layout_tonghua)
    LinearLayout layoutTonghua;
    @BindView(R.id.shunzi)
    TagFlowLayout shunzi;
    @BindView(R.id.layout_shunzi)
    LinearLayout layoutShunzi;
    @BindView(R.id.tonghuashun)
    TagFlowLayout tonghuashun;
    @BindView(R.id.layout_tonghuashun)
    LinearLayout layoutTonghuashun;
    @BindView(R.id.baozi)
    TagFlowLayout baozi;
    @BindView(R.id.layout_baozi)
    LinearLayout layoutBaozi;
    @BindView(R.id.duizi)
    TagFlowLayout duizi;
    @BindView(R.id.layout_duizi)
    LinearLayout layoutDuizi;
    @BindView(R.id.rone)
    TagFlowLayout rone;
    @BindView(R.id.layout_rone)
    LinearLayout layoutRone;
    @BindView(R.id.rtwo)
    TagFlowLayout rtwo;
    @BindView(R.id.layout_rtwo)
    LinearLayout layoutRtwo;
    @BindView(R.id.three)
    TagFlowLayout three;
    @BindView(R.id.layout_rthree)
    LinearLayout layoutRthree;
    @BindView(R.id.four)
    TagFlowLayout four;
    @BindView(R.id.layout_rfour)
    LinearLayout layoutRfour;
    @BindView(R.id.five)
    TagFlowLayout five;
    @BindView(R.id.layout_rfive)
    LinearLayout layoutRfive;
    @BindView(R.id.six)
    TagFlowLayout six;
    @BindView(R.id.layout_rsix)
    LinearLayout layoutRsix;
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
    @BindView(R.id.top_ly)
    LinearLayout topLy;
    @BindView(R.id.rrone)
    TagFlowLayout rrone;
    @BindView(R.id.layout_rrone)
    LinearLayout layoutRrone;
    @BindView(R.id.rrtwo)
    TagFlowLayout rrtwo;
    @BindView(R.id.layout_rrtwo)
    LinearLayout layoutRrtwo;
    @BindView(R.id.rthree)
    TagFlowLayout rthree;
    @BindView(R.id.layout_rrthree)
    LinearLayout layoutRrthree;
    @BindView(R.id.rfour)
    TagFlowLayout rfour;
    @BindView(R.id.layout_rrfour)
    LinearLayout layoutRrfour;
    @BindView(R.id.rfive)
    TagFlowLayout rfive;
    @BindView(R.id.layout_rrfive)
    LinearLayout layoutRrfive;
    @BindView(R.id.rsix)
    TagFlowLayout rsix;
    @BindView(R.id.layout_rrsix)
    LinearLayout layoutRrsix;
    @BindView(R.id.rtwo2)
    TagFlowLayout rtwo2;
    @BindView(R.id.three2)
    TagFlowLayout three2;
    @BindView(R.id.four2)
    TagFlowLayout four2;
    @BindView(R.id.five2)
    TagFlowLayout five2;
    @BindView(R.id.six2)
    TagFlowLayout six2;

    private int mSelecte_Mode = S_BAOXUAN;
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

    //包选
    private List<String> baoxuanNums = new ArrayList<>();
    private TagAdapter<String> baoxuanAdapter;
    private List<NumInfo> baoxuanList = new ArrayList<>();

    //同花单选
    private List<String> tonghuaNums = new ArrayList<>();
    private TagAdapter<String> tonghuaAdapter;
    private List<NumInfo> tonghuaList = new ArrayList<>();


    //顺子单选
    private List<String> shunziNums = new ArrayList<>();
    private TagAdapter<String> shunziAdapter;
    private List<NumInfo> shunziList = new ArrayList<>();

    //同花顺单选
    private List<String> tonghuashunNums = new ArrayList<>();
    private TagAdapter<String> tonghuashunAdapter;
    private List<NumInfo> tonghuashunList = new ArrayList<>();
    //豹子单选
    private List<String> baoziNums = new ArrayList<>();
    private TagAdapter<String> baoziAdapter;
    private List<NumInfo> baoziList = new ArrayList<>();

    //对子单选
    private List<String> duiziNums = new ArrayList<>();
    private TagAdapter<String> duiziAdapter;
    private List<NumInfo> duiziList = new ArrayList<>();


//    //任选1
//    // down 拖胆
//    private List<String> RONENums = new ArrayList<>();
//    private TagAdapter<String> RONEAdapter;
//    private List<NumInfo> RONEList = new ArrayList<>();


    //任选2拖胆
    private List<String> tuodan_twonums = new ArrayList<>();
    private TagAdapter<String> twodan_twoadapter;
    private List<NumInfo> tuodan_twolist = new ArrayList<>();
    //任选3拖胆
    private List<String> tuodan_threenums = new ArrayList<>();
    private TagAdapter<String> tuodan_threeadapter;
    private List<NumInfo> tuodan_threelist = new ArrayList<>();
    //任选4拖胆
    private List<String> tuodan_fournums = new ArrayList<>();
    private TagAdapter<String> tuodan_fouradapter;
    private List<NumInfo> tuodan_fourlist = new ArrayList<>();
    //任选5拖胆
    private List<String> tuodan_fivenums = new ArrayList<>();
    private TagAdapter<String> tuodan_fiveadapter;
    private List<NumInfo> tuodan_fivelist = new ArrayList<>();
    //任选6拖胆
    private List<String> tuodan_sixnums = new ArrayList<>();
    private TagAdapter<String> tuodan_sixadapter;
    private List<NumInfo> tuodan_sixlist = new ArrayList<>();


    //任选1   普通投注
    private List<String> putong_onenums = new ArrayList<>();
    private TagAdapter<String> putong_neadapter;
    private List<NumInfo> putong_onelist = new ArrayList<>();
    //任选2   普通投注
    private List<String> putong_twonums = new ArrayList<>();
    private TagAdapter<String> putong_twoadapter;
    private List<NumInfo> putong_twolist = new ArrayList<>();
    //任选3   普通投注
    private List<String> putong_threenums = new ArrayList<>();
    private TagAdapter<String> putong_threeadapter;
    private List<NumInfo> putongthreelist = new ArrayList<>();
    //任选4   普通投注
    private List<String> putong_fournums = new ArrayList<>();
    private TagAdapter<String> putong_fouradapter;
    private List<NumInfo> putong_fourlist = new ArrayList<>();
    //任选5   普通投注
    private List<String> putong_fivenums = new ArrayList<>();
    private TagAdapter<String> putong_fiveadapter;
    private List<NumInfo> putong_fivelist = new ArrayList<>();
    //任选6   普通投注
    private List<String> putong_sixnums = new ArrayList<>();
    private TagAdapter<String> putong_sixadapter;
    private List<NumInfo> putong_sixlist = new ArrayList<>();

    //任选2-6的拖胆

    //任选2
    private List<String> child_twonums = new ArrayList<>();
    private TagAdapter<String> child_twoAdapter;
    private List<NumInfo> child_twolist = new ArrayList<>();
    //任选3
    private List<String> child_threenums = new ArrayList<>();
    private TagAdapter<String> child_threeadpter;
    private List<NumInfo> child_threelist = new ArrayList<>();
    //任选4
    private List<String> child_fournums = new ArrayList<>();
    private TagAdapter<String> child_fourAdapter;
    private List<NumInfo> child_fourlist = new ArrayList<>();
    //任选5
    private List<String> child_fivenums = new ArrayList<>();
    private TagAdapter<String> child_fiveAdapter;
    private List<NumInfo> child_fivelist = new ArrayList<>();
    //任选6
    private List<String> child_sixnums = new ArrayList<>();
    private TagAdapter<String> child_sixAdapter;
    private List<NumInfo> child_sixlist = new ArrayList<>();


    private ProgressDialog dialog_test;
    private ProgressDialog dialog;
    private ACache mAcache;


    private  String zq_type,buytype;
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
    int[] images_baoxuan = {
            R.drawable.tonghuashunbx,
            R.drawable.tonghuabx,
            R.drawable.duizibx,
            R.drawable.baozibx,
            R.drawable.shunzibx};
    int[] images_baoxuan_selected = {
            R.drawable.tonghuashunbx_selected,
            R.drawable.tonghuabx_selected,
            R.drawable.duizibx_selected,
            R.drawable.baozibx_selected,
            R.drawable.shunzibx_selected
    };
    //同花
    int[] images_tonghua = {
            R.drawable.tonghua1,
            R.drawable.tonghua2,
            R.drawable.tonghua3,
            R.drawable.tonghua4,
    };
    int[] images_tonghua_selected = {
            R.drawable.tonghua1_selected,
            R.drawable.tonghua2_selected,
            R.drawable.tonghua3_selected,
            R.drawable.tonghua4_selected,

    };
    //同花顺
    int[] images_tonghuashun = {
            R.drawable.tonghuashun1,
            R.drawable.tonghuashun2,
            R.drawable.tonghuashun3,
            R.drawable.tonghuashun4,
    };
    int[] images_tonghuashun_selected = {
            R.drawable.tonghuashun1_2,
            R.drawable.tonghuashun2_2,
            R.drawable.tonghuashun3_2,
            R.drawable.tonghuashun4_2,

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
    int[] images_baozi_selected = {
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
    int[] images_duizi_selected = {
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
    int[] images_shunzi_selected = {
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
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("正在联网下载数据...");
        dialog.setMessage("请稍后...");
        dialog.show();
        sh_name = getIntent().getStringExtra("sh_name");
        mContinue = getIntent().getBooleanExtra("continue", false);
        mSelecte_Mode = S_BAOXUAN;
        mAcache = ACache.get(this);
        initData();
        initTdata();
        initRule();
        initBAOXUAN();
         getDetail();
        //initBefore();
    }


    private void initBefore() {
        String mode = mAcache.getAsString("PUKEMODE");
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
                zq_type=mInfo.getZq_type();
                Log.e("zq_type",zq_type);
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
            switch (i) {
                case 0:
                    info.setNum("同花顺包选");
                    info.setUpload_data("同花顺包选");
                    break;
                case 1:
                    info.setNum("同花包选");
                    info.setUpload_data("同花包选");
                    break;
                case 2:
                    info.setNum("对子包选");
                    info.setUpload_data("对子包选");
                    break;
                case 3:
                    info.setNum("豹子包选");
                    info.setUpload_data("豹子包选");
                    break;
                case 4:
                    info.setNum("顺子包选");
                    info.setUpload_data("顺子包选");
                    break;

            }

            baoxuanList.add(info);
        }
        //同花 yes
        for (int i = 0; i < 4; i++) {
            tonghuaNums.add("");
        }
        for (int i = 0; i < 4; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("♠");
                    info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("♥");
                    info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("♣");
                    info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("♦");
                    info.setUpload_data("4");
                    break;


            }
            tonghuaList.add(info);
        }
        //顺子 yes
        for (int i = 0; i < 12; i++) {
            shunziNums.add(i + "");
        }
        for (int i = 0; i < 12; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A  2  3");
                    info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2  3  4");
                    info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3  4  5");
                    info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4  5  6");
                    info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5  6  7");
                    info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6  7  8");
                    info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7  8  9");
                    info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8  9  10");
                    info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9  10  J");
                    info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10 J  Q");
                    info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J  Q  K");
                    info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q  K  A");
                    info.setUpload_data("12");
                    break;


            }
            shunziList.add(info);
        }
//        //同花顺 yes
        for (int i = 0; i < 4; i++) {
            tonghuashunNums.add(i * 1 + "");
        }
        for (int i = 0; i < 4; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("♠");
                    info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("♥");
                    info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("♣");
                    info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("♦");
                    info.setUpload_data("4");
                    break;


            }
            tonghuashunList.add(info);
        }
        //豹子 yes
        for (int i = 0; i < 13; i++) {
            baoziNums.add(i + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();

            switch (i) {
                case 0:
                    info.setNum("A  A  A");
                    info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2  2  2");
                    info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3  3  3");
                    info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4  4  4");
                    info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5  5  5");
                    info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6  6  6");
                    info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7  7  7");
                    info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8  8  8");
                    info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9  9  9");
                    info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10 10 10");
                    info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J  J  J");
                    info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q  Q  Q");
                    info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K  K  K");
                    info.setUpload_data("13");
                    break;


            }
            baoziList.add(info);
        }
        //对子 yes
        for (int i = 0; i < 13; i++) {
            duiziNums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A A");
                    info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2 2");
                    info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3 3");
                    info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4 4");
                    info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5 5");
                    info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6 6");
                    info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7 7");
                    info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8 8");
                    info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9 9");
                    info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10 10");
                    info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J J");
                    info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q Q");
                    info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K K");
                    info.setUpload_data("13");
                    break;


            }

            duiziList.add(info);
        }

        //任选2
        for (int i = 0; i < 13; i++) {
            tuodan_twonums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");
                    info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");
                    info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");
                    info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            tuodan_twolist.add(info);
        }
        //任选3
        for (int i = 0; i < 13; i++) {
            tuodan_threenums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            tuodan_threelist.add(info);
        }
        //任选4
        for (int i = 0; i < 13; i++) {
            tuodan_fournums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            tuodan_fourlist.add(info);
        }
        //任选5
        for (int i = 0; i < 13; i++) {
            tuodan_fivenums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            tuodan_fivelist.add(info);
        }
        //任选6
        for (int i = 0; i < 13; i++) {
            tuodan_sixnums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;

            }
            tuodan_sixlist.add(info);
        }
        /////up//////
        //任选1
        for (int i = 0; i < 13; i++) {
            putong_onenums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }


            putong_onelist.add(info);
        }
        //任选2
        for (int i = 0; i < 13; i++) {
            putong_twonums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            putong_twolist.add(info);
        }
        //任选3
        for (int i = 0; i < 13; i++) {
            putong_threenums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;

            }
            putongthreelist.add(info);
        }
        //任选4
        for (int i = 0; i < 13; i++) {
            putong_fournums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            putong_fourlist.add(info);
        }
        //任选5
        for (int i = 0; i < 13; i++) {
            putong_fivenums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            putong_fivelist.add(info);
        }
        //任选6
        for (int i = 0; i < 13; i++) {
            putong_sixnums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;

            }
            putong_sixlist.add(info);
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
        //up
        ElevenSortInfo info13 = new ElevenSortInfo();
        info13.setName("任选一");
        elevenNormalList.add(info13);

        ElevenSortInfo info14 = new ElevenSortInfo();
        info14.setName("任选二");
        elevenNormalList.add(info14);

        ElevenSortInfo info15 = new ElevenSortInfo();
        info15.setName("任选三");
        elevenNormalList.add(info15);

        ElevenSortInfo info16 = new ElevenSortInfo();
        info16.setName("任选四");
        elevenNormalList.add(info16);

        ElevenSortInfo info17 = new ElevenSortInfo();
        info17.setName("任选五");
        elevenNormalList.add(info17);
        ElevenSortInfo info18 = new ElevenSortInfo();
        info18.setName("任选六");
        elevenNormalList.add(info18);
        //

        //拖胆投注
       /* ElevenSortInfo info7 = new ElevenSortInfo();
        info7.setName("任选一");
        elevenSpaceList.add(info7);*/

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

    //拖胆的数据
    private void initTdata() {
        //任选2
        for (int i = 0; i < 13; i++) {
            child_twonums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            child_twolist.add(info);
        }
        //任选3
        for (int i = 0; i < 13; i++) {
            child_threenums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            child_threelist.add(info);
        }
        //任选4
        for (int i = 0; i < 13; i++) {
            child_fournums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            child_fourlist.add(info);
        }
        //任选5
        for (int i = 0; i < 13; i++) {
            child_fivenums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            child_fivelist.add(info);
        }
        //任选6
        for (int i = 0; i < 13; i++) {
            child_sixnums.add(i * 1 + "");
        }
        for (int i = 0; i < 13; i++) {
            NumInfo info = new NumInfo();
            switch (i) {
                case 0:
                    info.setNum("A");info.setUpload_data("1");
                    break;
                case 1:
                    info.setNum("2");info.setUpload_data("2");
                    break;
                case 2:
                    info.setNum("3");info.setUpload_data("3");
                    break;
                case 3:
                    info.setNum("4");info.setUpload_data("4");
                    break;
                case 4:
                    info.setNum("5");info.setUpload_data("5");
                    break;
                case 5:
                    info.setNum("6");info.setUpload_data("6");
                    break;
                case 6:
                    info.setNum("7");info.setUpload_data("7");
                    break;
                case 7:
                    info.setNum("8");info.setUpload_data("8");
                    break;
                case 8:
                    info.setNum("9");info.setUpload_data("9");
                    break;
                case 9:
                    info.setNum("10");info.setUpload_data("10");
                    break;
                case 10:
                    info.setNum("J");info.setUpload_data("11");
                    break;
                case 11:
                    info.setNum("Q");info.setUpload_data("12");
                    break;
                case 12:
                    info.setNum("K");info.setUpload_data("13");
                    break;


            }
            child_sixlist.add(info);
        }

    }

    /**
     * 初始化规则,
     */
    private void initRule() {
        switch (mSelecte_Mode) {
            case S_BAOXUAN:
                title.setText("包选");
                ruleTv.setText("包选");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.VISIBLE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);


                break;
            case danxuan_tonghua:
                title.setText("同花");
                ruleTv.setText("同花");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.VISIBLE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case danxuan_tonghuashun:
                title.setText("同花顺");
                ruleTv.setText("同花顺");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.VISIBLE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case danxuan_baozi:
                title.setText("豹子");
                ruleTv.setText("豹子");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.VISIBLE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case danxuan_shunzi:
                title.setText("顺子");
                ruleTv.setText("顺子");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.VISIBLE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case danxuan_duizi:
                title.setText("对子");
                ruleTv.setText("对子");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.VISIBLE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;

            case tuodan_two:
                title.setText("任选二");
                ruleTv.setText("任选二");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.VISIBLE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case tuodan_three:
                title.setText("任选三");
                ruleTv.setText("任选三");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.VISIBLE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case tuodan_four:
                title.setText("任选四");
                ruleTv.setText("任选四");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.VISIBLE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case tuodan_five:
                title.setText("任选五");
                ruleTv.setText("任选五");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.VISIBLE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case tuodan_six:
                title.setText("任选六");
                ruleTv.setText("任选六");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.GONE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.VISIBLE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case putong_one:
                title.setText("任选一");
                ruleTv.setText("任选一");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.VISIBLE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case putong_two:
                title.setText("任选二");
                ruleTv.setText("任选二");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.VISIBLE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case putong_three:
                title.setText("任选三");
                ruleTv.setText("任选三");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.VISIBLE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case putong_four:
                title.setText("任选四");
                ruleTv.setText("任选四");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.VISIBLE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case putong_five:
                title.setText("任选五");
                ruleTv.setText("任选五");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.VISIBLE);
                layoutRrsix.setVisibility(View.GONE);
                break;
            case putong_six:
                title.setText("任选六");
                ruleTv.setText("任选六");

                ruleTv.setVisibility(View.VISIBLE);
                randomBtn.setVisibility(View.VISIBLE);
                layoutBaoxuan.setVisibility(View.GONE);
                layoutTonghua.setVisibility(View.GONE);
                layoutShunzi.setVisibility(View.GONE);
                layoutTonghuashun.setVisibility(View.GONE);
                layoutBaozi.setVisibility(View.GONE);
                layoutDuizi.setVisibility(View.GONE);
                layoutRone.setVisibility(View.GONE);
                layoutRtwo.setVisibility(View.GONE);
                layoutRthree.setVisibility(View.GONE);
                layoutRfour.setVisibility(View.GONE);
                layoutRfive.setVisibility(View.GONE);
                layoutRsix.setVisibility(View.GONE);
                //up
                layoutRrone.setVisibility(View.GONE);
                layoutRrtwo.setVisibility(View.GONE);
                layoutRrthree.setVisibility(View.GONE);
                layoutRrfour.setVisibility(View.GONE);
                layoutRrfive.setVisibility(View.GONE);
                layoutRrsix.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 包选   视图初始化
     */
    private void initBAOXUAN() {
        Log.e("initBAOXUAN", "initBAOXUAN");
        for (int i = 0; i < baoxuanList.size(); i++) {
            baoxuanList.get(i).setIs_checked(false);
        }
        baoxuanAdapter = new TagAdapter<String>(baoxuanNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, baoxuan, false);
                if (baoxuanList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_baoxuan_selected[position]);
                } else {
                    tv.setBackgroundResource(images_baoxuan[position]);
                }

                return tv;
            }
        };
        baoxuan.setAdapter(baoxuanAdapter);
        baoxuan.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                    //如果已经被点击！

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

        Log.e("inittonghua", "inittonghua");

        for (int i = 0; i < tonghuaList.size(); i++) {
            tonghuaList.get(i).setIs_checked(false);
        }
        tonghuaAdapter = new TagAdapter<String>(tonghuaNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, tonghua, false);
                if (tonghuaList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_tonghua_selected[position]);
                } else {
                    tv.setBackgroundResource(images_tonghua[position]);
                }


                return tv;
            }
        };
        tonghua.setAdapter(tonghuaAdapter);
        tonghua.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
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
        Log.e("initSHUNZI", "initSHUNZI");

        for (int i = 0; i < shunziList.size(); i++) {
            shunziList.get(i).setIs_checked(false);
        }
        shunziAdapter = new TagAdapter<String>(shunziNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_baozi, shunzi, false);

                if (shunziList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_shunzi_selected[position]);
                } else {
                    tv.setBackgroundResource(images_shunzi[position]);
                }
                return tv;
            }
        };
        shunzi.setAdapter(shunziAdapter);
        shunzi.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
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
        Log.e("initSHUNZI", "initSHUNZI");

        Log.e("tonghuashunlist", String.valueOf(tonghuashunList.size()));
        for (int i = 0; i < tonghuashunList.size(); i++) {
            tonghuashunList.get(i).setIs_checked(false);
        }
        tonghuashunAdapter = new TagAdapter<String>(tonghuashunNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, tonghuashun, false);

                if (tonghuashunList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_tonghuashun_selected[position]);
                } else {
                    tv.setBackgroundResource(images_tonghuashun[position]);
                }
                return tv;
            }
        };
        tonghuashun.setAdapter(tonghuashunAdapter);
        tonghuashun.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
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
        Log.e("initbaozi", String.valueOf(baoziList.size()));
        Log.e("initBAOZI", "initBAOZI");

        for (int i = 0; i < baoziList.size(); i++) {
            baoziList.get(i).setIs_checked(false);
        }
        baoziAdapter = new TagAdapter<String>(baoziNums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_baozi, baozi, false);
                if (baoziList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_baozi_selected[position]);
                } else {
                    tv.setBackgroundResource(images_baozi[position]);
                }

                return tv;
            }
        };
        baozi.setAdapter(baoziAdapter);
        baozi.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
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
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_duizi, duizi, false);
                if (duiziList.get(position).is_checked()) {
                    tv.setBackgroundResource(images_duizi_selected[position]);
                } else {
                    tv.setBackgroundResource(images_duizi[position]);
                }
                return tv;
            }
        };
        duizi.setAdapter(duiziAdapter);
        duizi.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
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
     * 任选2 拖胆
     */
    private void initTWO() {
        for (int i = 0; i < tuodan_twolist.size(); i++) {
            tuodan_twolist.get(i).setIs_checked(false);
        }
        twodan_twoadapter = new TagAdapter<String>(tuodan_twonums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, rtwo, false);

                if (tuodan_twolist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }

                return tv;
            }
        };
        rtwo.setAdapter(twodan_twoadapter);
        rtwo.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                int number = 0;
                //如果被点击
                if (tuodan_twolist.get(position).is_checked()) {
                    //计算number
                    for (int i = 0; i < tuodan_twolist.size(); i++)
                    {
                        if (tuodan_twolist.get(i).is_checked()) {
                            number++;
                        }
                    }
                    //超出胆码数量，提示，且设为false；
                    if (number >0)
                    {
                        ToastUtil.toast(mActivity, "胆码只能选择一个");
                        tuodan_twolist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_twolist.get(position).is_checked()) {
                            child_twolist.get(position).setIs_checked(false);
                            child_twoAdapter.notifyDataChanged();

                        }
                    }
                    //没超出胆码数量
                    else
                    {
                        tuodan_twolist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_twolist.get(position).is_checked()) {
                            child_twolist.get(position).setIs_checked(false);
                            child_twoAdapter.notifyDataChanged();

                        }
                    }
                    child_twoAdapter.notifyDataChanged();
                }
                //没被点击，设为true
                else
                {
                    //计算num
                    for (int i = 0; i < tuodan_twolist.size(); i++)
                    {
                        if (tuodan_twolist.get(i).is_checked()) {
                            number++;
                        }
                    }

                    if (number >0)
                    {
                        ToastUtil.toast(mActivity, "胆码只能选择一个");
                        tuodan_twolist.get(position).setIs_checked(false);
                        if (child_twolist.get(position).is_checked()) {
                            child_twolist.get(position).setIs_checked(false);
                            child_twoAdapter.notifyDataChanged();
                        }
                    }
                    else
                    {
                        tuodan_twolist.get(position).setIs_checked(true);
                        if (child_twolist.get(position).is_checked()) {
                            child_twolist.get(position).setIs_checked(false);
                            child_twoAdapter.notifyDataChanged();
                        }
                    }
                    child_twoAdapter.notifyDataChanged();
                }
                calculate_two();
                return false;
            }
        });

        //拖胆

        for (int i = 0; i < child_twolist.size(); i++) {
            child_twolist.get(i).setIs_checked(false);
        }
        child_twoAdapter = new TagAdapter<String>(child_twonums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, rtwo2, false);

                if (child_twolist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }

                return tv;
            }
        };
        rtwo2.setAdapter(child_twoAdapter);
        rtwo2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //同样的位置如果已经被点击
                if (child_twolist.get(position).is_checked()) {
                    child_twolist.get(position).setIs_checked(false);
                    if(tuodan_twolist.get(position).is_checked())
                    {
                        tuodan_twolist.get(position).setIs_checked(false);
                        child_twoAdapter.notifyDataChanged();

                    }


                    child_twoAdapter.notifyDataChanged();
                } else {

                    child_twolist.get(position).setIs_checked(true);
                    if(tuodan_twolist.get(position).is_checked())
                    {
                        tuodan_twolist.get(position).setIs_checked(false);
                        child_twoAdapter.notifyDataChanged();

                    }
                    child_twoAdapter.notifyDataChanged();
                }
                calculate_two();
                return false;
            }
        });
    }

    /**
     * 任选3 拖胆
     */
    private void initTHREE() {
        for (int i = 0; i < tuodan_threelist.size(); i++) {
            tuodan_threelist.get(i).setIs_checked(false);
        }
        tuodan_threeadapter = new TagAdapter<String>(tuodan_threenums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, three, false);

                if (tuodan_threelist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        three.setAdapter(tuodan_threeadapter);
        three.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                int number = 0;
                //如果被点击
                if (tuodan_threelist.get(position).is_checked()) {
                    //计算number
                    for (int i = 0; i < tuodan_threelist.size(); i++)
                    {
                        if (tuodan_threelist.get(i).is_checked()) {
                            number++;
                        }
                    }
                    //超出胆码数量，提示，且设为false；
                    if (number > 1)
                    {
                        ToastUtil.toast(mActivity, "胆码只能选择二个");
                        tuodan_threelist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_threelist.get(position).is_checked()) {
                            child_threelist.get(position).setIs_checked(false);
                            child_threeadpter.notifyDataChanged();

                        }
                    }
                    //没超出胆码数量
                    else
                    {
                        tuodan_threelist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_threelist.get(position).is_checked()) {
                            child_threelist.get(position).setIs_checked(false);
                            child_threeadpter.notifyDataChanged();

                        }
                    }
                    tuodan_threeadapter.notifyDataChanged();
                }
                //没被点击，设为true
                else
                {
                    //计算num
                    for (int i = 0; i < tuodan_threelist.size(); i++)
                    {
                        if (tuodan_threelist.get(i).is_checked()) {
                            number++;
                        }
                    }

                    if (number > 1)
                    {
                        ToastUtil.toast(mActivity, "胆码只能选择二个");
                        tuodan_threelist.get(position).setIs_checked(false);
                        if (child_threelist.get(position).is_checked()) {
                            child_threelist.get(position).setIs_checked(false);
                            child_threeadpter.notifyDataChanged();
                        }
                    }
                    else
                    {
                        tuodan_threelist.get(position).setIs_checked(true);
                        if (child_threelist.get(position).is_checked()) {
                            child_threelist.get(position).setIs_checked(false);
                            child_threeadpter.notifyDataChanged();
                        }
                    }
                    tuodan_threeadapter.notifyDataChanged();
                }
                calculate_three();
                return false;
            }
        });

        //拖胆
        for (int i = 0; i < child_threelist.size(); i++) {
            child_threelist.get(i).setIs_checked(false);
        }
        child_threeadpter = new TagAdapter<String>(child_threenums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, three2, false);

                if (child_threelist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        three2.setAdapter(child_threeadpter);
        three2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //同样的位置如果已经被点击
                if (child_threelist.get(position).is_checked()) {
                    child_threelist.get(position).setIs_checked(false);
                    if(tuodan_threelist.get(position).is_checked())
                    {
                        tuodan_threelist.get(position).setIs_checked(false);
                        tuodan_threeadapter.notifyDataChanged();

                    }


                    child_threeadpter.notifyDataChanged();
                } else {

                    child_threelist.get(position).setIs_checked(true);
                    if(tuodan_threelist.get(position).is_checked())
                    {
                        tuodan_threelist.get(position).setIs_checked(false);
                        tuodan_threeadapter.notifyDataChanged();

                    }
                    child_threeadpter.notifyDataChanged();
                }
                calculate_three();
                return false;
            }
        });
    }

    /**
     * 任选4 拖胆
     */
    private void initFOUR() {
        for (int i = 0; i < tuodan_fourlist.size(); i++) {
            tuodan_fourlist.get(i).setIs_checked(false);
        }
        tuodan_fouradapter = new TagAdapter<String>(tuodan_fournums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, four, false);

                if (tuodan_fourlist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        four.setAdapter(tuodan_fouradapter);
        four.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                int number = 0;
                //如果被点击
                if (tuodan_fourlist.get(position).is_checked()) {
                    //计算number
                    for (int i = 0; i < tuodan_fourlist.size(); i++)
                    {
                        if (tuodan_fourlist.get(i).is_checked()) {
                            number++;
                        }
                    }
                    //超出胆码数量，提示，且设为false；
                    if (number > 2)
                    {
                        ToastUtil.toast(mActivity, "胆码只能选择三个");
                        tuodan_fourlist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_fourlist.get(position).is_checked()) {
                            child_fourlist.get(position).setIs_checked(false);
                            child_fourAdapter.notifyDataChanged();

                        }
                    }
                    //没超出胆码数量
                    else
                    {
                        tuodan_fourlist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_fourlist.get(position).is_checked()) {
                            child_fourlist.get(position).setIs_checked(false);
                            child_fourAdapter.notifyDataChanged();

                        }
                    }
                    tuodan_fouradapter.notifyDataChanged();
                }
                //没被点击，设为true
                else
                {
                    //计算num
                    for (int i = 0; i < tuodan_fourlist.size(); i++)
                    {
                        if (tuodan_fourlist.get(i).is_checked()) {
                            number++;
                        }
                    }

                    if (number > 2)
                    {
                        ToastUtil.toast(mActivity, "胆码只能选择三个");
                        tuodan_fourlist.get(position).setIs_checked(false);
                        if (child_fourlist.get(position).is_checked()) {
                            child_fourlist.get(position).setIs_checked(false);
                            child_fourAdapter.notifyDataChanged();
                        }
                    }
                    else
                    {
                        tuodan_fourlist.get(position).setIs_checked(true);
                        if (child_fourlist.get(position).is_checked()) {
                            child_fourlist.get(position).setIs_checked(false);
                            child_fourAdapter.notifyDataChanged();
                        }
                    }
                    tuodan_fouradapter.notifyDataChanged();
                }
                calculate_four();
                return false;
            }
        });
        //拖胆
        for (int i = 0; i < child_fourlist.size(); i++) {
            child_fourlist.get(i).setIs_checked(false);
        }
        child_fourAdapter = new TagAdapter<String>(child_fournums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, four2, false);

                if (child_fourlist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        four2.setAdapter(child_fourAdapter);
        four2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //同样的位置如果已经被点击
                if (child_fourlist.get(position).is_checked()) {
                    child_fourlist.get(position).setIs_checked(false);
                    if(tuodan_fourlist.get(position).is_checked())
                    {
                        tuodan_fourlist.get(position).setIs_checked(false);
                        tuodan_fouradapter.notifyDataChanged();

                    }


                    child_fourAdapter.notifyDataChanged();
                } else {

                    child_fourlist.get(position).setIs_checked(true);
                    if(tuodan_fourlist.get(position).is_checked())
                    {
                        tuodan_fourlist.get(position).setIs_checked(false);
                        tuodan_fouradapter.notifyDataChanged();

                    }
                    child_fourAdapter.notifyDataChanged();
                }
                calculate_four();
                return false;
            }
        });
    }

    /**
     * 任选5 拖胆
     */
    private void initFIVE() {
        for (int i = 0; i < tuodan_fivelist.size(); i++) {
            tuodan_fivelist.get(i).setIs_checked(false);
        }
        tuodan_fiveadapter = new TagAdapter<String>(tuodan_fivenums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, five, false);

                if (tuodan_fivelist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        five.setAdapter(tuodan_fiveadapter);
        five.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                int number = 0;
                //如果被点击
                if (tuodan_fivelist.get(position).is_checked()) {
                    //计算number
                    for (int i = 0; i < tuodan_fivelist.size(); i++)
                    {
                        if (tuodan_fivelist.get(i).is_checked()) {
                            number++;
                        }
                    }
                    //超出胆码数量，提示，且设为false；
                    if (number > 3)
                    {
                        ToastUtil.toast(mActivity, "胆码只能选择四个");
                        tuodan_fivelist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_fivelist.get(position).is_checked()) {
                            child_fivelist.get(position).setIs_checked(false);
                            child_fiveAdapter.notifyDataChanged();

                        }
                    }
                    //没超出胆码数量
                    else
                    {
                        tuodan_fivelist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_fivelist.get(position).is_checked()) {
                            child_fivelist.get(position).setIs_checked(false);
                            child_fiveAdapter.notifyDataChanged();

                        }
                    }
                    tuodan_fiveadapter.notifyDataChanged();
                }
                //没被点击，设为true
                else
                {
                    //计算num
                    for (int i = 0; i < tuodan_fivelist.size(); i++)
                    {
                        if (tuodan_fivelist.get(i).is_checked()) {
                            number++;
                        }
                    }

                    if (number > 3)
                    {
                        ToastUtil.toast(mActivity, "胆码只能选择四个");
                        tuodan_fivelist.get(position).setIs_checked(false);
                        if (child_fivelist.get(position).is_checked()) {
                            child_fivelist.get(position).setIs_checked(false);
                            child_fiveAdapter.notifyDataChanged();
                        }
                    }
                    else
                    {
                        tuodan_fivelist.get(position).setIs_checked(true);
                        if (child_fivelist.get(position).is_checked()) {
                            child_fivelist.get(position).setIs_checked(false);
                            child_fiveAdapter.notifyDataChanged();
                        }
                    }
                    tuodan_fiveadapter.notifyDataChanged();
                }
                calculate_five();
                return false;
            }
        });

        //胆拖
        for (int i = 0; i < child_fivelist.size(); i++) {
            child_fivelist.get(i).setIs_checked(false);
        }
        child_fiveAdapter = new TagAdapter<String>(child_fivenums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, five2, false);

                if (child_fivelist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        five2.setAdapter(child_fiveAdapter);
        five2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //同样的位置如果已经被点击
                if (child_fivelist.get(position).is_checked()) {
                    child_fivelist.get(position).setIs_checked(false);
                    if(tuodan_fivelist.get(position).is_checked())
                    {
                        tuodan_fivelist.get(position).setIs_checked(false);
                        tuodan_fiveadapter.notifyDataChanged();

                    }


                    child_fiveAdapter.notifyDataChanged();
                } else {

                    child_fivelist.get(position).setIs_checked(true);
                    if(tuodan_fivelist.get(position).is_checked())
                    {
                        tuodan_fivelist.get(position).setIs_checked(false);
                        tuodan_fiveadapter.notifyDataChanged();

                    }
                    child_fiveAdapter.notifyDataChanged();
                }
                calculate_five();
                return false;
            }
        });
    }

    /**
     * 任选6 拖胆
     */
    private void initSIX() {
        //胆码
        for (int i = 0; i < tuodan_sixlist.size(); i++) {
            tuodan_sixlist.get(i).setIs_checked(false);
        }
        tuodan_sixadapter = new TagAdapter<String>(tuodan_sixnums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, six, false);

                if (tuodan_sixlist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        six.setAdapter(tuodan_sixadapter);
        six.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                int number = 0;
                    //如果被点击
                if (tuodan_sixlist.get(position).is_checked()) {
                    //计算number
                    for (int i = 0; i < tuodan_sixlist.size(); i++) {
                        if (tuodan_sixlist.get(i).is_checked()) {
                            number++;
                        }
                    }
                    //超出胆码数量，提示，且设为false；
                    if (number > 4) {
                        ToastUtil.toast(mActivity, "胆码只能选择五个");
                        tuodan_sixlist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_sixlist.get(position).is_checked()) {
                            child_sixlist.get(position).setIs_checked(false);
                            child_sixAdapter.notifyDataChanged();

                        }
                    }
                    //没超出胆码数量
                    else {
                        tuodan_sixlist.get(position).setIs_checked(false);
                        //取消冲突
                        if (child_sixlist.get(position).is_checked()) {
                            child_sixlist.get(position).setIs_checked(false);
                            child_sixAdapter.notifyDataChanged();

                        }
                    }
                    tuodan_sixadapter.notifyDataChanged();
                }
                //没被点击，设为true
                else
                    {
                        //计算num
                    for (int i = 0; i < tuodan_sixlist.size(); i++) {
                        if (tuodan_sixlist.get(i).is_checked()) {
                            number++;
                        }
                    }
                    if (number > 4) {
                        ToastUtil.toast(mActivity, "胆码只能选择五个");
                        tuodan_sixlist.get(position).setIs_checked(false);
                        if (child_sixlist.get(position).is_checked()) {
                            child_sixlist.get(position).setIs_checked(false);
                            child_sixAdapter.notifyDataChanged();

                        }
                    }
                    else {
                        tuodan_sixlist.get(position).setIs_checked(true);
                        if (child_sixlist.get(position).is_checked()) {
                            child_sixlist.get(position).setIs_checked(false);
                            child_sixAdapter.notifyDataChanged();

                        }
                    }

                    tuodan_sixadapter.notifyDataChanged();
                }
                calculate_six();
                return false;
            }
        });
        //拖码
        for (int i = 0; i < child_sixlist.size(); i++) {
            child_sixlist.get(i).setIs_checked(false);
        }
        child_sixAdapter = new TagAdapter<String>(child_sixnums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, six2, false);

                if (child_sixlist.get(position).is_checked()) {

                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        six2.setAdapter(child_sixAdapter);
        six2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                    //同样的位置如果已经被点击
                if (child_sixlist.get(position).is_checked()) {
                    child_sixlist.get(position).setIs_checked(false);
                    if(tuodan_sixlist.get(position).is_checked())
                    {
                        tuodan_sixlist.get(position).setIs_checked(false);
                        tuodan_sixadapter.notifyDataChanged();

                    }


                    child_sixAdapter.notifyDataChanged();
                } else {

                    child_sixlist.get(position).setIs_checked(true);
                    if(tuodan_sixlist.get(position).is_checked())
                    {
                        tuodan_sixlist.get(position).setIs_checked(false);
                        tuodan_sixadapter.notifyDataChanged();

                    }
                    child_sixAdapter.notifyDataChanged();
                }
                calculate_six();
                return false;
            }
        });
    }

    //  普通投注

    /**
     * 任选1  普通投注
     */
    private void initRONE() {
        Log.e("initRONE", "initRONE");
        for (int i = 0; i < putong_onelist.size(); i++) {
            putong_onelist.get(i).setIs_checked(false);
        }
        putong_neadapter = new TagAdapter<String>(putong_onenums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, rrone, false);

                if (putong_onelist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        rrone.setAdapter(putong_neadapter);
        rrone.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (putong_onelist.get(position).is_checked()) {
                    putong_onelist.get(position).setIs_checked(false);
                    putong_neadapter.notifyDataChanged();
                } else {
                    putong_onelist.get(position).setIs_checked(true);
                    putong_neadapter.notifyDataChanged();
                }
                calculate_rone();
                return false;
            }
        });
    }

    /**
     * 任选2  普通投注
     */
    private void initRTWO() {
        for (int i = 0; i < putong_twolist.size(); i++) {
            putong_twolist.get(i).setIs_checked(false);
        }
        putong_twoadapter = new TagAdapter<String>(putong_twonums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, rrtwo, false);

                if (putong_twolist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }

                return tv;
            }
        };
        rrtwo.setAdapter(putong_twoadapter);
        rrtwo.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (putong_twolist.get(position).is_checked()) {
                    putong_twolist.get(position).setIs_checked(false);
                    putong_threeadapter.notifyDataChanged();
                } else {
                    putong_twolist.get(position).setIs_checked(true);
                    putong_twoadapter.notifyDataChanged();
                }
                calculate_rtwo();
                return false;
            }
        });
    }

    /**
     * 任选3  普通投注
     */
    private void initRTHREE() {
        for (int i = 0; i < putongthreelist.size(); i++) {
            putongthreelist.get(i).setIs_checked(false);
        }
        putong_threeadapter = new TagAdapter<String>(putong_threenums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, rthree, false);

                if (putongthreelist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        rthree.setAdapter(putong_threeadapter);
        rthree.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (putongthreelist.get(position).is_checked()) {
                    putongthreelist.get(position).setIs_checked(false);
                    putong_threeadapter.notifyDataChanged();
                } else {
                    putongthreelist.get(position).setIs_checked(true);
                    putong_threeadapter.notifyDataChanged();
                }
                calculate_rthree();
                return false;
            }
        });
    }

    /**
     * 任选4  普通投注
     */
    private void initRFOUR() {
        for (int i = 0; i < putong_fourlist.size(); i++) {
            putong_fourlist.get(i).setIs_checked(false);
        }
        putong_fouradapter = new TagAdapter<String>(putong_fournums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, rfour, false);

                if (putong_fourlist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        rfour.setAdapter(putong_fouradapter);
        rfour.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (putong_fourlist.get(position).is_checked()) {
                    putong_fourlist.get(position).setIs_checked(false);
                    putong_fouradapter.notifyDataChanged();
                } else {
                    putong_fourlist.get(position).setIs_checked(true);
                    putong_fouradapter.notifyDataChanged();
                }
                calculate_rfour();
                return false;
            }
        });
    }

    /**
     * 任选5  普通投注
     */
    private void initRFIVE() {
        for (int i = 0; i < putong_fivelist.size(); i++) {
            putong_fivelist.get(i).setIs_checked(false);
        }
        putong_fiveadapter = new TagAdapter<String>(putong_fivenums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, rfive, false);

                if (putong_fivelist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        rfive.setAdapter(putong_fiveadapter);
        rfive.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (putong_fivelist.get(position).is_checked()) {
                    putong_fivelist.get(position).setIs_checked(false);
                    putong_fiveadapter.notifyDataChanged();
                } else {
                    putong_fivelist.get(position).setIs_checked(true);
                    putong_fiveadapter.notifyDataChanged();
                }
                calculate_rfive();
                return false;
            }
        });
    }

    /**
     * 任选6  普通投注
     */
    private void initRSIX() {
        for (int i = 0; i < putong_sixlist.size(); i++) {
            putong_sixlist.get(i).setIs_checked(false);
        }
        putong_sixadapter = new TagAdapter<String>(putong_sixnums) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView tv = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.list_item_puke_tv, rsix, false);

                if (putong_sixlist.get(position).is_checked()) {
                    tv.setBackgroundResource(images_selected[position]);
                } else {
                    tv.setBackgroundResource(images[position]);
                }
                return tv;
            }
        };
        rsix.setAdapter(putong_sixadapter);
        rsix.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (putong_sixlist.get(position).is_checked()) {
                    putong_sixlist.get(position).setIs_checked(false);
                    putong_sixadapter.notifyDataChanged();
                } else {
                    putong_sixlist.get(position).setIs_checked(true);
                    putong_sixadapter.notifyDataChanged();
                }
                calculate_rsix();
                return false;
            }
        });
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

    @OnClick({R.id.comfirm_btn, R.id.clear_btn, R.id.back, R.id.title_ly, R.id.more, R.id.random_btn})
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
                    case S_BAOXUAN:
                        initBAOXUAN();
                        break;
                    case danxuan_tonghua:
                        initTONGHUA();
                        break;
                    case danxuan_tonghuashun:
                        initTONGHUASHUN();
                        break;
                    case danxuan_baozi:
                        initBAOZI();
                        break;
                    case danxuan_duizi:
                        initDUIZI();
                        break;
                    case danxuan_shunzi:
                        initSHUNZI();
                        break;
                   /* case SD_ONE:
                        initONE();
                        break;*/
                    case tuodan_two:
                        initTWO();
                        break;
                    case tuodan_three:
                        initTHREE();
                        break;
                    case tuodan_four:
                        initFOUR();
                        break;
                    case tuodan_five:
                        initFIVE();
                        break;
                    case tuodan_six:
                        initSIX();
                        break;
                    //up
                    case putong_one:
                        initRONE();
                        break;
                    case putong_two:
                        initRTWO();
                        break;
                    case putong_three:
                        initRTHREE();
                        break;
                    case putong_four:
                        initRFOUR();
                        break;
                    case putong_five:
                        initRFIVE();
                        break;
                    case putong_six:
                        initRSIX();
                        break;

                }
                getRandow();
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
            in.putExtra("baoxuanList", (Serializable) baoxuanList);
            in.putExtra("zq_type",zq_type);
            in.putExtra("buytype",String.valueOf(mSelecte_Mode));
            in.putExtra("tonghuaList", (Serializable) tonghuaList);
            in.putExtra("shunziList", (Serializable) shunziList);
            in.putExtra("tonghuashunList", (Serializable) tonghuashunList);
            in.putExtra("baoziList", (Serializable) baoziList);
            in.putExtra("duiziList", (Serializable) duiziList);
            in.putExtra("tuodan_twolist", (Serializable) tuodan_twolist);
            in.putExtra("tuodan_threelist", (Serializable) tuodan_threelist);
            in.putExtra("tuodan_fourlist", (Serializable) tuodan_fourlist);
            in.putExtra("tuodan_fivelist", (Serializable) tuodan_fivelist);
            in.putExtra("tuodan_sixlist", (Serializable) tuodan_sixlist);
            in.putExtra("child_twolist", (Serializable) child_twolist);
            in.putExtra("child_threelist", (Serializable) child_threelist);
            in.putExtra("child_fourlist", (Serializable) child_fourlist);
            in.putExtra("child_fivelist", (Serializable) child_fivelist);
            in.putExtra("child_sixlist", (Serializable) child_sixlist);

            in.putExtra("putong_onelist", (Serializable) putong_onelist);
            in.putExtra("putong_twolist", (Serializable) putong_twolist);
            in.putExtra("putongthreelist", (Serializable) putongthreelist);
            in.putExtra("putong_fourlist", (Serializable) putong_fourlist);
            in.putExtra("putong_fivelist", (Serializable) putong_fivelist);
            in.putExtra("putong_sixlist", (Serializable) putong_sixlist);
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
//            startActivity(in);
//            clearNum();
//            finish();
        } else {
            Log.e("mContinue", "false");
            Intent in = new Intent(PukeActivity.this, PukeDetailActivity.class);
            in.putExtra("zq_type",zq_type);
            in.putExtra("buytype",String.valueOf(mSelecte_Mode));
            in.putExtra("baoxuanList", (Serializable) baoxuanList);
            in.putExtra("tonghuaList", (Serializable) tonghuaList);
            in.putExtra("shunziList", (Serializable) shunziList);
            in.putExtra("tonghuashunList", (Serializable) tonghuashunList);
            in.putExtra("baoziList", (Serializable) baoziList);
            in.putExtra("duiziList", (Serializable) duiziList);
            in.putExtra("tuodan_twolist", (Serializable) tuodan_twolist);
            in.putExtra("tuodan_threelist", (Serializable) tuodan_threelist);
            in.putExtra("tuodan_fourlist", (Serializable) tuodan_fourlist);
            in.putExtra("tuodan_fivelist", (Serializable) tuodan_fivelist);
            in.putExtra("tuodan_sixlist", (Serializable) tuodan_sixlist);
            in.putExtra("child_twolist", (Serializable) child_twolist);
            in.putExtra("child_threelist", (Serializable) child_threelist);
            in.putExtra("child_fourlist", (Serializable) child_fourlist);
            in.putExtra("child_fivelist", (Serializable) child_fivelist);
            in.putExtra("child_sixlist", (Serializable) child_sixlist);

            in.putExtra("putong_onelist", (Serializable) putong_onelist);
            in.putExtra("putong_twolist", (Serializable) putong_twolist);
            in.putExtra("putongthreelist", (Serializable) putongthreelist);
            in.putExtra("putong_fourlist", (Serializable) putong_fourlist);
            in.putExtra("putong_fivelist", (Serializable) putong_fivelist);
            in.putExtra("putong_sixlist", (Serializable) putong_sixlist);
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


    public void calculate_baoxuan() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < baoxuanList.size(); i++) {
            if (baoxuanList.get(i).is_checked()) {
                buy_acount++;
                //Log.e("sumValueSelecte", baoxuanList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }


    public void calculate_tonghua() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < tonghuaList.size(); i++) {
            if (tonghuaList.get(i).is_checked()) {
                buy_acount++;
                //Log.e("sumValueSelecte", tonghuaList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }


    public void calculate_shunzi() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < shunziList.size(); i++) {
            if (shunziList.get(i).is_checked()) {
                buy_acount++;
                // Log.e("sumValueSelecte", shunziList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }


    public void calculate_tonghuashun() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < tonghuashunList.size(); i++) {
            if (tonghuashunList.get(i).is_checked()) {
                buy_acount++;
                // Log.e("sumValueSelecte", tonghuashunList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }


    public void calculate_baozi() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < baoziList.size(); i++) {
            if (baoziList.get(i).is_checked()) {
                buy_acount++;
                // Log.e("sumValueSelecte", baoziList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }


    public void calculate_duizi() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < duiziList.size(); i++) {
            if (duiziList.get(i).is_checked()) {
                buy_acount++;
                //  Log.e("sumValueSelecte", duiziList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }

    public void calculate_two() {
        //计算选中的注数和金额
        int buy_acount = 0;
        int but_two=0;
        for (int i = 0; i < tuodan_twolist.size(); i++) {
            if (tuodan_twolist.get(i).is_checked()) {
                buy_acount++;

            }
        }
        for (int i = 0; i < putong_twolist.size(); i++) {
            if (putong_twolist.get(i).is_checked()) {
                but_two++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }



    public void calculate_three() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < tuodan_threelist.size(); i++) {
            if (tuodan_threelist.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }


    public void calculate_four() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < tuodan_fourlist.size(); i++) {
            if (tuodan_fourlist.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }


    public void calculate_five() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < tuodan_fivelist.size(); i++) {
            if (tuodan_fivelist.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }


    public void calculate_six() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < tuodan_sixlist.size(); i++) {
            if (tuodan_sixlist.get(i).is_checked()) {
                buy_acount++;
                // Log.e("sumValueSelecte", duiziList.get(i).getNum());
            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }

    //普通投注

    /**
     * 计算rone  普通投注
     */
    public void calculate_rone() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < putong_onelist.size(); i++) {
            if (putong_onelist.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }

    /**
     * 计算rtwo  普通投注
     */
    public void calculate_rtwo() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < putong_twolist.size(); i++) {
            if (putong_twolist.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }

    /**
     * 计算rthree  普通投注
     */
    public void calculate_rthree() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < putongthreelist.size(); i++) {
            if (putongthreelist.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }

    /**
     * 计算rfour  普通投注
     */
    public void calculate_rfour() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < putong_fourlist.size(); i++) {
            if (putong_fourlist.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }

    /**
     * 计算rfive  普通投注
     */
    public void calculate_rfive() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < putong_fivelist.size(); i++) {
            if (putong_fivelist.get(i).is_checked()) {
                buy_acount++;

            }
        }
        num.setText(buy_acount + "");
        price.setText(buy_acount * TicketPrice + "");
    }

    /**
     * 计算rsix  普通投注
     */
    public void calculate_rsix() {
        //计算选中的注数和金额
        int buy_acount = 0;

        for (int i = 0; i < putong_sixlist.size(); i++) {
            if (putong_sixlist.get(i).is_checked()) {
                buy_acount++;
                // Log.e("sumValueSelecte", duiziList.get(i).getNum());
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

                            mSelecte_Mode = danxuan_tonghua;
                            break;
                        case "顺子":

                            mSelecte_Mode = danxuan_shunzi;
                            break;
                        case "同花顺":

                            mSelecte_Mode = danxuan_tonghuashun;
                            break;
                        case "豹子":

                            mSelecte_Mode = danxuan_baozi;

                            break;
                        case "对子":

                            mSelecte_Mode = danxuan_duizi;
                            break;
                        //普通投注
                        case "任选一":

                            mSelecte_Mode = putong_one;
                            //Log.e("mSelecte_Mode", "putong_one");
                            break;
                        case "任选二":

                            mSelecte_Mode = putong_two;
                            break;
                        case "任选三":

                            mSelecte_Mode = putong_three;
                            break;
                        case "任选四":

                            mSelecte_Mode = putong_four;
                            break;
                        case "任选五":

                            mSelecte_Mode = putong_five;
                            break;
                        case "任选六":

                            mSelecte_Mode = putong_six;
                            break;


                    }
                    mAcache.put("PUKEMODE", mSelecte_Mode + "");
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

                        case "任选二":

                            mSelecte_Mode = tuodan_two;
                            break;
                        case "任选三":

                            mSelecte_Mode = tuodan_three;
                            break;
                        case "任选四":

                            mSelecte_Mode = tuodan_four;
                            break;
                        case "任选五":

                            mSelecte_Mode = tuodan_five;
                            break;
                        case "任选六":

                            mSelecte_Mode = tuodan_six;
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
            case danxuan_tonghua:
                initTONGHUA();
                break;
            case danxuan_baozi:
                initBAOZI();
                break;
            case danxuan_tonghuashun:
                initTONGHUASHUN();
                break;
            case danxuan_duizi:
                initDUIZI();
                break;
            case danxuan_shunzi:
                initSHUNZI();
                break;
            //拖胆
            case tuodan_two:
                initTWO();
                break;
            case tuodan_three:
                initTHREE();
                break;
            case tuodan_four:
                initFOUR();
                break;
            case tuodan_five:
                initFIVE();
                break;
            case tuodan_six:
                initSIX();
                break;
            //普通投注
            case putong_one:
                initRONE();
                break;
            case putong_two:
                initRTWO();
                break;
            case putong_three:
                initRTHREE();
                break;
            case putong_four:
                initRFOUR();
                break;
            case putong_five:
                initRFIVE();
                break;
            case putong_six:
                initRSIX();
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
            case danxuan_tonghua:
                result = numberRandom(1, tonghuaNums);
                for (int i = 0; i < result.length; i++) {
                    tonghuaList.get(result[i]).setIs_checked(true);
                    tonghuaAdapter.setSelectedList(result[i]);
                }
                break;
            case danxuan_shunzi:
                result = numberRandom(1, shunziNums);
                for (int i = 0; i < result.length; i++) {
                    shunziList.get(result[i]).setIs_checked(true);
                    shunziAdapter.setSelectedList(result[i]);
                }
                break;
            case danxuan_tonghuashun:
                result = numberRandom(1, tonghuashunNums);
                for (int i = 0; i < result.length; i++) {
                    tonghuashunList.get(result[i]).setIs_checked(true);
                    tonghuashunAdapter.setSelectedList(result[i]);
                }
                break;
            case danxuan_baozi:
                result = numberRandom(1, baoziNums);
                for (int i = 0; i < result.length; i++) {
                    baoziList.get(result[i]).setIs_checked(true);
                    baoziAdapter.setSelectedList(result[i]);
                }

                break;
            case danxuan_duizi:
                result = numberRandom(1, duiziNums);
                for (int i = 0; i < result.length; i++) {
                    duiziList.get(result[i]).setIs_checked(true);
                    duiziAdapter.setSelectedList(result[i]);
                }
                break;


                //任选2
            case tuodan_two:
                result = numberRandom(2, tuodan_twonums);
                for (int i = 0; i < result.length; i++) {
                    tuodan_twolist.get(result[i]).setIs_checked(true);
                    twodan_twoadapter.setSelectedList(result[i]);
                }

                break;
            case tuodan_three:
                result = numberRandom(3, tuodan_threenums);
                for (int i = 0; i < result.length; i++) {
                    tuodan_threelist.get(result[i]).setIs_checked(true);
                    tuodan_threeadapter.setSelectedList(result[i]);
                }
                break;
            case tuodan_four:
                result = numberRandom(4, tuodan_fournums);
                for (int i = 0; i < result.length; i++) {
                    tuodan_fourlist.get(result[i]).setIs_checked(true);
                    tuodan_fouradapter.setSelectedList(result[i]);
                }
                break;
            case tuodan_five:
                result = numberRandom(5, tuodan_fivenums);
                for (int i = 0; i < result.length; i++) {
                    tuodan_fivelist.get(result[i]).setIs_checked(true);
                    tuodan_fiveadapter.setSelectedList(result[i]);
                }
                break;

            case tuodan_six:
                result = numberRandom(6, tuodan_sixnums);
                for (int i = 0; i < result.length; i++) {
                    tuodan_sixlist.get(result[i]).setIs_checked(true);
                    tuodan_sixadapter.setSelectedList(result[i]);
                }
                result = numberRandom(6, tuodan_sixnums);
                for (int i = 0; i < result.length; i++) {
                    tuodan_sixlist.get(result[i]).setIs_checked(true);
                    tuodan_sixadapter.setSelectedList(result[i]);
                }





            case putong_one:
                result = numberRandom(1, putong_onenums);
                for (int i = 0; i < result.length; i++) {
                    putong_onelist.get(result[i]).setIs_checked(true);
                    putong_neadapter.setSelectedList(result[i]);
                }
                break;
            case putong_two:
                result = numberRandom(2, putong_twonums);
                for (int i = 0; i < result.length; i++) {
                    putong_twolist.get(result[i]).setIs_checked(true);
                    putong_twoadapter.setSelectedList(result[i]);
                }
                break;
            case putong_three:
                result = numberRandom(3, putong_threenums);
                for (int i = 0; i < result.length; i++) {
                    putongthreelist.get(result[i]).setIs_checked(true);
                    putong_threeadapter.setSelectedList(result[i]);
                }
                break;
            case putong_four:
                result = numberRandom(4, putong_fournums);
                for (int i = 0; i < result.length; i++) {
                    putong_fourlist.get(result[i]).setIs_checked(true);
                    putong_fouradapter.setSelectedList(result[i]);
                }
                break;
            case putong_five:
                result = numberRandom(5, putong_fivenums);
                for (int i = 0; i < result.length; i++) {
                    putong_fivelist.get(result[i]).setIs_checked(true);
                    putong_fiveadapter.setSelectedList(result[i]);
                }
                break;

            case putong_six:
                result = numberRandom(6, putong_sixnums);
                for (int i = 0; i < result.length; i++) {
                    putong_sixlist.get(result[i]).setIs_checked(true);
                    putong_sixadapter.setSelectedList(result[i]);
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
