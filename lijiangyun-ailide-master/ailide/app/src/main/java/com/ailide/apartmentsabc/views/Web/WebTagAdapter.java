package com.ailide.apartmentsabc.views.Web;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.WebTag;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class WebTagAdapter extends BaseQuickAdapter<WebTag, BaseViewHolder> {

    private boolean manage = false;

    public WebTagAdapter(@Nullable List<WebTag> data) {
        super(R.layout.item_web_tag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final WebTag item) {
        helper.setText(R.id.tv_title, item.getTag_name());
        if (helper.getAdapterPosition() == getItemCount() - 1) {
            helper.setImageResource(R.id.iv_icon, R.drawable.icon_web_add);
        } else {
            helper.setImageResource(R.id.iv_icon, R.drawable.icon_vertical_line);
        }
        helper.addOnClickListener(R.id.iv_delete);
        if (manage) {
            helper.setVisible(R.id.iv_delete, !TextUtils.isEmpty(item.getMid()));
//            helper.setVisible(R.id.iv_delete, item.getIs_default() == 1);
        } else {
            helper.setVisible(R.id.iv_delete, false);
        }
        WebAdapter adapter = new WebAdapter(item.getWeb());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.SHOW_WEB, item.getWeb().get(position).getWebsite()));
            }
        });
        RecyclerView rvWeb = helper.getView(R.id.rv_web);
        rvWeb.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvWeb.setAdapter(adapter);
        adapter.setManage(manage);
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage(boolean manage) {
        this.manage = manage;
        notifyDataSetChanged();
    }
}
