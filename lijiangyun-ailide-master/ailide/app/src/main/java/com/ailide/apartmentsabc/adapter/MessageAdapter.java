package com.ailide.apartmentsabc.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.FriendChatBean;
import com.ailide.apartmentsabc.model.PrintNameBean;
import com.ailide.apartmentsabc.tools.Contants;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.TimeUtils;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.MyApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class MessageAdapter extends BaseMultiItemQuickAdapter<FriendChatBean, BaseViewHolder> {
	private List<FriendChatBean> data;
	private int readPosition = -1;
	
	public MessageAdapter(List<FriendChatBean> data) {
		super(data);
		addItemType(1, R.layout.item_friend_left);
		addItemType(2, R.layout.item_friend_right);
		this.data = data;
	}
	
	@Override
	protected void convert(BaseViewHolder helper, FriendChatBean item) {
		if(item.getType().equals("1")){
			final ImageView imageView =helper.getView(R.id.item_left_content_img);
			ImageView imageView1 = helper.getView(R.id.item_left_head_img);
			if(!TextUtils.isEmpty(item.getContent())){
				Glide.with(MyApplication.context)
						.load(Urls.BASE_IMG +item.getContent())
						.asBitmap()
						.diskCacheStrategy(DiskCacheStrategy.SOURCE)
						.into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
							@Override
							public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
								int width = bitmap.getWidth();
								int height = bitmap.getHeight();
								imageView.setImageBitmap(bitmap);
							}
						});
			}
//				NetWorkImageLoader.loadNetworkImage(null,Urls.BASE_IMG +item.getContent(), imageView);
			if(!TextUtils.isEmpty(item.getProfile_image_url()))
				NetWorkImageLoader.loadCircularImage(null,item.getProfile_image_url(),imageView1);
			if(item.getCreate_time()!=0)
				helper.setText(R.id.item_friend_left_time, TimeUtils.formatTimeMinute(item.getCreate_time()*1000));
			helper.addOnClickListener(R.id.item_left_head_img);
		}else {
			final ImageView imageView =helper.getView(R.id.item_right_content_img);
			ImageView imageView1 = helper.getView(R.id.item_right_head_img);
			if(!TextUtils.isEmpty(item.getContent())){
				if(!TextUtils.isEmpty(item.getContent())){
					Glide.with(MyApplication.context)
							.load(Urls.BASE_IMG +item.getContent())
							.asBitmap()
							.diskCacheStrategy(DiskCacheStrategy.SOURCE)
							.into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
								@Override
								public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
									int width = bitmap.getWidth();
									int height = bitmap.getHeight();
									imageView.setImageBitmap(bitmap);
								}
							});
				}
			}
//				NetWorkImageLoader.loadNetworkImage(null,Urls.BASE_IMG +item.getContent(),imageView);
			if(!TextUtils.isEmpty(item.getProfile_image_url()))
				NetWorkImageLoader.loadCircularImage(null,item.getProfile_image_url(),imageView1);
			if(item.getCreate_time()!=0)
				helper.setText(R.id.item_friend_right_time, TimeUtils.formatTimeMinute(item.getCreate_time()*1000));
			helper.addOnClickListener(R.id.item_right_head_img);
		}
	}
	
	public void setListData(List<FriendChatBean> data) {
		this.data = data;
	}
	
	public void setReadPosition(int position) {
		readPosition = position;
	}
}
