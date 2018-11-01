package com.daotian.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daotian.Base.App;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.BuyNumBO;
import com.daotian.Model.NumInfo;
import com.daotian.Model.OrderInitBO;
import com.daotian.Model.ResultBO;
import com.daotian.Model.TicketResultListInfo;
import com.daotian.R;
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
 * 投注详情页
 * Created by yzx on 16/11/15.
 */

public class PukeDetailActivity extends AppCompatActivity {
    int count = 0;
    //每注彩票价格
    private final int TicketPrice = 2;
    //普通投注
    private final int S_BAOXUAN = 1;//包选
    private final int danxuan_tonghua = 2;//同花单选
    private final int danxuan_shunzi = 3;//顺子单选
    private final int danxuan_tonghuashun = 4;//同花顺单选
    private final int danxuan_baozi = 5;//豹子单选
    private final int danxuan_duizi = 6;//对子单选

    //拖胆
   // private final int SD_ONE = 7;//任选1拖胆
    private final int tuodan_two = 8;//任选2拖胆
    private final int tuodan_three = 9;//任选3拖胆
    private final int tuodan_four = 10;//任选4拖胆
    private final int tuodan_five = 11;//任选5拖胆
    private final int tuodan_six = 12;//任选6拖胆
    //普通任选
    private final int putong_one = 13;//任选1普通任选
    private final int putong_two = 14;//任选2普通任选
    private final int putong_three = 15;//任选3普通任选
    private final int putong_four = 16;//任选4普通任选
    private final int putong_five = 17;//任选5普通任选
    private final int putong_six = 18;//任选6普通任选


