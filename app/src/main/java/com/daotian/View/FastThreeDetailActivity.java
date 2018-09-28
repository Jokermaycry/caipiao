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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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

public class FastThreeDetailActivity extends AppCompatActivity {
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

    private int mSelecte_Mode = SUM_VALUE;

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

    private List<NumInfo> sumList = new ArrayList<>();
    private List<NumInfo> sameThreeList = new ArrayList<>();
    private List<NumInfo> sameTwo_S_List = new ArrayList<>();
    private List<NumInfo> sameTwo_D_List = new ArrayList<>();
    private List<NumInfo> sameTwo_M_List = new ArrayList<>();
    private List<NumInfo> difThreeList = new ArrayList<>();
    private List<NumInfo> difTwoList = new ArrayList<>();
    private List<NumInfo> sDifTwoList = new ArrayList<>();
    private List<NumInfo> sSDifTwoList = new ArrayList<>();
    private List<NumInfo> sSDifThreeList = new ArrayList<>();
    private List<NumInfo> sDifThreeList = new ArrayList<>();


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
        Log.e("mSelecte_Mode",mSelecte_Mode+"");
        sumList=(List<NumInfo>) this.getIntent().getSerializableExtra("sumList");
        sameThreeList=(List<NumInfo>) this.getIntent().getSerializableExtra("sameThreeList");
        sameTwo_S_List=(List<NumInfo>) this.getIntent().getSerializableExtra("sameTwo_S_List");
        sameTwo_D_List=(List<NumInfo>) this.getIntent().getSerializableExtra("sameTwo_D_List");
        sameTwo_M_List=(List<NumInfo>) this.getIntent().getSerializableExtra("sameTwo_M_List");
        difThreeList=(List<NumInfo>) this.getIntent().getSerializableExtra("difThreeList");
        difTwoList=(List<NumInfo>) this.getIntent().getSerializableExtra("difTwoList");
        sDifTwoList=(List<NumInfo>) this.getIntent().getSerializableExtra("sDifTwoList");
        sSDifTwoList=(List<NumInfo>) this.getIntent().getSerializableExtra("sSDifTwoList");
        sSDifThreeList=(List<NumInfo>) this.getIntent().getSerializableExtra("sSDifThreeList");
        sDifThreeList=(List<NumInfo>) this.getIntent().getSerializableExtra("sDifThreeList");

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
            case SUM_VALUE:
                for(int i=0;i<sumList.size();i++){
                    if(sumList.get(i).is_checked()){
                        info.getNumbers1().add(sumList.get(i));
                    }
                }
                break;
            case S_THREE:
                for(int i=0;i<sameThreeList.size();i++){
                    if(sameThreeList.get(i).is_checked()){
                        info.getNumbers1().add(sameThreeList.get(i));
                    }
                }
                break;
            case S_TWO:
                for(int i=0;i<sameTwo_S_List.size();i++){
                    if(sameTwo_S_List.get(i).is_checked()){
                        info.getNumbers1().add(sameTwo_S_List.get(i));
                    }
                }
                for(int i=0;i<sameTwo_D_List.size();i++){
                    if(sameTwo_D_List.get(i).is_checked()){
                        info.getNumbers2().add(sameTwo_D_List.get(i));
                    }
                }
                break;
            case S_TWO_S:
                for(int i=0;i<sameTwo_M_List.size();i++){
                    if(sameTwo_M_List.get(i).is_checked()){
                        info.getNumbers1().add(sameTwo_M_List.get(i));
                    }
                }
                break;
            case D_THREE:
                for(int i=0;i<difThreeList.size();i++){
                    if(difThreeList.get(i).is_checked()){
                        info.getNumbers1().add(difThreeList.get(i));
                    }
                }
                break;
            case D_TWO:
                for(int i=0;i<difTwoList.size();i++){
                    if(difTwoList.get(i).is_checked()){
                        info.getNumbers1().add(difTwoList.get(i));
                    }
                }

