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
    int count=0;
    //每注彩票价格
    private final int TicketPrice = 2;
    //普通投注
    private final int S_BAOXUAN = 1;//包选
    private final int S_TONGHUA = 2;//同花
    private final int S_SHUNZI = 3;//顺子
    private final int S_TONGHUASHUN = 4;//同花顺
    private final int S_BAOZI = 5;//豹子
    private final int S_DUIZI = 6;//对子
    //普通任选
    private final int SSD_ONE = 13;//任选1
    private final int SSD_TWO = 14;//任选2
    private final int SSD_THREE = 15;//任选3
    private final int SSD_FOUR = 16;//任选4
    private final int SSD_FIVE = 17;//任选5
    private final int SSD_SIX = 18;//任选6
    //拖胆
    private final int SD_ONE = 7;//任选1
    private final int SD_TWO = 8;//任选2
    private final int SD_THREE = 9;//任选3
    private final int SD_FOUR = 10;//任选4
    private final int SD_FIVE = 11;//任选5
    private final int SD_SIX = 12;//任选6


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
    private boolean check_term_top=true;

    private List<NumInfo> baoxuanList = new ArrayList<>();
    private List<NumInfo> tonghuaList = new ArrayList<>();
    private List<NumInfo> shunziList = new ArrayList<>();
    private List<NumInfo> tonghuashunList = new ArrayList<>();
    private List<NumInfo> baoziList = new ArrayList<>();
    private List<NumInfo> duiziList = new ArrayList<>();

