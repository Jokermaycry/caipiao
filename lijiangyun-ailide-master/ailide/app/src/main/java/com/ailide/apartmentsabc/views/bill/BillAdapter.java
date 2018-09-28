package com.ailide.apartmentsabc.views.bill;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.model.Bill;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BillAdapter extends BaseQuickAdapter<Bill, BaseViewHolder> {

    private boolean manage = false;

    public BillAdapter(@Nullable List<Bill> data) {
        super(R.layout.item_bill, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Bill item) {
        NetWorkImageLoader.loadNetworkImage(null, Urls.BASE_IMG + item.getImg_url(), (ImageView) helper.getView(R.id.iv_show));
        helper.setVisible(R.id.iv_delete, manage);
        helper.addOnClickListener(R.id.iv_delete);
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage(boolean manage) {
        this.manage = manage;
        notifyDataSetChanged();
    }
}