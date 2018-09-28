package com.ailide.apartmentsabc.views.material;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.CollectMaterialGroup;
import com.ailide.apartmentsabc.model.CollectMaterialGroups;
import com.ailide.apartmentsabc.model.MaterialClass;
import com.ailide.apartmentsabc.model.MaterialClasses;
import com.ailide.apartmentsabc.model.MaterialGroup;
import com.ailide.apartmentsabc.model.MaterialGroups;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.tools.view.galleryviewpager.DepthPageTransformer;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MaterialCenterActivity extends BaseActivity {

    @BindView(R.id.tv_shared_material)
    TextView mTvSharedMaterial;
    @BindView(R.id.line_shared_material)
    View mLineSharedMaterial;
    @BindView(R.id.tv_collection_material)
    TextView mTvCollectionMaterial;
    @BindView(R.id.line_collection_material)
    View mLineCollectionMaterial;
    @BindView(R.id.vp)
    ViewPager mVp;

    private MaterialTypeAdapter materialTypeAdapter;
    private List<Fragment> mFragments;
    private CloudMaterialFragment mCloudFragment;
    private LocalMaterialFragment mLocalFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_center);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        mFragments = new ArrayList<>();
        mCloudFragment = new CloudMaterialFragment();
        mLocalFragment = new LocalMaterialFragment();
        mFragments.add(mCloudFragment);
        mFragments.add(mLocalFragment);
        materialTypeAdapter = new MaterialTypeAdapter(getSupportFragmentManager(), mFragments);
        mVp.setAdapter(materialTypeAdapter);
        mVp.setOffscreenPageLimit(2);
        mVp.setCurrentItem(0);// 设置起始位置
//        mVp.setPageTransformer(true, new DepthPageTransformer());
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (mVp.getCurrentItem()){
                    case 0:
                        onClickSharedMaterial();
                        break;
                    case 1:
                        onClickCommonMaterial();
                        break;
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.rl_shared_material, R.id.rl_collection_material})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_shared_material:
                onClickSharedMaterial();
                mVp.setCurrentItem(0);
                break;
            case R.id.rl_collection_material:
                onClickCommonMaterial();
                mVp.setCurrentItem(1);
                break;
        }
    }

    public void onClickSharedMaterial(){
        mTvSharedMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTvCollectionMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mLineSharedMaterial.setVisibility(View.VISIBLE);
        mLineCollectionMaterial.setVisibility(View.INVISIBLE);
    }

    public void onClickCommonMaterial(){
        mTvSharedMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mTvCollectionMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mLineSharedMaterial.setVisibility(View.INVISIBLE);
        mLineCollectionMaterial.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEntity event) {
        switch (event.getId()) {
            case EventConstant.TO_MATERIAL_DETAIL:
                CollectMaterialGroup collectGroup = null;
                for (CollectMaterialGroup group :
                        mLocalFragment.getCollectGroups()) {
                    if (group.getId() == (int) event.getData()) {
                        collectGroup = group;
                        break;
                    }
                }
                Intent intent = new Intent(MaterialCenterActivity.this, MaterialDetailActivity.class);
                intent.putExtra("id", event.getData() + "");
                intent.putExtra("class", mCloudFragment.getNowMaterialClass().getClass_name());
                if (collectGroup != null) {
                    intent.putExtra("collects", collectGroup);
                }
                jumpToOtherActivity(intent, false);
                break;
            case EventConstant.TO_COLLECT_MATERIAL_DETAIL:
                Intent collectIntent = new Intent(MaterialCenterActivity.this, CollectMaterialDetailActivity.class);
                collectIntent.putExtra("data", (CollectMaterialGroup) event.getData());
                jumpToOtherActivity(collectIntent, false);
                break;
            case EventConstant.COLLECT_MATERIAL:
                mLocalFragment.getCollectMaterials();
                break;
        }
    }
}