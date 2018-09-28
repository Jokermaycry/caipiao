package com.ailide.apartmentsabc.views.material;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Material;
import com.ailide.apartmentsabc.tools.Urls;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MaterialDetailPicAdapter extends BaseQuickAdapter<Material, BaseViewHolder> {

    public MaterialDetailPicAdapter(@Nullable List<Material> data) {
        super(R.layout.item_material_pic_detail, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Material item) {
        if (!TextUtils.isEmpty(item.getImage_url())) {
            Glide.with(mContext)
                    .load(Urls.BASE_IMG + item.getImage_url())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                            ImageView ivPic = helper.getView(R.id.iv_pic);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivPic.getLayoutParams();
                            if (bitmap.getWidth() > (ivPic.getWidth() - params.leftMargin - params.rightMargin)) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                float scaleWidth = ((float) (ivPic.getWidth() - params.leftMargin - params.rightMargin)) / width;
                                Matrix matrix = new Matrix();
                                matrix.postScale(scaleWidth, scaleWidth);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                                ivPic.setImageBitmap(bitmap);
                            } else {
                                ivPic.setImageBitmap(bitmap);
                            }
                        }
                    });
            helper.getView(R.id.ll_pic).setBackground(mContext.getResources().getDrawable(R.drawable.bg_material_pic));
        }else {
            helper.getView(R.id.ll_pic).setBackground(null);
        }
    }
}