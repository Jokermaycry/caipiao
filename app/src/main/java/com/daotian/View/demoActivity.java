package com.daotian.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daotian.R;

import butterknife.ButterKnife;

/**
 * Created by yzx on 16/11/15.
 */

public class demoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        ButterKnife.bind(this);
    }
    @Override
    protected void onResume( ) {
        super.onResume();

    }
    @Override
    protected void onDestroy( ) {
        super.onDestroy();
    }


}
