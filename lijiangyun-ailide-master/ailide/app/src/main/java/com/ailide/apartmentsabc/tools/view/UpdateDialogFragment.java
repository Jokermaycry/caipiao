package com.ailide.apartmentsabc.tools.view;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ailide.apartmentsabc.BuildConfig;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.UpdateBean;
import com.ailide.apartmentsabc.tools.Contants;
import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 更新提示对话框
 *
 * @author zhangky@chinasunfun.com
 * @since 2017/2/20
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
	
	private static final String EXTRA_DTO = "extra_dto";
	private static final String EXTRA_FORCE = "extra_force";
	private TextView mTvVersion;
	private ProgressBar mProgressBar;
	private View mLayoutBottom;
	private TextView mTvUpdateNow;
	private ImageView mTvUpdateNext;
	private boolean force = false;
	private boolean isDownloadFinish = false;
	
	public static UpdateDialogFragment newInstance(UpdateBean dto, boolean needForceUpdate) {
		UpdateDialogFragment dialog = new UpdateDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_DTO, dto);
		bundle.putBoolean(EXTRA_FORCE, needForceUpdate);
		dialog.setArguments(bundle);
		return dialog;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.rectangle_no);
			//设置对话框按屏幕宽度的75%
			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
		View inflate = inflater.inflate(R.layout.dialog_fragment_update, container, false);
		mTvVersion = (TextView) inflate.findViewById(R.id.tv_version);
		mProgressBar = (ProgressBar) inflate.findViewById(R.id.progress_bar_download);
		mLayoutBottom = inflate.findViewById(R.id.layout_bottom);
		mTvUpdateNow = (TextView) inflate.findViewById(R.id.tv_update_now);
		mTvUpdateNext = (ImageView) inflate.findViewById(R.id.tv_update_next);
		UpdateBean dto = (UpdateBean) getArguments().getSerializable(EXTRA_DTO);
		force = getArguments().getBoolean(EXTRA_FORCE, false);
		setCancelable(!force);
		mTvVersion.setText(dto != null ? dto.getDescribe() : "未知");
		mTvUpdateNext.setVisibility(force ? View.GONE : View.VISIBLE);
		mTvUpdateNext.setOnClickListener(this);
		mTvUpdateNow.setOnClickListener(this);
		return inflate;
	}
	
	private void hideBottom() {
		mProgressBar.setVisibility(View.VISIBLE);
		mLayoutBottom.setVisibility(View.GONE);
	}
	
	/**
	 * /sdcard/Android/data/data/包名/cache
	 *
	 * @return
	 */
	private String getApkSavePath() {
		return Contants.FILE_PATH;
	}
	
	private String getApkName() {
		int appLabelRes = getContext().getApplicationInfo().labelRes;
		return getContext().getString(appLabelRes) + "_newest.apk";
	}
	
	@Override
	public void onClick(View v) {
		if (v == mTvUpdateNext) {
			dismiss();
		} else if (v == mTvUpdateNow) {
			if (isDownloadFinish) {
				openApk();
			} else {
				UpdateBean dto = (UpdateBean) getArguments().getSerializable(EXTRA_DTO);
				if (dto != null) {
					if (dto.getUrl().contains("apk") || dto.getUrl().contains("APK")) {
						hideBottom();
						if (!force) {
							mTvUpdateNext.setEnabled(false);
							setCancelable(false);
						}
						new DownloadTask().execute(dto.getUrl());
					} else {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						Uri content_url = Uri.parse(dto.getUrl());
						intent.setData(content_url);
						startActivity(intent);
						if (!force) {
							dismiss();
						}
					}
				}
			}
		}
	}
	
	private class DownloadTask extends AsyncTask<String, Integer, String> {
		
		@Override
		protected String doInBackground(String... params) {
			int count;
			try {
				URL url = new URL(params[0]);
				URLConnection conexion = url.openConnection();
				conexion.setRequestProperty("Accept-Encoding", "identity");
				conexion.connect();
				
				int lenghtOfFile = conexion.getContentLength();
				Logger.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
				
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(getApkSavePath() + File.separator + getApkName());
				
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress((int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}
				
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "finish";
		}
		
		protected void onProgressUpdate(Integer... progress) {
			mProgressBar.setProgress(progress[0]);
		}
		
		protected void onPostExecute(String result) {
			openApk();
			mProgressBar.setVisibility(View.GONE);
			mTvUpdateNow.setVisibility(View.VISIBLE);
			mTvUpdateNow.setText("安装");
			mTvUpdateNow.setEnabled(true);
			mLayoutBottom.setVisibility(View.VISIBLE);
			isDownloadFinish = true;
			if (!force) {
				mTvUpdateNext.setEnabled(true);
				setCancelable(true);
				dismiss();
			}
		}
	}
	
	private void openApk() {
		//下载完成，安装apk
		Intent promptInstall = new Intent(Intent.ACTION_VIEW);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			promptInstall.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri contentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", new File(getApkSavePath() + File.separator + getApkName()));
			promptInstall.setDataAndType(contentUri, "application/vnd.android.package-archive");
		} else {
			promptInstall.setDataAndType(
					Uri.parse("file://" + getApkSavePath() + File.separator + getApkName()),
					"application/vnd.android.package-archive");
			promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		getActivity().startActivity(promptInstall);
	}
}
