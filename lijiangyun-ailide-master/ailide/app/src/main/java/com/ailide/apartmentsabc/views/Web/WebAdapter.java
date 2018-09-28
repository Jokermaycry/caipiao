package com.ailide.apartmentsabc.views.Web;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.Web;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class WebAdapter extends BaseQuickAdapter<Web, BaseViewHolder> {

    private boolean manage = false;

    public WebAdapter(@Nullable List<Web> data) {
        super(R.layout.item_web, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Web item) {
        helper.setText(R.id.tv_name, item.getWeb_name());
        NetWorkImageLoader.loadNetworkImage(null, item.getUrl_ico(), (ImageView) helper.getView(R.id.iv_icon));

        if (manage) {
            helper.setVisible(R.id.iv_delete, true);
//            helper.setVisible(R.id.iv_delete, item.getIs_default() == 1);
        } else {
            helper.setVisible(R.id.iv_delete, false);
        }
        helper.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.DELETE_WEB, item));
            }
        });
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage(boolean manage) {
        this.manage = manage;
        notifyDataSetChanged();
    }
}