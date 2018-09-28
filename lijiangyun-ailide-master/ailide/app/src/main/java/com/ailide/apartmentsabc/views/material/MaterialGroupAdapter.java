package com.ailide.apartmentsabc.views.material;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Material;
import com.ailide.apartmentsabc.model.MaterialGroup;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.MyApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MaterialGroupAdapter extends BaseQuickAdapter<MaterialGroup, BaseViewHolder> {

    private String groupName;
    private int ivSize;

    public MaterialGroupAdapter(@Nullable List<MaterialGroup> data, String groupName) {
        super(R.layout.item_material_group, data);
        this.groupName = groupName;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MaterialGroup item) {
        helper.setText(R.id.tv_title, item.getTag_name());
        helper.setVisible(R.id.ll_title, !TextUtils.isEmpty(groupName));
        helper.setText(R.id.tv_group, groupName);
        helper.setText(R.id.tv_collect, item.getCollection_r() + "");
        helper.setText(R.id.tv_save, item.getPrint_num_r() + "");
        NetWorkImageLoader.loadCircularImage(null, Urls.BASE_IMG + item.getImage_url(), (ImageView) helper.getView(R.id.iv_avatar), R.drawable.icon_circle_mrtx, R.drawable.icon_circle_mrtx);
        switch (item.getMaterial().size()) {
            case 0:
                helper.setVisible(R.id.fl_pic1, false);
                helper.setVisible(R.id.fl_pic2, false);
                helper.setVisible(R.id.fl_pic3, false);
                helper.setVisible(R.id.fl_pic4, false);
                break;
            case 1:
                helper.setVisible(R.id.fl_pic1, true);
                showIv((ImageView) helper.getView(R.id.iv_pic1), item.getMaterial().get(0));
                hideView(helper.getView(R.id.fl_pic2));
                hideView(helper.getView(R.id.fl_pic3));
                hideView(helper.getView(R.id.fl_pic4));
                break;
            case 2:
                helper.setVisible(R.id.fl_pic1, true);
                helper.setVisible(R.id.fl_pic2, true);
                showIv((ImageView) helper.getView(R.id.iv_pic1), item.getMaterial().get(0));
                showIv((ImageView) helper.getView(R.id.iv_pic2), item.getMaterial().get(1));
                hideView(helper.getView(R.id.fl_pic3));
                hideView(helper.getView(R.id.fl_pic4));
                break;
            case 3:
                helper.setVisible(R.id.fl_pic1, true);
                helper.setVisible(R.id.fl_pic2, true);
                helper.setVisible(R.id.fl_pic3, true);
                showIv((ImageView) helper.getView(R.id.iv_pic1), item.getMaterial().get(0));
                showIv((ImageView) helper.getView(R.id.iv_pic2), item.getMaterial().get(1));
                showIv((ImageView) helper.getView(R.id.iv_pic3), item.getMaterial().get(2));
                hideView(helper.getView(R.id.fl_pic4));
                break;
            default:
                helper.setVisible(R.id.fl_pic1, true);
                helper.setVisible(R.id.fl_pic2, true);
                helper.setVisible(R.id.fl_pic3, true);
                helper.setVisible(R.id.fl_pic4, true);
                showIv((ImageView) helper.getView(R.id.iv_pic1), item.getMaterial().get(0));
                showIv((ImageView) helper.getView(R.id.iv_pic2), item.getMaterial().get(1));
                showIv((ImageView) helper.getView(R.id.iv_pic3), item.getMaterial().get(2));
                showIv((ImageView) helper.getView(R.id.iv_pic4), item.getMaterial().get(3));
                break;
        }
   /*     RecyclerView rvPic = helper.getView(R.id.rv_pic);
        MaterialPicAdapter adapter;
        List<Material> data = new ArrayList<>();
        if (item.getMaterial().size() >= 4) {
            data.addAll(item.getMaterial().subList(0, 4));
        } else {
            data.addAll(item.getMaterial());
            for (int i = 0, size = data.size(); i < 4 - size; i++) {
                data.add(new Material());
            }
        }
        adapter = new MaterialPicAdapter(data);
        rvPic.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvPic.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.TO_MATERIAL_DETAIL, item.getId()));
            }
        });*/
    }

    public void showIv(final ImageView iv, final Material item) {
        iv.setVisibility(View.VISIBLE);
        if (ivSize == 0) {
            ViewTreeObserver vto = iv.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    ivSize = iv.getWidth();
                    showBitmap(iv, item.getImage_url());
                }
            });
        } else {
            showBitmap(iv, item.getImage_url());
        }
    }

    public void showBitmap(ImageView iv, String url) {
      /*  Glide.with(mContext)
                .load(Urls.BASE_IMG + url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.mrtx)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        try {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            float scaleWidth = ((float) iv.getWidth()) / width;
                            Matrix matrix = new Matrix();
                            matrix.postScale(scaleWidth, scaleWidth);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                            iv.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });*/

            Glide.with(MyApplication.context)
                    .load(Urls.BASE_IMG + url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .placeholder(R.drawable.mrtx)
                    .dontAnimate()
                    .override(ivSize, Target.SIZE_ORIGINAL)
                    .fitCenter()
                    .into(iv);

    }

    public void hideView(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
        notifyDataSetChanged();
    }
}