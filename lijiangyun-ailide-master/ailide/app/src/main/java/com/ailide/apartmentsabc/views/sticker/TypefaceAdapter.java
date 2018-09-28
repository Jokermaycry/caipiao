package com.ailide.apartmentsabc.views.sticker;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Ttf;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class TypefaceAdapter extends BaseQuickAdapter<Ttf, BaseViewHolder> {

    public TypefaceAdapter(@Nullable List<Ttf> data) {
        super(R.layout.item_typeface, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Ttf item) {
        NetWorkImageLoader.loadNetworkImage(null, Urls.BASE_IMG + item.getImage(), (ImageView) helper.getView(R.id.iv_typeface));
        if (item.isSelect()) {
            helper.getView(R.id.iv_select).setBackground(mContext.getResources().getDrawable(R.drawable.bg_border_typeface_p));
        } else {
            helper.getView(R.id.iv_select).setBackground(mContext.getResources().getDrawable(R.drawable.bg_border_typeface));
        }
    }
}