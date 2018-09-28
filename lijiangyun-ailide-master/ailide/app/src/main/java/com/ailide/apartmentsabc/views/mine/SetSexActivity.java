package com.ailide.apartmentsabc.views.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetSexActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.sex_nan)
    ImageView sexNan;
    @BindView(R.id.sex_nan_ly)
    LinearLayout sexNanLy;
    @BindView(R.id.sex_nv)
    ImageView sexNv;
    @BindView(R.id.sex_nv_ly)
    LinearLayout sexNvLy;

    private String gender="2";
    private UserBean userBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_sex);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("设置性别");
        tvIncludeRight.setText("完成");
        tvIncludeRight.setVisibility(View.VISIBLE);
        ivIncludeRight.setVisibility(View.GONE);
        initView();
    }

    private void initView(){
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString())){
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
            if(userBean.getData().getGender()==1){
                sexNan.setVisibility(View.VISIBLE);
                sexNv.setVisibility(View.GONE);
                gender = "1";
            }else if(userBean.getData().getGender() == 2){
                sexNan.setVisibility(View.GONE);
                sexNv.setVisibility(View.VISIBLE);
                gender = "2";
            }
        }
    }

    @OnClick({R.id.iv_include_back, R.id.sex_nan_ly, R.id.sex_nv_ly,R.id.tv_include_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.sex_nan_ly:
                sexNan.setVisibility(View.VISIBLE);
                sexNv.setVisibility(View.GONE);
                gender = "1";
                break;
            case R.id.sex_nv_ly:
                sexNan.setVisibility(View.GONE);
                sexNv.setVisibility(View.VISIBLE);
                gender = "2";
                break;
            case R.id.tv_include_right:
                post();
                break;
        }
    }
    private void post() {
        showLoading("加载中...");
        OkGo.<String>post(Urls.SETUSERINFO)//
                .tag(this)//
                .params("id", userBean.getData().getId()+"")//
                .params("gender", gender)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            userBean.getData().setGender(Integer.parseInt(gender));
                            SPUtil.put(SetSexActivity.this,"user", JSON.toJSONString(userBean));
                            doBack();
                        }
                    }
                });
    }
}
