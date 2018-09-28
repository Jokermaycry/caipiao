package com.ailide.apartmentsabc.tools;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.ailide.apartmentsabc.model.UpdateBean;
import com.ailide.apartmentsabc.tools.view.UpdateDialogFragment;
import com.orhanobut.logger.Logger;


/**
 * 版本更新逻辑管理
 *
 * @author zhangky@chinasunfun.com
 * @since 2017/2/21
 */
public class UpdateManager {
    private static UpdateManager sInstance;
    private Context mContext;
    private UpdateBean updateBean;

    private UpdateManager(Context context, UpdateBean dto) {
        mContext = context;
        updateBean = dto;
    }

    public static UpdateManager getInstance(Context context, UpdateBean dto) {
        if (sInstance == null) sInstance = new UpdateManager(context, dto);
        return sInstance;
    }

    //解析服务器版本中的大版本，如1.2.3的1
    private int getNewVersion1() {
        if (updateBean != null) {
            String newVersion = updateBean.getAppVersion();
            int index = newVersion.indexOf(".");
            String version1 = newVersion.substring(0, index);
            int ver = 0;
            try {
                ver = Integer.parseInt(version1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ver;
        }
        return 0;
    }

    //解析服务器版本中的中版本，如1.2.3的2
    private int getNewVersion2() {
        if (updateBean != null) {
            String newVersion = updateBean.getAppVersion();
            int start = newVersion.indexOf(".");
            int end = newVersion.lastIndexOf(".");
            String version2 = newVersion.substring(start + 1, end);
            int ver = 0;
            try {
                ver = Integer.parseInt(version2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ver;
        }
        return 0;
    }

    //解析服务器版本中的小版本，如1.2.3的3
    private int getNewVersion3() {
        if (updateBean != null) {
            String newVersion = updateBean.getAppVersion();
            int end = newVersion.lastIndexOf(".");
            String version3 = newVersion.substring(end + 1, newVersion.length());
            int ver = 0;
            try {
                ver = Integer.parseInt(version3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ver;
        }
        return 0;
    }

    //解析当前版本中的大版本，如1.2.3的1
    private int getCurVersion1() {
        if (mContext != null) {
            try {
                String curVersion =VersionUtil.getVersionName(mContext);
                int index = curVersion.indexOf(".");
                String version1 = curVersion.substring(0, index);
                return Integer.parseInt(version1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    //解析当前版本中的中版本，如1.2.3的2
    private int getCurVersion2() {
        if (mContext != null) {
            try {
                String curVersion = VersionUtil.getVersionName(mContext);
                int start = curVersion.indexOf(".");
                int end = curVersion.lastIndexOf(".");
                String version2 = curVersion.substring(start + 1, end);
                return Integer.parseInt(version2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    //解析当前版本中的小版本，如1.2.3的3
    private int getCurVersion3() {
        if (mContext != null) {
            try {
                String curVersion = VersionUtil.getVersionName(mContext);
                int end = curVersion.lastIndexOf(".");
                String version3 = curVersion.substring(end + 1, curVersion.length());
                return Integer.parseInt(version3);
            } catch (Exception e) {
              e.printStackTrace();
            }
        }
        return 0;
    }

    public boolean hasNewVersion() {
        if (getNewVersion1() > getCurVersion1()) {
            return true;
        }
        if (getNewVersion1() >= getCurVersion1() && getNewVersion2() > getCurVersion2()) {
            return true;
        }
        if (getNewVersion1() >= getCurVersion1() && getNewVersion2() >= getNewVersion2() && getNewVersion3() > getCurVersion3()) {
            return true;
        }
        return false;
    }

    public void showUpdateDialog(FragmentManager manager, String fragTag) {
        UpdateDialogFragment fragment = UpdateDialogFragment.newInstance(updateBean, false);
        fragment.show(manager, fragTag);
    }

    private boolean needForceUpdate() {
        if (updateBean != null && updateBean.isForce()) {
            return true;
        }
        Logger.e("eeeee",getNewVersion1() +"&&" + getCurVersion1());
        if (getNewVersion1() > getCurVersion1()) {
            return true;
        }
        Logger.e("jjjjj",getNewVersion2() + "&"+getCurVersion2());
        if (getNewVersion1() >= getCurVersion1() && getNewVersion2() > getCurVersion2()) {
            return true;
        }
        return false;
    }
}
