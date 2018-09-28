package com.ailide.apartmentsabc.views.sticker;

import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.utils.FileUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailide.apartmentsabc.model.StickerDraft;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class ScrapPaperAdapter extends BaseQuickAdapter<StickerDraft, BaseViewHolder> {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private boolean edit = false;

    public ScrapPaperAdapter(@Nullable List<StickerDraft> data) {
        super(R.layout.item_scrap_paper, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StickerDraft item) {
        helper.setText(R.id.tv_time, sdf.format(item.getTime()));
        File file = FileUtil.getStickerDraftFile(item.getTime());
        helper.setImageBitmap(R.id.iv_draft, BitmapFactory.decodeFile(file.getAbsolutePath()));
        helper.setVisible(R.id.iv_select, edit);
        helper.setImageResource(R.id.iv_select, item.isSelect() ? R.drawable.icon_draft_select_p : R.drawable.icon_draft_select_n);
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
        notifyDataSetChanged();
    }
}
