package com.ailide.apartmentsabc.views.sticker;

import android.support.annotation.Nullable;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.LabelSize;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class LabelSizeAdapter extends BaseQuickAdapter<LabelSize, BaseViewHolder> {

    public LabelSizeAdapter(@Nullable List<LabelSize> data) {
        super(R.layout.item_label_size, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LabelSize item) {
        helper.setText(R.id.tv_name, item.getName());
    }
}
