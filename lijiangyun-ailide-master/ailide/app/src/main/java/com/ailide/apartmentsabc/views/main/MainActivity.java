package com.ailide.apartmentsabc.views.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ailide.apartmentsabc.BuildConfig;
import com.ailide.apartmentsabc.eventbus.ChangeTabEvent;
import com.ailide.apartmentsabc.eventbus.JpushEvent;
import com.ailide.apartmentsabc.model.HtmlBean;
import com.ailide.apartmentsabc.model.UpdateBean;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.Contants;
import com.ailide.apartmentsabc.tools.SetJpushUtil;
import com.ailide.apartmentsabc.tools.TimeUtils;
import com.ailide.apartmentsabc.tools.UpdateManager;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.VersionUtil;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.views.login.LoginActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.ailide.apartmentsabc.views.mine.SettingActivity;
import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.framework.util.ActivityManagerUtil;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.TabEntity;
import com.ailide.apartmentsabc.tools.ToastUtil;
import com.ailide.apartmentsabc.utils.WordAndPicture;
import com.ailide.apartmentsabc.views.addequitment.AddEquitmentActivity;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.FindFragment;
import com.ailide.apartmentsabc.views.main.fragment.FriendFragment;
import com.ailide.apartmentsabc.views.main.fragment.MineFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.apartment_main_view_pager)
    ViewPager apartmentMainViewPager;
    @BindView(R.id.apartment_main_tab_layout)
    CommonTabLayout apartmentMainTabLayout;

    private PopupWindow popupWindow;
    private String[] mTitles;
    private int[] mIconUnselectIds = {
            R.drawable.tab_home_unselect, R.drawable.tab_speech_unselect,
            R.drawable.tab_loan_unselect ,R.drawable.tab_contact_unselect};
    private int[] mIconSelectIds = {
            R.drawable.tab_home_select, R.drawable.tab_speech_select,
            R.drawable.tab_loan_select , R.drawable.tab_contact_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(!TextUtils.isEmpty(SPUtil.get(this,"user","").toString())){
            Logger.e("user",SPUtil.get(this,"user","").toString());
            UserBean userBean = JSON.parseObject(SPUtil.get(this,"user","").toString(),UserBean.class);
            SetJpushUtil setJpush=new SetJpushUtil(this);
            setJpush.setTag(userBean.getData().getNote_num());
        }
        mTitles = getResources().getStringArray(R.array.main_activity);
        initTab();
        post();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TextUtils.isEmpty(SPUtil.get(this,"first_equitment","").toString())){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopView();
                }
            },500);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(popupWindow!=null)
            popupWindow.dismiss();
    }

    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        PrintFragment printFragment = new PrintFragment();
        FriendFragment friendFragment = new FriendFragment();
        FindFragment findFragment = new FindFragment();
        MineFragment mineFragment = new MineFragment();
        mFragments.add(printFragment);
        mFragments.add(friendFragment);
        mFragments.add(findFragment);
        mFragments.add(mineFragment);
        apartmentMainViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        apartmentMainViewPager.setOffscreenPageLimit(4);
        apartmentMainTabLayout.setTabData(mTabEntities);
        apartmentMainTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                apartmentMainViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
//                    apartmentMainTabLayout.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        apartmentMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                apartmentMainTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//
//                exitTime = System.currentTimeMillis();
//
//                ToastUtil.toast(MainActivity.this, getResources().getString(R.string.reclick_back_key_exit));
//
//            } else {
//                moveTaskToBack(true);
////                ActivityManagerUtil.getInstance().exit();// exit
//            }
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void showPopView() {
        SPUtil.put(this,"first_equitment","1");
        View layout = getLayoutInflater().inflate(R.layout.pop_add_equitment, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, apartmentMainViewPager, 0, 0, 1, 0.5f, true);
        TextView pop_equitment_add = layout.findViewById(R.id.pop_equitment_add);
        TextView pop_equitment_not = layout.findViewById(R.id.pop_equitment_not);
        TextView equipment_tv = layout.findViewById(R.id.equipment_tv);
        equipment_tv.setText("还没有识别到设备哦");
        pop_equitment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                jumpToOtherActivity(new Intent(MainActivity.this, AddEquitmentActivity.class),false);
            }
        });
        pop_equitment_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeTabEvent event) {
        apartmentMainViewPager.setCurrentItem(1);
    }

    private void post() {
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
                            UpdateManager manager = UpdateManager.getInstance(MainActivity.this, updateBean);
                            if (manager.hasNewVersion()) {
//                                manager.showUpdateDialog(getSupportFragmentManager(), "UpdateDialogFragment");
                                showPopView(updateBean);
                            } else {

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
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout,apartmentMainViewPager , 0, 0, 1, 0.5f, true);
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
