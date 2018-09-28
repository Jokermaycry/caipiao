package com.ailide.apartmentsabc.views.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.ailide.apartmentsabc.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import kr.co.namee.permissiongen.PermissionGen;


/**
 * 简单逻辑的fragment基类
 */
public abstract class BaseSimpleFragment extends Fragment {
	
	protected BaseActivity mActivity;
	Unbinder unbinder;
	View view = null;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (BaseActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(getLayoutId(), container, false);
		unbinder = ButterKnife.bind(this, view);
		initView(view, savedInstanceState);
		initData();
		setListener();
		return view;
	}
	
	/**
	 * 获取布局文件ID
	 */
	protected abstract int getLayoutId();
	
	/**
	 * 初始化布局UI
	 *
	 * @return 返回布局UI的View
	 */
	protected abstract void initView(View view, Bundle savedInstanceState);
	
	/**
	 * 初始化数据
	 */
	protected abstract void initData();
	
	/**
	 * 设置监听事件
	 */
	protected abstract void setListener();
	
	
	/**
	 * 获取宿主Activity
	 */
	protected BaseActivity getHoldingActivity() {
		return mActivity;
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
	
	private View mLoadingView;
	
	protected void showLoading(String msg) {
		View contentView = getActivity().findViewById(android.R.id.content);
		if (contentView != null && contentView instanceof FrameLayout) {
			if (mLoadingView == null) {
				mLoadingView = View.inflate(getContext(), R.layout.common_loading_view, null);
			}
			mLoadingView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});
			// Make the first word's letters jump
			final TextView textView2 = (TextView) mLoadingView.findViewById(R.id.loading_msg);
			textView2.setText(msg);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			mLoadingView.setLayoutParams(params);
			ViewParent viewParent = mLoadingView.getParent();
			if (viewParent != null) {
				((FrameLayout) viewParent).removeView(mLoadingView);
			}
			((FrameLayout) contentView).addView(mLoadingView);
		}
	}
	
	protected void dismissLoading() {
		if (null == getActivity()) {
			return;
		}
		if (null == getActivity().findViewById(android.R.id.content)) {
			return;
		}
		View contentView = getActivity().findViewById(android.R.id.content);
		if (contentView != null && contentView instanceof FrameLayout) {
			if (mLoadingView != null) {
				((FrameLayout) contentView).removeView(mLoadingView);
			}
		}
	}
	
}
