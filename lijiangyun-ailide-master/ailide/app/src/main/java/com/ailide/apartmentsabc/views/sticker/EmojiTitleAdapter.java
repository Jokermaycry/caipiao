package com.ailide.apartmentsabc.views.sticker;

import android.support.annotation.Nullable;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.EmojiGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class EmojiTitleAdapter extends BaseQuickAdapter<EmojiGroup, BaseViewHolder> {

    public EmojiTitleAdapter(@Nullable List<EmojiGroup> data) {
        super(R.layout.item_emoji_title, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmojiGroup item) {
        helper.setText(R.id.tv_title, item.getTag_name());
        helper.setVisible(R.id.view_line, item.isSelect());
    }
}
