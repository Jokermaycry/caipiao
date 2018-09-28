package com.ailide.apartmentsabc.views.material;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.glide.GlideCircleTransform;
import com.ailide.apartmentsabc.model.CollectMaterialGroup;
import com.ailide.apartmentsabc.model.Material;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyMaterialGroupAdapter extends BaseQuickAdapter<CollectMaterialGroup, BaseViewHolder> {

    public MyMaterialGroupAdapter(@Nullable List<CollectMaterialGroup> data) {
        super(R.layout.item_collect_material_group, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CollectMaterialGroup item) {
        helper.setText(R.id.tv_title, item.getTag_name());
        NetWorkImageLoader.loadCircularImage(null,Urls.BASE_IMG+item.getImage_url(),(ImageView) helper.getView(R.id.iv_avatar));
        RecyclerView rvPic = helper.getView(R.id.rv_pic);
        MaterialPicAdapter adapter;
        List<Material> data = new ArrayList<>();
        if (item.getData().size() >= 4) {
            data.addAll(item.getData().subList(0, 4));
        } else {
            data.addAll(item.getData());
            for (int i = 0,size = data.size(); i < 4 - size; i++) {
                data.add(new Material());
            }
        }
        adapter = new MaterialPicAdapter(data);
        rvPic.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvPic.setAdapter(adapter);
   /*     rvPic.setClickable(false);
        rvPic.setPressed(false);
        rvPic.setEnabled(false);*/
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.TO_MY_MATERIAL_DETAIL, item));
            }
        });
    }
}