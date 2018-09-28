package com.daotian.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.daotian.Base.App;
import com.daotian.Base.Constant;
import com.daotian.Http.ParamUtil;
import com.daotian.Http.ServiceInterface;
import com.daotian.Http.TicketService;
import com.daotian.Model.ResultBO;
import com.daotian.Model.UserBO;
import com.daotian.R;
import com.daotian.Utils.ACache;
import com.daotian.Utils.ToastUtil;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * ��ӭ����
 * @author YZX 2015 11.12
 *
 */
public class WelcomeActivity extends Activity {

	private RelativeLayout mLaunchLayout;
	/**
	 * �����л��Ķ���
	 */
	private Animation mFadeIn;
	private Animation mFadeInScale;
	private Animation mFadeOut;
	private Activity mActivity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
        mLaunchLayout=(RelativeLayout)this.findViewById(R.id.launch);
		mActivity=this;
		init();
		setListener();

    }
    
    /**
	 * �����¼�
	 */
	private void setListener() {
		mFadeIn.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				mLaunchLayout.startAnimation(mFadeInScale);
			}
		});
		mFadeInScale.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				AutoLogin();
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				mLaunchLayout.startAnimation(mFadeOut);
			}
		});
		mFadeOut.setAnimationListener(new AnimationListener() {	
			public void onAnimationStart(Animation animation) {			
				
				
			}

			public void onAnimationRepeat(Animation animation) {
				
			}

			public void onAnimationEnd(Animation animation) {
//				Intent in=new Intent(mActivity,LoginActivity.class);
//				startActivity(in);
//				finish();
			}

		});
		
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		initAnim();
		mLaunchLayout.startAnimation(mFadeIn);
	}

	/**
	 * ��ʼ������
	 */
	private void initAnim() {
		mFadeIn = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.welcome_fade_in);
		mFadeIn.setDuration(1000);
		mFadeInScale = AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.welcome_fade_in_scale);
		mFadeInScale.setDuration(1000);
		mFadeOut = AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.welcome_fade_out);
		mFadeOut.setDuration(500);
	}

	private void AutoLogin() {
		//自动登录
		final ACache mCache = ACache.get(this);
		final String username=mCache.getAsString(Constant.ACACHE_USER_NAME);
		final String pwd=mCache.getAsString(Constant.ACACHE_USER_PWD);

		if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)){
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("account", username);
			paramMap.put("pwd", pwd);
			RequestParams params = ParamUtil.requestParams(paramMap);
			TicketService.post(params, ServiceInterface.Login, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					String result = new String(responseBody);
					Log.e("welcome_result", result);
					if (!TextUtils.isEmpty(result)) {
						result = ParamUtil.unicodeToChinese(result);
					}
					ResultBO resultBO = JSON.parseObject(result, ResultBO.class);
					if (resultBO.getResultId() != 0) {
						ToastUtil.toast(WelcomeActivity.this, resultBO.getResultMsg());
						Intent in=new Intent(mActivity,LoginActivity.class);
						startActivity(in);
						finish();
						return;
					}
					UserBO mUserInfo = JSON.parseObject(resultBO.getResultData(), UserBO.class);
					if(mUserInfo!=null){
						App.mUser=mUserInfo;
						mCache.put(Constant.ACACHE_USERINFO, mUserInfo);
						mCache.put(Constant.ACACHE_USER_NAME, username);
						mCache.put(Constant.ACACHE_USER_PWD, pwd);
						mCache.put(Constant.ACACHE_USER_TOKEN, mUserInfo.getAccess_token());
					}

					Intent in=new Intent(mActivity,BaseActivity.class);
					startActivity(in);
					finish();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
					String arg1 = new String(responseBody);
					ToastUtil.toast(WelcomeActivity.this, "请求失败:" + arg1);
					Intent in=new Intent(mActivity,LoginActivity.class);
					startActivity(in);
					finish();
				}
			});
		}else{
			Intent in=new Intent(mActivity,LoginActivity.class);
			startActivity(in);
			finish();
		}

	}
}