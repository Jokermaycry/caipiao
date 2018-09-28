package com.daotian.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.ResultBO;
import com.daotian.R;
import com.daotian.Utils.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yzx on 16/11/15.
 */

public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.regist_edit_mobile)
    EditText registEditMobile;
    @BindView(R.id.regist_edit_pwd)
    EditText registEditPwd;
    @BindView(R.id.register_btn)
    TextView registerBtn;
    @BindView(R.id.regist_edit_pwd_comfirm)
    EditText registEditPwdComfirm;
    @BindView(R.id.invite_code)
    EditText inviteCode;
    private Activity mActivity;
//    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mActivity = this;
//        time = new TimeCount(60000, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.register_btn})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.regist_btn_get_code:
//                sendCode();
//                break;
            case R.id.register_btn:
                register();
                break;
        }
    }

    private void register() {
        if (TextUtils.isEmpty(registEditMobile.getText().toString()) || registEditMobile.getText().toString().length() < 6) {
            ToastUtil.toast(mActivity, "账号长度不得小于6位数");
            return;
        }
        if (TextUtils.isEmpty(registEditPwd.getText().toString()) || registEditMobile.getText().toString().length() < 6) {
            ToastUtil.toast(mActivity, "密码长度不得小于6位");
            return;
        }
        if (!registEditPwd.getText().toString().equals(registEditPwdComfirm.getText().toString())) {
            ToastUtil.toast(mActivity, "两次密码不一致");
            return;
        }
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("account", registEditMobile.getText().toString());
        paramMap.put("pwd", registEditPwd.getText().toString());
        if(!TextUtils.isEmpty(inviteCode.getText().toString())){
            paramMap.put("qrcode", inviteCode.getText().toString());
        }
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.Register, new AsyncHttpResponseHandler() {
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
                    return;
                }
                Intent in = new Intent(mActivity, LoginActivity.class);
                in.putExtra("acount", registEditMobile.getText().toString());
                in.putExtra("pwd", registEditPwd.getText().toString());
                startActivity(in);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
            }
        });
    }

//    private void sendCode() {
//        if (TextUtils.isEmpty(registEditMobile.getText().toString()) || registEditMobile.getText().toString().length() != 11) {
//            ToastUtil.toast(mActivity, "请输入正确的手机号码");
//            return;
//        }
//        HashMap<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("tel", registEditMobile.getText().toString());
//        RequestParams params = ParamUtil.requestParams(paramMap);
//        TicketService.post(params, ServiceInterface.sendMsg, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String result = new String(responseBody);
//                Log.e("init_result", result);
//                if (!TextUtils.isEmpty(result)) {
//                    result = ParamUtil.unicodeToChinese(result);
//                }
//                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
//                if (resultBO.getResultId() != 0) {
//                    return;
//                }
//                time.start();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                String arg1 = new String(responseBody);
//                ToastUtil.toast(mActivity, "请求失败:" + arg1);
//            }
//        });
//    }

//    class TimeCount extends CountDownTimer {
//        public TimeCount(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//
//        @Override
//        public void onFinish() {
//            registBtnGetCode.setText("获取验证码");
//            registBtnGetCode.setBackgroundResource(R.drawable.btn_bg_getcode);
//            registBtnGetCode.setTextColor(Color.parseColor("#ffffff"));
//            registBtnGetCode.setClickable(true);
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            registBtnGetCode.setClickable(false);
//            registBtnGetCode.setBackgroundResource(R.drawable.btn_bg_waitcode);
//            registBtnGetCode.setTextColor(Color.parseColor("#ffffff"));
//            registBtnGetCode.setText(millisUntilFinished / 1000 + "秒");
//        }
//    }
}
