package com.ailide.apartmentsabc.views.addequitment;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailide.apartmentsabc.eventbus.ConnectEvent;
import com.ailide.apartmentsabc.model.EquitmentBean;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.MyApplication;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.ailide.apartmentsabc.views.mine.EditEquipmentActivity;
import com.ailide.apartmentsabc.views.mine.MyEquitmentActivity;
import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


public class ScanActivity extends BaseActivity implements QRCodeView.Delegate{

    @BindView(R.id.zxingview)
    ZXingView mQRCodeView;
    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;

    private List<EquitmentBean> menuList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        tvIncludeTitle.setText("扫一扫");
        PermissionGen.needPermission(this, 101, new String[]{Manifest.permission.CAMERA});
        if(!TextUtils.isEmpty(SPUtil.get(this,"equitment","").toString())){
            menuList = JSON.parseArray(SPUtil.get(this,"equitment","").toString(),EquitmentBean.class);
        }
    }
    @PermissionFail(requestCode = 101)
    private void fail() {
        toast("授权失败");
    }
    @PermissionSuccess(requestCode = 101)
    private void success() {
    }
    @Override
    protected void onStart() {
        super.onStart();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }
    @OnClick(R.id.iv_include_back)
    public void onViewClicked() {
        doBack();
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    private EquitmentBean equitmentBean;
    @Override
    public void onScanQRCodeSuccess(String result) {
        Logger.e("result",result);
        vibrate();
        mQRCodeView.stopSpot();
        String[] str = result.split("\\?");
        if(str.length==3){
            equitmentBean =new EquitmentBean();
            equitmentBean.setName(str[1].toUpperCase()+"_BR");
            equitmentBean.setNameTwo(str[1].toUpperCase()+"_BR");
            equitmentBean.setAddress(str[2]);
            new AsyncTask<Integer, Integer, String>() {

                @Override
                protected void onPreExecute() {
                    showLoading("连接中...");
                }

                @Override
                protected String doInBackground(Integer... integers) {
                    if (PrintFragment.printPP != null && PrintFragment.printPP.isConnected()) {
                        PrintFragment.printPP.disconnect();
//                            MyApplication.isConnected = false;
                    }
                    String sdata =equitmentBean.getName() + "\n" + equitmentBean.getAddress();
                    String address = sdata.substring(sdata.length() - 17);
                    String  equitmentName = sdata.substring(0, (sdata.length() - 17));
                    if (PrintFragment.printPP == null || !PrintFragment.printPP.isConnected()) {
                        if (PrintFragment.printPP.connect(equitmentName, address)) {
//                                MyApplication.isConnected = true;
                            if(PrintFragment.printPP!=null && !TextUtils.isEmpty(PrintFragment.printPP.printerMac()))
                                equitmentBean.setMac(PrintFragment.printPP.printerMac());
                            if(PrintFragment.printPP!=null && !TextUtils.isEmpty(PrintFragment.printPP.printerVersion()))
                                equitmentBean.setVersion(PrintFragment.printPP.printerVersion());
                            if(PrintFragment.printPP!=null && !TextUtils.isEmpty(PrintFragment.printPP.PrinterSN()))
                                equitmentBean.setSn(PrintFragment.printPP.PrinterSN());

                            return "1";
                        } else {
//                                MyApplication.isConnected = false;
                            return "";
                        }

                    }
                    return "";
                }

                @Override
                protected void onPostExecute(String bitmap) {
                    if (bitmap.equals("1")) {
                        toast("连接成功");
                        for (int i = 0; i <menuList.size() ; i++) {
                            if(equitmentBean.getName().equals(menuList.get(i).getName())){
                                break;
                            }
                            if(menuList.size()-1 == i)
                                menuList.add(equitmentBean);
                        }
                        if(menuList.size()==0){
                            menuList.add(equitmentBean);
                        }
                        SPUtil.put(ScanActivity.this,"equitment",JSON.toJSONString(menuList));
                        ConnectEvent connectEvent = new ConnectEvent();
                        connectEvent.setFail(1);
                        connectEvent.setEquitmentName(equitmentBean.getNameTwo());
                        EventBus.getDefault().post(connectEvent);
                        jumpToOtherActivity(new Intent(ScanActivity.this, AddEquitmentActivity.class),true);
                    } else {
                        ConnectEvent connectEvent = new ConnectEvent();
                        connectEvent.setFail(0);
                        EventBus.getDefault().post(connectEvent);
//                    MyApplication.isConnected = false;
                        toast("连接失败");
                        jumpToOtherActivity(new Intent(ScanActivity.this, MyEquitmentActivity.class),true);
                    }
                    dismissLoading();
                }
            }.execute();
        }else {
            toast("错误二维码");
            doBack();
        }

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }
}
