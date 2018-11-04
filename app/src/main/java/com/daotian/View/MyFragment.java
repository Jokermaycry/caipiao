package com.daotian.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daotian.Base.App;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.ResultBO;
import com.daotian.Model.UserBO;
import com.daotian.R;
import com.daotian.Utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 *
 */
public class MyFragment extends Fragment {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.setting_btn)
    ImageView settingBtn;
    @BindView(R.id.head_ly)
    RelativeLayout headLy;
    @BindView(R.id.ll_personal_data_edit)
    RelativeLayout llPersonalDataEdit;
    @BindView(R.id.my_ticket_ly)
    LinearLayout myTicketLy;
    @BindView(R.id.win_ly)
    LinearLayout winLy;
    @BindView(R.id.chase_num_ly)
    LinearLayout chaseNumLy;
    @BindView(R.id.money_ly)
    LinearLayout moneyLy;
    @BindView(R.id.login_btn)
    TextView loginBtn;
    @BindView(R.id.no_login_ly)
    RelativeLayout noLoginLy;
    @BindView(R.id.user_icon)
    ImageView userIcon;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.frz_balance)
    TextView frzBalance;
    @BindView(R.id.my_child_ly)
    LinearLayout myChildLy;
    @BindView(R.id.month_balance)
    TextView monthBalance;
    @BindView(R.id.stream_ly)
    LinearLayout streamLy;
    @BindView(R.id.puke)
    LinearLayout puke;
    private Activity mActivity;
    private String TAG = "fragment_my";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        initView();
        Log.e(TAG, "onCreateView");
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();

    }

    private void initData() {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.getCenter, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.e("init_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                if (resultBO.getResultId() != 0) {
                    return;
                }
                UserBO user = JSON.parseObject(resultBO.getResultData(), UserBO.class);
                balance.setText("余额:" + user.getBalance());
                frzBalance.setText("冻结:" + user.getFreeze_balance());
                if (!TextUtils.isEmpty(user.getBetting())) {
                    monthBalance.setText("月投:" + user.getBetting());
                } else {
                    monthBalance.setText("月投:0");
                }

                Log.e("MyFrament", "数据初始化");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ToastUtil.toast(mActivity, "请求失败:" + responseBody);
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        if (App.mUser == null || App.mUser.getAccess_token() == null) {
            return;
        }
        llPersonalDataEdit.setVisibility(View.VISIBLE);
        noLoginLy.setVisibility(View.GONE);
//        if (!TextUtils.isEmpty(App.mUser.getPid()) && App.mUser.getPid().equals("0")) {
//            myChildLy.setVisibility(View.VISIBLE);
//        }

        userName.setText(App.mUser.getAccount());
        balance.setText("剩余:" + App.mUser.getBalance());
        frzBalance.setText("冻结:" + App.mUser.getFreeze_balance());
        if (!TextUtils.isEmpty(App.mUser.getBetting())) {
            monthBalance.setText("月投注额:" + App.mUser.getBetting());
        } else {
            monthBalance.setText("月投注额:0");
        }

    }



    @OnClick({R.id.login_btn,R.id.puke, R.id.head_ly, R.id.my_ticket_ly, R.id.win_ly, R.id.chase_num_ly, R.id.money_ly, R.id.setting_btn, R.id.my_child_ly, R.id.stream_ly, R.id.twolve_ly})
    public void onClick(View view) {
        Intent in;
        switch (view.getId()) {
            case R.id.login_btn:
                in = new Intent(mActivity, LoginActivity.class);
                startActivity(in);
                break;
            case R.id.head_ly:

                break;
            case R.id.my_ticket_ly:
                checkLogin();
                in = new Intent(mActivity, OrderActivity.class);
                in.putExtra("current", 0);
                in.putExtra("type", 1);
                startActivity(in);
                break;
            case R.id.puke:
                checkLogin();
                in = new Intent(mActivity, OrderActivity.class);
                in.putExtra("current", 0);
                in.putExtra("type", 4);
                startActivity(in);
                break;
            case R.id.twolve_ly:
                checkLogin();
                in = new Intent(mActivity, OrderActivity.class);
                in.putExtra("current", 0);
                in.putExtra("type", 3);
                startActivity(in);
                break;
            case R.id.win_ly:
                checkLogin();
                in = new Intent(mActivity, OrderActivity.class);
                in.putExtra("current", 0);
                in.putExtra("type", 2);
                startActivity(in);
                break;
            case R.id.chase_num_ly:
                checkLogin();
                in = new Intent(mActivity, OrderActivity.class);
                in.putExtra("current", 4);
                startActivity(in);
                break;
            case R.id.money_ly:
                checkLogin();
                in = new Intent(mActivity, FundActivity.class);
                startActivity(in);
                break;
            case R.id.setting_btn:
                in = new Intent(mActivity, SettingActivity.class);
                startActivity(in);
                break;
            //子账号页面
            case R.id.my_child_ly:
                checkLogin();
                in = new Intent(mActivity, MChildActivity.class);
                startActivity(in);
                break;
            //个人流水
            case R.id.stream_ly:
                in = new Intent(mActivity, SteamActivity.class);
                startActivity(in);
                break;
        }
    }

    public void checkLogin() {
        if (App.mUser == null && App.mUser.getAccess_token() == null) {
            Intent in = new Intent(mActivity, LoginActivity.class);
            startActivity(in);
            return;
        }
    }
}
