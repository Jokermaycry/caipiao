package com.ailide.apartmentsabc.framework.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;


/**
 * 加载对话框
 */
public class GenericProgressDialog extends AlertDialog {
	private TextView mMessageView;
	private CharSequence mMessage;
	private boolean mIndeterminate;
	private boolean mProgressVisiable;
	private AnimationDrawable animationRefresh;
	private Context context;
	private ProgressBar mProgress;
	
	public GenericProgressDialog(Context context) {
		super(context/*,R.style.Float*/);
		this.context = context;
	}
	
	public GenericProgressDialog(Context context, int theme) {
		super(context,/*, R.style.Float*/theme);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_progress_dialog);
		mProgress = (ProgressBar) findViewById(R.id.pb_refresh);
		mMessageView = (TextView) findViewById(R.id.message);
		animationRefresh = new AnimationDrawable();
		animationRefresh.start();
		setMessageAndView();
	}
	
	private void setMessageAndView() {
		mMessageView.setText(mMessage);
		
		if (mMessage == null || "".equals(mMessage)) {
			mMessageView.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void setMessage(CharSequence message) {
		mMessage = message;
	}
	
	/**
	 * 圈圈可见性设置
	 *
	 * @param progressVisiable 是否显示圈圈
	 */
	public void setProgressVisiable(boolean progressVisiable) {
		mProgressVisiable = progressVisiable;
	}
	
	public void setIndeterminate(boolean indeterminate) {
		if (mProgress != null) {
			mProgress.setIndeterminate(indeterminate);
		} else {
			mIndeterminate = indeterminate;
		}
	}
}
