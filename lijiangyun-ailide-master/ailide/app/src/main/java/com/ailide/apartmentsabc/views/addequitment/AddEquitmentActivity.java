package com.ailide.apartmentsabc.views.addequitment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ailide.apartmentsabc.eventbus.ConnectEvent;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.EquitmentBean;
import com.ailide.apartmentsabc.model.PrintNameBean;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.mine.EditEquipmentActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailide.apartmentsabc.R;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddEquitmentActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.equitment_recycle_view)
    RecyclerView equitmentRecycleView;
    @BindView(R.id.equitment_search_ly)
    LinearLayout equitmentSearchLy;
    @BindView(R.id.equitment_scan_ly)
    LinearLayout equitmentScanLy;

    private RecycleViewAdapter recycleViewAdapter;
    private List<EquitmentBean> menuList = new ArrayList<>();
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private static final int REQUEST_ENABLE_BT = 2;
    private PopupWindow popupWindow;
//    private Set<BluetoothDevice> pairedDevices;
    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled
        // setupChat() will then be called during onActivityRe//sultsetupChat
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equitment);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("添加设备");
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        initRecycleView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }else {
            menuList.clear();
            doDiscovery();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        if(popupWindow!=null)
            popupWindow.dismiss();

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }
    private void doDiscovery() {
//        if (mBtAdapter == null) {
//            pairedDevices = new HashSet<>();
//        } else {
//            pairedDevices = mBtAdapter.getBondedDevices();
//        }
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                if(!TextUtils.isEmpty(device.getName()) && device.getName().contains("Alison")){
//                    EquitmentBean equitmentBean = new EquitmentBean();
//                    equitmentBean.setName(device.getName());
//                    equitmentBean.setNameTwo(device.getName());
//                    equitmentBean.setAddress(device.getAddress());
//                    menuList.add(equitmentBean);
//                }
//            }
//        }
//        showLoading("加载中...");
        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    EquitmentBean equitmentBean = new EquitmentBean();
                    if(!TextUtils.isEmpty(device.getName()) && device.getName().contains("Alison")){
                        if(!device.getName().contains("BLE")){
                            for (int i = 0; i < menuList.size() ; i++) {
                                if(menuList.get(i).getName().equals(device.getName())){
                                    return;
                                }
                            }
                            equitmentBean.setName(device.getName());
                            equitmentBean.setNameTwo(device.getName());
                            equitmentBean.setAddress(device.getAddress());
                            menuList.add(equitmentBean);
                        }
                    }

//                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                dismissLoading();
                setProgressBarIndeterminateVisibility(false);
                if (menuList.size() == 0) {
                    menuList.clear();
                    if(menuList.size()<=0){
                        View view = getLayoutInflater().inflate(R.layout.empty_view, null);
                        TextView tvEmpty = (TextView) view.findViewById(R.id.empty_tv);
                        tvEmpty.setText("没有发现设备");
                        recycleViewAdapter.setEmptyView(view);
                    }
                }
            }
            recycleViewAdapter.setNewData(menuList);
        }
    };
    private void initRecycleView() {
        equitmentRecycleView.setLayoutManager(new LinearLayoutManager(this));
        equitmentRecycleView.setNestedScrollingEnabled(false);
        equitmentRecycleView.setItemAnimator(new DefaultItemAnimator());
        recycleViewAdapter = new RecycleViewAdapter(R.layout.item_add_equitment, menuList);
//        recycleViewAdapter.setOnLoadMoreListener(this, appointmentMainRecycleView);
        equitmentRecycleView.setAdapter(recycleViewAdapter);
        if(menuList.size()<=0){
            View view = getLayoutInflater().inflate(R.layout.empty_view, null);
            TextView tvEmpty = (TextView) view.findViewById(R.id.empty_tv);
            tvEmpty.setText("没有发现设备");
            recycleViewAdapter.setEmptyView(view);
        }
        recycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!TextUtils.isEmpty(SPUtil.get(AddEquitmentActivity.this, "equitment", "").toString())) {
                    List<EquitmentBean> m = JSON.parseArray(SPUtil.get(AddEquitmentActivity.this, "equitment", "").toString(), EquitmentBean.class);
                    for (int i = 0; i < m.size(); i++) {
                        if (menuList.get(position).getName().equals(m.get(i).getName())) {
                            toast("您已经添加过该设备");
                            return;
                        }
                        if(menuList.get(position).getName().substring(0,menuList.get(position).getName().length()-3).equals(m.get(i).getName())){
                            toast("您已经添加过该设备");
                            return;
                        }
                        if(menuList.get(position).getName().equals(m.get(i).getName().substring(0,m.get(i).getName().length()-3))){
                            toast("您已经添加过该设备");
                            return;
                        }
                    }
                }
                Intent intent=new Intent(AddEquitmentActivity.this, EditEquipmentActivity.class);
                intent.putExtra("equipment", JSON.toJSONString(menuList.get(position)));
                jumpToOtherActivity(intent,false);
            }
        });
    }

    private class RecycleViewAdapter extends BaseQuickAdapter<EquitmentBean, BaseViewHolder> {
        public RecycleViewAdapter(@LayoutRes int layoutResId, @Nullable List<EquitmentBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, EquitmentBean item) {
            if(!TextUtils.isEmpty(item.getMessage()))
                helper.setText(R.id.equitment_tv,item.getMessage());
            if(!TextUtils.isEmpty(item.getName()))
                helper.setText(R.id.equitment_tv,item.getName());
        }
    }
    @OnClick({R.id.iv_include_back, R.id.equitment_search_ly, R.id.equitment_scan_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.equitment_search_ly:
                if (!mBtAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    return;
                }
                toast("搜索中");
                menuList.clear();
                doDiscovery();
                break;
            case R.id.equitment_scan_ly:
                showPopView();
                break;
        }
    }
    private void showPopView() {
        View layout = getLayoutInflater().inflate(R.layout.pop_add_equitment, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, tvIncludeTitle, 0, 0, 1, 0.5f, true);
        TextView pop_equitment_add = layout.findViewById(R.id.pop_equitment_add);
        TextView pop_equitment_not = layout.findViewById(R.id.pop_equitment_not);
        TextView equipment_tv = layout.findViewById(R.id.equipment_tv);
        equipment_tv.setText("开机后，双击开机键\n打印设备信息二维码");
        pop_equitment_not.setVisibility(View.GONE);
        pop_equitment_add.setText("下一步");
        ImageView pop_equitment_img = layout.findViewById(R.id.pop_equitment_img);
        pop_equitment_img.setBackgroundResource(R.drawable.tjsb_pic);
        pop_equitment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                jumpToOtherActivity(new Intent(AddEquitmentActivity.this,ScanActivity.class),false);
            }
        });
    }
}
