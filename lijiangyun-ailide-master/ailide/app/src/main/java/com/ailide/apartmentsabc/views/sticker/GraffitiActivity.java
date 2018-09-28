package com.ailide.apartmentsabc.views.sticker;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.utils.FileUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.xiaopo.flying.graffiti.GraffitiView;
import com.xiaopo.flying.sticker.StickerUtils;
import com.xiaopo.flying.util.BitmapUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GraffitiActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.iv_title_paint)
    ImageView mIvTitlePaint;
    @BindView(R.id.view_title_paint)
    View mViewTitlePaint;
    @BindView(R.id.iv_title_palette)
    ImageView mIvTitlePalette;
    @BindView(R.id.view_title_palette)
    View mViewTitlePalette;
    @BindView(R.id.iv_title_eraser)
    ImageView mIvTitleEraser;
    @BindView(R.id.view_title_eraser)
    View mViewTitleEraser;
    @BindView(R.id.iv_title_undo)
    ImageView mIvTitleUndo;
    @BindView(R.id.view_title_undo)
    View mViewTitleUndo;
    @BindView(R.id.iv_title_do)
    ImageView mIvTitleDo;
    @BindView(R.id.view_title_do)
    View mViewTitleDo;
    @BindView(R.id.ll_paint)
    LinearLayout mLlPaint;
    @BindView(R.id.iv_paint1)
    ImageView mIvPaint1;
    @BindView(R.id.iv_paint2)
    ImageView mIvPaint2;
    @BindView(R.id.iv_paint3)
    ImageView mIvPaint3;
    @BindView(R.id.iv_paint4)
    ImageView mIvPaint4;
    @BindView(R.id.iv_paint5)
    ImageView mIvPaint5;
    @BindView(R.id.iv_expand)
    ImageView mIvExpand;
    @BindView(R.id.ll_eraser)
    LinearLayout mLlEraser;
    @BindView(R.id.iv_eraser1)
    ImageView mIvEraser1;
    @BindView(R.id.iv_eraser2)
    ImageView mIvEraser2;
    @BindView(R.id.iv_eraser3)
    ImageView mIvEraser3;
    @BindView(R.id.iv_eraser4)
    ImageView mIvEraser4;
    @BindView(R.id.iv_eraser5)
    ImageView mIvEraser5;
    @BindView(R.id.ll_palette)
    LinearLayout mLlPalette;
    @BindView(R.id.iv_red)
    ImageView mIvRed;
    @BindView(R.id.iv_orange)
    ImageView mIvOrange;
    @BindView(R.id.iv_yellow)
    ImageView mIvYellow;
    @BindView(R.id.iv_green)
    ImageView mIvGreen;
    @BindView(R.id.iv_blue)
    ImageView mIvBlue;
    @BindView(R.id.iv_black)
    ImageView mIvBlack;
    @BindView(R.id.iv_pink)
    ImageView mIvPink;
    @BindView(R.id.iv_gray)
    ImageView mIvGray;
    @BindView(R.id.iv_purple)
    ImageView mIvPurple;
    @BindView(R.id.iv_light_blue)
    ImageView mIvLightBlue;

    private GraffitiView graffiti_view;
    private boolean mPaletteInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graffiti);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        graffiti_view = (GraffitiView) findViewById(R.id.graffiti_view);
        graffiti_view.setGraffitiable(true);
        init();
    }

    @OnClick({R.id.iv_back, R.id.iv_expand, R.id.ll_add,
            R.id.iv_title_paint, R.id.iv_title_palette, R.id.iv_title_eraser, R.id.iv_title_undo, R.id.iv_title_do,
            R.id.iv_paint1, R.id.iv_paint2, R.id.iv_paint3, R.id.iv_paint4, R.id.iv_paint5,
            R.id.iv_eraser1, R.id.iv_eraser2, R.id.iv_eraser3, R.id.iv_eraser4, R.id.iv_eraser5,
            R.id.iv_red, R.id.iv_orange, R.id.iv_yellow, R.id.iv_green, R.id.iv_blue, R.id.iv_black, R.id.iv_pink, R.id.iv_gray, R.id.iv_purple, R.id.iv_light_blue})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_expand:
                onClickExpand();
                break;
            case R.id.iv_title_paint:
                onClickPaintTitle();
                break;
            case R.id.iv_title_palette:
                onClickPaletteTitle();
                break;
            case R.id.iv_title_eraser:
                onClickEraserTitle();
                break;
            case R.id.iv_title_undo:
                onClickUndo();
                break;
            case R.id.iv_title_do:
                onClickDo();
                break;
            case R.id.iv_paint1:
                onClickPaint1();
                break;
            case R.id.iv_paint2:
                onClickPaint2();
                break;
            case R.id.iv_paint3:
                onClickPaint3();
                break;
            case R.id.iv_paint4:
                onClickPaint4();
                break;
            case R.id.iv_paint5:
                onClickPaint5();
                break;
            case R.id.iv_eraser1:
                onClickEraser1();
                break;
            case R.id.iv_eraser2:
                onClickEraser2();
                break;
            case R.id.iv_eraser3:
                onClickEraser3();
                break;
            case R.id.iv_eraser4:
                onClickEraser4();
                break;
            case R.id.iv_eraser5:
                onClickEraser5();
                break;
            case R.id.iv_red:
                onClickRed();
                break;
            case R.id.iv_orange:
                onClickOrange();
                break;
            case R.id.iv_yellow:
                onClickYellow();
                break;
            case R.id.iv_green:
                onClickGreen();
                break;
            case R.id.iv_blue:
                onClickBlue();
                break;
            case R.id.iv_black:
                onClickBlack();
                break;
            case R.id.iv_pink:
                onClickPink();
                break;
            case R.id.iv_gray:
                onClickGray();
                break;
            case R.id.iv_purple:
                onClickPurple();
                break;
            case R.id.iv_light_blue:
                onClickLightBlue();
                break;
            case R.id.ll_add:
                File file = FileUtil.getStickerFile();
                StickerUtils.saveImageToGallery(file, BitmapUtil.createBitmap(graffiti_view));
                EventBus.getDefault().post(new EventBusEntity(EventConstant.ADD_GRAFFITI, file.getAbsolutePath()));
                finish();
                break;
        }
    }

    public void init() {
        onClickPaintTitle();
        onClickPaint1();
        mIvExpand.setImageResource(R.drawable.icon_graffiti_contract);
        mFlContent.setVisibility(View.VISIBLE);
    }

    public void onClickExpand() {
        if (mIvExpand.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.icon_graffiti_expand).getConstantState()) {
            mIvExpand.setImageResource(R.drawable.icon_graffiti_contract);
            mFlContent.setVisibility(View.VISIBLE);
        } else {
            mIvExpand.setImageResource(R.drawable.icon_graffiti_expand);
            mFlContent.setVisibility(View.GONE);
        }
    }

    public void onClickPaintTitle() {
        mIvTitlePaint.setImageResource(R.drawable.icon_paint3_p);
        mIvTitlePalette.setImageResource(R.drawable.icon_palette_n);
        mIvTitleEraser.setImageResource(R.drawable.icon_eraser_n);
        mViewTitlePaint.setVisibility(View.VISIBLE);
        mViewTitlePalette.setVisibility(View.GONE);
        mViewTitleEraser.setVisibility(View.GONE);
        mViewTitleUndo.setVisibility(View.GONE);
        mViewTitleDo.setVisibility(View.GONE);
        mLlPaint.setVisibility(View.VISIBLE);
        mLlPalette.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.GONE);
    }

    public void onClickPaletteTitle() {
        if (!mPaletteInit) {
            mIvBlack.setImageResource(R.drawable.palette_black_p);
            mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_black));
            mPaletteInit = true;
        }
        mIvTitlePaint.setImageResource(R.drawable.icon_paint3_n);
        mIvTitleEraser.setImageResource(R.drawable.icon_eraser_n);
        mViewTitlePaint.setVisibility(View.GONE);
        mViewTitlePalette.setVisibility(View.VISIBLE);
        mViewTitleEraser.setVisibility(View.GONE);
        mViewTitleUndo.setVisibility(View.GONE);
        mViewTitleDo.setVisibility(View.GONE);
        mLlPalette.setVisibility(View.VISIBLE);
        mLlPaint.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.GONE);
    }

    public void onClickEraserTitle() {
        mIvTitlePaint.setImageResource(R.drawable.icon_paint3_n);
        mIvTitlePalette.setImageResource(R.drawable.icon_palette_n);
        mIvTitleEraser.setImageResource(R.drawable.icon_eraser_p);
        mViewTitlePaint.setVisibility(View.GONE);
        mViewTitlePalette.setVisibility(View.GONE);
        mViewTitleEraser.setVisibility(View.VISIBLE);
        mViewTitleUndo.setVisibility(View.GONE);
        mViewTitleDo.setVisibility(View.GONE);
        mLlPaint.setVisibility(View.GONE);
        mLlPalette.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.VISIBLE);
    }

    public void onClickUndo() {
      /*  mLlPaint.setVisibility(View.GONE);
        mLlPalette.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.GONE);*/
        mViewTitlePaint.setVisibility(View.GONE);
        mViewTitlePalette.setVisibility(View.GONE);
        mViewTitleEraser.setVisibility(View.GONE);
        mViewTitleUndo.setVisibility(View.VISIBLE);
        mViewTitleDo.setVisibility(View.GONE);
        graffiti_view.undo();
    }

    public void onClickDo() {
        /*mLlPaint.setVisibility(View.GONE);
        mLlPalette.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.GONE);*/
        mViewTitlePaint.setVisibility(View.GONE);
        mViewTitlePalette.setVisibility(View.GONE);
        mViewTitleEraser.setVisibility(View.GONE);
        mViewTitleUndo.setVisibility(View.GONE);
        mViewTitleDo.setVisibility(View.VISIBLE);
        graffiti_view.reundo();
    }

    public void onClickPaint1() {
        mIvPaint1.setImageResource(R.drawable.bg_paint1_p);
        mIvPaint2.setImageResource(R.drawable.bg_paint2_n);
        mIvPaint3.setImageResource(R.drawable.bg_paint3_n);
        mIvPaint4.setImageResource(R.drawable.bg_paint4_n);
        mIvPaint5.setImageResource(R.drawable.bg_paint5_n);
        graffiti_view.setPaintWidth(6);
//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 6));
    }

    public void onClickPaint2() {
        mIvPaint1.setImageResource(R.drawable.bg_paint1_n);
        mIvPaint2.setImageResource(R.drawable.bg_paint2_p);
        mIvPaint3.setImageResource(R.drawable.bg_paint3_n);
        mIvPaint4.setImageResource(R.drawable.bg_paint4_n);
        mIvPaint5.setImageResource(R.drawable.bg_paint5_n);
        graffiti_view.setPaintWidth(14);
//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 10));
    }

    public void onClickPaint3() {
        mIvPaint1.setImageResource(R.drawable.bg_paint1_n);
        mIvPaint2.setImageResource(R.drawable.bg_paint2_n);
        mIvPaint3.setImageResource(R.drawable.bg_paint3_p);
        mIvPaint4.setImageResource(R.drawable.bg_paint4_n);
        mIvPaint5.setImageResource(R.drawable.bg_paint5_n);
        graffiti_view.setPaintWidth(22);
//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 14));
    }

    public void onClickPaint4() {
        mIvPaint1.setImageResource(R.drawable.bg_paint1_n);
        mIvPaint2.setImageResource(R.drawable.bg_paint2_n);
        mIvPaint3.setImageResource(R.drawable.bg_paint3_n);
        mIvPaint4.setImageResource(R.drawable.bg_paint4_p);
        mIvPaint5.setImageResource(R.drawable.bg_paint5_n);
        graffiti_view.setPaintWidth(30);
//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 18));
    }

    public void onClickPaint5() {
        mIvPaint1.setImageResource(R.drawable.bg_paint1_n);
        mIvPaint2.setImageResource(R.drawable.bg_paint2_n);
        mIvPaint3.setImageResource(R.drawable.bg_paint3_n);
        mIvPaint4.setImageResource(R.drawable.bg_paint4_n);
        mIvPaint5.setImageResource(R.drawable.bg_paint5_p);
        graffiti_view.setPaintWidth(38);
//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 22));
    }

    public void onClickEraser1() {
        mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
        mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
        mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
        mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
        if (mIvEraser1.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser1_p).getConstantState()) {
            mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
            graffiti_view.noEraser();
//            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser1.setImageResource(R.drawable.bg_eraser1_p);
            graffiti_view.eraser(6);
//            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 6));
        }
    }

    public void onClickEraser2() {
        mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
        mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
        mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
        mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
        if (mIvEraser2.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser2_p).getConstantState()) {
            mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
            graffiti_view.noEraser();
//            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser2.setImageResource(R.drawable.bg_eraser2_p);
            graffiti_view.eraser(14);
//            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 10));
        }
    }

    public void onClickEraser3() {
        mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
        mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
        mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
        mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
        if (mIvEraser3.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser3_p).getConstantState()) {
            mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
            graffiti_view.noEraser();
//            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser3.setImageResource(R.drawable.bg_eraser3_p);
            graffiti_view.eraser(22);
//            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 14));
        }
    }

    public void onClickEraser4() {
        mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
        mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
        mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
        mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
        if (mIvEraser4.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser4_p).getConstantState()) {
            mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
            graffiti_view.noEraser();
//            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser4.setImageResource(R.drawable.bg_eraser4_p);
            graffiti_view.eraser(30);
//            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 18));
        }
    }

    public void onClickEraser5() {
        mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
        mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
        mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
        mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
        if (mIvEraser5.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser5_p).getConstantState()) {
            mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
            graffiti_view.noEraser();
//            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser5.setImageResource(R.drawable.bg_eraser5_p);
            graffiti_view.eraser(38);

//            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 22));
        }
    }

    public void onClickRed() {
        mIvRed.setImageResource(R.drawable.palette_red_p);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvPink.setImageResource(R.drawable.palette_pink_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        graffiti_view.setPaintColor(R.color.palette_red);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_red));


