package com.ailide.apartmentsabc.views.friendchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.CheckFriendBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.friend_search)
    LinearLayout friendSearch;
    @BindView(R.id.friend_add)
    TextView friendAdd;
    @BindView(R.id.ten_friend_ly)
    LinearLayout tenFriendLy;
    @BindView(R.id.search_edit)
    EditText searchEdit;

    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString()))
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
    }

    private void addFriend(String note_num) {
        showLoading("加载中...");
        OkGo.<String>post(Urls.CHECK_FRIEND)//
                .tag(this)//
                .params("id", userBean.getData().getId())//
                .params("note_num", note_num)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("check_friend", response.body());
                            CheckFriendBean checkFriendBean  = JSON.parseObject(response.body(),CheckFriendBean.class);
                            Intent intent = new Intent(AddFriendActivity.this,AddFriendResultActivity.class);
                            intent.putExtra("result",JSON.toJSONString(checkFriendBean.getData()));
                            jumpToOtherActivity(intent,false);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_include_back, R.id.friend_add, R.id.ten_friend_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.friend_add:
                if(!TextUtils.isEmpty(searchEdit.getText().toString().trim())){
                    addFriend(searchEdit.getText().toString().trim());
                }else {
                    toast("请输入好友熊号");
                }
                break;
            case R.id.ten_friend_ly:
                jumpToOtherActivity(new Intent(this,TenFriendActivity.class),false);
                break;
        }
    }
}