    private final int type_TONGHUA = 21;//同花包选
    private final int type_SHUNZI = 22;//顺子包选
    private final int type_TONGHUASHUN = 23;//同花顺包选
    private final int type_BAOZI = 24;//豹子包选
    private final int type_DUIZI = 25;//对子包选
    @BindView(R.id.zhui)
    LinearLayout zhui;
    @BindView(R.id.zhongstop)
    LinearLayout zhongstop;
    private int mSelecte_Mode = S_BAOXUAN;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.continue_btn)
    TextView continueBtn;
    @BindView(R.id.random_btn)
    TextView randomBtn;
    @BindView(R.id.top_ly)
    LinearLayout topLy;
    @BindView(R.id.ticket_listview)
    ListView ticketListview;
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
    @BindView(R.id.term_num_edit)
    EditText termNumEdit;
    @BindView(R.id.multiple_num_edit)
    EditText multipleNumEdit;
    @BindView(R.id.term_stop_check)
    ImageView termStopCheck;
    private boolean check_term_top = true;

    private List<NumInfo> baoxuanList = new ArrayList<>();
    private List<NumInfo> tonghuaList = new ArrayList<>();
    private List<NumInfo> shunziList = new ArrayList<>();
    private List<NumInfo> tonghuashunList = new ArrayList<>();
    private List<NumInfo> baoziList = new ArrayList<>();
    private List<NumInfo> duiziList = new ArrayList<>();

    //拖胆
    private List<NumInfo> tuodan_RONEList = new ArrayList<>();
    private List<NumInfo> tuodan_twolist = new ArrayList<>();
    private List<NumInfo> tuodan_threelist = new ArrayList<>();
    private List<NumInfo> tuodan_fourlist = new ArrayList<>();
    private List<NumInfo> tuodan_fivelist = new ArrayList<>();
    private List<NumInfo> tuodan_sixlist = new ArrayList<>();

    //
    private List<NumInfo> child_twolist = new ArrayList<>();
    private List<NumInfo> child_threelist = new ArrayList<>();
    private List<NumInfo> child_fourlist = new ArrayList<>();
    private List<NumInfo> child_fivelist = new ArrayList<>();
    private List<NumInfo> child_sixlist = new ArrayList<>();
    //普通
    private List<NumInfo> putong_onelist = new ArrayList<>();
    private List<NumInfo> putong_twolist = new ArrayList<>();
    private List<NumInfo> putongthreelist = new ArrayList<>();
    private List<NumInfo> putong_fourlist = new ArrayList<>();
    private List<NumInfo> putong_fivelist = new ArrayList<>();
    private List<NumInfo> putong_sixlist = new ArrayList<>();


    private TicketAdapter adpater;
    private List<TicketResultListInfo> list_result = new ArrayList<>();
    private Activity mActivity;
    private OrderInitBO mOrderInit;
    private String sh_name, type, now_qs, zq_type,buytype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puke_detail);
        ButterKnife.bind(this);
        mActivity = this;
        Log.e("onCreate！！！！！！！！！",mSelecte_Mode+"");
        initView();
        initIntent();
        getData();
    }
    private void getData() {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.getCenter, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.e("orderinit_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    ToastUtil.toast(mActivity, resultBO.getResultMsg());
                    return;
                }
                mOrderInit = JSON.parseObject(resultBO.getResultData(), OrderInitBO.class);
                if (mOrderInit == null) {
                    multipleNumEdit.setEnabled(false);
                    termNumEdit.setEnabled(false);
                }
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
            }
        });
    }
    private  void init_intent_baoxuan(){
    baoxuanList = (List<NumInfo>) this.getIntent().getSerializableExtra("baoxuanList");
//    num.setText("1");
//    price.setText("2");
    title.setText(this.getIntent().getStringExtra("title"));
    sh_name = this.getIntent().getStringExtra("sh_name");
    type = this.getIntent().getStringExtra("type");
    now_qs = this.getIntent().getStringExtra("now_qs");
    zq_type = this.getIntent().getStringExtra("zq_type");
    buytype = this.getIntent().getStringExtra("buytype");
    switch (zq_type) {
        case "1":

            break;
        case "2":
            zhongstop.setVisibility(View.GONE);
            break;
        case "3":
            zhui.setVisibility(View.GONE);
            zhongstop.setVisibility(View.GONE);
            break;
    }
    for (int i = 0; i < baoxuanList.size(); i++) {
        if (baoxuanList.get(i).is_checked()) {
            TicketResultListInfo info = new TicketResultListInfo();
            info.setNum("1");
            switch (i)
            {
                case 0:
                    info.setMode(type_TONGHUASHUN);
                    break;
                case 1:
                    info.setMode(type_TONGHUA);
                    break;
                case 2:
                    info.setMode(type_DUIZI);
                    break;
                case 3:
                    info.setMode(type_BAOZI);
                    break;
                case 4:
                    info.setMode(type_SHUNZI);
                    break;
            }

            info.setPrice("2");
            info.getNumbers1().add(baoxuanList.get(i));
            list_result.add(info);
        }
    }

        adpater.notifyDataSetChanged();
        calcluateNumAndPrice();
}
    private  void init_inten_else()
    {
        //Log.e("mSelecte_Mode！！！！！！！！！",mSelecte_Mode+"");
        //baoxuanList = (List<NumInfo>) this.getIntent().getSerializableExtra("baoxuanList");
        tonghuaList = (List<NumInfo>) this.getIntent().getSerializableExtra("tonghuaList");
        shunziList = (List<NumInfo>) this.getIntent().getSerializableExtra("shunziList");
        tonghuashunList = (List<NumInfo>) this.getIntent().getSerializableExtra("tonghuashunList");
        baoziList = (List<NumInfo>) this.getIntent().getSerializableExtra("baoziList");
        duiziList = (List<NumInfo>) this.getIntent().getSerializableExtra("duiziList");


        //up    普通
        putong_onelist = (List<NumInfo>) this.getIntent().getSerializableExtra("putong_onelist");
        putong_twolist = (List<NumInfo>) this.getIntent().getSerializableExtra("putong_twolist");
        putongthreelist = (List<NumInfo>) this.getIntent().getSerializableExtra("putongthreelist");
        putong_fourlist = (List<NumInfo>) this.getIntent().getSerializableExtra("putong_fourlist");
        putong_fivelist = (List<NumInfo>) this.getIntent().getSerializableExtra("putong_fivelist");
        putong_sixlist = (List<NumInfo>) this.getIntent().getSerializableExtra("putong_sixlist");

        //down 拖胆
        tuodan_RONEList = (List<NumInfo>) this.getIntent().getSerializableExtra("tuodan_RONEList");
        tuodan_twolist = (List<NumInfo>) this.getIntent().getSerializableExtra("tuodan_twolist");
        tuodan_threelist = (List<NumInfo>) this.getIntent().getSerializableExtra("tuodan_threelist");
        tuodan_fourlist = (List<NumInfo>) this.getIntent().getSerializableExtra("tuodan_fourlist");
        tuodan_fivelist = (List<NumInfo>) this.getIntent().getSerializableExtra("tuodan_fivelist");
        tuodan_sixlist = (List<NumInfo>) this.getIntent().getSerializableExtra("tuodan_sixlist");
        //拖胆的下面

        child_twolist = (List<NumInfo>) this.getIntent().getSerializableExtra("child_twolist");
        child_threelist = (List<NumInfo>) this.getIntent().getSerializableExtra("child_threelist");
        child_fourlist = (List<NumInfo>) this.getIntent().getSerializableExtra("child_fourlist");
        child_fivelist = (List<NumInfo>) this.getIntent().getSerializableExtra("child_fivelist");
        child_sixlist = (List<NumInfo>) this.getIntent().getSerializableExtra("child_sixlist");






        num.setText(this.getIntent().getStringExtra("num"));
        price.setText(this.getIntent().getStringExtra("price"));
        title.setText(this.getIntent().getStringExtra("title"));
        sh_name = this.getIntent().getStringExtra("sh_name");
        type = this.getIntent().getStringExtra("type");
        now_qs = this.getIntent().getStringExtra("now_qs");
        zq_type = this.getIntent().getStringExtra("zq_type");
        buytype = this.getIntent().getStringExtra("buytype");
        switch (zq_type) {
            case "1":

                break;
            case "2":
                zhongstop.setVisibility(View.GONE);
                break;
            case "3":
                zhui.setVisibility(View.GONE);
                zhongstop.setVisibility(View.GONE);
                break;
        }
        TicketResultListInfo info = new TicketResultListInfo();
        info.setMode(mSelecte_Mode);
        info.setNum(num.getText().toString());
        info.setPrice(price.getText().toString());

        switch (mSelecte_Mode) {
//                case S_BAOXUAN:
//                    for (int i = 0; i < baoxuanList.size(); i++) {
//                        if (baoxuanList.get(i).is_checked()) {
//                            info.getNumbers1().add(baoxuanList.get(i));
//                        }
//                    }
//                    break;
            case danxuan_tonghua:
                for (int i = 0; i < tonghuaList.size(); i++) {
                    if (tonghuaList.get(i).is_checked()) {
                        info.getNumbers1().add(tonghuaList.get(i));
                    }
                }
                break;
            case danxuan_shunzi:
                for (int i = 0; i < shunziList.size(); i++) {
                    if (shunziList.get(i).is_checked()) {
                        info.getNumbers1().add(shunziList.get(i));
                    }
                }

                break;
            case danxuan_tonghuashun:
                for (int i = 0; i < tonghuashunList.size(); i++) {
                    if (tonghuashunList.get(i).is_checked()) {
                        info.getNumbers1().add(tonghuashunList.get(i));
                    }
                }
                break;
            case danxuan_baozi:
                for (int i = 0; i < baoziList.size(); i++) {
                    if (baoziList.get(i).is_checked()) {
                        info.getNumbers1().add(baoziList.get(i));
                    }
                }
                break;
            case danxuan_duizi:
                for (int i = 0; i < duiziList.size(); i++) {
                    if (duiziList.get(i).is_checked()) {
                        info.getNumbers1().add(duiziList.get(i));
                    }
                }

                break;
//                case SD_ONE:
//                    for (int i = 0; i < tuodan_RONEList.size(); i++) {
//                        if (tuodan_RONEList.get(i).is_checked()) {
//                            info.getNumbers1().add(tuodan_RONEList.get(i));
//                        }
//                    }
//                    break;
            case tuodan_two:
                for (int i = 0; i < tuodan_twolist.size(); i++) {
                    if (tuodan_twolist.get(i).is_checked()) {
                        info.getNumbers1().add(tuodan_twolist.get(i));
                    }
                }
                break;
            case tuodan_three:
                for (int i = 0; i < tuodan_threelist.size(); i++) {
                    if (tuodan_threelist.get(i).is_checked()) {
                        info.getNumbers1().add(tuodan_threelist.get(i));
                    }
                }
                break;
            case tuodan_four:
                for (int i = 0; i < tuodan_fourlist.size(); i++) {
                    if (tuodan_fourlist.get(i).is_checked()) {
                        info.getNumbers1().add(tuodan_fourlist.get(i));
                    }
                }
                break;
            case tuodan_five:
                for (int i = 0; i < tuodan_fivelist.size(); i++) {
                    if (tuodan_fivelist.get(i).is_checked()) {
                        info.getNumbers1().add(tuodan_fivelist.get(i));
                    }
                }
                break;
            case tuodan_six:
                for (int i = 0; i < tuodan_sixlist.size(); i++) {
                    if (tuodan_sixlist.get(i).is_checked()) {
                        info.getNumbers1().add(tuodan_sixlist.get(i));
                    }
                }
                break;

            //up
            case putong_one:
                for (int i = 0; i < putong_onelist.size(); i++) {
                    if (putong_onelist.get(i).is_checked()) {
                        info.getNumbers1().add(putong_onelist.get(i));
                    }
                }
                break;
            case putong_two:
                for (int i = 0; i < putong_twolist.size(); i++) {
                    if (putong_twolist.get(i).is_checked()) {
                        info.getNumbers1().add(putong_twolist.get(i));
                    }
                }
                break;
            case putong_three:
                for (int i = 0; i < putongthreelist.size(); i++) {
                    if (putongthreelist.get(i).is_checked()) {
                        info.getNumbers1().add(putongthreelist.get(i));
                    }
                }
                break;
            case putong_four:
                for (int i = 0; i < putong_fourlist.size(); i++) {
                    if (putong_fourlist.get(i).is_checked()) {
                        info.getNumbers1().add(putong_fourlist.get(i));
                    }
                }
                break;
            case putong_five:
                for (int i = 0; i < putong_fivelist.size(); i++) {
                    if (putong_fivelist.get(i).is_checked()) {
                        info.getNumbers1().add(putong_fivelist.get(i));
                    }
                }
                break;
            case putong_six:
                for (int i = 0; i < putong_sixlist.size(); i++) {
                    if (putong_sixlist.get(i).is_checked()) {
                        info.getNumbers1().add(putong_sixlist.get(i));
                    }
                }
                break;
        }

        list_result.add(info);

    }
    private void initIntent() {

        mSelecte_Mode = this.getIntent().getIntExtra("selecte_mode", -1);
        if (mSelecte_Mode == -1) {
            ToastUtil.toast(mActivity, "模式错误");
            return;
        }
        //如果是包选
        if (mSelecte_Mode==S_BAOXUAN)
        {
            init_intent_baoxuan();
        }
        //如果是其他玩法
       else {
            init_inten_else();
        }
    }
    private void initView() {

        adpater = new TicketAdapter(this, R.layout.list_item_ticket_result, list_result);
        ticketListview.setAdapter(adpater);

        multipleNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mOrderInit.getMax_bs() == null) {
                    return;
                }
                if (TextUtils.isEmpty(price.getText().toString()) || TextUtils.isEmpty(num.getText().toString())) {
                    return;
                }
                if (TextUtils.isEmpty(termNumEdit.getText().toString())) {
                    termNumEdit.setText("1");
                }
                if (TextUtils.isEmpty(multipleNumEdit.getText().toString())) {
                    multipleNumEdit.setHint("1");
                    price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(termNumEdit.getText().toString()) * TicketPrice * 1 + "");
                } else if (Integer.valueOf(multipleNumEdit.getText().toString()) < 1) {
                    ToastUtil.toast(PukeDetailActivity.this, "最小倍数为1");
                    multipleNumEdit.setText("1");
                    price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(termNumEdit.getText().toString()) * TicketPrice * 1 + "");
                } else if (!mOrderInit.getMax_bs().equals("0") && Integer.valueOf(multipleNumEdit.getText().toString()) > Integer.valueOf(mOrderInit.getMax_bs())) {
                    ToastUtil.toast(PukeDetailActivity.this, "最大倍数为" + mOrderInit.getMax_bs());
                    multipleNumEdit.setText(mOrderInit.getMax_bs());
                    price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(termNumEdit.getText().toString()) * Integer.valueOf(multipleNumEdit.getText().toString()) * TicketPrice + "");
                } else {
                    price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(termNumEdit.getText().toString()) * Integer.valueOf(multipleNumEdit.getText().toString()) * TicketPrice + "");
                }
                multipleNumEdit.setSelection(multipleNumEdit.length());
            }
        });
        termNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mOrderInit.getMax_zq() == null) {
                    return;
                }
                if (TextUtils.isEmpty(price.getText().toString()) || TextUtils.isEmpty(num.getText().toString())) {
                    return;
                }
                if (TextUtils.isEmpty(multipleNumEdit.getText().toString())) {
                    multipleNumEdit.setText("1");
                }
                if (TextUtils.isEmpty(termNumEdit.getText().toString())) {
                    termNumEdit.setHint("1");
                    price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(multipleNumEdit.getText().toString()) * 1 * TicketPrice + "");
                } else if (Integer.valueOf(termNumEdit.getText().toString()) < 1) {
                    ToastUtil.toast(PukeDetailActivity.this, "最少需要追1期");
                    termNumEdit.setText("1");
                    price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(multipleNumEdit.getText().toString()) * 1 * TicketPrice + "");
                } else if (Integer.valueOf(termNumEdit.getText().toString()) > Integer.valueOf(mOrderInit.getMax_zq())) {
                    ToastUtil.toast(PukeDetailActivity.this, "最多可追" + mOrderInit.getMax_zq() + "期");
                    termNumEdit.setText(mOrderInit.getMax_zq());
                    price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(multipleNumEdit.getText().toString()) * Integer.valueOf(mOrderInit.getMax_zq()) * TicketPrice + "");
                } else if (Integer.valueOf(termNumEdit.getText().toString()) < 1 || TextUtils.isEmpty(termNumEdit.getText().toString())) {
                    ToastUtil.toast(PukeDetailActivity.this, "最少需要追1期");
                    termNumEdit.setText("1");
                    price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(multipleNumEdit.getText().toString()) * 1 * TicketPrice + "");
                } else {
                    price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(termNumEdit.getText().toString()) * Integer.valueOf(multipleNumEdit.getText().toString()) * TicketPrice + "");
                }
                termNumEdit.setSelection(termNumEdit.length());

            }
        });
    }
    @OnClick({R.id.back, R.id.continue_btn, R.id.random_btn, R.id.clear_btn, R.id.comfirm_btn, R.id.term_stop_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                showBackPop();
                break;
            case R.id.continue_btn:
                Intent in = new Intent(mActivity, PukeActivity.class);
                in.putExtra("sh_name", sh_name);
                in.putExtra("continue",true);
                startActivityForResult(in, 0);
                //startActivity(in);
                break;
            //机选
            case R.id.random_btn:
                getRandow();
                break;
            //情况
            case R.id.clear_btn:
                showCanclePop();
                break;
            //下注
            case R.id.comfirm_btn:
                comfirmBtn.setClickable(false);
                comfirmBtn.setBackgroundResource(R.drawable.btn_bg_grey_fill);
                BuyComfirm();
                break;
            case R.id.term_stop_check:
                if (check_term_top) {
                    termStopCheck.setImageResource(R.drawable.icon_shape_unselete);
                    check_term_top = false;
                } else {
                    termStopCheck.setImageResource(R.drawable.icon_shape_seleted);
                    check_term_top = true;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //继续选号
        if (requestCode == 0 && resultCode == RESULT_OK) {
            mSelecte_Mode = data.getIntExtra("selecte_mode", -1);
            if (mSelecte_Mode == -1) {
                ToastUtil.toast(mActivity, "模式错误");
                return;
            }
            //继续按号---如果是包选模式
            if (mSelecte_Mode == S_BAOXUAN) {
                baoxuanList = (List<NumInfo>) data.getSerializableExtra("baoxuanList");
                sh_name = this.getIntent().getStringExtra("sh_name");

                //如果被点击，添加到数组
                for (int i = 0; i < baoxuanList.size(); i++)
                {
                  if (baoxuanList.get(i).is_checked())
                {
                    TicketResultListInfo info = new TicketResultListInfo();
                    baoxuanList.get(i).setIs_spe(true);
                    switch (i)
                    {
                        case 0:
                            info.setMode(type_TONGHUASHUN);
                            break;
                        case 1:
                            info.setMode(type_TONGHUA);
                            break;
                        case 2:
                            info.setMode(type_DUIZI);
                            break;
                        case 3:
                            info.setMode(type_BAOZI);
                            break;
                        case 4:
                            info.setMode(type_SHUNZI);
                            break;
                    }
                    info.setNum("1");
                    info.setPrice("2");
                    info.getNumbers1().add(baoxuanList.get(i));
                    list_result.add(info);
                }
                }
                adpater.notifyDataSetChanged();
                calcluateNumAndPrice();
            }
            //如果是其他模式
            else {

                tonghuaList = (List<NumInfo>) data.getSerializableExtra("tonghuaList");
                shunziList = (List<NumInfo>) data.getSerializableExtra("shunziList");
                tonghuashunList = (List<NumInfo>) data.getSerializableExtra("tonghuashunList");
                baoziList = (List<NumInfo>) data.getSerializableExtra("baoziList");
                duiziList = (List<NumInfo>) data.getSerializableExtra("duiziList");
                tuodan_RONEList = (List<NumInfo>) data.getSerializableExtra("tuodan_RONEList");
                tuodan_twolist = (List<NumInfo>) data.getSerializableExtra("tuodan_twolist");
                tuodan_threelist = (List<NumInfo>) data.getSerializableExtra("tuodan_threelist");
                tuodan_fourlist = (List<NumInfo>) data.getSerializableExtra("tuodan_fourlist");
                tuodan_fivelist = (List<NumInfo>) data.getSerializableExtra("tuodan_fivelist");
                tuodan_sixlist = (List<NumInfo>) data.getSerializableExtra("tuodan_sixlist");
                putong_onelist = (List<NumInfo>) data.getSerializableExtra("putong_onelist");
                putong_twolist = (List<NumInfo>) data.getSerializableExtra("putong_twolist");
                putongthreelist = (List<NumInfo>) data.getSerializableExtra("putongthreelist");
                putong_fourlist = (List<NumInfo>) data.getSerializableExtra("putong_fourlist");
                putong_fivelist = (List<NumInfo>) data.getSerializableExtra("putong_fivelist");
                putong_sixlist = (List<NumInfo>) data.getSerializableExtra("putong_sixlist");
                child_twolist = (List<NumInfo>)  data.getSerializableExtra("child_twolist");
                child_threelist = (List<NumInfo>)  data.getSerializableExtra("child_threelist");
                child_fourlist = (List<NumInfo>) data.getSerializableExtra("child_fourlist");
                child_fivelist = (List<NumInfo>) data.getSerializableExtra("child_fivelist");
                child_sixlist = (List<NumInfo>)  data.getSerializableExtra("child_sixlist");

                sh_name = this.getIntent().getStringExtra("sh_name");
                TicketResultListInfo info = new TicketResultListInfo();
                info.setMode(data.getIntExtra("selecte_mode", 1));
                info.setNum(data.getStringExtra("num"));
                info.setPrice(data.getStringExtra("price"));
                switch (mSelecte_Mode) {
                    case danxuan_tonghua:
                        for (int i = 0; i < 4; i++) {
                            if (tonghuaList.get(i).is_checked()) {
                                info.getNumbers1().add(tonghuaList.get(i));
                            }
                        }
                        break;
                    case danxuan_shunzi:
                        for (int i = 0; i <12; i++) {
                            if (shunziList.get(i).is_checked()) {
                                info.getNumbers1().add(shunziList.get(i));
                            }
                        }

                        break;
                    case danxuan_tonghuashun:
                        for (int i = 0; i < 4; i++) {
                            if (tonghuashunList.get(i).is_checked()) {
                                info.getNumbers1().add(tonghuashunList.get(i));
                            }
                        }
                        break;
                    case danxuan_baozi:
                        for (int i = 0; i < 13; i++) {
                            if (baoziList.get(i).is_checked()) {
                                info.getNumbers1().add(baoziList.get(i));
                            }
                        }
                        break;
                    case danxuan_duizi:
                        for (int i = 0; i < 13; i++) {
                            if (duiziList.get(i).is_checked()) {
                                info.getNumbers1().add(duiziList.get(i));
                            }
                        }
                        break;

                    case tuodan_two:
                        for (int i = 0; i < 13; i++) {
                            if (tuodan_twolist.get(i).is_checked()) {
                                tuodan_twolist.get(i).setIs_spe(true);
                                info.getNumbers1().add(tuodan_twolist.get(i));
                            }
                        }
                        for (int i = 0; i < 13; i++) {
                            if (child_twolist.get(i).is_checked()) {
                                child_twolist.get(i).setIs_spe(true);
                                info.getNumbers2().add(child_twolist.get(i));
                            }
                        }
                        break;
                    case tuodan_three:
                        for (int i = 0; i < 13; i++) {
                            if (tuodan_threelist.get(i).is_checked()) {
                                tuodan_threelist.get(i).setIs_spe(true);
                                info.getNumbers1().add(tuodan_threelist.get(i));
                            }
                        }
                        for (int i = 0; i < 13; i++) {
                            if (child_threelist.get(i).is_checked()) {
                                child_threelist.get(i).setIs_spe(true);
                                info.getNumbers2().add(child_threelist.get(i));
                            }
                        }
                        break;
                    case tuodan_four:
                        for (int i = 0; i < 13; i++) {
                            if (tuodan_fourlist.get(i).is_checked()) {
                                tuodan_fourlist.get(i).setIs_spe(true);
                                info.getNumbers1().add(tuodan_fourlist.get(i));
                            }
                        }
                        for (int i = 0; i < 13; i++) {
                            if (child_fourlist.get(i).is_checked()) {
                                child_fourlist.get(i).setIs_spe(true);
                                info.getNumbers2().add(child_fourlist.get(i));
                            }
                        }
                        break;
                    case tuodan_five:
                        for (int i = 0; i < 13; i++) {
                            if (tuodan_fivelist.get(i).is_checked()) {
                                tuodan_fivelist.get(i).setIs_spe(true);
                                info.getNumbers1().add(tuodan_fivelist.get(i));
                            }
                        }
                        for (int i = 0; i < 13; i++) {
                            if (child_fivelist.get(i).is_checked()) {
                                child_fivelist.get(i).setIs_spe(true);
                                info.getNumbers2().add(child_fivelist.get(i));
                            }
                        }
                        break;
                    case tuodan_six:
                        for (int i = 0; i <13; i++) {
                            if (tuodan_sixlist.get(i).is_checked()) {
                                tuodan_sixlist.get(i).setIs_spe(true);
                                info.getNumbers1().add(tuodan_sixlist.get(i));
                            }
                        }
                        for (int i = 0; i < 13; i++) {
                            if (child_sixlist.get(i).is_checked()) {
                                child_sixlist.get(i).setIs_spe(true);
                                info.getNumbers2().add(child_sixlist.get(i));
                            }
                        }
                        //up
                    case putong_one:
                        for (int i = 0; i < 13; i++) {
                            if (putong_onelist.get(i).is_checked()) {
                                putong_onelist.get(i).setIs_spe(true);
                                info.getNumbers1().add(putong_onelist.get(i));
                            }
                        }

                        break;
                    case putong_two:
                        for (int i = 0; i <13; i++) {
                            if (putong_twolist.get(i).is_checked()) {
                                putong_twolist.get(i).setIs_spe(true);
                                info.getNumbers1().add(putong_twolist.get(i));
                            }
                        }

                        break;
                    case putong_three:
                        for (int i = 0; i < 13; i++) {
                            if (putongthreelist.get(i).is_checked()) {
                                putongthreelist.get(i).setIs_spe(true);
                                info.getNumbers1().add(putongthreelist.get(i));
                            }
                        }

                        break;
                    case putong_four:
                        for (int i = 0; i <13; i++) {
                            if (putong_fourlist.get(i).is_checked()) {
                                putong_fourlist.get(i).setIs_spe(true);
                                info.getNumbers1().add(putong_fourlist.get(i));
                            }
                        }

                        break;
                    case putong_five:
                        for (int i = 0; i < 13; i++) {
                            if (putong_fivelist.get(i).is_checked()) {
                                putong_fivelist.get(i).setIs_spe(true);
                                info.getNumbers1().add(putong_fivelist.get(i));
                            }
                        }

                        break;
                    case putong_six:
                        for (int i = 0; i < 13; i++) {
                            if (putong_sixlist.get(i).is_checked()) {
                                putong_sixlist.get(i).setIs_spe(true);
                                info.getNumbers1().add(putong_sixlist.get(i));
                            }
                        }

                        break;
                }
                list_result.add(info);
                calcluateNumAndPrice();
                adpater.notifyDataSetChanged();
            }
        }
    }
    private void BuyComfirm() {
        if (App.mUser == null || App.mUser.getAccess_token() == null) {
            ToastUtil.toast(mActivity, "请先登录！");
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
            return;
        }
//        if(Double.valueOf(App.mUser.getBalance())<Double.valueOf(price.getText().toString())){
//            ToastUtil.toast(mActivity,"金额不足，请先充值！");
//            return;
//        }
        if (list_result.size() < 1) {
            ToastUtil.toast(mActivity, "请先购买号码！");
            return;
        }
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sh_name", sh_name);
        paramMap.put("lottery_type", "4");
        paramMap.put("total_fee", price.getText().toString());//!!!!!!!!!!!!!!!!!!!!
        Log.e("total_fee", price.getText().toString());
        paramMap.put("log_num", now_qs);
        if (TextUtils.isEmpty(termNumEdit.getText().toString())) {
            paramMap.put("sery_num", "1");
            //Log.e("sery_num", "1");
        } else {
            paramMap.put("sery_num", termNumEdit.getText().toString());
           // Log.e("sery_num", termNumEdit.getText().toString());
        }
        if (TextUtils.isEmpty(multipleNumEdit.getText().toString())) {
            paramMap.put("buy_bs", "1");
            //Log.e("buy_bs", "1");
        } else {
            paramMap.put("buy_bs", multipleNumEdit.getText().toString());
           // Log.e("buy_bs", multipleNumEdit.getText().toString());
        }

        switch (zq_type) {
            case "1":
                paramMap.put("is_sery_stop", "1");
                break;
            case "2":
                paramMap.put("is_sery_stop", "0");
                zhongstop.setVisibility(View.GONE);
                break;
            case "3":
                paramMap.put("is_sery_stop", "0");
                zhui.setVisibility(View.GONE);
                zhongstop.setVisibility(View.GONE);
                break;


        }
        paramMap.put("buy_array", setbuyArray());
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.buyOrder, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.e("init_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                ToastUtil.toast(mActivity, resultBO.getResultMsg());
                if (resultBO.getResultId() != 0) {
                    comfirmBtn.setClickable(true);
                    comfirmBtn.setBackgroundResource(R.drawable.btn_bg_red_fill);
                    return;
                }
                Intent in = new Intent(mActivity, PukeActivity.class);
                in.putExtra("sh_name", sh_name);
                startActivity(in);
                finish();
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
                comfirmBtn.setClickable(true);
                comfirmBtn.setBackgroundResource(R.drawable.btn_bg_red_fill);
            }
        });

    }
    private String setbuyArray() {
        List<BuyNumBO> buylist = new ArrayList<>();
        for (int i = 0; i < list_result.size(); i++) {
            BuyNumBO info = new BuyNumBO();
            info.setType(list_result.get(i).getMode()+"");
            String s1 = "";
            for (int j = 0; j < list_result.get(i).getNumbers1().size(); j++) {
                if (j == 0) {
                    s1 = s1 + list_result.get(i).getNumbers1().get(j).getUpload_data();
                } else {
                    s1 = s1 + "," + list_result.get(i).getNumbers1().get(j).getUpload_data();
                }
            }
            info.setS1(s1);
            if (list_result.get(i).getNumbers2().size() > 0) {
                String s2 = "";
                for (int j = 0; j < list_result.get(i).getNumbers2().size(); j++) {
                    if (j == 0) {
                        s2 = s2 + list_result.get(i).getNumbers2().get(j).getUpload_data();
                    } else {
                        s2 = s2 + "," + list_result.get(i).getNumbers2().get(j).getUpload_data();
                    }
                }
                info.setS2(s2);
            }
            buylist.add(info);
        }
        Log.e("buy_arry", JSON.toJSONString(buylist));
        return JSON.toJSONString(buylist);
    }
    private void clearAll() {
        list_result.clear();
        num.setText("0");
        price.setText("0");
        termNumEdit.setText("1");
        multipleNumEdit.setText("1");
        adpater.notifyDataSetChanged();
    }
    public class TicketAdapter extends CommonAdapter<TicketResultListInfo> {

        public TicketAdapter(Context context, int layoutId, List<TicketResultListInfo> datas) {
            super(context, layoutId, datas);

        }

        @Override
        public void convert(ViewHolder holder, final TicketResultListInfo info) {
            List<NumInfo> nums = new ArrayList<>();
            //Log.e("mode", String.valueOf(mSelecte_Mode));
           // Log.e("size", String.valueOf(info.getNumbers1().size()));
            for (int i = 0; i < info.getNumbers1().size(); i++) {
                nums.add(info.getNumbers1().get(i));
            }
            Log.e("num", String.valueOf(nums.size()));
            final TagFlowLayout numTag = holder.getView(R.id.num_tag);
            final TagAdapter<NumInfo> numAdapter = new TagAdapter<NumInfo>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, NumInfo s) {
                    TextView tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                    switch (info.getMode()) {
                        case S_BAOXUAN:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                            tv.setBackgroundResource(R.drawable.baozi_temp);
                            break;
                        case danxuan_tonghua:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_tonghua, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case danxuan_shunzi:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_three, numTag, false);
                            tv.setBackgroundResource(R.drawable.shunzi_empty);
                            break;
                        case danxuan_tonghuashun:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_tonghua, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case danxuan_baozi:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_three, numTag, false);
                            tv.setBackgroundResource(R.drawable.shunzi_empty);
                            break;
                        case danxuan_duizi:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_two, numTag, false);
                            tv.setBackgroundResource(R.drawable.duizi_empty);
                            break;
