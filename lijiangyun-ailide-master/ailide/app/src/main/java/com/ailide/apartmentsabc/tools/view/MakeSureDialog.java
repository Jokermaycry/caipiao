package com.ailide.apartmentsabc.tools.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;


/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class MakeSureDialog extends DialogFragment {
	private View mView;
	private String content = "";
	private String remain_content = "";
	private boolean isVisibility = false;
	private boolean isCancleVisility = false;
	private String sureText="确定";
	private String cancleText="取消";
	private String Text="";
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		mView = inflater.inflate(R.layout.dialog_make_sure, container);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		initView();
		return mView;
	}
	
	private void initView() {
		TextView mTvSure = (TextView) mView.findViewById(R.id.dialog_confirm);
		TextView mTvCancel = (TextView) mView.findViewById(R.id.dialog_cancel);
		CenterTextView mTvContent = (CenterTextView) mView.findViewById(R.id.dialog_center_tv);
		CenterTextView dialog_center_remain_tv = (CenterTextView) mView.findViewById(R.id.dialog_center_remain_tv);
		mTvContent.setText(content);
		mTvSure.setText(sureText);
		mTvCancel.setText(cancleText);
		dialog_center_remain_tv.setText(remain_content);
		if (isVisibility) {
			dialog_center_remain_tv.setVisibility(View.VISIBLE);
			dialog_center_remain_tv.setText(Text);
		}
		if(isCancleVisility){
			mTvCancel.setVisibility(View.GONE);
		}
		mTvSure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onSureClick();
				}
				dismiss();
			}
		});
		mTvCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onCancelClick();
				}
				dismiss();
			}
		});
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setSureText(String sureText) {
		this.sureText=sureText;
	}
	public void setCancleText(String sureText) {
		this.cancleText=sureText;
	}
	public void setText(String Text) {
		this.Text=Text;
	}
	public void setRemainContent(String remain_content) {
		this.remain_content = remain_content;
	}
	
	public void setVisibility(boolean isVisibility) {
		this.isVisibility = isVisibility;
	}
	public void setCancleVisibility(boolean isCancleVisibility) {
		this.isCancleVisility = isCancleVisibility;
	}
	public interface onDialogClickListener {
		public void onSureClick();
		
		public void onCancelClick();
	}
	
	private onDialogClickListener mListener;
	
	public void setDialogClickListener(onDialogClickListener mListener) {
		this.mListener = mListener;
	}
	
	public void show(FragmentManager manager, String tag) {
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(this, tag);
		ft.commitAllowingStateLoss();
	}
}
