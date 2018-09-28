package com.ailide.apartmentsabc.views.mine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.ConnectEvent;
import com.ailide.apartmentsabc.model.EquitmentBean;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditEquipmentActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.mac)
    TextView mac;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.sn)
    TextView sn;
    @BindView(R.id.mac_ly)
    LinearLayout macLy;
    @BindView(R.id.version_ly)
    LinearLayout versionLy;
    @BindView(R.id.sn_ly)
    LinearLayout snLy;
    @BindView(R.id.power)
    TextView power;
    @BindView(R.id.power_ly)
    LinearLayout powerLy;
    @BindView(R.id.number_ly)
    LinearLayout numberLy;

    private List<EquitmentBean> menuList = new ArrayList<>();
    private int position;
    private EquitmentBean equitmentBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equipment);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("编辑设备");
        tvIncludeRight.setText("保存");
        tvIncludeRight.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(SPUtil.get(this, "equitment", "").toString())) {
            menuList = JSON.parseArray(SPUtil.get(this, "equitment", "").toString(), EquitmentBean.class);
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("equipment"))) {
            equitmentBean = JSON.parseObject(getIntent().getStringExtra("equipment"), EquitmentBean.class);
            for (int i = 0; i < menuList.size(); i++) {
                if (equitmentBean.getName().equals(menuList.get(i).getName())) {
                    equitmentBean = menuList.get(i);
                    break;
                }
            }
        } else {
            position = getIntent().getIntExtra("position", 0);
            equitmentBean = menuList.get(position);
        }

        initDate();
    }

    private void initDate() {
        if (!TextUtils.isEmpty(equitmentBean.getNameTwo())) {
            edit.setText(equitmentBean.getNameTwo());
            edit.setSelection(edit.getText().toString().trim().length());
        }
        if (!TextUtils.isEmpty(equitmentBean.getSn())){
            number.setText(equitmentBean.getSn());
        }else {
            numberLy.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(equitmentBean.getMac())) {
            mac.setText(equitmentBean.getMac());
        } else {
            macLy.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(equitmentBean.getVersion())) {
            version.setText(equitmentBean.getVersion());
        } else {
            versionLy.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(equitmentBean.getSn())) {
            sn.setText(equitmentBean.getSn());
        } else {
            snLy.setVisibility(View.GONE);
        }
        if (PrintFragment.printPP != null && PrintFragment.printPP.isConnected()) {
            if (PrintFragment.printPP.printerMac().equals(equitmentBean.getMac())) {
                powerLy.setVisibility(View.VISIBLE);
                if (PrintFragment.printPP != null) {
                    power.setText(PrintFragment.printPP.printerBatteryVol() + "%");
                }
            } else {
                powerLy.setVisibility(View.GONE);
            }
        } else {
            powerLy.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_include_back, R.id.tv_include_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.tv_include_right:
                if (TextUtils.isEmpty(edit.getText().toString().trim())) {
                    toast("设备名称不能为空");
                    return;
                }
                equitmentBean.setNameTwo(edit.getText().toString().trim());
                if (!TextUtils.isEmpty(getIntent().getStringExtra("equipment"))) {
                    if (menuList.size() == 0) {
                        menuList.add(equitmentBean);
                    } else {
                        for (int i = 0; i < menuList.size(); i++) {
                            if (equitmentBean.getName().equals(menuList.get(i).getName())) {
                                break;
                            }
                            if (menuList.size() - 1 == i) {
                                menuList.add(equitmentBean);
                            }

                        }
                    }
                } else {
                    menuList.remove(position);
                    menuList.add(position, equitmentBean);
                }

                Log.e("getName()", menuList.get(position).getName());
                Log.e("getAddress()", menuList.get(position).getAddress());
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
                        String sdata = menuList.get(position).getName() + "\n" + menuList.get(position).getAddress();
                        String address = sdata.substring(sdata.length() - 17);
                        String equitmentName = sdata.substring(0, (sdata.length() - 17));
                        if (!PrintFragment.printPP.isConnected()) {
                            if (PrintFragment.printPP.connect(equitmentName, address)) {
//                                MyApplication.isConnected = true;
                                if (PrintFragment.printPP != null && !TextUtils.isEmpty(PrintFragment.printPP.printerMac()))
                                    menuList.get(position).setMac(PrintFragment.printPP.printerMac());
                                if (PrintFragment.printPP != null && !TextUtils.isEmpty(PrintFragment.printPP.printerVersion()))
                                    menuList.get(position).setVersion(PrintFragment.printPP.printerVersion());
                                if (PrintFragment.printPP != null && !TextUtils.isEmpty(PrintFragment.printPP.PrinterSN()))
                                    menuList.get(position).setSn(PrintFragment.printPP.PrinterSN());

                                SPUtil.put(EditEquipmentActivity.this, "equitment", JSON.toJSONString(menuList));
                                ConnectEvent connectEvent = new ConnectEvent();
                                connectEvent.setFail(1);
                                connectEvent.setEquitmentName(menuList.get(position).getNameTwo());
                                EventBus.getDefault().post(connectEvent);
                                return "1";
                            } else {
                                ConnectEvent connectEvent = new ConnectEvent();
                                connectEvent.setFail(0);
                                EventBus.getDefault().post(connectEvent);
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
                            SPUtil.put(EditEquipmentActivity.this, "equitment", JSON.toJSONString(menuList));
                        } else {
                            toast("连接失败");
                        }
                        dismissLoading();
                        doBack();
                    }
                }.execute();

                break;
        }
    }
}