//                        case SD_ONE:
//                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
//                            tv.setBackgroundResource(R.drawable.puke_empty);
//                            break;
                        case tuodan_two:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case tuodan_three:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case tuodan_four:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case tuodan_five:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case tuodan_six:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        //up
                        case putong_one:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case putong_two:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case putong_three:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case putong_four:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case putong_five:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                        case putong_six:
                            tv = (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_single, numTag, false);
                            tv.setBackgroundResource(R.drawable.puke_empty);
                            break;
                    }


                    tv.setText(s.getNum());

                    return tv;

                }
            };
            numTag.setAdapter(numAdapter);
            holder.setText(R.id.ticket_num, info.getNum() + "注");
            holder.setText(R.id.ticket_price, info.getPrice() + "元");
            TextView mode_tv = holder.getView(R.id.ticket_mode);
            switch (info.getMode()) {
                case S_BAOXUAN:
                    mode_tv.setText("包选");
                    break;
                case danxuan_tonghua:
                    mode_tv.setText("同花");
                    break;
                case danxuan_shunzi:
                    mode_tv.setText("顺子");
                    break;
                case danxuan_tonghuashun:
                    mode_tv.setText("同花顺");
                    break;
                case danxuan_baozi:
                    mode_tv.setText("豹子");
                    break;
                case danxuan_duizi:
                    mode_tv.setText("对子");
                    break;
//                case SD_ONE:
//                    mode_tv.setText("任选一");
//                    break;
                case tuodan_two:
                    mode_tv.setText("任选二");
                    break;
                case tuodan_three:
                    mode_tv.setText("任选三");
                    break;
                case tuodan_four:
                    mode_tv.setText("任选四");
                    break;
                case tuodan_five:
                    mode_tv.setText("任选五");
                    break;
                case tuodan_six:
                    mode_tv.setText("任选六");
                    break;
                //up
                case putong_one:
                    mode_tv.setText("任选一");
                    break;
                case putong_two:
                    mode_tv.setText("任选二");
                    break;
                case putong_three:
                    mode_tv.setText("任选三");
                    break;
                case putong_four:
                    mode_tv.setText("任选四");
                    break;
                case putong_five:
                    mode_tv.setText("任选五");
                    break;
                case putong_six:
                    mode_tv.setText("任选六");
                    break;
                case type_BAOZI:
                    mode_tv.setText("豹子包选");
                    break;
                case type_SHUNZI:
                    mode_tv.setText("顺子包选");
                    break;
                case type_DUIZI:
                    mode_tv.setText("对子包选");
                    break;
                case type_TONGHUA:
                    mode_tv.setText("同花包选");
                    break;
                case type_TONGHUASHUN:
                    mode_tv.setText("同花顺包选");
                    break;

            }

            ImageView delete = holder.getView(R.id.ticket_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list_result.remove(info);
                    notifyDataSetChanged();
                    calcluateNumAndPrice();
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
                clearAll();
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
    public void showCanclePop() {
        // 一个自定义的布局，作为显示的内容
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_close, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView comfirm = (TextView) view.findViewById(R.id.comfirm);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("您是否确定清空数据？");
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
                clearAll();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setTouchable(true);
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
    private void getRandow() {
        int[] result = null;
        int[] result2 = null;

        List<NumInfo> result_list1 = new ArrayList<>();
        List<NumInfo> result_list2 = new ArrayList<>();
        List<String> random_list = new ArrayList<>();
        List<String> random_list2 = new ArrayList<>();

        switch (mSelecte_Mode) {
            case S_BAOXUAN:
                for (int i = 0; i < 5; i++) {
                    random_list.add(""+i);

                }
                result = numberRandom(1, random_list);

                NumInfo info1 = new NumInfo();
                switch (result[0]) {
                    case 0:
                        info1.setNum("同花包选");
                        break;
                    case 1:
                        info1.setNum("顺子包选");
                        break;
                    case 2:
                        info1.setNum("同花顺包选");
                        break;
                    case 3:
                        info1.setNum("对子包选");
                        break;
                    case 4:
                        info1.setNum("豹子包选");
                        break;
                }
                result_list1.add(info1);
                break;
            case danxuan_tonghua:
                for (int i = 0; i < 4; i++) {
                    random_list.add(i +"");
                }
                result = numberRandom(1, random_list);

                    NumInfo info2 = new NumInfo();
                    switch (result[0]) {
                        case 0:
                            info2.setNum("♠");
                            break;
                        case 1:
                            info2.setNum("♥");
                            break;
                        case 2:
                            info2.setNum("♣");
                            break;
                        case 3:
                            info2.setNum("♦");
                            break;
                    }

                    result_list1.add(info2);

                break;
            case danxuan_shunzi:
                for (int i = 0; i < 12; i++) {
                    random_list.add( i +"");
                }
                result = numberRandom(1, random_list);

                    NumInfo info3 = new NumInfo();
                    switch (result[0]) {
                        case 0:
                            info3.setNum("A  2  3");
                            break;
                        case 1:
                            info3.setNum("2  3  4");
                            break;
                        case 2:
                            info3.setNum("3  4  5");
                            break;
                        case 3:
                            info3.setNum("4  5  6");
                            break;
                        case 4:
                            info3.setNum("5  6  7");
                            break;
                        case 5:
                            info3.setNum("6  7  8");
                            break;
                        case 6:
                            info3.setNum("7  8  9");
                            break;
                        case 7:
                            info3.setNum("8  9  10");
                            break;
                        case 8:
                            info3.setNum("9  10  J");
                            break;
                        case 9:
                            info3.setNum("10 J  Q");
                            break;
                        case 10:
                            info3.setNum("J  Q  K");
                            break;
                        case 11:
                            info3.setNum("Q  K  A");
                            break;


                    }
                    result_list1.add(info3);

                break;
            case danxuan_tonghuashun:
                for (int i = 0; i < 4; i++) {
                    random_list.add(i +"");
                }
                result = numberRandom(1, random_list);

                    NumInfo tonghua_info = new NumInfo();
                    switch (result[0]) {
                        case 0:
                            tonghua_info.setNum("♠");
                            break;
                        case 1:
                            tonghua_info.setNum("♥");
                            break;
                        case 2:
                            tonghua_info.setNum("♣");
                            break;
                        case 3:
                            tonghua_info.setNum("♦");
                            break;
                    }
                    result_list1.add(tonghua_info);

                break;
            case danxuan_baozi:

                for (int i = 0; i < 13; i++) {
                    random_list.add(i +"");
                }
                result = numberRandom(1, random_list);
                    NumInfo baozi_info = new NumInfo();
                    switch (result[0]) {
                        case 0:
                            baozi_info.setNum("A  A  A");
                            break;
                        case 1:
                            baozi_info.setNum("2  2  2");
                            break;
                        case 2:
                            baozi_info.setNum("3  3  3");
                            break;
                        case 3:
                            baozi_info.setNum("4  4  4");
                            break;
                        case 4:
                            baozi_info.setNum("5  5  5");
                            break;
                        case 5:
                            baozi_info.setNum("6  6  6");
                            break;
                        case 6:
                            baozi_info.setNum("7  7  7");
                            break;
                        case 7:
                            baozi_info.setNum("8  8  8");
                            break;
                        case 8:
                            baozi_info.setNum("9  9  9");
                            break;
                        case 9:
                            baozi_info.setNum("10 10 10");
                            break;
                        case 10:
                            baozi_info.setNum("J  J  J");
                            break;
                        case 11:
                            baozi_info.setNum("Q  Q  Q");
                            break;
                        case 12:
                            baozi_info.setNum("K  K  K");
                            break;
                    }
                    result_list1.add(baozi_info);

                break;
            case danxuan_duizi:

                for (int i = 0; i < 13; i++) {
                    random_list.add(i +"");
                }
                result = numberRandom(1, random_list);

                    NumInfo duizi_info = new NumInfo();
                    switch (result[0]) {
                        case 0:
                            duizi_info.setNum("A A");
                            break;
                        case 1:
                            duizi_info.setNum("2 2");
                            break;
                        case 2:
                            duizi_info.setNum("3 3");
                            break;
                        case 3:
                            duizi_info.setNum("4 4");
                            break;
                        case 4:
                            duizi_info.setNum("5 5");
                            break;
                        case 5:
                            duizi_info.setNum("6 6");
                            break;
                        case 6:
                            duizi_info.setNum("7 7");
                            break;
                        case 7:
                            duizi_info.setNum("8 8");
                            break;
                        case 8:
                            duizi_info.setNum("9 9");
                            break;
                        case 9:
                            duizi_info.setNum("10 10");
                            break;
                        case 10:
                            duizi_info.setNum("J J");
                            break;
                        case 11:
                            duizi_info.setNum("Q Q");
                            break;
                        case 12:
                            duizi_info.setNum("K K");
                            break;

                    }

                    result_list1.add(duizi_info);


                break;
                    //普通任选模式
//            case SD_ONE:
//                for (int i = 0; i < 13; i++) {
//                    random_list.add(i +"");
//                }
//                result = numberRandom(1, random_list);
//
//                    NumInfo putong_one_info = new NumInfo();
//                    switch (result[0]) {
//                        case 0:
//                            putong_one_info.setNum("A");
//                            break;
//                        case 1:
//                            putong_one_info.setNum("2");
//                            break;
//                        case 2:
//                            putong_one_info.setNum("3");
//                            break;
//                        case 3:
//                            putong_one_info.setNum("4");
//                            break;
//                        case 4:
//                            putong_one_info.setNum("5");
//                            break;
//                        case 5:
//                            putong_one_info.setNum("6");
//                            break;
//                        case 6:
//                            putong_one_info.setNum("7");
//                            break;
//                        case 7:
//                            putong_one_info.setNum("8");
//                            break;
//                        case 8:
//                            putong_one_info.setNum("9");
//                            break;
//                        case 9:
//                            putong_one_info.setNum("10");
//                            break;
//                        case 10:
//                            putong_one_info.setNum("J");
//                            break;
//                        case 11:
//                            putong_one_info.setNum("Q");
//                            break;
//                        case 12:
//                            putong_one_info.setNum("K");
//                            break;
//
//
//                    }
//                    result_list1.add(putong_one_info);
//
//                break;
            case tuodan_two:
                for (int i = 0; i < 13; i++) {
                    random_list.add(i +"");
                }
                result = numberRandom(2, random_list);
                for (int i = 0; i < result.length; i++) {
                    NumInfo info = new NumInfo();
                    switch (result[i]) {
                        case 0:
                            info.setNum("A");
                            break;
                        case 1:
                            info.setNum("2");
                            break;
                        case 2:
                            info.setNum("3");
                            break;
                        case 3:
                            info.setNum("4");
                            break;
                        case 4:
                            info.setNum("5");
                            break;
                        case 5:
                            info.setNum("6");
                            break;
                        case 6:
                            info.setNum("7");
                            break;
                        case 7:
                            info.setNum("8");
                            break;
                        case 8:
                            info.setNum("9");
                            break;
                        case 9:
                            info.setNum("10");
                            break;
                        case 10:
                            info.setNum("J");
                            break;
                        case 11:
                            info.setNum("Q");
                            break;
                        case 12:
                            info.setNum("K");
                            break;

                    }
                    result_list1.add(info);
                }
                break;
            case tuodan_three:
                for (int i = 0; i < 13; i++) {
                    random_list.add(i +"");
                }
                result = numberRandom(3, random_list);
                for (int i = 0; i < result.length; i++) {
                    NumInfo info = new NumInfo();
                    switch (result[i]) {
                        case 0:
                            info.setNum("A");
                            break;
                        case 1:
                            info.setNum("2");
                            break;
                        case 2:
                            info.setNum("3");
                            break;
                        case 3:
                            info.setNum("4");
                            break;
                        case 4:
                            info.setNum("5");
                            break;
                        case 5:
                            info.setNum("6");
                            break;
                        case 6:
                            info.setNum("7");
                            break;
                        case 7:
                            info.setNum("8");
                            break;
                        case 8:
                            info.setNum("9");
                            break;
                        case 9:
                            info.setNum("10");
                            break;
                        case 10:
                            info.setNum("J");
                            break;
                        case 11:
                            info.setNum("Q");
                            break;
                        case 12:
                            info.setNum("K");
                            break;

                    }
                    result_list1.add(info);
                }
                break;
            case tuodan_four:
                for (int i = 0; i < 13; i++) {
                    random_list.add(i +"");
                }
                result = numberRandom(4, random_list);
                for (int i = 0; i < result.length; i++) {
                    NumInfo info = new NumInfo();
                    switch (result[i]) {
                        case 0:
                            info.setNum("A");
                            break;
                        case 1:
                            info.setNum("2");
                            break;
                        case 2:
                            info.setNum("3");
                            break;
                        case 3:
                            info.setNum("4");
                            break;
                        case 4:
                            info.setNum("5");
                            break;
                        case 5:
                            info.setNum("6");
                            break;
                        case 6:
                            info.setNum("7");
                            break;
                        case 7:
                            info.setNum("8");
                            break;
                        case 8:
                            info.setNum("9");
                            break;
                        case 9:
                            info.setNum("10");
                            break;
                        case 10:
                            info.setNum("J");
                            break;
                        case 11:
                            info.setNum("Q");
                            break;
                        case 12:
                            info.setNum("K");
                            break;

                    }
                    result_list1.add(info);
                }
                break;
            case tuodan_five:
                for (int i = 0; i < 13; i++) {
                    random_list.add(i +"");
                }
                result = numberRandom(5, random_list);
                for (int i = 0; i < result.length; i++) {
                    NumInfo info = new NumInfo();
                    switch (result[i]) {
                        case 0:
                            info.setNum("A");
                            break;
                        case 1:
                            info.setNum("2");
                            break;
                        case 2:
                            info.setNum("3");
                            break;
                        case 3:
                            info.setNum("4");
                            break;
                        case 4:
                            info.setNum("5");
                            break;
                        case 5:
                            info.setNum("6");
                            break;
                        case 6:
                            info.setNum("7");
                            break;
                        case 7:
                            info.setNum("8");
                            break;
                        case 8:
                            info.setNum("9");
                            break;
                        case 9:
                            info.setNum("10");
                            break;
                        case 10:
                            info.setNum("J");
                            break;
                        case 11:
                            info.setNum("Q");
                            break;
                        case 12:
                            info.setNum("K");
                            break;

                    }
                    result_list1.add(info);
                }
                break;
            case tuodan_six:
                for (int i = 0; i < 13; i++) {
                    random_list.add(i +"");
                }
                result = numberRandom(6, random_list);
                for (int i = 0; i < result.length; i++) {
                    NumInfo info = new NumInfo();
                    switch (result[i]) {
                        case 0:
                            info.setNum("A");
                            break;
                        case 1:
                            info.setNum("2");
                            break;
                        case 2:
                            info.setNum("3");
                            break;
                        case 3:
                            info.setNum("4");
                            break;
                        case 4:
                            info.setNum("5");
                            break;
                        case 5:
                            info.setNum("6");
                            break;
                        case 6:
                            info.setNum("7");
                            break;
                        case 7:
                            info.setNum("8");
                            break;
                        case 8:
                            info.setNum("9");
                            break;
                        case 9:
                            info.setNum("10");
                            break;
                        case 10:
                            info.setNum("J");
                            break;
                        case 11:
                            info.setNum("Q");
                            break;
                        case 12:
                            info.setNum("K");
                            break;

                    }
                    result_list1.add(info);
                }
                break;


        }
        if (result == null) {
            ToastUtil.toast(this, "模式错误");
            finish();
            return;
        }

        TicketResultListInfo data = new TicketResultListInfo();
        data.setNumbers1(result_list1);
        data.setNumbers2(result_list2);
        data.setNum("1");
        data.setPrice(TicketPrice + "");
        data.setMode(mSelecte_Mode);
        list_result.add(data);
        adpater.notifyDataSetChanged();
        //重新计算金额和注数
        calcluateNumAndPrice();
    }
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
    public void calcluateNumAndPrice() {
        int c_num = 0;

        for (int i = 0; i < list_result.size(); i++) {
            c_num = c_num + Integer.valueOf(list_result.get(i).getNum());
        }
        num.setText(c_num + "");
        if (TextUtils.isEmpty(multipleNumEdit.getText().toString()) && TextUtils.isEmpty(termNumEdit.getText().toString())) {
            price.setText(Integer.valueOf(num.getText().toString()) * 1 * 1 * TicketPrice + "");
        } else if (TextUtils.isEmpty(multipleNumEdit.getText().toString())) {
            price.setText(Integer.valueOf(num.getText().toString()) * 1 * Integer.valueOf(termNumEdit.getText().toString()) * TicketPrice + "");
        } else if (TextUtils.isEmpty(termNumEdit.getText().toString())) {
            price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(multipleNumEdit.getText().toString()) * 1 * TicketPrice + "");
        } else {
            price.setText(Integer.valueOf(num.getText().toString()) * Integer.valueOf(multipleNumEdit.getText().toString()) * Integer.valueOf(termNumEdit.getText().toString()) * TicketPrice + "");
        }
    }
}
