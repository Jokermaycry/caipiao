package com.ailide.apartmentsabc.views.bill;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.framework.util.KeyBoardUtils;
import com.ailide.apartmentsabc.model.Bill;
import com.ailide.apartmentsabc.tools.Contants;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.ToastUtil;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.utils.FileUtil;
import com.ailide.apartmentsabc.views.MyApplication;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.ailide.apartmentsabc.views.mine.MyEquitmentActivity;
import com.ailide.apartmentsabc.views.sticker.BillPrePrintActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaopo.flying.sticker.StickerUtils;
import com.xiaopo.flying.util.BitmapUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditBillActivity extends BaseActivity {

    @BindView(R.id.sv_canvas)
    ScrollView mSvCanvas;
    @BindView(R.id.ll_canvas)
    LinearLayout mLlCanvas;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.rv_content_show)
    RecyclerView mRvContentShow;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.iv_download)
    ImageView mIvDownload;

    private Bill bill;
    private BillContentAdapter mAdapter;
    private BillContentAdapter mAdapterShow;
    private List<String> mContents;
    private Handler handler = new Handler();
    private ImageView ivHead;
    private ImageView ivTail;

    private ImageView ivHeadShow;
    private ImageView ivTailShow;

    private PopupWindow popupWindow;
    private int select = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_edit_bill);
        setTouchHideInput(false);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        bill = (Bill) getIntent().getSerializableExtra("data");
        if(getIntent().getBooleanExtra("local",false)){
            mIvDownload.setVisibility(View.GONE);
        }else {
            mIvDownload.setVisibility(View.VISIBLE);
        }
        mContents = new ArrayList<>();
        mAdapter = new BillContentAdapter(bill, mContents);
       /* mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                select = position;
                mEtContent.setText(mContents.get(position));
                mEtContent.setSelection(mContents.get(position).length());
                KeyBoardUtils.showInput(EditBillActivity.this, mEtContent);
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                new MaterialDialog.Builder(EditBillActivity.this)
                        .content("是否删除清单")
                        .positiveText(R.string.confirm)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mContents.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
                return false;
            }
        });*/
        mAdapterShow = new BillContentAdapter(bill, mContents);
        mAdapterShow.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                select = position;
                mEtContent.setText(mContents.get(position));
                mEtContent.setSelection(mContents.get(position).length());
                KeyBoardUtils.showInput(EditBillActivity.this, mEtContent);
            }
        });
        mAdapterShow.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                new MaterialDialog.Builder(EditBillActivity.this)
                        .content("是否删除清单")
                        .positiveText(R.string.confirm)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mContents.remove(position);
                                mAdapterShow.notifyDataSetChanged();
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
                return false;
            }
        });

        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.setAdapter(mAdapter);

        mRvContentShow.setLayoutManager(new LinearLayoutManager(this));
        mRvContentShow.setAdapter(mAdapterShow);

        ivHead = new ImageView(this);
        ivHead.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ivHead.setScaleType(ImageView.ScaleType.FIT_XY);

        ivHeadShow = new ImageView(this);
        ivHeadShow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ivHeadShow.setScaleType(ImageView.ScaleType.FIT_XY);

        ivTail = new ImageView(this);
        ivTail.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ivTail.setScaleType(ImageView.ScaleType.FIT_XY);

        ivTailShow = new ImageView(this);
        ivTailShow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ivTailShow.setScaleType(ImageView.ScaleType.FIT_XY);

        NetWorkImageLoader.loadNetworkImage(null, Urls.BASE_IMG + bill.getHead_img(), ivHead);
        NetWorkImageLoader.loadNetworkImage(null, Urls.BASE_IMG + bill.getHead_img(), ivHeadShow);
        NetWorkImageLoader.loadNetworkImage(null, Urls.BASE_IMG + bill.getTail_img(), ivTail);
        NetWorkImageLoader.loadNetworkImage(null, Urls.BASE_IMG + bill.getTail_img(), ivTailShow);

        mAdapter.addHeaderView(ivHead);
        mAdapter.addFooterView(ivTail);

        mAdapterShow.addHeaderView(ivHeadShow);
        mAdapterShow.addFooterView(ivTailShow);

        mSvCanvas.setVerticalScrollBarEnabled(false);
        mSvCanvas.setHorizontalScrollBarEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.showInput(EditBillActivity.this, mEtContent);
            }
        }, 100);
    }

    @OnClick({R.id.iv_back, R.id.tv_send, R.id.iv_download, R.id.iv_print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_download:
                File file = FileUtil.getBillFile();
                try {
                    StickerUtils.saveImageToGallery(file, BitmapUtil.createBitmap(mLlCanvas));
                    List<Bill> bills = (List<Bill>) SPUtil.readObject(Contants.SP_BILL);
                    if (bills == null) {
                        bills = new ArrayList<>();
                    }
                    for (int i = 0; i < bills.size(); i++) {
                        if (bills.get(i).getId() == bill.getId()) {
                            bills.remove(i);
                            break;
                        }
                    }
                    bills.add(0, bill);
                    SPUtil.saveObject(Contants.SP_BILL, bills);
                    ToastUtil.toast("保存成功");
                    EventBus.getDefault().post(new EventBusEntity(EventConstant.EDIT_BILL));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_print:
                if (PrintFragment.printPP!=null && PrintFragment.printPP.isConnected()) {
                    File printFile = FileUtil.getBillFile();
//                    StickerUtils.saveImageToGallery(printFile, BitmapUtil.createBitmap(mRvContent));
                    StickerUtils.saveImageToGallery(printFile, BitmapUtil.createBitmap(mLlCanvas));
                    Intent intent = new Intent(this, BillPrePrintActivity.class);
                    intent.putExtra("path", printFile.getAbsolutePath());
                    startActivity(intent);
                } else {
                    showPopView();
                }
                break;
            case R.id.tv_send:
                if (select == -1) {
                    if (!TextUtils.isEmpty(mEtContent.getText().toString())) {
                        mContents.add(mEtContent.getText().toString());
                        mAdapter.notifyDataSetChanged();
                        mAdapterShow.notifyDataSetChanged();
                 /*       mSvCanvas.fullScroll(ScrollView.FOCUS_DOWN);
                        mRvContent.scrollToPosition(mContents.size());*/
                        mRvContentShow.scrollToPosition(mContents.size());
                    }
                } else {
                    mContents.remove(select);
                    mContents.add(select, mEtContent.getText().toString());
                    mAdapterShow.notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
                    mRvContentShow.scrollToPosition(select);
                }
                select = -1;
                mEtContent.setText("");
                mEtContent.setFocusable(true);
                mEtContent.setFocusableInTouchMode(true);
                mEtContent.requestFocus();
                break;
        }
    }

    private void showPopView() {
        View layout = getLayoutInflater().inflate(R.layout.pop_add_equitment, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, mEtContent, 0, 0, 1, 0.5f, true);
        TextView pop_equitment_add = layout.findViewById(R.id.pop_equitment_add);
        TextView pop_equitment_not = layout.findViewById(R.id.pop_equitment_not);
        TextView equipment_tv = layout.findViewById(R.id.equipment_tv);
        pop_equitment_add.setText("连 接 设 备");
        pop_equitment_not.setText("取 消 打 印");
        equipment_tv.setText("还没有连接到设备哦");
        pop_equitment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                jumpToOtherActivity(new Intent(EditBillActivity.this, MyEquitmentActivity.class), false);
            }
        });
        pop_equitment_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (popupWindow != null)
            popupWindow.dismiss();
    }
}