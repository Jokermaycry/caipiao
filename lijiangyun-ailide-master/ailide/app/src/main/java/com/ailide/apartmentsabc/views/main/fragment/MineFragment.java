package com.ailide.apartmentsabc.views.main.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.ailide.apartmentsabc.views.login.LoginActivity;
import com.ailide.apartmentsabc.views.mine.CommonProListActivity;
import com.ailide.apartmentsabc.views.mine.HtmlActivity;
import com.ailide.apartmentsabc.views.mine.MyEquitmentActivity;
import com.ailide.apartmentsabc.views.mine.PersonalActivity;
import com.ailide.apartmentsabc.views.mine.SettingActivity;
import com.alibaba.fastjson.JSON;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseSimpleFragment {


    @BindView(R.id.mine_setting)
    ImageView mineSetting;
    @BindView(R.id.mine_head_img)
    ImageView mineHeadImg;
    @BindView(R.id.mine_wexin)
    ImageView mineWexin;
    @BindView(R.id.mine_qq)
    ImageView mineQq;
    @BindView(R.id.mine_equitment)
    LinearLayout mineEquitment;
    @BindView(R.id.mine_use_directions)
    LinearLayout mineUseDirections;
    @BindView(R.id.mine_wenti)
    LinearLayout mineWenti;
    @BindView(R.id.mine_kefu)
    LinearLayout mineKefu;
    @BindView(R.id.mine_name)
    TextView mineName;
    @BindView(R.id.mine_head_img_rl)
    RelativeLayout mineHeadImgRl;
    @BindView(R.id.mine_qqorweixin_rl)
    RelativeLayout mineQqorweixinRl;
    @BindView(R.id.mine_xiong_hao)
    TextView mineXiongHao;

    private UserBean userBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {



    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(SPUtil.get(mActivity, "user", "").toString())) {
            userBean = JSON.parseObject(SPUtil.get(mActivity, "user", "").toString(), UserBean.class);
            mineXiongHao.setText("熊号："+userBean.getData().getNote_num());
            if (!TextUtils.isEmpty(userBean.getData().getScreen_name())) {
                mineName.setText(userBean.getData().getScreen_name());
            } else {
                mineName.setText("未登录");
            }

            if (!TextUtils.isEmpty(userBean.getData().getProfile_image_url()))
                NetWorkImageLoader.loadCircularImage(null, userBean.getData().getProfile_image_url(), mineHeadImg,R.drawable.icon_circle_mrtx,R.drawable.icon_circle_mrtx);
            mineQqorweixinRl.setVisibility(View.GONE);
            mineHeadImgRl.setVisibility(View.VISIBLE);
        } else {
            mineQqorweixinRl.setVisibility(View.VISIBLE);
            mineHeadImgRl.setVisibility(View.GONE);
            mineXiongHao.setText("");
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }


    @OnClick({R.id.mine_setting, R.id.mine_wexin, R.id.mine_qq, R.id.mine_equitment, R.id.mine_use_directions, R.id.mine_wenti, R.id.mine_kefu, R.id.mine_head_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_setting:
                mActivity.jumpToOtherActivity(new Intent(mActivity, SettingActivity.class), false);
                break;
            case R.id.mine_wexin:
                mActivity.jumpToOtherActivity(new Intent(mActivity, LoginActivity.class), false);
                break;
            case R.id.mine_qq:
                mActivity.jumpToOtherActivity(new Intent(mActivity, LoginActivity.class), false);
                break;
            case R.id.mine_equitment:
                mActivity.jumpToOtherActivity(new Intent(mActivity, MyEquitmentActivity.class), false);
                break;
            case R.id.mine_use_directions:
                mActivity.jumpToOtherActivity(new Intent(mActivity, HtmlActivity.class), false);
                break;
            case R.id.mine_wenti:
                mActivity.jumpToOtherActivity(new Intent(mActivity, CommonProListActivity.class), false);
                break;
            case R.id.mine_kefu:
                Intent intent = new Intent(mActivity, HtmlActivity.class);
                intent.putExtra("witch_html", "1");
                mActivity.jumpToOtherActivity(intent, false);
                break;
            case R.id.mine_head_img:
                mActivity.jumpToOtherActivity(new Intent(mActivity, PersonalActivity.class), false);
                break;
        }
    }
}
