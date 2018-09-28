package com.ailide.apartmentsabc.views.material;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Material;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;

import java.util.List;

public class MaterialPicAdapter extends BaseQuickAdapter<Material, BaseViewHolder> {

    public MaterialPicAdapter(@Nullable List<Material> data) {
        super(R.layout.item_material_pic, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Material item) {
        final ImageView ivPic = helper.getView(R.id.iv_pic);
        if (!TextUtils.isEmpty(item.getImage_url())) {
            ViewTreeObserver vto = ivPic.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ivPic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    Glide.with(mContext)
                            .load(Urls.BASE_IMG + item.getImage_url())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.drawable.mrtx)
                            .dontAnimate()
                            .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                                @Override
                                public void onLoadStarted(Drawable placeholder) {
                                    super.onLoadStarted(placeholder);
                                    NetWorkImageLoader.loadNetworkImage(null,R.drawable.mrtx,ivPic);
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    super.onLoadFailed(e, errorDrawable);
                                    NetWorkImageLoader.loadNetworkImage(null,R.drawable.mrtx,ivPic);
                                }
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                    try {
                                        if (bitmap.getWidth() > ivPic.getWidth()) {
                                            int width = bitmap.getWidth();
                                            int height = bitmap.getHeight();
                                            float scaleWidth = ((float) ivPic.getWidth()) / width;
                                            Matrix matrix = new Matrix();
                                            matrix.postScale(scaleWidth, scaleWidth);
                                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                                            ivPic.setImageBitmap(bitmap);
                                        } else {
                                            ivPic.setImageBitmap(bitmap);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            });
            helper.getView(R.id.ll_pic).setBackground(mContext.getResources().getDrawable(R.drawable.bg_material_pic));
        } else {
            helper.getView(R.id.ll_pic).setBackground(null);
        }
        helper.setVisible(R.id.fv_shade, !TextUtils.isEmpty(item.getImage_url()) && helper.getLayoutPosition() == 3);
    }
}