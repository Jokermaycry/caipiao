package com.ailide.apartmentsabc.tools;

import android.app.Activity;
import android.graphics.Bitmap;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.ShareBean;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class ShareUtil {
	public static void toShare(Activity activity, SHARE_MEDIA platform, ShareBean shareBean, boolean isPicture, UMShareListener umShareListener) {
//		shareBean = new ShareBean();
//		shareBean.setShare_content(" csv sd");
//		shareBean.setShare_title("1111");
//		shareBean.setShare_img("http://static.daily.17bxw.com/ff80808160bf38670160c00bd23c0010/YVnI7b.png");
//		shareBean.setShare_url("http://h5.daily.17bxw.com/yf-h5page?newId=6");
		UMImage umImage;
		if (!"".equals(shareBean.getShare_img()) && null != shareBean.getShare_img()) {
			umImage = new UMImage(activity, shareBean.getShare_img());
		} else {
			umImage = new UMImage(activity, R.mipmap.ic_launcher);
		}
		if (!isPicture) {
			UMWeb web = new UMWeb(shareBean.getShare_url());
			web.setTitle(shareBean.getShare_title());//标题
			web.setThumb(umImage);  //缩略图
			web.setDescription(shareBean.getShare_content());//描述
			new ShareAction(activity).setPlatform(platform)
					.withMedia(web)
					.setCallback(umShareListener)
					.share();
		} else {
			new ShareAction(activity).setPlatform(platform)
					.withMedia(umImage)
					.setCallback(umShareListener)
					.share();
		}
	}
	
	public static void shareOnlyPicture(Activity activity, SHARE_MEDIA platform, Bitmap bitmap, UMShareListener umShareListener) {
		UMImage image = new UMImage(activity, bitmap);//bitmap文件
		new ShareAction(activity).setPlatform(platform)
				.withMedia(image)
				.setCallback(umShareListener)
				.share();
	}
}
