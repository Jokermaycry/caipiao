
package com.ailide.apartmentsabc.views.base;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.eventbus.NetWorkEvent;
import com.ailide.apartmentsabc.framework.service.NetworkStateService;
import com.ailide.apartmentsabc.framework.util.ActivityHelper;
import com.ailide.apartmentsabc.framework.util.ActivityManagerUtil;
import com.ailide.apartmentsabc.tools.ToastUtil;
import com.ailide.apartmentsabc.tools.view.InputLayout;
import com.ailide.apartmentsabc.tools.view.gitview.GifMovieView;
import com.ailide.apartmentsabc.views.MyApplication;
import com.jaeger.library.StatusBarUtil;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.framework.util.ActivityResponsable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import framework.utils.KeyBoardUtil;
import kr.co.namee.permissiongen.PermissionGen;


/**
 * Activity基类，共用方法
 */
public abstract class BaseActivity extends AppCompatActivity implements ActivityResponsable {
	
	// Activity辅助类
	private ActivityHelper mActivityHelper;
	private AlertDialog dialog = null;
//	// 用户id
//	public String mUserID;
//	// 登录token
//	public String mAuthToken;
//	// 登录手机号码
//	public String mPhoneNumber;
//	//头像地址
//	public String mImageUrl;
//	public SharedPreferences mShardPreferences;
//	public String mID;
//	public String mNickName;
//	public int mApproveAble;
	
	MyApplication myApplication;

	public boolean isTouchHideInput() {
		return touchHideInput;
	}

	public void setTouchHideInput(boolean touchHideInput) {
		this.touchHideInput = touchHideInput;
	}

	private boolean touchHideInput = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
//		initBaseData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//初始化shardpreference数据
		initBaseData();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		setCurrentStatusColor();
	}
	
	private void init() {
		mActivityHelper = new ActivityHelper(this);
		//注册EventBus
		EventBus.getDefault().register(this);
		//启动网络检查服务
		startService(new Intent(this, NetworkStateService.class));
		//把当前activity添加到activity管理栈
		ActivityManagerUtil.getInstance().addActivity(this);
	}
	
	private void initBaseData() {
//		mShardPreferences = getSharedPreferences(CacheConfig.CONFIG_SHARED, 0);
//		mUserID = mShardPreferences.getString(CacheConfig.ID, "");
//		mPhoneNumber = mShardPreferences.getString(CacheConfig.PHONE_NUMBER, "");
//		mImageUrl = mShardPreferences.getString(CacheConfig.HEAD_IAMGE, "");
//		mID = mShardPreferences.getString(CacheConfig.USERINFO_ID, "");
//		mNickName = mShardPreferences.getString(CacheConfig.NICK_NAME, "");
//		mApproveAble = mShardPreferences.getInt(CacheConfig.APPROVEABLE, 0);
	}

