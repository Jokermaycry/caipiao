package com.privateticket.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privateticket.Base.App;
import com.privateticket.Base.Constant;
import com.privateticket.Http.ParamUtil;
import com.privateticket.Http.ServiceInterface;
import com.privateticket.Http.TicketService;
import com.privateticket.Model.UserBO;
import com.privateticket.Model.ResultBO;
import com.daotian.R;
import com.privateticket.Utils.ACache;
import com.privateticket.Utils.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yzx on 16/11/15.
 */

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.login_edit_username)
    EditText loginEditUsername;
    @BindView(R.id.tel_clear)
    ImageView telClear;
    @BindView(R.id.login_edit_pwd)
    EditText loginEditPwd;
    @BindView(R.id.show_pwd)
    ImageView showPwd;
    @BindView(R.id.login_btn_login)
    TextView loginBtnLogin;
    @BindView(R.id.login_btn_findpwd)
    TextView loginBtnFindpwd;

    private boolean mShowPwd=false;
    private Activity mActivity;
    private UserBO mUserInfo;
    private String acount,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mActivity = this;
        acount=getIntent().getStringExtra("acount");
        pwd=getIntent().getStringExtra("pwd");
        initView();
    }

    private void initView() {
        if(!TextUtils.isEmpty(acount)){
            loginEditUsername.setText(acount);
            loginEditUsername.setSelection(acount.length());
        }
        if(!TextUtils.isEmpty(pwd)){
            loginEditPwd.setText(pwd);
        }
        loginEditUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    telClear.setVisibility(View.VISIBLE);
                } else {
                    telClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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


    @OnClick({R.id.back,R.id.login_btn_register,R.id.tel_clear, R.id.show_pwd, R.id.login_btn_login, R.id.login_btn_findpwd})
    public void onClick(View view) {
        Intent in;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tel_clear:
                loginEditUsername.setText("");
                break;
            case R.id.show_pwd:
                if(mShowPwd){
                    loginEditPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPwd.setImageResource(R.drawable.login_eyeon);
                    mShowPwd=false;
                }else{
                    loginEditPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPwd.setImageResource(R.drawable.login_eyeoff);
                    mShowPwd=true;
                }
                loginEditPwd.setSelection(loginEditPwd.getText().length());
                break;
            case R.id.login_btn_login:
                String acount=loginEditUsername.getText().toString();
                String pwd=loginEditPwd.getText().toString();
                if(TextUtils.isEmpty(acount)){
                    ToastUtil.toast(mActivity,"请输入账号");
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    ToastUtil.toast(mActivity,"请输入密码");
                    return;
                }
                login(acount,pwd);
                break;
            case R.id.login_btn_findpwd:
                in=new Intent(this,ForgetPwdActivity.class);
                startActivity(in);
                break;
            case R.id.login_btn_register:
                in=new Intent(this,RegisterActivity.class);
                startActivity(in);
                break;
        }
    }

    /**
     * 登录
     */
    private void login(final String account, final String pwd) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("account", account);
        paramMap.put("pwd", pwd);
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.Login, new AsyncHttpResponseHandler() {
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
                mUserInfo = JSON.parseObject(resultBO.getResultData(), UserBO.class);
                if(mUserInfo!=null){
                    final ACache mCache = ACache.get(LoginActivity.this);
                    mCache.put(Constant.ACACHE_USERINFO, mUserInfo);
                    mCache.put(Constant.ACACHE_USER_NAME, account);
                    mCache.put(Constant.ACACHE_USER_PWD, pwd);
                    mCache.put(Constant.ACACHE_USER_TOKEN, mUserInfo.getAccess_token());

                    App.mUser=mUserInfo;
                    Intent in=new Intent(mActivity,BaseActivity.class);
                    startActivity(in);
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ToastUtil.toast(mActivity, "请求失败:" + responseBody);
            }
        });
    }
}
