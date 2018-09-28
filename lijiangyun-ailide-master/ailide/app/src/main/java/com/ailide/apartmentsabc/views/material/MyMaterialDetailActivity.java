package com.ailide.apartmentsabc.views.material;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.glide.GlideCircleTransform;
import com.ailide.apartmentsabc.model.CollectMaterialGroup;
import com.ailide.apartmentsabc.model.Material;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyMaterialDetailActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_pic)
    RecyclerView mRvPic;

    private CollectMaterialGroup materialGroup;
    private MaterialDetailPicAdapter mAdapter;
    private List<Material> materials;
    private List<Material> collectMaterials;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_material_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        materialGroup = (CollectMaterialGroup) getIntent().getSerializableExtra("data");
        NetWorkImageLoader.loadCircularImage(null,Urls.BASE_IMG + materialGroup.getImage_url(),mIvAvatar);
        mTvTitle.setText(materialGroup.getTag_name());
        materials = new ArrayList<>();
        collectMaterials = new ArrayList<>();
        materials.addAll(materialGroup.getData());
        collectMaterials.addAll(materialGroup.getData());
        mAdapter = new MaterialDetailPicAdapter(materials);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Glide.with(MyMaterialDetailActivity.this)
                        .load(Urls.BASE_IMG + materials.get(position).getImage_url())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                EventBus.getDefault().post(new EventBusEntity(EventConstant.MY_MATERIAL, bitmap));
                                finish();
                            }
                        });
            }
        });
        mRvPic.setLayoutManager(new GridLayoutManager(this, 3));
        mRvPic.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}