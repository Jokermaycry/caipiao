package com.ailide.apartmentsabc.views.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ailide.apartmentsabc.BuildConfig;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.framework.util.ActivityManagerUtil;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.HtmlBean;
import com.ailide.apartmentsabc.model.UpdateBean;
import com.ailide.apartmentsabc.tools.Contants;
import com.ailide.apartmentsabc.tools.UpdateManager;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.VersionUtil;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.tools.view.UpdateDialogFragment;
import com.ailide.apartmentsabc.views.addequitment.AddEquitmentActivity;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.login.LoginActivity;
import com.ailide.apartmentsabc.views.main.MainActivity;
import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.iv_include_back)
    ImageView ivIncludeBack;
    @BindView(R.id.tv_include_title)
    TextView tvIncludeTitle;
    @BindView(R.id.iv_include_right)
    ImageView ivIncludeRight;
    @BindView(R.id.tv_include_right)
    TextView tvIncludeRight;
    @BindView(R.id.setting_gengxin)
    TextView settingGengxin;
    @BindView(R.id.setting_login_out)
    TextView settingLoginOut;

    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        tvIncludeTitle.setText("设置");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(popupWindow!=null)
            popupWindow.dismiss();
    }

    @OnClick({R.id.iv_include_back, R.id.setting_gengxin, R.id.setting_login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_include_back:
                doBack();
                break;
            case R.id.setting_gengxin:
                post();
                break;
            case R.id.setting_login_out:
                ActivityManagerUtil.getInstance().closeAll();
                SPUtil.put(this,"user","");
                SPUtil.put(this,"expiration","");
                jumpToOtherActivity(new Intent(SettingActivity.this, LoginActivity.class), true);
                break;
        }
    }
    private void post() {
        showLoading("加载中...");
        OkGo.<String>post(Urls.SETTING)//
                .tag(this)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (isFinishing())
                            return;
                        dismissLoading();
                        if (response.isSuccessful()) {
                            Logger.e("tag",response.body());
                            HtmlBean htmlBean = JSON.parseObject(response.body(), HtmlBean.class);
                            UpdateBean updateBean = new UpdateBean();
                            updateBean.setId(htmlBean.getData().getApp().getId());
                            updateBean.setAppId(htmlBean.getData().getApp().getAppfor());
                            updateBean.setAppVersion(htmlBean.getData().getApp().getEdition());
                            updateBean.setAppName(htmlBean.getData().getApp().getFile_name());
                            updateBean.setDescribe(htmlBean.getData().getApp().getRemark());
                            updateBean.setForce(false);
                            updateBean.setUrl(Urls.BASE_IMG + htmlBean.getData().getApp().getFile());
                            UpdateManager manager = UpdateManager.getInstance(SettingActivity.this, updateBean);
                            if (manager.hasNewVersion()) {
//                                manager.showUpdateDialog(getSupportFragmentManager(), "UpdateDialogFragment");
                                showPopView(updateBean);
                            } else {
                                toast("已是最新版本");
                            }
                        }
                    }
                });
    }

    private boolean isDownloadFinish = false;
    private ProgressBar mProgressBar;
    private TextView pop_equitment_add;
    private TextView pop_equitment_not;
    private LinearLayout progress_bar_download_ly;
    private TextView progress_bar_download_tv;
    private void showPopView(final UpdateBean updateBean) {
        View layout = getLayoutInflater().inflate(R.layout.pop_update_equitment, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, tvIncludeTitle, 0, 0, 1, 0.5f, true);
        pop_equitment_add = layout.findViewById(R.id.pop_equitment_add);
        pop_equitment_not = layout.findViewById(R.id.pop_equitment_not);
        TextView pop_update_remain = layout.findViewById(R.id.pop_update_remain);
        pop_update_remain.setText(updateBean != null ? updateBean.getDescribe() : "未知");
        TextView equipment_tv = layout.findViewById(R.id.equipment_tv);
        equipment_tv.setText("发现新版本-"+ VersionUtil.getVersionName(this));
        progress_bar_download_ly = layout.findViewById(R.id.progress_bar_download_ly);
        progress_bar_download_tv = layout.findViewById(R.id.progress_bar_download_tv);
        mProgressBar = layout.findViewById(R.id.progress_bar_download);
        pop_equitment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDownloadFinish) {
                    openApk();
                } else {
                    UpdateBean dto = updateBean;
                    if (dto != null) {
                        if (dto.getUrl().contains("apk") || dto.getUrl().contains("APK")) {
                            progress_bar_download_ly.setVisibility(View.VISIBLE);
                            pop_equitment_add.setVisibility(View.GONE);
                            pop_equitment_not.setVisibility(View.GONE);
                            new DownloadTask().execute(dto.getUrl());
                        } else {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(dto.getUrl());
                            intent.setData(content_url);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        pop_equitment_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    private void openApk() {
        //下载完成，安装apk
        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            promptInstall.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", new File(getApkSavePath() + File.separator + getApkName()));
            promptInstall.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            promptInstall.setDataAndType(
                    Uri.parse("file://" + getApkSavePath() + File.separator + getApkName()),
                    "application/vnd.android.package-archive");
            promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(promptInstall);
    }
    private String getApkSavePath() {
        return Contants.FILE_PATH;
    }
    private String getApkName() {
        int appLabelRes = this.getApplicationInfo().labelRes;
        return this.getString(appLabelRes) + "_newest.apk";
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
            progress_bar_download_tv.setText(progress[0]+"%");
        }

        protected void onPostExecute(String result) {
            openApk();
            progress_bar_download_ly.setVisibility(View.GONE);
            pop_equitment_add.setVisibility(View.VISIBLE);
            pop_equitment_not.setVisibility(View.VISIBLE);
            pop_equitment_add.setText("安装");
            pop_equitment_add.setEnabled(true);
            isDownloadFinish = true;
        }
    }
}
