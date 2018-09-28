package com.ailide.apartmentsabc.views.mine;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ailide.apartmentsabc.model.PictureBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.ImagePickerImageLoader;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.TimeUtils;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.R;
import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class PersonalActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.personal_head)
    ImageView personalHead;
    @BindView(R.id.personal_head_ly)
    LinearLayout personalHeadLy;
    @BindView(R.id.personal_name)
    TextView personalName;
    @BindView(R.id.personal_name_ly)
    LinearLayout personalNameLy;
    @BindView(R.id.personal_sex)
    TextView personalSex;
    @BindView(R.id.personal_sex_ly)
    LinearLayout personalSexLy;
    @BindView(R.id.personal_birthday)
    TextView personalBirthday;
    @BindView(R.id.personal_birthday_ly)
    LinearLayout personalBirthdayLy;
    @BindView(R.id.personal_address)
    TextView personalAddress;
    @BindView(R.id.personal_address_ly)
    LinearLayout personalAddressLy;
    @BindView(R.id.personal_sign)
    TextView personalSign;
    @BindView(R.id.personal_sign_ly)
    LinearLayout personalSignLy;

    private UserBean userBean;
    private TimePickerView pvCustomTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("个人信息");
        initImagePicker();
        initCustomTimePicker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView(){
        if (!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString())) {
            userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
            NetWorkImageLoader.loadCircularImage(null,userBean.getData().getProfile_image_url(),personalHead,R.drawable.icon_circle_mrtx,R.drawable.icon_circle_mrtx);
            if(!TextUtils.isEmpty(userBean.getData().getScreen_name()))
                personalName.setText(userBean.getData().getScreen_name());
            if(userBean.getData().getGender() == 2){
                personalSex.setText("女");
            }else if(userBean.getData().getGender() == 1){
                personalSex.setText("男");
            }else {
                personalSex.setText("保密");
            }
            if(userBean.getData().getBirthday() !=0){
                personalBirthday.setText(TimeUtils.formatDate(userBean.getData().getBirthday()*1000));
            }
            if(!TextUtils.isEmpty(userBean.getData().getAddress()))
                personalAddress.setText(userBean.getData().getAddress());
            if(!TextUtils.isEmpty(userBean.getData().getSignature()))
                personalSign.setText(userBean.getData().getSignature());
        }
    }
    @OnClick({R.id.iv_include_back, R.id.personal_head_ly, R.id.personal_name_ly, R.id.personal_sex_ly, R.id.personal_birthday_ly, R.id.personal_address_ly, R.id.personal_sign_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.personal_head_ly:
                PermissionGen.needPermission(this, 102, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                break;
            case R.id.personal_name_ly:
                jumpToOtherActivity(new Intent(this,SetNameActivity.class),false);
                break;
            case R.id.personal_sex_ly:
                jumpToOtherActivity(new Intent(this,SetSexActivity.class),false);
                break;
            case R.id.personal_birthday_ly:
                pvCustomTime.show();
                break;
            case R.id.personal_address_ly:
                jumpToOtherActivity(new Intent(this,SetAddressActivity.class),false);
                break;
            case R.id.personal_sign_ly:
                jumpToOtherActivity(new Intent(this,SetSignActivity.class),false);
                break;
        }
    }
    private void initCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030, 11, 31);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                personalBirthday.setText(TimeUtils.getTime(date));
                post(date.getTime()/1000);
            }
        })
                .setDate(selectedDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
//                .setRangDate(startDate,endDate)
                .build();
    }
    private void post(long birthday) {
        Logger.e("time",birthday+"");
        showLoading("加载中...");
        OkGo.<String>post(Urls.SETUSERINFO)//
                .tag(this)//
                .params("id", userBean.getData().getId()+"")//
                .params("birthday", birthday)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("body",response.body());
                            UserBean user = JSON.parseObject(response.body(),UserBean.class);
                            userBean.getData().setBirthday(user.getData().getBirthday());
                            SPUtil.put(PersonalActivity.this,"user", JSON.toJSONString(userBean));
                        }
                    }
                });
    }
    @PermissionSuccess(requestCode = 102)
    private void success() {
        choosePicture();
    }

    @PermissionFail(requestCode = 102)
    private void fail() {
        toast("授权失败");
    }
    private void choosePicture() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, 100);
    }
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImagePickerImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
        imagePicker.setMultiMode(false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                compress(images.get(0).path);//压缩
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void compress(String filePath) {
        Luban.with(this)
                .load(new File(filePath))                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        String path = file.getPath();
                        post(path);//上传头像文件
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();    //启动压缩
    }
    private void post(String path) {
        showLoading("加载中...");
        OkGo.<String>post(Urls.UPLOAD)//
                .tag(this)//
                .params("image", new File(path))//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        Logger.e("body",response.body());
                        if (response.isSuccessful()) {
                            PictureBean pictureBean = JSON.parseObject(response.body(),PictureBean.class);
                            if(pictureBean.getStatus() == 1){
                                postTwo(pictureBean.getData().getImg_url());
                            }else {
                                toast(pictureBean.getMsg());
                            }
                        }
                    }
                });
    }
    private void postTwo(String path) {
        showLoading("加载中...");
        OkGo.<String>post(Urls.SETUSERINFO)//
                .tag(this)//
                .params("id", userBean.getData().getId()+"")//
                .params("profile_image_url",path)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            PictureBean pictureBean = JSON.parseObject(response.body(),PictureBean.class);
                            userBean.getData().setProfile_image_url(pictureBean.getData().getProfile_image_url());
                            SPUtil.put(PersonalActivity.this,"user", JSON.toJSONString(userBean));
                            NetWorkImageLoader.loadCircularImage(null,pictureBean.getData().getProfile_image_url(),personalHead);
                            toast("保存成功");
                        }
                    }
                });
    }
}
