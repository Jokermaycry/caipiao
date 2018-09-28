package com.ailide.apartmentsabc.views.material;

import android.support.annotation.Nullable;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.MaterialClass;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MaterialClassAdapter extends BaseQuickAdapter<MaterialClass, BaseViewHolder> {

    public MaterialClassAdapter(@Nullable List<MaterialClass> data) {
        super(R.layout.item_material_class, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MaterialClass item) {
        helper.setText(R.id.tv_title, item.getClass_name());
        if (item.isSelect()) {
            helper.setBackgroundRes(R.id.tv_title, R.drawable.bg_border_theme_p);
        } else {
            helper.setBackgroundRes(R.id.tv_title, R.drawable.bg_border_theme);
        }
    }
}