//	/**
//	 * 设置默认状态栏颜色样式
//	 */
//	private void setCurrentStatusColor() {
//		SharedPreferences preferences = getSharedPreferences(FrameworkConfig.PREFERENCE_STATUS_COLOR, this.MODE_PRIVATE);
//		int color = preferences.getInt(FrameworkConfig.STATUS_COLOR, 0);
//		int alpha = preferences.getInt(FrameworkConfig.STATUS_ALPHA, 0);
//		//当第一次启动时，配置本地默认样式
//		if (color == 0) {
//			SharedPreferences.Editor editor = preferences.edit();
//			//配置默认颜色
//			editor.putInt(FrameworkConfig.STATUS_COLOR, 0xfc543a);
//			//配置默认透明度
//			editor.putInt(FrameworkConfig.STATUS_ALPHA, 0);
//			editor.commit();
//			setStatusColor(0xfc543a, 0);
//		} else {
//			setStatusColor(0xfc543a, 0);
//		}
//	}
	
	/**
	 * 设置状态栏颜色样式
	 *
	 * @param color 颜色
	 * @param alpha 透明度
	 */
	public void setStatusColor(int color, int alpha) {
		StatusBarUtil.setColor(this, color, alpha);
	}
	
	/**
	 * 统一动画效果跳转到其他Activity
	 *
	 * @param intent 对应的Intent
	 * @param finish 是否关闭当前Acticity,传true为关闭
	 */
	public void jumpToOtherActivity(Intent intent, boolean finish) {
		if (finish) {
			this.finish();
			startActivity(intent);
			overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			return;
		}
		startActivity(intent);
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}
	
	/**
	 * 返回上一个页面
	 */
	public void doBack() {
//		onBackPressed();
		finish();
		overridePendingTransition(0, android.R.anim.slide_out_right);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, android.R.anim.slide_out_right);
	}
	
	/**
	 * 弹对话框
	 *
	 * @param title            标题
	 * @param msg              消息
	 * @param positive         确定
	 * @param positiveListener 确定回调
	 * @param negative         否定
	 * @param negativeListener 否定回调
	 */
	@Override
	public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener) {
		mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener);
	}
	
	/**
	 * 弹对话框
	 *
	 * @param title                    标题
	 * @param msg                      消息
	 * @param positive                 确定
	 * @param positiveListener         确定回调
	 * @param negative                 否定
	 * @param negativeListener         否定回调
	 * @param isCanceledOnTouchOutside 外部点是否可以取消对话框
	 */
	@Override
	public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener,
                      Boolean isCanceledOnTouchOutside) {
		mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener,
				isCanceledOnTouchOutside);
	}
	
	/**
	 * 显示TOAST
	 *
	 * @param msg 消息
	 */
	@Override
	public void toast(String msg) {
		ToastUtil.toast(getApplicationContext(), msg);
//		mActivityHelper.toast(msg);
	}
	
	/**
	 * 显示进度对话框
	 *
	 * @param msg 消息
	 */
	@Override
	public void showProgressDialog(String msg) {
		mActivityHelper.showProgressDialog(msg);
	}
	
	/**
	 * 显示可取消的进度对话框
	 *
	 * @param msg 消息
	 */
	public void showProgressDialog(final String msg, final boolean cancelable,
                                   final DialogInterface.OnCancelListener cancelListener) {
		mActivityHelper.showProgressDialog(msg, cancelable, cancelListener);
	}
	
	@Override
	public void dismissProgressDialog() {
		mActivityHelper.dismissProgressDialog();
	}
	
	
	//EventBus回掉请求数据,此处处理为当接受到断网广播，弹出提示框
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(NetWorkEvent event) {
		String mess = event.getMsg();
		if ("lost".equals(mess)) {
			dialog = new AlertDialog.Builder(this)
					.setTitle("您当前没有连接网络！")
					.setMessage(" ")
					.setNegativeButton("取消", null)
					.setPositiveButton("查看", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = null;
							try {
								String sdkVersion = android.os.Build.VERSION.SDK;
								if (Integer.valueOf(sdkVersion) > 10) {
									intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								} else {
									intent = new Intent();
									ComponentName comp = new ComponentName("com.android.settings",
											"com.android.settings.WirelessSettings");
									intent.setComponent(comp);
									intent.setAction("android.intent.action.VIEW");
								}
								BaseActivity.this.startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).show();
			dialog.setCanceledOnTouchOutside(false);
		}
		if ("connet".equals(mess)) {
			if (dialog == null) {
				return;
			} else {
				dialog.dismiss();
				dialog = null;
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//把销毁的activity从activity管理栈中移除
		ActivityManagerUtil.getInstance().removeActivity(this);
		//注销EventBus
		EventBus.getDefault().unregister(this);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	private View mLoadingView;
	
	protected void showLoading(String msg) {
		View contentView = findViewById(android.R.id.content);
		if (contentView != null && contentView instanceof FrameLayout) {
			if (mLoadingView == null) {
				mLoadingView = View.inflate(this, R.layout.common_loading_view, null);
			}
			mLoadingView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});
			GifMovieView gifMovieView = (GifMovieView) mLoadingView.findViewById(R.id.iv_loading);
			gifMovieView.setOnClickListener(null);
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
		View contentView = findViewById(android.R.id.content);
		if (contentView != null && contentView instanceof FrameLayout) {
			if (mLoadingView != null) {
				((FrameLayout) contentView).removeView(mLoadingView);
			}
		}
	}
	
	/**
	 * 当用户离开edittext区域后自动取消焦点
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (touchHideInput) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				View v = getCurrentFocus();
				if (v != null && v.getParent() != null && v.getParent().getParent() instanceof InputLayout) {
					Rect outRect = new Rect();
					((InputLayout) v.getParent().getParent()).getGlobalVisibleRect(outRect);
					if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
						KeyBoardUtil.closeKeybord((EditText) v, this);
						v.clearFocus();
					}
				} else if (v != null && v instanceof EditText) {
					Rect outRect = new Rect();
					v.getGlobalVisibleRect(outRect);
					if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
						KeyBoardUtil.closeKeybord((EditText) v, this);
						v.clearFocus();
					}
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}
}
