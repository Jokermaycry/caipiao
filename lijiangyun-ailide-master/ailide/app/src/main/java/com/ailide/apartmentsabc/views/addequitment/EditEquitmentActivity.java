package com.ailide.apartmentsabc.views.addequitment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditEquitmentActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.edit_equitment_edit)
    EditText editEquitmentEdit;
    @BindView(R.id.edit_equitment_number)
    TextView editEquitmentNumber;
    @BindView(R.id.edit_equitment_address)
    TextView editEquitmentAddress;
    @BindView(R.id.edit_equitment_banben)
    TextView editEquitmentBanben;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equitment);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("编辑设备");
        tvIncludeRight.setText("保存");
    }

    @OnClick({R.id.iv_include_back, R.id.tv_include_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.tv_include_right:
                break;
        }
    }
}
