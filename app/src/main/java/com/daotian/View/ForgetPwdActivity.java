package com.daotian.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.daotian.Base.App;
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

public class ForgetPwdActivity extends AppCompatActivity {


    @BindView(R.id.old_pwd)
    EditText oldPwd;
    @BindView(R.id.new_pwd)
    EditText newPwd;
    @BindView(R.id.new_pwd_comfirm)
    EditText newPwdComfirm;
    @BindView(R.id.btn)
    TextView btn;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        ButterKnife.bind(this);
        mActivity=this;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    public void post(){
        if(TextUtils.isEmpty(oldPwd.getText().toString()) || !oldPwd.getText().toString().equals(App.mUser.getPwd())){
            ToastUtil.toast(mActivity,"请输入正确的原密码");
            return;
        }
        if(TextUtils.isEmpty(newPwd.getText().toString()) || !newPwd.getText().toString().equals(newPwdComfirm.getText().toString())){
            ToastUtil.toast(mActivity,"两次输入新密码不同");
            return;
        }
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("old_pass",oldPwd.getText().toString());
        paramMap.put("new_pass",newPwd.getText().toString());
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.SaveInfo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.e("init_result", result);
                if (!TextUtils.isEmpty(result)) {
                    result = ParamUtil.unicodeToChinese(result);
                }
                ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
                ToastUtil.toast(mActivity,resultBO.getResultMsg());
                if (resultBO.getResultId() != 0) {
                    return;
                }
                Intent intent=new Intent(mActivity,LoginActivity.class);
                intent.putExtra("acount",App.mUser.getAccount());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String arg1 = new String(responseBody);
                ToastUtil.toast(mActivity, "请求失败:" + arg1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.back, R.id.title,R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title:
                break;
            case R.id.btn:
                post();
                break;
        }
    }
}
