package com.ailide.apartmentsabc.views.sticker;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.utils.CodeUtil;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventConstant;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class QrCodeToolFragment extends BaseSimpleFragment {

    @BindView(R.id.line_qr_code)
    View mLineQrCode;
    @BindView(R.id.line_bar_code)
    View mLineBarCode;
    @BindView(R.id.iv_qr_code)
    ImageView mIvQrCode;
    @BindView(R.id.iv_bar_code)
    ImageView mIvBarCode;
    @BindView(R.id.et_content)
    EditText mEtContent;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qr_code_tool;
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

    @OnClick({R.id.rl_qr_code, R.id.rl_bar_code, R.id.tv_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_qr_code:
                onClickQrCode();
                break;
            case R.id.rl_bar_code:
                onClickBarCode();
                break;
            case R.id.tv_ok:
                onClickOk();
                break;
        }
    }

    public void onClickQrCode() {
        mEtContent.setHint(R.string.print_to_qr_code);
        mEtContent.setText("");
        mIvQrCode.setImageResource(R.drawable.icon_qr_code_p);
        mIvBarCode.setImageResource(R.drawable.icon_bar_code_n);
        mLineQrCode.setVisibility(View.VISIBLE);
        mLineBarCode.setVisibility(View.GONE);
        mEtContent.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        InputFilter[] filters = {new InputFilter.LengthFilter(999)};
        mEtContent.setFilters(filters);
    }

    public void onClickBarCode() {
        mEtContent.setHint(R.string.print_to_bar_code);
        mEtContent.setText("");
        mIvQrCode.setImageResource(R.drawable.icon_qr_code_n);
        mIvBarCode.setImageResource(R.drawable.icon_bar_code_p);
        mLineQrCode.setVisibility(View.GONE);
        mLineBarCode.setVisibility(View.VISIBLE);
        mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = {new InputFilter.LengthFilter(18)};
        mEtContent.setFilters(filters);
    }

    public void hide(){
        EventBus.getDefault().post(new EventBusEntity(EventConstant.QR_CODE, null));
    }

    public void onClickOk() {
        String textContent = mEtContent.getText().toString();
        if (TextUtils.isEmpty(textContent)) {
            EventBus.getDefault().post(new EventBusEntity(EventConstant.QR_CODE, null));
            return;
        }
        mEtContent.setText("");
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        if (mLineQrCode.getVisibility() == View.VISIBLE) {
            Bitmap bitmap = CodeUtil.createQrCode(textContent, width, width, null);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.QR_CODE, bitmap));
        }else {
            Bitmap bitmap = CodeUtil.createBarCode(textContent, width, width / 2);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.BAR_CODE, bitmap));
        }
    }
}