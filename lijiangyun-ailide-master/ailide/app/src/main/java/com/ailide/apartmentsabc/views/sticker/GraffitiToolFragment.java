package com.ailide.apartmentsabc.views.sticker;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class GraffitiToolFragment extends BaseSimpleFragment {

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.iv_title_paint)
    ImageView mIvTitlePaint;
    @BindView(R.id.iv_title_palette)
    ImageView mIvTitlePalette;
    @BindView(R.id.iv_title_eraser)
    ImageView mIvTitleEraser;
    @BindView(R.id.iv_title_undo)
    ImageView mIvTitleUndo;
    @BindView(R.id.iv_title_do)
    ImageView mIvTitleDo;
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
    @BindView(R.id.iv_white)
    ImageView mIvWhite;
    @BindView(R.id.iv_gray)
    ImageView mIvGray;
    @BindView(R.id.iv_purple)
    ImageView mIvPurple;
    @BindView(R.id.iv_light_blue)
    ImageView mIvLightBlue;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_graffiti_tool;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
    }

    @OnClick({R.id.iv_expand,
            R.id.iv_title_paint, R.id.iv_title_palette, R.id.iv_title_eraser, R.id.iv_title_undo, R.id.iv_title_do,
            R.id.iv_paint1, R.id.iv_paint2, R.id.iv_paint3, R.id.iv_paint4, R.id.iv_paint5,
            R.id.iv_eraser1, R.id.iv_eraser2, R.id.iv_eraser3, R.id.iv_eraser4, R.id.iv_eraser5,
            R.id.iv_red, R.id.iv_orange, R.id.iv_yellow, R.id.iv_green, R.id.iv_blue, R.id.iv_black, R.id.iv_white, R.id.iv_gray, R.id.iv_purple, R.id.iv_light_blue})
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.iv_white:
                onClickWhite();
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
        }
    }

    public void init() {
        onClickPaintTitle();
        mIvExpand.setImageResource(R.drawable.icon_contract);
        mLlContent.setVisibility(View.VISIBLE);
    }

    public void onClickExpand() {
        if (mIvExpand.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.icon_expand).getConstantState()) {
            mIvExpand.setImageResource(R.drawable.icon_contract);
            mLlContent.setVisibility(View.VISIBLE);
        } else {
            mIvExpand.setImageResource(R.drawable.icon_expand);
            mLlContent.setVisibility(View.GONE);
        }
    }

    public void onClickPaintTitle() {
        mIvTitlePaint.setImageResource(R.drawable.icon_paint3_p);
        mIvTitlePalette.setImageResource(R.drawable.icon_palette_n);
        mIvTitleEraser.setImageResource(R.drawable.icon_eraser_n);
        mLlPaint.setVisibility(View.VISIBLE);
        mLlPalette.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.GONE);
    }

    public void onClickPaletteTitle() {
        mIvTitlePaint.setImageResource(R.drawable.icon_paint3_n);
        mIvTitlePalette.setImageResource(R.drawable.icon_palette_p);
        mIvTitleEraser.setImageResource(R.drawable.icon_eraser_n);
        mLlPalette.setVisibility(View.VISIBLE);
        mLlPaint.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.GONE);
    }

    public void onClickEraserTitle() {
        mIvTitlePaint.setImageResource(R.drawable.icon_paint3_n);
        mIvTitlePalette.setImageResource(R.drawable.icon_palette_n);
        mIvTitleEraser.setImageResource(R.drawable.icon_eraser_p);
        mLlPaint.setVisibility(View.GONE);
        mLlPalette.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.VISIBLE);
    }

    public void onClickUndo() {
        mLlPaint.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.GONE);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.UNDO));

    }

    public void onClickDo() {
        mLlPaint.setVisibility(View.GONE);
        mLlEraser.setVisibility(View.GONE);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.DO));
    }

    public void onClickPaint1() {
        mIvPaint1.setImageResource(R.drawable.icon_paint1_p);
        mIvPaint2.setImageResource(R.drawable.icon_paint2_n);
        mIvPaint3.setImageResource(R.drawable.icon_paint3_n);
        mIvPaint4.setImageResource(R.drawable.icon_paint4_n);
        mIvPaint5.setImageResource(R.drawable.icon_paint5_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 6));
    }

    public void onClickPaint2() {
        mIvPaint1.setImageResource(R.drawable.icon_paint1_n);
        mIvPaint2.setImageResource(R.drawable.icon_paint2_p);
        mIvPaint3.setImageResource(R.drawable.icon_paint3_n);
        mIvPaint4.setImageResource(R.drawable.icon_paint4_n);
        mIvPaint5.setImageResource(R.drawable.icon_paint5_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 10));
    }

    public void onClickPaint3() {
        mIvPaint1.setImageResource(R.drawable.icon_paint1_n);
        mIvPaint2.setImageResource(R.drawable.icon_paint2_n);
        mIvPaint3.setImageResource(R.drawable.icon_paint3_p);
        mIvPaint4.setImageResource(R.drawable.icon_paint4_n);
        mIvPaint5.setImageResource(R.drawable.icon_paint5_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 14));
    }

    public void onClickPaint4() {
        mIvPaint1.setImageResource(R.drawable.icon_paint1_n);
        mIvPaint2.setImageResource(R.drawable.icon_paint2_n);
        mIvPaint3.setImageResource(R.drawable.icon_paint3_n);
        mIvPaint4.setImageResource(R.drawable.icon_paint4_p);
        mIvPaint5.setImageResource(R.drawable.icon_paint5_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 18));
    }

    public void onClickPaint5() {
        mIvPaint1.setImageResource(R.drawable.icon_paint1_n);
        mIvPaint2.setImageResource(R.drawable.icon_paint2_n);
        mIvPaint3.setImageResource(R.drawable.icon_paint3_n);
        mIvPaint4.setImageResource(R.drawable.icon_paint4_n);
        mIvPaint5.setImageResource(R.drawable.icon_paint5_p);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_WIDTH, 22));
    }

    public void onClickEraser1() {
        mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
        mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
        mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
        mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
        if (mIvEraser1.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser1_p).getConstantState()) {
            mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser1.setImageResource(R.drawable.bg_eraser1_p);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 6));
        }
    }

    public void onClickEraser2() {
        mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
        mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
        mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
        mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
        if (mIvEraser2.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser2_p).getConstantState()) {
            mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser2.setImageResource(R.drawable.bg_eraser2_p);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 10));
        }
    }

    public void onClickEraser3() {
        mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
        mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
        mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
        mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
        if (mIvEraser3.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser3_p).getConstantState()) {
            mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser3.setImageResource(R.drawable.bg_eraser3_p);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 14));
        }
    }

    public void onClickEraser4() {
        mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
        mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
        mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
        mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
        if (mIvEraser4.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser4_p).getConstantState()) {
            mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser4.setImageResource(R.drawable.bg_eraser4_p);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 18));
        }
    }

    public void onClickEraser5() {
        mIvEraser1.setImageResource(R.drawable.bg_eraser1_n);
        mIvEraser2.setImageResource(R.drawable.bg_eraser2_n);
        mIvEraser3.setImageResource(R.drawable.bg_eraser3_n);
        mIvEraser4.setImageResource(R.drawable.bg_eraser4_n);
        if (mIvEraser5.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.bg_eraser5_p).getConstantState()) {
            mIvEraser5.setImageResource(R.drawable.bg_eraser5_n);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_ERASER));
        } else {
            mIvEraser5.setImageResource(R.drawable.bg_eraser5_p);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.ERASER, 22));
        }
    }

    public void onClickRed(){
        mIvRed.setImageResource(R.drawable.palette_red_p);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvWhite.setImageResource(R.drawable.palette_white_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_red));
    }

    public void onClickOrange(){
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_p);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvWhite.setImageResource(R.drawable.palette_white_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_orange));
    }

    public void onClickYellow(){
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_p);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvWhite.setImageResource(R.drawable.palette_white_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_yellow));
    }

    public void onClickGreen(){
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_p);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvWhite.setImageResource(R.drawable.palette_white_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_green));
    }

    public void onClickBlue(){
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_p);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvWhite.setImageResource(R.drawable.palette_white_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_blue));
    }

    public void onClickBlack(){
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_p);
        mIvWhite.setImageResource(R.drawable.palette_white_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_black));
    }

    public void onClickWhite(){
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvWhite.setImageResource(R.drawable.palette_white_p);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.bg_white));
    }

    public void onClickGray(){
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvWhite.setImageResource(R.drawable.palette_white_n);
        mIvGray.setImageResource(R.drawable.palette_gray_p);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_gray));
    }

    public void onClickPurple(){
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvWhite.setImageResource(R.drawable.palette_white_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_p);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_purple));
    }

    public void onClickLightBlue(){
        mIvRed.setImageResource(R.drawable.palette_red_n);
        mIvOrange.setImageResource(R.drawable.palette_orange_n);
        mIvYellow.setImageResource(R.drawable.palette_yellow_n);
        mIvGreen.setImageResource(R.drawable.palette_green_n);
        mIvBlue.setImageResource(R.drawable.palette_blue_n);
        mIvBlack.setImageResource(R.drawable.palette_black_n);
        mIvWhite.setImageResource(R.drawable.palette_white_n);
        mIvGray.setImageResource(R.drawable.palette_gray_n);
        mIvPurple.setImageResource(R.drawable.palette_purple_n);
        mIvLightBlue.setImageResource(R.drawable.palette_light_blue_p);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.PAINT_COLOR, R.color.palette_light_blue));
    }
}