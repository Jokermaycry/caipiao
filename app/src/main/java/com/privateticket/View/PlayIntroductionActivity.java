package com.privateticket.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.daotian.R;
import com.privateticket.Base.Constant;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 玩法说明
 * Created by yzx on 16/11/15.
 */

public class PlayIntroductionActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.introduction)
    TextView introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playintroduction);
        ButterKnife.bind(this);
        if(Constant.mSetting!=null){
            introduction.setText(Constant.mSetting.getPlay_setting());
        }else{
            introduction.setText("玩法说明：敬请期待！");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
