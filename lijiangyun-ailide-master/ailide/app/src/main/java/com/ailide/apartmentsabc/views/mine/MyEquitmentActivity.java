package com.ailide.apartmentsabc.views.mine;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailide.apartmentsabc.eventbus.ConnectEvent;
import com.ailide.apartmentsabc.model.EquitmentBean;
import com.ailide.apartmentsabc.model.PrintNameBean;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.tools.view.MakeSureDialog;
import com.ailide.apartmentsabc.views.MyApplication;
import com.ailide.apartmentsabc.views.addequitment.AddEquitmentActivity;
import com.ailide.apartmentsabc.views.addequitment.EditEquitmentActivity;
import com.ailide.apartmentsabc.views.addequitment.ScanActivity;
import com.ailide.apartmentsabc.views.main.MainActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyEquitmentActivity extends BaseActivity {

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

    private RecycleViewAdapter recycleViewAdapter;
    private List<EquitmentBean> menuList = new ArrayList<>();
    private int index = 1;
    private MakeSureDialog makeSureDialog;
    private BluetoothAdapter mBtAdapter;
    private static final int REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_equitment);
        ButterKnife.bind(this);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        tvIncludeTitle.setText("我的设备");
        tvIncludeRight.setText("编辑");
        tvIncludeRight.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(SPUtil.get(this,"equitment","").toString())){
            menuList = JSON.parseArray(SPUtil.get(this,"equitment","").toString(),EquitmentBean.class);
        }
        initRecycleView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recycleViewAdapter!=null){
            if(!TextUtils.isEmpty(SPUtil.get(this,"equitment","").toString())){
                menuList = JSON.parseArray(SPUtil.get(this,"equitment","").toString(),EquitmentBean.class);
                recycleViewAdapter.setNewData(menuList);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=makeSureDialog)
            makeSureDialog.dismiss();
    }

    private void initRecycleView() {
        equitmentRecycleView.setLayoutManager(new LinearLayoutManager(this));
        equitmentRecycleView.setNestedScrollingEnabled(false);
        equitmentRecycleView.setItemAnimator(new DefaultItemAnimator());
        recycleViewAdapter = new RecycleViewAdapter(R.layout.item_equitment, menuList);
//        recycleViewAdapter.setOnLoadMoreListener(this, appointmentMainRecycleView);
        equitmentRecycleView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                if(index ==1){
                    if (!mBtAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                        return;
                    }
                    if(PrintFragment.printPP!=null && !TextUtils.isEmpty(menuList.get(position).getMac())){
                        if(PrintFragment.printPP.printerMac().equals(menuList.get(position).getMac()))
                            return;
                    }
                    new AsyncTask<Integer, Integer, String>() {

                        @Override
                        protected void onPreExecute() {
                            showLoading("请稍后");
                        }

                        @Override
                        protected String doInBackground(Integer... integers) {
                            if (PrintFragment.printPP!=null && PrintFragment.printPP.isConnected()) {
                                PrintFragment.printPP.disconnect();
//                                MyApplication.isConnected = false;
                            }
                            String sdata = menuList.get(position).getName() + "\n" + menuList.get(position).getAddress();
                            String address = sdata.substring(sdata.length() - 17);
                            String  equitmentName = sdata.substring(0, (sdata.length() - 17));

                            if ( !PrintFragment.printPP.isConnected()) {
                                if (PrintFragment.printPP.connect(equitmentName, address)) {
//                                    MyApplication.isConnected = true;
                                    if(PrintFragment.printPP!=null && !TextUtils.isEmpty(PrintFragment.printPP.printerMac()))
                                        menuList.get(position).setMac(PrintFragment.printPP.printerMac());
                                    if(PrintFragment.printPP!=null && !TextUtils.isEmpty(PrintFragment.printPP.printerVersion()))
                                        menuList.get(position).setVersion(PrintFragment.printPP.printerVersion());
                                    if(PrintFragment.printPP!=null && !TextUtils.isEmpty(PrintFragment.printPP.PrinterSN()))
                                        menuList.get(position).setSn(PrintFragment.printPP.PrinterSN());
                                    SPUtil.put(MyEquitmentActivity.this,"equitment",JSON.toJSONString(menuList));
                                    ConnectEvent connectEvent = new ConnectEvent();
                                    connectEvent.setFail(1);
                                    connectEvent.setEquitmentName(menuList.get(position).getNameTwo());
                                    EventBus.getDefault().post(connectEvent);
                                    return "1";
                                } else {
                                    ConnectEvent connectEvent = new ConnectEvent();
                                    connectEvent.setFail(0);
                                    EventBus.getDefault().post(connectEvent);
//                                    MyApplication.isConnected = false;
                                    return "";
                                }

                            }
                            return "";
                        }

                        @Override
                        protected void onPostExecute(String bitmap) {
                            if(bitmap.equals("1")){
                                toast("连接成功");
                            }else {
                                toast("连接失败");
                            }
                            recycleViewAdapter.setNewData(menuList);
                            dismissLoading();

                        }
                    }.execute();

                }else {
                    Intent intent = new Intent(MyEquitmentActivity.this, EditEquipmentActivity.class);
                    intent.putExtra("position",position);
                    jumpToOtherActivity(intent,false);
                }
            }
        });
        recycleViewAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showMakeDialog(position);
                return true;
            }
        });
        if(menuList.size()<=0){
            View view = getLayoutInflater().inflate(R.layout.empty_view, null);
            TextView tvEmpty = (TextView) view.findViewById(R.id.empty_tv);
            tvEmpty.setText("没有绑定设备");
            recycleViewAdapter.setEmptyView(view);
        }
    }
    private void showMakeDialog(final int position){
        makeSureDialog = new MakeSureDialog();
        makeSureDialog.setContent("确实删除该设备吗？");
        makeSureDialog.setDialogClickListener(new MakeSureDialog.onDialogClickListener() {
            @Override
            public void onSureClick() {
                makeSureDialog.dismiss();
                if(PrintFragment.printPP!=null && !TextUtils.isEmpty(menuList.get(position).getMac())){
                    if(PrintFragment.printPP.printerMac().equals(menuList.get(position).getMac())){
                        if (PrintFragment.printPP!=null && PrintFragment.printPP.isConnected()) {
                            PrintFragment.printPP.disconnect();
                        }
                    }
                }
                menuList.remove(position);
                recycleViewAdapter.setNewData(menuList);
                SPUtil.put(MyEquitmentActivity.this,"equitment",JSON.toJSONString(menuList));

            }

            @Override
            public void onCancelClick() {
                makeSureDialog.dismiss();
            }
        });
        makeSureDialog.show(getSupportFragmentManager(),"");
    }
    private class RecycleViewAdapter extends BaseQuickAdapter<EquitmentBean, BaseViewHolder> {
        public RecycleViewAdapter(@LayoutRes int layoutResId, @Nullable List<EquitmentBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, EquitmentBean item) {
            ImageView imageView = helper.getView(R.id.item_equipment_img);
            if(index == 1){
                imageView.setBackgroundResource(R.drawable.grzx_wdsb_connect2);
            }else {
                imageView.setBackgroundResource(R.drawable.right_my_equip_into);
            }
            if(PrintFragment.printPP!=null && !TextUtils.isEmpty(item.getMac())){
                if(PrintFragment.printPP.printerMac().equals(item.getMac()))
                imageView.setBackgroundResource(R.drawable.grzx_wdsb_connect);
            }
            if(!TextUtils.isEmpty(item.getNameTwo()))
                helper.setText(R.id.equipment_name,item.getNameTwo());
            if(!TextUtils.isEmpty(item.getAddress()))
                helper.setText(R.id.equipment_number,"设备编号："+item.getAddress());
            if(menuList.size() -1 == helper.getLayoutPosition())
                helper.setVisible(R.id.main_select_line,false);
        }
    }

    @OnClick({R.id.iv_include_back,R.id.friend_qiao_hua,R.id.tv_include_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.friend_qiao_hua:
                jumpToOtherActivity(new Intent(MyEquitmentActivity.this, AddEquitmentActivity.class),false);
                break;
            case R.id.tv_include_right:
                if(index ==1){
                    index = 2;
                    tvIncludeRight.setText("完成");
                    recycleViewAdapter.setNewData(menuList);
                }else {
                    index = 1;
                    tvIncludeRight.setText("编辑");
                    recycleViewAdapter.setNewData(menuList);
                }
                break;
        }
    }
}
