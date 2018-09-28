package com.ailide.apartmentsabc.views.sticker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.ToastUtil;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.ailide.apartmentsabc.views.material.MyMaterialActivity;
import com.alibaba.fastjson.JSON;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class PicToolFragment extends BaseSimpleFragment {

    public static final int REQUEST_CAMERA_CODE = 100;
    public static final int REQUEST_LIST_CODE = 200;

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pic_tool;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
    }

    public void init() {
        mLlContent.setVisibility(View.VISIBLE);
    }

    @OnClick({ R.id.ll_photo, R.id.ll_album, R.id.ll_material})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_photo:
                onClickPhoto();
                break;
            case R.id.ll_album:
                onClickAlbum();
                break;
            case R.id.ll_material:
                onClickMaterial();
                break;
        }
    }

    public void onClickPhoto() {
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        ISCameraConfig config = new ISCameraConfig.Builder()
//                .needCrop(false) // 裁剪
//                .cropSize(1, 1, width, width)
                .build();
        ISNav.getInstance().toCameraActivity(this, config, REQUEST_CAMERA_CODE);
    }

    public void onClickAlbum() {
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
// 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(true)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.WHITE)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.drawable.ic_back)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                // 裁剪大小。needCrop为true的时候配置
//                .cropSize(1, 1, width, width)
//                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(3)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }

    public void onClickMaterial() {
        UserBean userBean = JSON.parseObject(SPUtil.get(mActivity, "user", "").toString(), UserBean.class);
        if(userBean == null){
            ToastUtil.toast("您还未登录");
        }else {
            mActivity.jumpToOtherActivity(new Intent(mActivity, MyMaterialActivity.class), false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("result"); // 图片地址
            EventBus.getDefault().post(new EventBusEntity(EventConstant.TAKE_PHOTO, path));
        }
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            for (String path : pathList) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.PICTURE_LIST, path));
            }
        }
    }
}