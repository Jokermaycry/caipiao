package com.ailide.apartmentsabc.views.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.model.CollectMaterialGroup;
import com.ailide.apartmentsabc.model.Material;
import com.ailide.apartmentsabc.model.StatusResponse;
import com.ailide.apartmentsabc.model.UserBean;
import com.ailide.apartmentsabc.tools.NetWorkImageLoader;
import com.ailide.apartmentsabc.tools.ToastUtil;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.tools.view.galleryviewpager.DepthPageTransformer;
import com.ailide.apartmentsabc.tools.view.galleryviewpager.GalleryViewPager;
import com.ailide.apartmentsabc.views.MyApplication;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.ailide.apartmentsabc.views.mine.MyEquitmentActivity;
import com.ailide.apartmentsabc.views.sticker.PrePrintActivity;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectMaterialDetailActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_collect_num)
    TextView mTvCollectNum;
    @BindView(R.id.tv_save_num)
    TextView mTvSaveNum;
    @BindView(R.id.rv_pic)
    RecyclerView mRvPic;
    @BindView(R.id.ll_gallery)
    LinearLayout mLlGallery;
    @BindView(R.id.vp_gallery)
    GalleryViewPager mVpGallery;
    @BindView(R.id.iv_collect)
    ImageView mIvCollect;
    @BindView(R.id.tv_collect)
    TextView mTvCollect;
    @BindView(R.id.iv_collect_group)
    ImageView mIvCollectGroup;

    private CollectMaterialGroup materialGroup;
    private MaterialDetailPicAdapter mAdapter;
    private List<Material> materials;
    private List<Material> collectMaterials;
    private MaterialGalleryAdapter materialGalleryAdapter;
    private PopupWindow popupWindow;
    private boolean isCollect = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_material_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        materialGroup = (CollectMaterialGroup) getIntent().getSerializableExtra("data");
        NetWorkImageLoader.loadCircularImage(null, Urls.BASE_IMG + materialGroup.getImage_url(), mIvAvatar);
        mTvTitle.setText(materialGroup.getTag_name());
        materials = new ArrayList<>();
        collectMaterials = new ArrayList<>();
        materials.addAll(materialGroup.getData());
        collectMaterials.addAll(materialGroup.getData());
        mAdapter = new MaterialDetailPicAdapter(materials);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mLlGallery.setVisibility(View.VISIBLE);
                mVpGallery.setCurrentItem(position, false);
                notifyPicCollectStatus(materials.get(mVpGallery.getCurrentItem()).getId());
            }
        });
        mRvPic.setLayoutManager(new GridLayoutManager(this, 3));
        mRvPic.setAdapter(mAdapter);

        materialGalleryAdapter = new MaterialGalleryAdapter(getSupportFragmentManager(), materials);
        mVpGallery.setAdapter(materialGalleryAdapter);
        mVpGallery.setPageMargin(30);// 设置页面间距
        mVpGallery.setOffscreenPageLimit(2);
        mVpGallery.setCurrentItem(0);// 设置起始位置
        mVpGallery.setPageTransformer(true, new DepthPageTransformer());
        mVpGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                notifyPicCollectStatus(materials.get(mVpGallery.getCurrentItem()).getId());
            }
        });
        isCollect = true;
        mIvCollectGroup.setImageResource(R.drawable.icon_web_collect_p);
        mTvCollect.setText(R.string.collected);
    }

    @OnClick({R.id.iv_back, R.id.tv_collect,R.id.iv_collect_group, R.id.fl_gallery, R.id.iv_collect, R.id.iv_print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_collect:
            case R.id.iv_collect_group:
                UserBean userBean = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
                if (userBean == null) {
                    ToastUtil.toast("您还未登录");
                } else {
                    if (!isCollect) {
                        showLoading("请稍后");
                        OkGo.<String>post(Urls.MATERIA_TAG_COLLECT)
                                .tag(this)
                                .params("mid", userBean.getData().getId() + "")
                                .params("tag_id", materialGroup.getId() + "")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        dismissLoading();
                                        if (response.isSuccessful()) {
                                            StatusResponse statusResponse = JSON.parseObject(response.body(), StatusResponse.class);
                                            if (statusResponse.getStatus() == 1) {
                                                mTvCollect.setText(R.string.collected);
                                                EventBus.getDefault().post(new EventBusEntity(EventConstant.COLLECT_MATERIAL));
                                                toast("已收藏");
                                                isCollect = true;
                                                mIvCollectGroup.setImageResource(R.drawable.icon_web_collect_p);
                                            } else {
                                                toast(statusResponse.getMsg());
                                            }
                                        }
                                    }
                                });
                    } else {
                        showLoading("请稍后");
                        OkGo.<String>post(Urls.MATERIA_TAG_DCOLLECT)
                                .tag(this)
                                .params("mid", userBean.getData().getId() + "")
                                .params("tag_id", materialGroup.getId() + "")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        dismissLoading();
                                        if (response.isSuccessful()) {
                                            StatusResponse statusResponse = JSON.parseObject(response.body(), StatusResponse.class);
                                            if (statusResponse.getStatus() == 1) {
                                                mTvCollect.setText(R.string.collect);
                                                EventBus.getDefault().post(new EventBusEntity(EventConstant.COLLECT_MATERIAL));
                                                toast("已取消");
                                                isCollect = false;
                                                mIvCollectGroup.setImageResource(R.drawable.icon_web_collect_n);
                                            } else {
                                                toast(statusResponse.getMsg());
                                            }
                                        }
                                    }
                                });
                    }
                }
                break;
            case R.id.fl_gallery:
                mLlGallery.setVisibility(View.GONE);
                break;
            case R.id.iv_collect:
                UserBean user = JSON.parseObject(SPUtil.get(this, "user", "").toString(), UserBean.class);
                if (user == null) {
                    ToastUtil.toast("您还未登录");
                } else {
                    if (mIvCollect.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.icon_web_collect_n).getConstantState()) {
                        showLoading("请稍后");
                        OkGo.<String>post(Urls.MATERIA_COLLECT)
                                .tag(this)
                                .params("mid", user.getData().getId() + "")
                                .params("material_id", materials.get(mVpGallery.getCurrentItem()).getId())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        dismissLoading();
                                        if (response.isSuccessful()) {
                                            StatusResponse statusResponse = JSON.parseObject(response.body(), StatusResponse.class);
                                            if (statusResponse.getStatus() == 1) {
                                                collectMaterials.add(materials.get(mVpGallery.getCurrentItem()));
                                                EventBus.getDefault().post(new EventBusEntity(EventConstant.COLLECT_MATERIAL));
                                            }
                                            notifyPicCollectStatus(materials.get(mVpGallery.getCurrentItem()).getId());
                                            toast(statusResponse.getMsg());
                                        }
                                    }
                                });
                    } else {
                        showLoading("请稍后");
                        OkGo.<String>post(Urls.MATERIA_MCOLLECT)
                                .tag(this)
                                .params("mid", user.getData().getId() + "")
                                .params("material_id", materials.get(mVpGallery.getCurrentItem()).getId())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        dismissLoading();
                                        if (response.isSuccessful()) {
                                            StatusResponse statusResponse = JSON.parseObject(response.body(), StatusResponse.class);
                                            if (statusResponse.getStatus() == 1) {
                                                collectMaterials.remove(materials.get(mVpGallery.getCurrentItem()));
                                                EventBus.getDefault().post(new EventBusEntity(EventConstant.COLLECT_MATERIAL));
                                            }
                                            notifyPicCollectStatus(materials.get(mVpGallery.getCurrentItem()).getId());
                                            toast(statusResponse.getMsg());
                                        }
                                    }
                                });
                    }
                }
                break;
            case R.id.iv_print:
                if (PrintFragment.printPP!=null && PrintFragment.printPP.isConnected()) {
                    Intent intent = new Intent(this, PrePrintActivity.class);
                    intent.putExtra("path", Urls.BASE_IMG + materials.get(mVpGallery.getCurrentItem()).getImage_url());
                    startActivity(intent);
                } else {
                    showPopView();
                }
                break;
        }
    }

    private void showPopView() {
        View layout = getLayoutInflater().inflate(R.layout.pop_add_equitment, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, mTvCollectNum, 0, 0, 1, 0.5f, true);
        TextView pop_equitment_add = layout.findViewById(R.id.pop_equitment_add);
        TextView pop_equitment_not = layout.findViewById(R.id.pop_equitment_not);
        TextView equipment_tv = layout.findViewById(R.id.equipment_tv);
        pop_equitment_add.setText("连 接 设 备");
        pop_equitment_not.setText("取 消 打 印");
        equipment_tv.setText("还没有连接到设备哦");
        pop_equitment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                jumpToOtherActivity(new Intent(CollectMaterialDetailActivity.this, MyEquitmentActivity.class), false);
            }
        });
        pop_equitment_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    public void notifyPicCollectStatus(int materialId) {
        boolean collect = false;
        for (Material material :
                collectMaterials) {
            if (material.getId() == materialId) {
                collect = true;
                break;
            }
        }
        if (collect) {
            mIvCollect.setImageResource(R.drawable.icon_web_collect_p);
        } else {
            mIvCollect.setImageResource(R.drawable.icon_web_collect_n);
        }
    }

    @Override
    public void onBackPressed() {
        if (mLlGallery.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            mLlGallery.setVisibility(View.GONE);
        }
    }
}