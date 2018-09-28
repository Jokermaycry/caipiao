package com.ailide.apartmentsabc.views.bill;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Bill;
import com.ailide.apartmentsabc.tools.Urls;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BillContentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Bill bill;
    private int viewWidth;

    public BillContentAdapter(Bill bill, @Nullable List<String> data) {
        super(R.layout.item_bill_content, data);
        this.bill = bill;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        final TextView tvContent = helper.getView(R.id.tv_content);
        if(viewWidth == 0){
            tvContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tvContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = tvContent.getWidth();
                    showView(helper,item);
                }
            });
        }else {
            showView(helper,item);
        }
    }

    public void showView(final BaseViewHolder helper,final String item){
        final TextView tvContent = helper.getView(R.id.tv_content);
        Glide.with(mContext)
                .load(Urls.BASE_IMG + bill.getMiddle_img())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        helper.getView(R.id.ll_content).setBackground(new BitmapDrawable(bitmap));
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvContent.getLayoutParams();
                        params.height = bitmap.getHeight();
                        float radious = bitmap.getWidth() *1f / viewWidth;
                        params.setMargins((int)(bill.getText_l_px()/radious), 0, (int)(bill.getText_r_px()/radious), 0);
                        if (item.split("\n").length > 1) {
                            tvContent.setText(item);
                            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                        } else {
                            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                            tvContent.setText(item);
                            tvContent.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (tvContent.getLineCount() > 1) {
                                        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                                    }
                                }
                            }, 100);
                        }
                        helper.getView(R.id.ll_content).setBackground(new BitmapDrawable(bitmap));
                    }
                });
    }
}