                break;
            case SD_THREE:
                for(int i=0;i<sDifThreeList.size();i++){
                    if(sDifThreeList.get(i).is_checked()){
                        sDifThreeList.get(i).setIs_spe(true);
                        info.getNumbers1().add(sDifThreeList.get(i));
                    }
                }
                for(int i=0;i<sSDifThreeList.size();i++){
                    if(sSDifThreeList.get(i).is_checked()){
                        info.getNumbers2().add(sSDifThreeList.get(i));
                    }
                }
                break;
            case SD_TWO:
                for(int i=0;i<sDifTwoList.size();i++){
                    if(sDifTwoList.get(i).is_checked()){
                        sDifTwoList.get(i).setIs_spe(true);
                        info.getNumbers1().add(sDifTwoList.get(i));

                    }
                }
                for(int i=0;i<sSDifTwoList.size();i++){
                    if(sSDifTwoList.get(i).is_checked()){
                        info.getNumbers2().add(sSDifTwoList.get(i));
                    }
                }
                break;
            case S_THREE_ALL:
                for(int i=1;i<7;i++){
                    NumInfo data=new NumInfo();
                    data.setNum(i*111+"");
                    info.getNumbers1().add(data);
                }
                break;
            case D_THREE_ALL:
                NumInfo data=new NumInfo();
                data.setNum("123");
                info.getNumbers1().add(data);
                NumInfo data2=new NumInfo();
                data2.setNum("234");
                info.getNumbers1().add(data2);
                NumInfo data3=new NumInfo();
                data3.setNum("345");
                info.getNumbers1().add(data3);
                NumInfo data4=new NumInfo();
                data4.setNum("456");
                info.getNumbers1().add(data4);
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
                    ToastUtil.toast(FastThreeDetailActivity.this,"最小倍数为1");
                    multipleNumEdit.setText("1");
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*TicketPrice*1+"");
                } else if(!mOrderInit.getMax_bs().equals("0")&&Integer.valueOf(multipleNumEdit.getText().toString())>Integer.valueOf(mOrderInit.getMax_bs())){
                    ToastUtil.toast(FastThreeDetailActivity.this,"最大倍数为"+mOrderInit.getMax_bs());
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
                    ToastUtil.toast(FastThreeDetailActivity.this,"最少需要追1期");
                    termNumEdit.setText("1");
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*1*TicketPrice+"");
                }
                else if(Integer.valueOf(termNumEdit.getText().toString())>Integer.valueOf(mOrderInit.getMax_zq())){
                    ToastUtil.toast(FastThreeDetailActivity.this,"最多可追"+mOrderInit.getMax_zq()+"期");
                    termNumEdit.setText(mOrderInit.getMax_zq());
                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*Integer.valueOf(mOrderInit.getMax_zq())*TicketPrice+"");
                }else if(Integer.valueOf(termNumEdit.getText().toString())<1 || TextUtils.isEmpty(termNumEdit.getText().toString())){
                    ToastUtil.toast(FastThreeDetailActivity.this,"最少需要追1期");
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
                Intent in=new Intent(mActivity,FastThreeActivity.class);
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
            sumList=(List<NumInfo>)data.getSerializableExtra("sumList");
            sameThreeList=(List<NumInfo>)data.getSerializableExtra("sameThreeList");
            sameTwo_S_List=(List<NumInfo>) data.getSerializableExtra("sameTwo_S_List");
            sameTwo_D_List=(List<NumInfo>) data.getSerializableExtra("sameTwo_D_List");
            sameTwo_M_List=(List<NumInfo>)data.getSerializableExtra("sameTwo_M_List");
            difThreeList=(List<NumInfo>)data.getSerializableExtra("difThreeList");
            difTwoList=(List<NumInfo>)data.getSerializableExtra("difTwoList");
            sDifTwoList=(List<NumInfo>) data.getSerializableExtra("sDifTwoList");
            sSDifTwoList=(List<NumInfo>) data.getSerializableExtra("sSDifTwoList");
            sSDifThreeList=(List<NumInfo>) data.getSerializableExtra("sSDifThreeList");
            sDifThreeList=(List<NumInfo>) data.getSerializableExtra("sDifThreeList");

            sh_name=this.getIntent().getStringExtra("sh_name");

            TicketResultListInfo info=new TicketResultListInfo();
            info.setMode(data.getIntExtra("selecte_mode",1));
            info.setNum(data.getStringExtra("num"));
            info.setPrice(data.getStringExtra("price"));


            switch (mSelecte_Mode){
                case SUM_VALUE:
                    for(int i=0;i<sumList.size();i++){
                        if(sumList.get(i).is_checked()){
                            info.getNumbers1().add(sumList.get(i));
                        }
                    }
                    break;
                case S_THREE:
                    for(int i=0;i<sameThreeList.size();i++){
                        if(sameThreeList.get(i).is_checked()){
                            info.getNumbers1().add(sameThreeList.get(i));
                        }
                    }
                    break;
                case S_TWO:
                    for(int i=0;i<sameTwo_S_List.size();i++){
                        if(sameTwo_S_List.get(i).is_checked()){
                            info.getNumbers1().add(sameTwo_S_List.get(i));
                        }
                    }
                    for(int i=0;i<sameTwo_D_List.size();i++){
                        if(sameTwo_D_List.get(i).is_checked()){
                            info.getNumbers2().add(sameTwo_D_List.get(i));
                        }
                    }
                    break;
                case S_TWO_S:
                    for(int i=0;i<sameTwo_M_List.size();i++){
                        if(sameTwo_M_List.get(i).is_checked()){
                            info.getNumbers1().add(sameTwo_M_List.get(i));
                        }
                    }
                    break;
                case D_THREE:
                    for(int i=0;i<difThreeList.size();i++){
                        if(difThreeList.get(i).is_checked()){
                            info.getNumbers1().add(difThreeList.get(i));
                        }
                    }
                    break;
                case D_TWO:
                    for(int i=0;i<difTwoList.size();i++){
                        if(difTwoList.get(i).is_checked()){
                            info.getNumbers1().add(difTwoList.get(i));
                        }
                    }
                    break;
                case SD_THREE:
                    for(int i=0;i<sDifThreeList.size();i++){
                        if(sDifThreeList.get(i).is_checked()){
                            sDifThreeList.get(i).setIs_spe(true);
                            info.getNumbers1().add(sDifThreeList.get(i));
                        }
                    }
                    for(int i=0;i<sSDifThreeList.size();i++){
                        if(sSDifThreeList.get(i).is_checked()){
                            info.getNumbers2().add(sSDifThreeList.get(i));
                        }
                    }
                    break;
                case SD_TWO:
                    for(int i=0;i<sDifTwoList.size();i++){
                        if(sDifTwoList.get(i).is_checked()){
                            sDifTwoList.get(i).setIs_spe(true);
                            info.getNumbers1().add(sDifTwoList.get(i));
                        }
                    }
                    for(int i=0;i<sSDifTwoList.size();i++){
                        if(sSDifTwoList.get(i).is_checked()){
                            info.getNumbers2().add(sSDifTwoList.get(i));
                        }
                    }
                    break;
                case S_THREE_ALL:
                    for(int i=1;i<7;i++){
                        NumInfo s=new NumInfo();
                        s.setNum(i*111+"");
                        info.getNumbers1().add(s);
                    }
                    break;
                case D_THREE_ALL:
                    NumInfo s1=new NumInfo();
                    s1.setNum("123");
                    info.getNumbers1().add(s1);
                    NumInfo s2=new NumInfo();
                    s2.setNum("234");
                    info.getNumbers1().add(s2);
                    NumInfo s3=new NumInfo();
                    s3.setNum("345");
                    info.getNumbers1().add(s3);
                    NumInfo s4=new NumInfo();
                    s4.setNum("456");
                    info.getNumbers1().add(s4);
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
            for(int i=0;i<info.getNumbers1().size();i++){
                nums.add(info.getNumbers1().get(i));
            }
            for(int i=0;i<info.getNumbers2().size();i++){
                nums.add(info.getNumbers2().get(i));
            }
            final TagFlowLayout numTag=holder.getView(R.id.num_tag);
            TagAdapter<NumInfo> numAdapter = new TagAdapter<NumInfo>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, NumInfo s) {
                    TextView tv = (TextView) LayoutInflater.from(FastThreeDetailActivity.this).inflate(R.layout.list_item_tv2, numTag, false);
                    if(s.is_spe()){
                        tv.setBackgroundResource(R.drawable.flow_layout_selector3);
                    }else{
                        tv.setBackgroundResource(R.drawable.flow_layout_selector2);
                    }
                    if(s.getNum().length()==1){
                        tv.setText("0"+s.getNum());
                    }else{
                        tv.setText(s.getNum());
                    }
                    return tv;
                }
            };
            numTag.setAdapter(numAdapter);
            holder.setText(R.id.ticket_num,info.getNum()+"注");
            holder.setText(R.id.ticket_price,info.getPrice()+"元");
            TextView mode_tv=holder.getView(R.id.ticket_mode);
            switch (info.getMode()){
                case SUM_VALUE:
                    mode_tv.setText("和值");
                    break;
                case S_THREE:
                    mode_tv.setText("三同号-单选");
                    break;
                case S_THREE_ALL:
                    mode_tv.setText("三同号-通选");
                    break;
                case S_TWO:
                    mode_tv.setText("二同号-单选");
                    break;
                case S_TWO_S:
                    mode_tv.setText("二同号-复选");
                    break;
                case D_THREE:
                    mode_tv.setText("三不同号");
                    break;
                case D_TWO:
                    mode_tv.setText("二不同号");
                    break;
                case SD_THREE:
                    mode_tv.setText("拖胆-三不同号");
                    break;
                case SD_TWO:
                    mode_tv.setText("拖胆-二不同号");
                    break;
                case D_THREE_ALL:
                    mode_tv.setText("三连号-通选");
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
            case SUM_VALUE:
                for (int i = 3; i <= 18; i++) {
                    random_list.add(""+i);
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum(result[i]+1+"");
                    result_list1.add(info);
                }
                break;
            case S_THREE:
                for (int i = 1; i <7; i++) {
                    random_list.add(i * 111 + "");
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*111+"");
                    result_list1.add(info);
                }
                break;
            case S_THREE_ALL:
                ToastUtil.toast(mActivity,"三同号通选不支持机选");
                break;
            case S_TWO:
                for (int i = 1; i <7; i++) {
                    random_list.add(i * 11 + "");
                    random_list2.add(i+"");
                }
                result=numberRandom(1,random_list);
                result2=numberRandom(1,random_list2);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*11+"");
                    result_list1.add(info);
                }
                for(int i=0;i<result2.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result2[i]+1)+"");
                    result_list2.add(info);
                }
                break;
            case S_TWO_S:;
                for (int i = 1; i <7; i++) {
                    random_list.add(i * 11  + "");
                }
                result=numberRandom(1,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)*11+"");
                    result_list1.add(info);
                }
                break;
            case D_THREE:

                for (int i = 1; i <7; i++) {
                    random_list.add(""+i);
                }
                result=numberRandom(3,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)+"");
                    result_list1.add(info);
                }

                break;
            case D_TWO:
                for (int i = 1; i <7; i++) {
                    random_list.add(""+i );
                }
                result=numberRandom(2,random_list);
                for(int i=0;i<result.length;i++){
                    NumInfo info=new NumInfo();
                    info.setNum((result[i]+1)+"");
                    result_list1.add(info);
                }
                break;
            case SD_TWO:
            case SD_THREE:
                ToastUtil.toast(mActivity,"该模式不支持机选");
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