//down
    private List<NumInfo> RONEList = new ArrayList<>();
    private List<NumInfo> RTWOList = new ArrayList<>();
    private List<NumInfo> RTHREEList = new ArrayList<>();
    private List<NumInfo> RFOURList = new ArrayList<>();
    private List<NumInfo> RFIVEList = new ArrayList<>();
    private List<NumInfo> RSIXList = new ArrayList<>();
    //up
    private List<NumInfo> RRONEList = new ArrayList<>();
    private List<NumInfo> RRTWOList = new ArrayList<>();
    private List<NumInfo> RRTHREEList = new ArrayList<>();
    private List<NumInfo> RRFOURList = new ArrayList<>();
    private List<NumInfo> RRFIVEList = new ArrayList<>();
    private List<NumInfo> RRSIXList = new ArrayList<>();





    private TicketAdapter adpater;
    private List<TicketResultListInfo> list_result=new ArrayList<>();
    private Activity mActivity;
    private OrderInitBO mOrderInit;
    private String sh_name,type,now_qs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevenforfive_detail);
        ButterKnife.bind(this);
        mActivity=this;
        initIntent();
        initView();
       // getData();

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
                mOrderInit= JSON.parseObject(resultBO.getResultData(),OrderInitBO.class);
                if(mOrderInit==null){
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

    private void initIntent() {

        mSelecte_Mode=this.getIntent().getIntExtra("selecte_mode",-1);
        if(mSelecte_Mode==-1){
            ToastUtil.toast(mActivity,"模式错误");
            return;
        }
        //Log.e("mSelecte_Mode！！！！！！！！！",mSelecte_Mode+"");
        baoxuanList=(List<NumInfo>) this.getIntent().getSerializableExtra("baoxuanList");
        tonghuaList=(List<NumInfo>) this.getIntent().getSerializableExtra("tonghuaList");
        shunziList=(List<NumInfo>) this.getIntent().getSerializableExtra("shunziList");
        tonghuashunList=(List<NumInfo>) this.getIntent().getSerializableExtra("tonghuashunList");
        baoziList=(List<NumInfo>) this.getIntent().getSerializableExtra("baoziList");
        duiziList=(List<NumInfo>) this.getIntent().getSerializableExtra("duiziList");
        RONEList=(List<NumInfo>) this.getIntent().getSerializableExtra("RONEList");
        RTWOList=(List<NumInfo>) this.getIntent().getSerializableExtra("RTWOList");
        RTHREEList=(List<NumInfo>) this.getIntent().getSerializableExtra("RTHREEList");
        RFOURList=(List<NumInfo>) this.getIntent().getSerializableExtra("RFOURList");
        RFIVEList=(List<NumInfo>) this.getIntent().getSerializableExtra("RFIVEList");
        RSIXList=(List<NumInfo>) this.getIntent().getSerializableExtra("RSIXList");

        //up
        RRONEList=(List<NumInfo>) this.getIntent().getSerializableExtra("RRONEList");
        RRTWOList=(List<NumInfo>) this.getIntent().getSerializableExtra("RRTWOList");
        RRTHREEList=(List<NumInfo>) this.getIntent().getSerializableExtra("RRTHREEList");
        RRFOURList=(List<NumInfo>) this.getIntent().getSerializableExtra("RRFOURList");
        RRFIVEList=(List<NumInfo>) this.getIntent().getSerializableExtra("RRFIVEList");
        RRSIXList=(List<NumInfo>) this.getIntent().getSerializableExtra("RRSIXList");

        num.setText(this.getIntent().getStringExtra("num"));
        price.setText(this.getIntent().getStringExtra("price"));
        title.setText(this.getIntent().getStringExtra("title"));
        sh_name=this.getIntent().getStringExtra("sh_name");
        type=this.getIntent().getStringExtra("type");
        now_qs=this.getIntent().getStringExtra("now_qs");

        TicketResultListInfo info=new TicketResultListInfo();
        info.setMode(mSelecte_Mode);
        info.setNum(num.getText().toString());
        info.setPrice(price.getText().toString());

        switch (mSelecte_Mode){
            case S_BAOXUAN:
                for(int i=0;i<baoxuanList.size();i++){
                    if(baoxuanList.get(i).is_checked()){

                        info.getNumbers1().add(baoxuanList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));

                    }
                }
                break;
            case S_TONGHUA:
                for(int i=0;i<tonghuaList.size();i++){
                    if(tonghuaList.get(i).is_checked()){

                        info.getNumbers1().add(tonghuaList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));

                    }
                }
                break;
            case S_SHUNZI:
                for(int i=0;i<shunziList.size();i++){
                    if(shunziList.get(i).is_checked()){

                        info.getNumbers1().add(shunziList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }

                break;
            case S_TONGHUASHUN:
                for(int i=0;i<tonghuashunList.size();i++){
                    if(tonghuashunList.get(i).is_checked()){

                        info.getNumbers1().add(tonghuashunList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }
                break;
            case S_BAOZI:
                for(int i=0;i<baoziList.size();i++){
                    if(baoziList.get(i).is_checked()){

                        info.getNumbers1().add(baoziList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }
                break;
            case S_DUIZI:
                for(int i=0;i<duiziList.size();i++){
                    if(duiziList.get(i).is_checked()){

                        info.getNumbers1().add(duiziList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }

                break;
            case SD_ONE:
                for(int i=0;i<RONEList.size();i++){
                    if(RONEList.get(i).is_checked()){

                        info.getNumbers1().add(RONEList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }
                break;
            case SD_TWO:
                for(int i=0;i<RTWOList.size();i++){
                    if(RTWOList.get(i).is_checked()){

                        info.getNumbers1().add(RTWOList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }
                break;
            case SD_THREE:
                for(int i=0;i<RTHREEList.size();i++){
                    if(RTHREEList.get(i).is_checked()){

                        info.getNumbers1().add(RTHREEList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }
                break;
            case SD_FOUR:
                for(int i=0;i<RFOURList.size();i++){
                    if(RFOURList.get(i).is_checked()){

                        info.getNumbers1().add(RFOURList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }
                break;
            case SD_FIVE:
                for(int i=0;i<RFIVEList.size();i++){
                    if(RFIVEList.get(i).is_checked()){

                        info.getNumbers1().add(RFIVEList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }
                break;
            case SD_SIX:
                for(int i=0;i<RSIXList.size();i++){
                    if(RSIXList.get(i).is_checked()){

                        info.getNumbers1().add(RSIXList.get(i));
                        Log.e("checked!!!!!!",String.valueOf(i));
                    }
                }
                break;

                //up
            case SSD_ONE:
                for(int i=0;i<RRONEList.size();i++){
                    if(RRONEList.get(i).is_checked()){

                        info.getNumbers1().add(RRONEList.get(i));
                    }
                }
                break;
            case SSD_TWO:
                for(int i=0;i<RRTWOList.size();i++){
                    if(RRTWOList.get(i).is_checked()){

                        info.getNumbers1().add(RRTWOList.get(i));
                    }
                }
                break;
            case SSD_THREE:
                for(int i=0;i<RRTHREEList.size();i++){
                    if(RRTHREEList.get(i).is_checked()){

                        info.getNumbers1().add(RRTHREEList.get(i));
                    }
                }
                break;
            case SSD_FOUR:
                for(int i=0;i<RRFOURList.size();i++){
                    if(RRFOURList.get(i).is_checked()){

                        info.getNumbers1().add(RRFOURList.get(i));

                    }
                }
                break;
            case SSD_FIVE:
                for(int i=0;i<RRFIVEList.size();i++){
                    if(RRFIVEList.get(i).is_checked()){

                        info.getNumbers1().add(RRFIVEList.get(i));

                    }
                }
                break;
            case SSD_SIX:
                for(int i=0;i<RRSIXList.size();i++){
                    if(RRSIXList.get(i).is_checked()){

                        info.getNumbers1().add(RRSIXList.get(i));

                    }
                }
                break;
        }

        list_result.add(info);

    }

    private void initView() {

        adpater=new TicketAdapter(this,R.layout.list_item_ticket_result,list_result);
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
                if(mOrderInit.getMax_bs()==null){
                    return;
                }
                if(TextUtils.isEmpty(price.getText().toString() ) ||TextUtils.isEmpty(num.getText().toString())){
                    return;
                }
                if(TextUtils.isEmpty(termNumEdit.getText().toString())){
                    termNumEdit.setText("1");
                }
                if( TextUtils.isEmpty(multipleNumEdit.getText().toString())){
                    multipleNumEdit.setHint("1");
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*TicketPrice*1+"");
                }else if(Integer.valueOf(multipleNumEdit.getText().toString())<1 ){
                    ToastUtil.toast(PukeDetailActivity.this,"最小倍数为1");
                    multipleNumEdit.setText("1");
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*TicketPrice*1+"");
                } else if(!mOrderInit.getMax_bs().equals("0")&&Integer.valueOf(multipleNumEdit.getText().toString())>Integer.valueOf(mOrderInit.getMax_bs())){
                    ToastUtil.toast(PukeDetailActivity.this,"最大倍数为"+mOrderInit.getMax_bs());
                    multipleNumEdit.setText(mOrderInit.getMax_bs());
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*TicketPrice+"");
                }else{
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*TicketPrice+"");
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
                if(mOrderInit.getMax_zq()==null){
                    return;
                }
                if(TextUtils.isEmpty(price.getText().toString() ) ||TextUtils.isEmpty(num.getText().toString())){
                    return;
                }
                if(TextUtils.isEmpty(multipleNumEdit.getText().toString())){
                    multipleNumEdit.setText("1");
                }
                if(TextUtils.isEmpty(termNumEdit.getText().toString())){
                    termNumEdit.setHint("1");
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*1*TicketPrice+"");
                }
                else if(Integer.valueOf(termNumEdit.getText().toString())<1 ){
                    ToastUtil.toast(PukeDetailActivity.this,"最少需要追1期");
                    termNumEdit.setText("1");
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*1*TicketPrice+"");
                }
                else if(Integer.valueOf(termNumEdit.getText().toString())>Integer.valueOf(mOrderInit.getMax_zq())){
                    ToastUtil.toast(PukeDetailActivity.this,"最多可追"+mOrderInit.getMax_zq()+"期");
                    termNumEdit.setText(mOrderInit.getMax_zq());
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*Integer.valueOf(mOrderInit.getMax_zq())*TicketPrice+"");
                }else if(Integer.valueOf(termNumEdit.getText().toString())<1 || TextUtils.isEmpty(termNumEdit.getText().toString())){
                    ToastUtil.toast(PukeDetailActivity.this,"最少需要追1期");
                    termNumEdit.setText("1");
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*1*TicketPrice+"");
                }else{
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*TicketPrice+"");
                }
                termNumEdit.setSelection(termNumEdit.length());

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.back, R.id.continue_btn, R.id.random_btn, R.id.clear_btn, R.id.comfirm_btn, R.id.term_stop_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                showBackPop();
                break;
            case R.id.continue_btn:
                Intent in=new Intent(mActivity,PukeActivity.class);
                in.putExtra("sh_name",sh_name);
                in.putExtra("continue",true);
                startActivityForResult(in,0);
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
                if(check_term_top){
                    termStopCheck.setImageResource(R.drawable.icon_shape_unselete);
                    check_term_top=false;
                }else{
                    termStopCheck.setImageResource(R.drawable.icon_shape_seleted);
                    check_term_top=true;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //继续选号
        if (requestCode == 0 && resultCode == RESULT_OK) {
            mSelecte_Mode=data.getIntExtra("selecte_mode",-1);
            if(mSelecte_Mode==-1){
                ToastUtil.toast(mActivity,"模式错误");
                return;
            }

            baoxuanList=(List<NumInfo>) data.getSerializableExtra("baoxuanList");
            tonghuaList=(List<NumInfo>)data.getSerializableExtra("tonghuaList");
            shunziList=(List<NumInfo>) data.getSerializableExtra("shunziList");
            tonghuashunList=(List<NumInfo>)data.getSerializableExtra("tonghuashunList");
            baoziList=(List<NumInfo>)data.getSerializableExtra("baoziList");
            duiziList=(List<NumInfo>) data.getSerializableExtra("duiziList");
            RONEList=(List<NumInfo>) data.getSerializableExtra("RONEList");
            RTWOList=(List<NumInfo>) data.getSerializableExtra("RTWOList");
            RTHREEList=(List<NumInfo>) data.getSerializableExtra("RTHREEList");
            RFOURList=(List<NumInfo>) data.getSerializableExtra("RFOURList");
            RFIVEList=(List<NumInfo>)data.getSerializableExtra("RFIVEList");
            RSIXList=(List<NumInfo>) data.getSerializableExtra("RSIXList");

            //up
            RRONEList=(List<NumInfo>) data.getSerializableExtra("RRONEList");
            RRTWOList=(List<NumInfo>) data.getSerializableExtra("RRTWOList");
            RRTHREEList=(List<NumInfo>) data.getSerializableExtra("RRTHREEList");
            RRFOURList=(List<NumInfo>) data.getSerializableExtra("RRFOURList");
            RRFIVEList=(List<NumInfo>)data.getSerializableExtra("RRFIVEList");
            RRSIXList=(List<NumInfo>) data.getSerializableExtra("RRSIXList");

            sh_name=this.getIntent().getStringExtra("sh_name");

            TicketResultListInfo info=new TicketResultListInfo();
            info.setMode(data.getIntExtra("selecte_mode",1));
            info.setNum(data.getStringExtra("num"));
            info.setPrice(data.getStringExtra("price"));


            switch (mSelecte_Mode){
                case S_BAOXUAN:
                    for(int i=0;i<baoxuanList.size();i++){
                        if(baoxuanList.get(i).is_checked()){
                            info.getNumbers1().add(baoxuanList.get(i));
                            Log.e("checked!!!!!!",String.valueOf(i));
                        }
                    }
                    break;
                case S_TONGHUA:
                    for(int i=0;i<tonghuaList.size();i++){
                        if(tonghuaList.get(i).is_checked()){
                            info.getNumbers1().add(tonghuaList.get(i));
                        }
                    }
                    break;
                case S_SHUNZI:
                    for(int i=0;i<shunziList.size();i++){
                        if(shunziList.get(i).is_checked()){
                            info.getNumbers1().add(shunziList.get(i));
                        }
                    }

                    break;
                case S_TONGHUASHUN:
                    for(int i=0;i<tonghuashunList.size();i++){
                        if(tonghuashunList.get(i).is_checked()){
                            info.getNumbers1().add(tonghuashunList.get(i));
                        }
                    }
                    break;
                case S_BAOZI:
                    for(int i=0;i<baoziList.size();i++){
                        if(baoziList.get(i).is_checked()){
                            info.getNumbers1().add(baoziList.get(i));
                        }
                    }
                    break;
                case S_DUIZI:
                    for(int i=0;i<duiziList.size();i++){
                        if(duiziList.get(i).is_checked()){
                            info.getNumbers1().add(duiziList.get(i));
                        }
                    }
                    break;
                case SD_ONE:
                    for(int i=0;i<RONEList.size();i++){
                        if(RONEList.get(i).is_checked()){
                            RONEList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RONEList.get(i));
                        }
                    }

                    break;
                case SD_TWO:
                    for(int i=0;i<RTWOList.size();i++){
                        if(RTWOList.get(i).is_checked()){
                            RTWOList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RTWOList.get(i));
                        }
                    }

                    break;
                case SD_THREE:
                    for(int i=0;i<RTHREEList.size();i++){
                        if(RTHREEList.get(i).is_checked()){
                            RTHREEList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RTHREEList.get(i));
                        }
                    }

                    break;
                case SD_FOUR:
                    for(int i=0;i<RFOURList.size();i++){
                        if(RFOURList.get(i).is_checked()){
                            RFOURList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RFOURList.get(i));
                        }
                    }

                    break;
                case SD_FIVE:
                    for(int i=0;i<RFIVEList.size();i++){
                        if(RFIVEList.get(i).is_checked()){
                            RFIVEList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RFIVEList.get(i));
                        }
                    }

                    break;
                case SD_SIX:
                    for(int i=0;i<RSIXList.size();i++){
                        if(RSIXList.get(i).is_checked()){
                            RSIXList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RSIXList.get(i));
                        }
                    }
                    //up
                case SSD_ONE:
                    for(int i=0;i<RRONEList.size();i++){
                        if(RRONEList.get(i).is_checked()){
                            RRONEList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RRONEList.get(i));
                        }
                    }

                    break;
                case SSD_TWO:
                    for(int i=0;i<RRTWOList.size();i++){
                        if(RRTWOList.get(i).is_checked()){
                            RRTWOList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RRTWOList.get(i));
                        }
                    }

                    break;
                case SSD_THREE:
                    for(int i=0;i<RRTHREEList.size();i++){
                        if(RRTHREEList.get(i).is_checked()){
                            RRTHREEList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RRTHREEList.get(i));
                        }
                    }

                    break;
                case SSD_FOUR:
                    for(int i=0;i<RRFOURList.size();i++){
                        if(RRFOURList.get(i).is_checked()){
                            RRFOURList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RRFOURList.get(i));
                        }
                    }

                    break;
                case SSD_FIVE:
                    for(int i=0;i<RRFIVEList.size();i++){
                        if(RRFIVEList.get(i).is_checked()){
                            RRFIVEList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RRFIVEList.get(i));
                        }
                    }

                    break;
                case SSD_SIX:
                    for(int i=0;i<RSIXList.size();i++){
                        if(RRSIXList.get(i).is_checked()){
                            RRSIXList.get(i).setIs_spe(true);
                            info.getNumbers1().add(RRSIXList.get(i));
                        }
                    }

                    break;
            }
            list_result.add(info);
            adpater.notifyDataSetChanged();
            calcluateNumAndPrice();
        }
    }
    /**
     * 确定下单
     */
    private void BuyComfirm() {
        if(App.mUser==null || App.mUser.getAccess_token()==null){
            ToastUtil.toast(mActivity,"请先登录！");
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
            return;
        }
//        if(Double.valueOf(App.mUser.getBalance())<Double.valueOf(price.getText().toString())){
//            ToastUtil.toast(mActivity,"金额不足，请先充值！");
//            return;
//        }
        if(list_result.size()<1){
            ToastUtil.toast(mActivity,"请先购买号码！");
            return;
        }
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sh_name", sh_name);
        paramMap.put("lottery_type", "2");
        paramMap.put("total_fee", price.getText().toString());
        paramMap.put("log_num", now_qs);
        if(TextUtils.isEmpty(termNumEdit.getText().toString())){
            paramMap.put("sery_num", "1");
            Log.e("sery_num","1");
        }else{
            paramMap.put("sery_num", termNumEdit.getText().toString());
            Log.e("sery_num",termNumEdit.getText().toString());
        }
        if(TextUtils.isEmpty(multipleNumEdit.getText().toString())){
            paramMap.put("buy_bs", "1");
            Log.e("buy_bs","1");
        }else{
            paramMap.put("buy_bs", multipleNumEdit.getText().toString());
            Log.e("buy_bs",multipleNumEdit.getText().toString());
        }
        if(check_term_top){
            paramMap.put("is_sery_stop", "1");
        }else{
            paramMap.put("is_sery_stop", "0");
        }
        Log.e("total_fee",price.getText().toString());

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
                Intent in=new Intent(mActivity,FastThreeActivity.class);
                in.putExtra("sh_name",sh_name);
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

    /**
     * 拼接参数
     * @return
     */
    private String  setbuyArray() {
        List<BuyNumBO> buylist=new ArrayList<>();


        for(int i=0;i<list_result.size();i++){
            BuyNumBO info=new BuyNumBO();
            info.setType(list_result.get(i).getMode()+"");
            String s1="";
            for(int j=0;j<list_result.get(i).getNumbers1().size();j++){
                if(j==0){
                    s1=s1+list_result.get(i).getNumbers1().get(j).getNum();
                }else{
                    s1=s1+","+list_result.get(i).getNumbers1().get(j).getNum();
                }
            }
            info.setS1(s1);
            if(list_result.get(i).getNumbers2().size()>0){
                String s2="";
                for(int j=0;j<list_result.get(i).getNumbers2().size();j++){
                    if(j==0){
                        s2=s2+list_result.get(i).getNumbers2().get(j).getNum();
                    }else{
                        s2=s2+","+list_result.get(i).getNumbers2().get(j).getNum();
                    }
                }
                info.setS2(s2);
            }
            buylist.add(info);
        }
        Log.e("buy_arry",JSON.toJSONString(buylist));
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


    /**
     * 投注列表适配器
     */
    public class TicketAdapter extends CommonAdapter<TicketResultListInfo> {

        public TicketAdapter(Context context, int layoutId, List<TicketResultListInfo> datas) {
            super(context, layoutId, datas);

        }

        @Override
        public void convert(ViewHolder holder, final TicketResultListInfo info) {
            List<NumInfo> nums = new ArrayList<>();
            Log.e("mode",String.valueOf(mSelecte_Mode));
            Log.e("size",String.valueOf(info.getNumbers1().size()));
            for(int i=0;i<info.getNumbers1().size();i++){
                nums.add(info.getNumbers1().get(i));

            }
            Log.e("num",String.valueOf(nums.size()));


            final TagFlowLayout numTag=holder.getView(R.id.num_tag);
            final TagAdapter<NumInfo> numAdapter = new TagAdapter<NumInfo>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, NumInfo s) {
                    TextView tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                    //Log.e("positon",String.valueOf(position));

                        switch (info.getMode()){
                            case S_BAOXUAN:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case S_TONGHUA:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_tonghua, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case S_SHUNZI:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_three, numTag, false);
                                tv.setBackgroundResource(R.drawable.shunzi_empty);
                                break;
                            case S_TONGHUASHUN:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_tonghua, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case S_BAOZI:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_three, numTag, false);
                                tv.setBackgroundResource(R.drawable.shunzi_empty);
                                break;
                            case S_DUIZI:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_two, numTag, false);
                                tv.setBackgroundResource(R.drawable.duizi_empty);
                                break;
                            case SD_ONE:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SD_TWO:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SD_THREE:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SD_FOUR:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SD_FIVE:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SD_SIX:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                                //up
                            case SSD_ONE:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SSD_TWO:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SSD_THREE:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SSD_FOUR:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SSD_FIVE:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                            case SSD_SIX:
                                tv= (TextView) LayoutInflater.from(PukeDetailActivity.this).inflate(R.layout.list_item_puke_detail_one, numTag, false);
                                tv.setBackgroundResource(R.drawable.puke_empty);
                                break;
                        }


                        tv.setText(s.getNum());

                    return tv;

                }
            };
            numTag.setAdapter(numAdapter);
            holder.setText(R.id.ticket_num,info.getNum()+"注");
            holder.setText(R.id.ticket_price,info.getPrice()+"元");
            TextView mode_tv=holder.getView(R.id.ticket_mode);
            switch (info.getMode()){
                case S_BAOXUAN:
                    mode_tv.setText("包选");
                    break;
                case S_TONGHUA:
                    mode_tv.setText("同花");
                    break;
                case S_SHUNZI:
                    mode_tv.setText("顺子");
                    break;
                case S_TONGHUASHUN:
                    mode_tv.setText("同花顺");
                    break;
                case S_BAOZI:
                    mode_tv.setText("豹子");
                    break;
                case S_DUIZI:
                    mode_tv.setText("对子");
                    break;
                case SD_ONE:
                    mode_tv.setText("任选一");
                    break;
                case SD_TWO:
                    mode_tv.setText("任选二");
                    break;
                case SD_THREE:
                    mode_tv.setText("任选三");
                    break;
                case SD_FOUR:
                    mode_tv.setText("任选四");
                    break;
                case SD_FIVE:
                    mode_tv.setText("任选五");
                    break;
                case SD_SIX:
                    mode_tv.setText("任选六");
                    break;
                    //up
                case SSD_ONE:
                    mode_tv.setText("任选一");
                    break;
                case SSD_TWO:
                    mode_tv.setText("任选二");
                    break;
                case SSD_THREE:
                    mode_tv.setText("任选三");
                    break;
                case SSD_FOUR:
                    mode_tv.setText("任选四");
                    break;
                case SSD_FIVE:
                    mode_tv.setText("任选五");
                    break;
                case SSD_SIX:
                    mode_tv.setText("任选六");
                    break;
            }

            ImageView delete=holder.getView(R.id.ticket_delete);
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
        TextView cancle=(TextView)view.findViewById(R.id.cancle);
        TextView comfirm=(TextView)view.findViewById(R.id.comfirm);
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
        TextView cancle=(TextView)view.findViewById(R.id.cancle);
        TextView comfirm=(TextView)view.findViewById(R.id.comfirm);
        TextView title=(TextView)view.findViewById(R.id.title);
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


    /**
     * 设置机选号码
     */
    private void getRandow() {
        int[] result=null;
        int[] result2=null;

        List<NumInfo> result_list1=new ArrayList<>();
        List<NumInfo> result_list2=new ArrayList<>();
        List<String> random_list=new ArrayList<>();
        List<String> random_list2=new ArrayList<>();

        switch (mSelecte_Mode){
            case S_BAOXUAN:
                for (int i = 0; i <5; i++) {
                    random_list.add(""+i);
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum(result[i]+1+"");
                    result_list1.add(info);
                }
                break;
            case S_TONGHUA:
                for (int i = 0; i <4; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
                break;
            case S_SHUNZI:
                for (int i = 0; i <12; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
                break;
            case S_TONGHUASHUN:
                for (int i = 0; i <4; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
                break;
            case S_BAOZI:;
                for (int i = 0; i <13; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
                break;
            case S_DUIZI:

                for (int i = 0; i <13; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }

                break;
            case SD_ONE:
                for (int i = 0; i <13; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
                break;
            case SD_TWO:
                for (int i = 0; i <13; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(2,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
            case SD_THREE:

                for (int i = 0; i <13; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(3,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
                break;
            case SD_FOUR:
                for (int i = 0; i <13; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(4,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
            case SD_FIVE:
                for (int i = 0; i <13; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(5,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
                break;
            case SD_SIX:
                for (int i = 0; i <13; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(6,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
                break;


        }
        if(result==null){
            ToastUtil.toast(this,"模式错误");
            finish();
            return;
        }

        TicketResultListInfo data=new TicketResultListInfo();
        data.setNumbers1(result_list1);
        data.setNumbers2(result_list2);
        data.setNum("1");
        data.setPrice(TicketPrice+"");
        data.setMode(mSelecte_Mode);
        list_result.add(data);
        adpater.notifyDataSetChanged();
        //重新计算金额和注数
        calcluateNumAndPrice();
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

    /**
     * 计算总的注数和金额
     */
    public void calcluateNumAndPrice(){
        int c_num=0;

        for(int i=0;i<list_result.size();i++){
            c_num=c_num+Integer.valueOf(list_result.get(i).getNum());
        }
        num.setText(c_num+"");
        if(TextUtils.isEmpty(multipleNumEdit.getText().toString()) && TextUtils.isEmpty(termNumEdit.getText().toString())){
            price.setText(Integer.valueOf(num.getText().toString())*1*1*TicketPrice+"");
        }else if(TextUtils.isEmpty(multipleNumEdit.getText().toString())){
            price.setText(Integer.valueOf(num.getText().toString())*1*Integer.valueOf(termNumEdit.getText().toString())*TicketPrice+"");
        } else if(TextUtils.isEmpty(termNumEdit.getText().toString())){
            price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*1*TicketPrice+"");
        } else{
            price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*TicketPrice+"");
        }
    }
}
