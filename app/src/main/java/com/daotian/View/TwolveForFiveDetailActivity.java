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

public class TwolveForFiveDetailActivity extends AppCompatActivity {
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

    private int mSelecte_Mode = N_OPTIONAL2;

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
    private List<NumInfo> one_normal=new ArrayList<>();
    private List<NumInfo> two_normal=new ArrayList<>();
    private List<NumInfo> three_normal=new ArrayList<>();
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
        mSelecte_Mode=this.getIntent().getIntExtra("selecte_mode",1);
        one_normal=(List<NumInfo>) this.getIntent().getSerializableExtra("result_one");
        two_normal=(List<NumInfo>) this.getIntent().getSerializableExtra("result_two");
        three_normal=(List<NumInfo>) this.getIntent().getSerializableExtra("result_three");
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


        for(int i=0;i<one_normal.size();i++){
            if(one_normal.get(i).is_checked()){
                if(mSelecte_Mode==S_OPTIONAL2||mSelecte_Mode==S_OPTIONAL3||mSelecte_Mode==S_OPTIONAL4||mSelecte_Mode==S_OPTIONAL5||mSelecte_Mode==S_OPTIONAL6){
                    one_normal.get(i).setIs_spe(true);
                }
                info.getNumbers1().add(one_normal.get(i));
            }

        }
        for(int i=0;i<two_normal.size();i++){
            if(two_normal.get(i).is_checked()){
                info.getNumbers2().add(two_normal.get(i));
            }

        }
        for(int i=0;i<three_normal.size();i++){
            if(three_normal.get(i).is_checked()){
                info.getNumbers3().add(three_normal.get(i));
            }
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
//                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*TicketPrice*1+"");
                }else if(Integer.valueOf(multipleNumEdit.getText().toString())<1 ){
                    ToastUtil.toast(TwolveForFiveDetailActivity.this,"最小倍数为1");
                    multipleNumEdit.setText("1");
//                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*TicketPrice*1+"");
                } else if(!mOrderInit.getMax_bs().equals("0")&&Integer.valueOf(multipleNumEdit.getText().toString())>Integer.valueOf(mOrderInit.getMax_bs())){
                    ToastUtil.toast(TwolveForFiveDetailActivity.this,"最大倍数为"+mOrderInit.getMax_bs());
                    multipleNumEdit.setText(mOrderInit.getMax_bs());
//                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*TicketPrice+"");
                }else{
//                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*TicketPrice+"");
                }
                calcluateNumAndPrice();
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
//                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*1*TicketPrice+"");
                }
                else if(Integer.valueOf(termNumEdit.getText().toString())<1 ){
                    ToastUtil.toast(TwolveForFiveDetailActivity.this,"最少需要追1期");
                    termNumEdit.setText("1");
//                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*1*TicketPrice+"");
                }
                else if(Integer.valueOf(termNumEdit.getText().toString())>Integer.valueOf(mOrderInit.getMax_zq())){
                    ToastUtil.toast(TwolveForFiveDetailActivity.this,"最多可追"+mOrderInit.getMax_zq()+"期");
                    termNumEdit.setText(mOrderInit.getMax_zq());
//                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*Integer.valueOf(mOrderInit.getMax_zq())*TicketPrice+"");
                }else if(Integer.valueOf(termNumEdit.getText().toString())<1 || TextUtils.isEmpty(termNumEdit.getText().toString())){
                    ToastUtil.toast(TwolveForFiveDetailActivity.this,"最少需要追1期");
                    termNumEdit.setText("1");
//                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*1*TicketPrice+"");
                }else{
//                    price.setText(Integer.valueOf(num.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())*Integer.valueOf(multipleNumEdit.getText().toString())*TicketPrice+"");
                }
                calcluateNumAndPrice();
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
                Intent in=new Intent(mActivity,TwolveForFiveActivity.class);
                in.putExtra("sh_name",sh_name);
                in.putExtra("mode",mSelecte_Mode);
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
            mSelecte_Mode=data.getIntExtra("selecte_mode",1);

            one_normal=(List<NumInfo>) data.getSerializableExtra("result_one");
            two_normal=(List<NumInfo>)data.getSerializableExtra("result_two");
            three_normal=(List<NumInfo>)data.getSerializableExtra("result_three");

            sh_name=this.getIntent().getStringExtra("sh_name");

            TicketResultListInfo info=new TicketResultListInfo();
            info.setMode(mSelecte_Mode);
            info.setNum(data.getStringExtra("num"));
            info.setPrice(data.getStringExtra("price"));


            for(int i=0;i<one_normal.size();i++){
                if(one_normal.get(i).is_checked()){
                    info.getNumbers1().add(one_normal.get(i));
                }

            }
            for(int i=0;i<two_normal.size();i++){
                if(two_normal.get(i).is_checked()){
                    info.getNumbers2().add(two_normal.get(i));
                }

            }
            for(int i=0;i<three_normal.size();i++){
                if(three_normal.get(i).is_checked()){
                    info.getNumbers3().add(three_normal.get(i));
                }
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
        paramMap.put("lottery_type", type);
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
                Intent in=new Intent(mActivity,TwolveForFiveActivity.class);
                in.putExtra("sh_name",sh_name);
                startActivity(in);
                finish();
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
                Log.e("arg1",arg1);
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
            if(list_result.get(i).getNumbers3().size()>0){
                String s3="";
                for(int j=0;j<list_result.get(i).getNumbers3().size();j++){
                    if(j==0){
                        s3=s3+list_result.get(i).getNumbers3().get(j).getNum();
                    }else{
                        s3=s3+","+list_result.get(i).getNumbers3().get(j).getNum();
                    }
                }
                info.setS3(s3);
            }
            if(list_result.get(i).getMode()==N_H_OPTIONAL3){
                info.setS1(info.getS1()+","+info.getS2()+","+info.getS3());
                info.setS2(null);
                info.setS3(null);
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
            for(int i=0;i<info.getNumbers3().size();i++){
                nums.add(info.getNumbers3().get(i));
            }
            final TagFlowLayout numTag=holder.getView(R.id.num_tag);
            TagAdapter<NumInfo> numAdapter = new TagAdapter<NumInfo>(nums) {
                @Override
                public View getView(FlowLayout parent, int position, NumInfo s) {
                    TextView tv = (TextView) LayoutInflater.from(TwolveForFiveDetailActivity.this).inflate(R.layout.list_item_tv2, numTag, false);
                    if(s.is_spe()){
                        tv.setBackgroundResource(R.drawable.flow_layout_selector3);
                    }else{
                        tv.setBackgroundResource(R.drawable.flow_layout_selector2);
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
                case N_OPTIONAL2:
                    mode_tv.setText("普通-任选二");
                    break;
                case N_OPTIONAL3:
                    mode_tv.setText("普通-任选三");
                    break;
                case N_OPTIONAL4:
                    mode_tv.setText("普通-任选四");
                    break;
                case N_OPTIONAL5:
                    mode_tv.setText("普通-任选五");
                    break;
                case N_OPTIONAL6:
                    mode_tv.setText("普通-任选六");
                    break;
                case N_OPTIONAL7:
                    mode_tv.setText("普通-任选七");
                    break;
                case N_OPTIONAL8:
                    mode_tv.setText("普通-任选八");
                    break;
                case N_TOP_OPTIONAL:
                    mode_tv.setText("普通-前一");
                    break;
                case N_TOP_OPTIONAL2:
                    mode_tv.setText("普通-前二直选");
                    break;
                case N_TOP_G_OPTIONAL2:
                    mode_tv.setText("普通-前二组选");
                    break;
                case N_TOP_OPTIONAL3:
                    mode_tv.setText("普通-前三直选");
                    break;
                case N_TOP_G_OPTIONAL3:
                    mode_tv.setText("普通-前三组选");
                    break;
                case N_H_OPTIONAL3:
                    mode_tv.setText("普通-乐选三");
                    break;
                case N_H_OPTIONAL4:
                    mode_tv.setText("普通-乐选四");
                    break;
                case N_H_OPTIONAL5:
                    mode_tv.setText("普通-乐选五");
                    break;
                case S_OPTIONAL2:
                    mode_tv.setText("拖胆-任选二");
                    break;
                case S_OPTIONAL3:
                    mode_tv.setText("拖胆-任选三");
                    break;
                case S_OPTIONAL4:
                    mode_tv.setText("拖胆-任选四");
                    break;
                case S_OPTIONAL5:
                    mode_tv.setText("拖胆-任选五");
                    break;
                case S_OPTIONAL6:
                    mode_tv.setText("拖胆-任选六");
                    break;
                case S_OPTIONAL7:
                    mode_tv.setText("拖胆-任选七");
                    break;
                case S_OPTIONAL8:
                    mode_tv.setText("拖胆-任选八");
                    break;
                case S_TOP_G_OPTIONAL2:
                    mode_tv.setText("拖胆-前二组选");
                    break;
                case S_TOP_OPTIONAL2:
                    mode_tv.setText("拖胆-前二直选");
                    break;
                case S_TOP_G_OPTIONAL3:
                    mode_tv.setText("拖胆-前三组选");
                    break;
                case S_TOP_OPTIONAL3:
                    mode_tv.setText("拖胆-前三直选");
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
        int[] result3=null;
        int[] result4=null;
        List<NumInfo> result_list1=new ArrayList<>();
        List<NumInfo> result_list2=new ArrayList<>();
        List<NumInfo> result_list3=new ArrayList<>();
        switch (mSelecte_Mode){
            case N_OPTIONAL2:
                result=numberRandom(2);
                break;
            case N_OPTIONAL3:
                result=numberRandom(3);
                break;
            case N_OPTIONAL4:
                result=numberRandom(4);
                break;
            case N_OPTIONAL5:
                result=numberRandom(5);
                break;
            case N_OPTIONAL6:
                result=numberRandom(6);

                break;
            case N_OPTIONAL7:
                result=numberRandom(7);

                break;
            case N_OPTIONAL8:
                result=numberRandom(8);

                break;
            case N_TOP_OPTIONAL:
                result=numberRandom(1);
                break;
            case N_TOP_G_OPTIONAL2:
                result=numberRandom(2);

                break;
            case N_TOP_G_OPTIONAL3:
                result=numberRandom(3);

                break;
            case N_TOP_OPTIONAL2:
                result4=numberRandom(2);
                result=new int[1];
                result2=new int[1];
                result[0]=result4[0];
                result2[0]=result4[1];
                break;
            case N_TOP_OPTIONAL3:
                result4=numberRandom(3);
                result=new int[1];
                result2=new int[1];
                result3=new int[1];
                result[0]=result4[0];
                result2[0]=result4[1];
                result3[0]=result4[2];
                break;

            case N_H_OPTIONAL3:
                result4=numberRandom(3);
                result=new int[1];
                result2=new int[1];
                result3=new int[1];
                result[0]=result4[0];
                result2[0]=result4[1];
                result3[0]=result4[2];
                break;
            case N_H_OPTIONAL4:
                result=numberRandom(4);
                break;
            case N_H_OPTIONAL5:
                result=numberRandom(5);
                break;

        }
        if(result==null){
            ToastUtil.toast(this,"该玩法不支持机选");
            return;
        }
        for(int i=0;i<result.length;i++){
            NumInfo info=new NumInfo();
            if(result[i]>=9){
                info.setNum(result[i]+1+"");
            }else{
                info.setNum("0"+(result[i]+1));
            }
            result_list1.add(info);
        }
        if(result2!=null){
            for(int i=0;i<result2.length;i++){
                NumInfo info=new NumInfo();
                if(result2[i]>=9){
                    info.setNum(result2[i]+1+"");
                }else{
                    info.setNum("0"+(result2[i]+1));
                }
                result_list2.add(info);
            }
        }
        if(result3!=null){
            for(int i=0;i<result3.length;i++){
                NumInfo info=new NumInfo();
                if(result3[i]>=9){
                    info.setNum(result3[i]+1+"");
                }else{
                    info.setNum("0"+(result3[i]+1));
                }
                result_list3.add(info);
            }
        }

        TicketResultListInfo data=new TicketResultListInfo();
        data.setNumbers1(result_list1);
        data.setNumbers2(result_list2);
        data.setNumbers3(result_list3);
        data.setNum("1");
        if(mSelecte_Mode==N_H_OPTIONAL3){
            data.setPrice("6");
        }else if(mSelecte_Mode==N_H_OPTIONAL4){
            data.setPrice("10");
        }else if(mSelecte_Mode==N_H_OPTIONAL5){
            data.setPrice("14");
        }else{
            data.setPrice(TicketPrice+"");
        }
        data.setMode(mSelecte_Mode);
        list_result.add(data);
        adpater.notifyDataSetChanged();

        //重新计算金额和注数
        calcluateNumAndPrice();
    }

    /**
     * 机选号码
     */
    private int[] numberRandom(int select_num) {
        int suit[] = new int[select_num]; //存储select_num个随机数
        boolean sw[] = new boolean[11]; //随机数存在。则为真，否则为假

        int key = 0;
        for(int i = 0; i < suit.length; i++)
        {
            while(true)
            {
                key = (int)(Math.random()*11);
                if(sw[key] == false){
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
        int all_price=0;
        for(int i=0;i<list_result.size();i++){
            c_num=c_num+Integer.valueOf(list_result.get(i).getNum());

            all_price=all_price+Integer.valueOf(list_result.get(i).getPrice());

        }

        num.setText(c_num+"");
        if(TextUtils.isEmpty(multipleNumEdit.getText().toString()) && TextUtils.isEmpty(termNumEdit.getText().toString())){
            price.setText(all_price+"");
        }else if(TextUtils.isEmpty(multipleNumEdit.getText().toString())){
            price.setText(all_price*Integer.valueOf(termNumEdit.getText().toString())+"");
        } else if(TextUtils.isEmpty(termNumEdit.getText().toString())){
            price.setText(all_price*Integer.valueOf(multipleNumEdit.getText().toString())+"");
        } else{
            price.setText(all_price*Integer.valueOf(multipleNumEdit.getText().toString())*Integer.valueOf(termNumEdit.getText().toString())+"");
        }
    }
}