//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_red));
    }

    public void onClickOrange() {
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_p);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvPink.setImageResource(R.drawable.palette_pink_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        graffiti_view.setPaintColor(R.color.palette_orange);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_orange));

//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_orange));
    }

    public void onClickYellow() {
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_p);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvPink.setImageResource(R.drawable.palette_pink_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        graffiti_view.setPaintColor(R.color.palette_yellow);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_yellow));

//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_yellow));
    }

    public void onClickGreen() {
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_p);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvPink.setImageResource(R.drawable.palette_pink_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        graffiti_view.setPaintColor(R.color.palette_green);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_green));

//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_green));
    }

    public void onClickBlue() {
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_p);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvPink.setImageResource(R.drawable.palette_pink_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        graffiti_view.setPaintColor(R.color.palette_blue);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_blue));

//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_blue));
    }

    public void onClickBlack() {
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_p);
        mIvPink.setImageResource(R.drawable.palette_pink_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        graffiti_view.setPaintColor(R.color.palette_black);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_black));
//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_black));
    }

    public void onClickPink() {
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvPink.setImageResource(R.drawable.palette_pink_p);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        graffiti_view.setPaintColor(R.color.palette_pink);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_pink));

//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.bg_white));
    }

    public void onClickGray() {
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvPink.setImageResource(R.drawable.palette_pink_n);
        mIvGray.setImageResource(R.drawable.palette_gray_p);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        graffiti_view.setPaintColor(R.color.palette_gray);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_gray));

//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_gray));
    }

    public void onClickPurple() {
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvPink.setImageResource(R.drawable.palette_pink_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_p);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        graffiti_view.setPaintColor(R.color.palette_purple);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_purple));

//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_purple));
    }

    public void onClickLightBlue() {
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvPink.setImageResource(R.drawable.palette_pink_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_p);
        graffiti_view.setPaintColor(R.color.palette_light_blue);
        mIvTitlePalette.setColorFilter(getResources().getColor(R.color.palette_light_blue));

//        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_light_blue));
    }
}