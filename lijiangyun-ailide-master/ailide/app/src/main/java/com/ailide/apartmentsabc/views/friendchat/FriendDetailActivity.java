package com.ailide.apartmentsabc.views.friendchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.DrawEvent;
import com.ailide.apartmentsabc.eventbus.RefreshEvent;
import com.ailide.apartmentsabc.eventbus.RefreshFriendEvent;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.BaseDateBean;
import com.ailide.apartmentsabc.model.CheckFriendBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.utils.WordAndPicture;
import com.ailide.apartmentsabc.views.addequitment.AddEquitmentActivity;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.MainActivity;
import com.ailide.apartmentsabc.views.mine.SetNameActivity;
import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendDetailActivity extends BaseActivity {

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
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.confirm)
    TextView confirm;

    private UserBean userBean;
    private PopupWindow popupWindow;
    private String note_num;
    private CheckFriendBean checkFriendBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        ButterKnife.bind(this);
        note_num = getIntent().getStringExtra("note_num");
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString()))
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
        addFriend(note_num);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != popupWindow)
            popupWindow.dismiss();
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
                            checkFriendBean  = JSON.parseObject(response.body(),CheckFriendBean.class);
                            if(checkFriendBean.getStatus() == 1){
                                if (!TextUtils.isEmpty(checkFriendBean.getData().getProfile_image_url())) {
                                    NetWorkImageLoader.loadGaussianImage(null, checkFriendBean.getData().getProfile_image_url(), headImg);
                                }
                                if (!TextUtils.isEmpty(checkFriendBean.getData().getScreen_name()))
                                    name.setText(checkFriendBean.getData().getScreen_name());
                                if(checkFriendBean.getData().getGender() ==1){
                                    sexImg.setBackgroundResource(R.drawable.hyzl_nan);
                                }else {
                                    sexImg.setBackgroundResource(R.drawable.hyzl_nv);
                                }
                                if (!TextUtils.isEmpty(checkFriendBean.getData().getNote_num()))
                                    zhitiaoNum.setText("熊号：" + checkFriendBean.getData().getNote_num());
                                if (!TextUtils.isEmpty(checkFriendBean.getData().getSignature())){
                                    siginature.setText("个性签名：" + checkFriendBean.getData().getSignature());
                                }else {
                                    siginature.setText("个性签名：无");
                                }
                                if(!TextUtils.isEmpty(checkFriendBean.getData().getAddress()))
                                    address.setText("所在地：" + checkFriendBean.getData().getAddress());
                                if(userBean.getData().getId()==checkFriendBean.getData().getId()){
                                    more.setVisibility(View.GONE);
                                    confirm.setVisibility(View.GONE);
                                }

                            }else {
                                toast(checkFriendBean.getMsg());
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.back, R.id.confirm,R.id.more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                doBack();
                break;
            case R.id.confirm:
                Intent intent = new Intent(this, FriendChatActivity.class);
                intent.putExtra("fid",checkFriendBean.getData().getId());
                intent.putExtra("title",checkFriendBean.getData().getScreen_name());
                jumpToOtherActivity(intent, false);
                break;
            case R.id.more:
                showPopView();
                break;
        }
    }
    private void showPopView() {
        View layout = getLayoutInflater().inflate(R.layout.pop_friend_detail, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, back, 1, 0, 1, 0.5f, true);
        TextView clear = layout.findViewById(R.id.clear);
        TextView delete = layout.findViewById(R.id.delete);
        TextView cancel = layout.findViewById(R.id.cancel);
        TextView set_remark_name = layout.findViewById(R.id.set_remark_name);
        set_remark_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendDetailActivity.this, SetNameActivity.class);
                intent.putExtra("name",checkFriendBean.getData().getScreen_name());
                intent.putExtra("fid",checkFriendBean.getData().getId()+"");
                jumpToOtherActivity(intent,false);
                popupWindow.dismiss();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                cleanChat(checkFriendBean.getData().getId()+"");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                deleteFriend(checkFriendBean.getData().getId()+"");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    private void deleteFriend(String friend_id){
        showLoading("加载中...");
        OkGo.<String>post(Urls.DELETE_FRIEND)//
                .tag(this)//
                .params("id", userBean.getData().getId())//
                .params("friend_id",friend_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        Logger.e("eeeeeee",response.body());
                        if (response.isSuccessful()) {
                            BaseDateBean baseDateBean = JSON.parseObject(response.body(),BaseDateBean.class);
                            if(baseDateBean.getStatus()==1){
                                toast("删除成功");
                                doBack();
                                RefreshEvent refreshEvent = new RefreshEvent();
                                EventBus.getDefault().post(refreshEvent);
                            }else {
                                toast(baseDateBean.getMsg());
                            }
                        }
                    }
                });
    }
    private void cleanChat(String friend_id){
        showLoading("加载中...");
        OkGo.<String>post(Urls.CLEAN_CHAT)//
                .tag(this)//
                .params("mid", userBean.getData().getId())//
                .params("friend_id",friend_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        Logger.e("eeeeeee",response.body());
                        if (response.isSuccessful()) {
                            BaseDateBean baseDateBean = JSON.parseObject(response.body(),BaseDateBean.class);
                            if(baseDateBean.getStatus()==1){
                                toast("清除成功");
                            }else {
                                toast(baseDateBean.getMsg());
                            }
                        }
                    }
                });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshFriendEvent event) {
        addFriend(note_num);
    }
}
