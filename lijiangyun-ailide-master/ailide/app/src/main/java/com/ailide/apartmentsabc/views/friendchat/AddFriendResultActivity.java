package com.ailide.apartmentsabc.views.friendchat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.BaseDateBean;
import com.ailide.apartmentsabc.model.CheckFriendBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendResultActivity extends BaseActivity {

    @BindView(R.id.head_img)
    ImageView headImg;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex_img)
    ImageView sexImg;
    @BindView(R.id.zhitiao_num)
    TextView zhitiaoNum;
    @BindView(R.id.siginature)
    TextView siginature;
    @BindView(R.id.result_edit)
    EditText resultEdit;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    private CheckFriendBean.DataBean checkFriendBean;
    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_result);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucentForImageView(this, 1, relativeLayout);
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString()))
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        initDate();
    }

    private void initDate() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("result"))) {
            checkFriendBean = JSON.parseObject(getIntent().getStringExtra("result"), CheckFriendBean.DataBean.class);
            if (!TextUtils.isEmpty(checkFriendBean.getProfile_image_url())) {
                NetWorkImageLoader.loadGaussianImage(null, checkFriendBean.getProfile_image_url(), headImg);
            }
            if (!TextUtils.isEmpty(checkFriendBean.getScreen_name()))
                name.setText(checkFriendBean.getScreen_name());
            if (!TextUtils.isEmpty(checkFriendBean.getNote_num()))
                zhitiaoNum.setText("熊号：" + checkFriendBean.getNote_num());
            if (!TextUtils.isEmpty(checkFriendBean.getSignature()))
                siginature.setText("个性签名：" + checkFriendBean.getSignature());
        }
    }

    @OnClick({R.id.back, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                doBack();
                break;
            case R.id.confirm:
                if (checkFriendBean != null)
                    addFriend(checkFriendBean.getId() + "");
                break;
        }
    }

    private void addFriend(String id) {
        showLoading("加载中...");
        OkGo.<String>post(Urls.ADD_RRIEND)//
                .tag(this)//
                .params("mid", userBean.getData().getId())//
                .params("friend_id", id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("check_friend", response.body());
                            BaseDateBean baseDateBean = JSON.parseObject(response.body(), BaseDateBean.class);
                            if(baseDateBean.getStatus()==1){
                                toast("添加好友成功");
                                doBack();
                            }else {
                                toast(baseDateBean.getMsg());
                            }
                        }
                    }
                });
    }
}
