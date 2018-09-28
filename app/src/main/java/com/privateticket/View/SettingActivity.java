package com.privateticket.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daotian.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.privateticket.Base.App;
import com.privateticket.Http.ParamUtil;
import com.privateticket.Http.ServiceInterface;
import com.privateticket.Http.TicketService;
import com.privateticket.Model.ResultBO;

import com.privateticket.Utils.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yzx on 16/11/15.
 */

public class SettingActivity extends AppCompatActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.setting_action_change_pwd)
    LinearLayout settingActionChangePwd;
    @BindView(R.id.setting_txt_version)
    TextView settingTxtVersion;
    @BindView(R.id.setting_action_version)
    LinearLayout settingActionVersion;
    @BindView(R.id.setting_action_logout)
    TextView settingActionLogout;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mActivity=this;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.back,R.id.setting_action_change_pwd, R.id.setting_action_version, R.id.setting_action_logout})
    public void onClick(View view) {
        Intent in;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.setting_action_change_pwd:
                if(App.mUser==null || App.mUser.getAccess_token()==null){
                    ToastUtil.toast(mActivity,"请先登录！");
                    in=new Intent(this,LoginActivity.class);
                    startActivity(in);
                    return;
                }
                in=new Intent(this,ForgetPwdActivity.class);
                startActivity(in);
                break;
            case R.id.setting_action_version:

                break;
            case R.id.setting_action_logout:
                in=new Intent(mActivity,LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();
                break;
        }
    }

    private void out() {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        RequestParams params = ParamUtil.requestParams(paramMap);
        TicketService.post(params, ServiceInterface.Out, new AsyncHttpResponseHandler() {
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

                Intent in=new Intent(mActivity,LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
}
