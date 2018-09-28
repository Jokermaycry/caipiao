package com.ailide.apartmentsabc.views.main.fragment;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.ConnectEvent;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.EquitmentBean;
import com.ailide.apartmentsabc.model.PrintNameBean;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.Web.WebActivity;
import com.ailide.apartmentsabc.views.addequitment.AddEquitmentActivity;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.ailide.apartmentsabc.views.bill.BillActivity;
import com.ailide.apartmentsabc.views.material.MaterialCenterActivity;
import com.ailide.apartmentsabc.views.sticker.LabelActivity;
import com.ailide.apartmentsabc.views.sticker.StickerActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.sdk.PrintPP;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrintFragment extends BaseSimpleFragment {


    private static final int REQUEST_ENABLE_BT = 2;
    public static PrintPP printPP;
    @BindView(R.id.print_select_ly)
    LinearLayout printSelectLy;
    @BindView(R.id.print_recycle_view)
    RecyclerView printRecycleView;
    @BindView(R.id.print_equitment_name)
    TextView printEquitmentName;
    @BindView(R.id.print_select_img)
    ImageView printSelectImg;
    private List<PrintNameBean> menuList = new ArrayList<>();
    private List<EquitmentBean> menuListOne = new ArrayList<>();
    private List<EquitmentBean> equitmentBeanList = new ArrayList<>();
    private RecycleViewAdapter recycleViewAdapter;
    private String[] name;
    private String[] nameOne;
    private int[] drw = {R.drawable.twbj_icon, R.drawable.bqbj_icon, R.drawable.blqd_icon, R.drawable.scwy_icon, R.drawable.sczx_icon};
    private PopupWindow mPopupWindow;
    private SelectAdapter selectAdapter;
    private BluetoothAdapter mBtAdapter;
    private String address = "";
    private String equitmentName = "";
//    private Set<BluetoothDevice> pairedDevices;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_print;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        printPP = new PrintPP(mActivity) {
            @Override
            public void onConnected() {
                Log.e("Mainactivity", "onConnected");
            }

            @Override
            public void ondisConnected() {
                Log.e("Mainactivity", "ondisConnected");
            }

            @Override
            public void onsateOFF() {
                Log.e("Mainactivity", "onsateOFF");
            }

            @Override
            public void onsateOn() {
                Log.e("Mainactivity", "onsateOn");

            }
        };
        name = getResources().getStringArray(R.array.print_name);
        nameOne = getResources().getStringArray(R.array.print_name_two);
        for (int i = 0; i < name.length; i++) {
            PrintNameBean p = new PrintNameBean();
            p.setName(name[i]);
            p.setNameOne(nameOne[i]);
            p.setImg(drw[i]);
            menuList.add(p);
        }
        if (!TextUtils.isEmpty(SPUtil.get(mActivity, "equitment", "").toString())) {
            equitmentBeanList = JSON.parseArray(SPUtil.get(mActivity, "equitment", "").toString(), EquitmentBean.class);
        }
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (mBtAdapter == null) {
//            pairedDevices = new HashSet<>();
//        } else {
//            pairedDevices = mBtAdapter.getBondedDevices();
//        }
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                if (!TextUtils.isEmpty(SPUtil.get(mActivity, "equitment", "").toString())) {
//                    for (int i = 0; i < equitmentBeanList.size(); i++) {
//                        if (equitmentBeanList.get(i).getName().equals(device.getName())) {
//                            break;
//                        }
//                        if (equitmentBeanList.size() - 1 == i) {
//                            EquitmentBean equitmentBean = new EquitmentBean();
//                            equitmentBean.setAddress(device.getAddress());
//                            equitmentBean.setName(device.getName());
//                            equitmentBean.setNameTwo(device.getName());
//                            equitmentBeanList.add(equitmentBean);
//                            SPUtil.put(mActivity, "equitment", JSON.toJSONString(equitmentBeanList));
//                        }
//                    }
//                } else {
//                    EquitmentBean equitmentBean = new EquitmentBean();
//                    equitmentBean.setAddress(device.getAddress());
//                    equitmentBean.setName(device.getName());
//                    equitmentBean.setNameTwo(device.getName());
//                    equitmentBeanList.add(equitmentBean);
//                    SPUtil.put(mActivity, "equitment", JSON.toJSONString(equitmentBeanList));
//                }
//            }
//        } else {
//        }
        initRecycleView();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(PrintFragment.printPP==null || !PrintFragment.printPP.isConnected()){
            printEquitmentName.setText("未连接设备");
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mActivity.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mActivity.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mActivity.unregisterReceiver(mReceiver);
    }

    @Override
    protected void setListener() {
        recycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    mActivity.jumpToOtherActivity(new Intent(mActivity, StickerActivity.class), false);
                } else if (position == 1) {
                    mActivity.jumpToOtherActivity(new Intent(mActivity, LabelActivity.class), false);
                } else if (position == 2) {
                    mActivity.jumpToOtherActivity(new Intent(mActivity, BillActivity.class), false);
                } else if (position == 3) {
                    mActivity.jumpToOtherActivity(new Intent(mActivity, WebActivity.class), false);
                } else if (position == 4) {
                    mActivity.jumpToOtherActivity(new Intent(mActivity, MaterialCenterActivity.class), false);
                }
            }
        });

    }

    private void initRecycleView() {
        printRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        printRecycleView.setNestedScrollingEnabled(false);
        printRecycleView.setItemAnimator(new DefaultItemAnimator());
        recycleViewAdapter = new RecycleViewAdapter(R.layout.item_print, menuList);
//        recycleViewAdapter.setOnLoadMoreListener(this, appointmentMainRecycleView);
        printRecycleView.setAdapter(recycleViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPopupWindow != null)
            mPopupWindow.dismiss();
        if (printPP != null)
            printPP.disconnect();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.print_select_ly, R.id.print_recycle_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.print_select_ly:
                if (!mBtAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    return;
                }
                if (!TextUtils.isEmpty(SPUtil.get(mActivity, "equitment", "").toString())) {
                    equitmentBeanList = JSON.parseArray(SPUtil.get(mActivity, "equitment", "").toString(), EquitmentBean.class);
                }
//                pairedDevices = mBtAdapter.getBondedDevices();
                menuListOne.clear();
                doDiscovery();
                showPopView();
                break;
            case R.id.print_recycle_view:
                break;
        }
    }

    private void showPopView() {
        View layout = mActivity.getLayoutInflater().inflate(R.layout.pop_main_select_apart, null);
        mPopupWindow = CommonFunction.getInstance().InitPopupWindow(mActivity, layout, printEquitmentName, 4, 1, 1, 1f, true);
        RecyclerView selectRV = layout.findViewById(R.id.appointment_main_select_recycle_view);
        selectRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        selectRV.setNestedScrollingEnabled(false);
        selectRV.setItemAnimator(new DefaultItemAnimator());
        selectAdapter = new SelectAdapter(R.layout.item_main_select, equitmentBeanList);
        selectRV.setAdapter(selectAdapter);
        if(equitmentBeanList.size()<=0) selectRV.setVisibility(View.GONE);
        selectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPopupWindow.dismiss();
                if (PrintFragment.printPP != null && PrintFragment.printPP.isConnected()) {
                    printPP.disconnect();
//                    MyApplication.isConnected = false;
                }
                String sdata = equitmentBeanList.get(position).getName() + "\n" + equitmentBeanList.get(position).getAddress();
                address = sdata.substring(sdata.length() - 17);
                equitmentName = sdata.substring(0, (sdata.length() - 17));
                if (PrintFragment.printPP == null || !PrintFragment.printPP.isConnected()) {
                    if (printPP.connect(equitmentName, address)) {
//                        MyApplication.isConnected = true;
                        printEquitmentName.setText(equitmentBeanList.get(position).getNameTwo());
                        if (printPP != null && !TextUtils.isEmpty(printPP.printerMac()))
                            equitmentBeanList.get(position).setMac(printPP.printerMac());
                        if (printPP != null && !TextUtils.isEmpty(printPP.printerVersion()))
                            equitmentBeanList.get(position).setVersion(printPP.printerVersion());
                        if (printPP != null && !TextUtils.isEmpty(printPP.PrinterSN()))
                            equitmentBeanList.get(position).setSn(printPP.PrinterSN());
                        SPUtil.put(mActivity, "equitment", JSON.toJSONString(equitmentBeanList));

                    } else {
                        printEquitmentName.setText("未连接设备");
//                        MyApplication.isConnected = false;
                        mActivity.toast("连接失败");

                    }

                }
            }
        });
        TextView add = layout.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                mActivity.jumpToOtherActivity(new Intent(mActivity, AddEquitmentActivity.class), false);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ConnectEvent event) {
        if (event.getFail() == 1) {
            printEquitmentName.setText(event.getEquitmentName());
        } else {
            printEquitmentName.setText("未连接设备");
        }
    }

    private class RecycleViewAdapter extends BaseQuickAdapter<PrintNameBean, BaseViewHolder> {
        public RecycleViewAdapter(@LayoutRes int layoutResId, @Nullable List<PrintNameBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PrintNameBean item) {
            if (!TextUtils.isEmpty(item.getName()))
                helper.setText(R.id.print_name, item.getName());
            if (!TextUtils.isEmpty(item.getNameOne()))
                helper.setText(R.id.print_name_one, item.getNameOne());
            helper.setImageResource(R.id.print_img, item.getImg());
        }
    }

    private class SelectAdapter extends BaseQuickAdapter<EquitmentBean, BaseViewHolder> {

        public SelectAdapter(@LayoutRes int layoutResId, @Nullable List<EquitmentBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, EquitmentBean item) {
            helper.setBackgroundRes(R.id.doc, R.drawable._8b8b8b_bg_oval);
            for (int i = 0; i <menuListOne.size() ; i++) {
               if( menuListOne.get(i).getName().equals(item.getName())){
                   helper.setBackgroundRes(R.id.doc,R.drawable._00d9d8_bg_oval);
               }
            }
            if (PrintFragment.printPP != null && PrintFragment.printPP.isConnected()) {
                Logger.e("ddddd", printEquitmentName.getText().toString() + "&&" + item.getNameTwo());
                if (printEquitmentName.getText().toString().trim().equals(item.getNameTwo()))
                    helper.setBackgroundRes(R.id.doc, R.drawable._74c239_bg_oval);
            }
            if (!TextUtils.isEmpty(item.getNameTwo()))
                helper.setText(R.id.main_select_tv, item.getNameTwo());
            if (equitmentBeanList.size() - 1 == helper.getLayoutPosition())
                helper.setVisible(R.id.main_select_line, false);
        }
    }
    private void doDiscovery() {
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
                        for (int i = 0; i < menuListOne.size() ; i++) {
                            if(menuListOne.get(i).getName().equals(device.getName())){
                                return;
                            }
                        }
                        equitmentBean.setName(device.getName());
                        equitmentBean.setNameTwo(device.getName());
                        equitmentBean.setAddress(device.getAddress());
                        menuListOne.add(equitmentBean);
                    }
                }

//                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            }
            if(selectAdapter!=null)
            selectAdapter.setNewData(equitmentBeanList);
        }
    };
}
