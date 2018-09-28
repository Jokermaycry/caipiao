package com.ailide.apartmentsabc.views.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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


public class SetAddressActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.set_name_edit)
    EditText setNameEdit;
    @BindView(R.id.set_name_delete)
    ImageView setNameDelete;

    private UserBean userBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("设置地址");
        tvIncludeRight.setText("完成");
        tvIncludeRight.setVisibility(View.VISIBLE);
        ivIncludeRight.setVisibility(View.GONE);
        initView();
    }

    private void initView(){
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString())){
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
            setNameEdit.setText(userBean.getData().getAddress());
            setNameEdit.setSelection(setNameEdit.getText().toString().length());
            if(TextUtils.isEmpty(setNameEdit.getText().toString().trim()))
                setNameDelete.setVisibility(View.GONE);
        }
        setNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()>0){
                    setNameDelete.setVisibility(View.VISIBLE);
                }else {
                    setNameDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.iv_include_back, R.id.tv_include_right, R.id.set_name_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.tv_include_right:
                post();
                break;
            case R.id.set_name_delete:
                setNameEdit.setText("");
                break;
        }
    }
    private void post() {
        if(TextUtils.isEmpty(setNameEdit.getText().toString().trim())){
            toast("请输入地址");
            return;
        }
        showLoading("加载中...");
        OkGo.<String>post(Urls.SETUSERINFO)//
                .tag(this)//
                .params("id", userBean.getData().getId()+"")//
                .params("address", setNameEdit.getText().toString().trim())//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            userBean.getData().setAddress(setNameEdit.getText().toString().trim());
                            SPUtil.put(SetAddressActivity.this,"user", JSON.toJSONString(userBean));
                            doBack();
                        }
                    }
                });
    }
}
