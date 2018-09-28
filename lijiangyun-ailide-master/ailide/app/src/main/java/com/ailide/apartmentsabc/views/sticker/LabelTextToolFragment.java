package com.ailide.apartmentsabc.views.sticker;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.model.Bubble;
import com.ailide.apartmentsabc.model.Bubbles;
import com.ailide.apartmentsabc.model.Ttf;
import com.ailide.apartmentsabc.model.Ttfs;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.views.base.BaseSimpleFragment;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LabelTextToolFragment extends BaseSimpleFragment {

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.iv_title_font)
    ImageView mIvTitleFont;
    @BindView(R.id.iv_title_align)
    ImageView mIvTitleAlign;
    @BindView(R.id.iv_title_bold)
    ImageView mIvTitleBold;
    @BindView(R.id.iv_title_bubble)
    ImageView mIvTitleBubble;
    @BindView(R.id.ll_font)
    LinearLayout mLlFont;
    @BindView(R.id.sb_font)
    CustomSeekBar mSbFont;
    @BindView(R.id.ll_align)
    LinearLayout mLlAlign;
    @BindView(R.id.iv_align_left)
    ImageView mIvAlignLeft;
    @BindView(R.id.iv_align_center)
    ImageView mIvAlignCenter;
    @BindView(R.id.iv_align_right)
    ImageView mIvAlignRight;
    @BindView(R.id.ll_bold)
    LinearLayout mLlBold;
    @BindView(R.id.iv_bold)
    ImageView mIvBold;
    @BindView(R.id.iv_italic)
    ImageView mIvItalic;
    @BindView(R.id.iv_underline)
    ImageView mIvUnderline;
    @BindView(R.id.rv_typeface)
    RecyclerView mRvTypeface;
    @BindView(R.id.rv_bubble)
    RecyclerView mRvBubble;
    @BindView(R.id.line_title_font)
    View mLineTitleFont;
    @BindView(R.id.line_title_align)
    View mLineTitleAlign;
    @BindView(R.id.line_title_bold)
    View mLineTitleBold;

    private List<Ttf> ttfs;
    private List<Bubble> bubbles;
    private TypefaceAdapter ttfAdapter;
    private BubbleAdapter bubbleAdapter;

    private int currentTtfPage = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_label_text_tool;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mSbFont.setMax(290);
        mSbFont.setValue("16");
        mSbFont.setProgress(6);
        mSbFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSbFont.setValue((progress + 10) + "");
                EventBus.getDefault().post(new EventBusEntity(EventConstant.CHANGE_FONT, progress + 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        ttfs = new ArrayList<>();
        ttfAdapter = new TypefaceAdapter(ttfs);
        ttfAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < ttfs.size(); i++) {
                    if (position == i) {
                        ttfs.get(position).setSelect(!ttfs.get(position).isSelect());
                        if (ttfs.get(position).isSelect()) {
                            EventBus.getDefault().post(new EventBusEntity(EventConstant.TYPEFACE, ttfs.get(position)));
                        } else {
                            EventBus.getDefault().post(new EventBusEntity(EventConstant.NO_TYPEFACE));
                        }
                    } else {
                        ttfs.get(i).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        ttfAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getTtf(currentTtfPage + 1);
            }
        }, mRvTypeface);
        mRvTypeface.setLayoutManager(new NoScrollGridLayoutManager(mActivity, 6));
        mRvTypeface.setAdapter(ttfAdapter);

        bubbles = new ArrayList<>();
        bubbleAdapter = new BubbleAdapter(bubbles);
        bubbleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EventBus.getDefault().post(new EventBusEntity(EventConstant.ADD_BUBBLE, bubbles.get(position)));
            }
        });
        LinearLayoutManager bubbleManager = new LinearLayoutManager(mActivity);
        bubbleManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRvBubble.setLayoutManager(bubbleManager);
        mRvBubble.setLayoutManager(new GridLayoutManager(mActivity, 4));
        mRvBubble.setAdapter(bubbleAdapter);
    }

    @Override
    protected void initData() {
        getTtf(0);
    }

    public void getTtf(final int page) {
        OkGo.<String>post(Urls.TTF)
                .tag(this)
                .params("page", page + "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            currentTtfPage = page;
                            if (page == 0) {
                                ttfs.clear();
                            }
                            Ttfs data = JSON.parseObject(response.body(), Ttfs.class);
                            ttfs.addAll(data.getData());
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvTypeface.getLayoutParams();
                            if (ttfs.size() > 6) {
                                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, mActivity.getResources().getDisplayMetrics());
                            } else {
                                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, mActivity.getResources().getDisplayMetrics());
                            }
                            mRvTypeface.setLayoutParams(params);
                            ttfAdapter.notifyDataSetChanged();
                            if (data.getData().size() < 8) {
                                ttfAdapter.loadMoreEnd();
                                ttfAdapter.loadMoreEnd(true);
                            } else {
                                if (page == 0) {
                                    getTtf(1);
                                }
                                ttfAdapter.loadMoreComplete();
                            }
                        } else {
                            ttfAdapter.loadMoreEnd();
                            ttfAdapter.loadMoreEnd(true);
                        }
                    }
                });
    }

    @Override
    protected void setListener() {
    }

    @OnClick({R.id.iv_title_font, R.id.iv_title_align, R.id.iv_title_bold, R.id.iv_title_bubble,
            R.id.iv_font_reduce, R.id.iv_font_add,
            R.id.iv_bold, R.id.iv_italic, R.id.iv_underline,
            R.id.iv_align_left, R.id.iv_align_center, R.id.iv_align_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_font:
                onClickFontTitle();
                break;
            case R.id.iv_title_align:
                onClickAlignTitle();
                break;
            case R.id.iv_title_bold:
                onClickBoldTitle();
                break;
            case R.id.iv_title_bubble:
                onClickBubbleTitle();
                break;
            case R.id.iv_font_reduce:
                onClickFontReduce();
                break;
            case R.id.iv_font_add:
                onClickFontAdd();
                break;
            case R.id.iv_bold:
                onClickBold();
                break;
            case R.id.iv_italic:
                onClickItalic();
                break;
            case R.id.iv_underline:
                onClickUnderline();
                break;
            case R.id.iv_align_left:
                onClickAlignLeft();
                break;
            case R.id.iv_align_center:
                onClickAlignCenter();
                break;
            case R.id.iv_align_right:
                onClickAlignRight();
                break;
        }
    }

    public void init() {
        onClickFontTitle();
        mLlContent.setVisibility(View.VISIBLE);
    }

    public void onClickFontTitle() {
        mIvTitleFont.setImageResource(R.drawable.icon_font_p);
        mIvTitleAlign.setImageResource(R.drawable.icon_align_n);
        mIvTitleBold.setImageResource(R.drawable.icon_bold_n);
        mIvTitleBubble.setImageResource(R.drawable.icon_bubble_n);
        mLlFont.setVisibility(View.VISIBLE);
        mLlAlign.setVisibility(View.GONE);
        mLlBold.setVisibility(View.GONE);
        mRvBubble.setVisibility(View.GONE);
        mLineTitleFont.setVisibility(View.VISIBLE);
        mLineTitleAlign.setVisibility(View.GONE);
        mLineTitleBold.setVisibility(View.GONE);
    }

    public void onClickFontReduce() {
        if (mSbFont.getProgress() > 0) {
            mSbFont.setProgress(mSbFont.getProgress() - 1);
        }
    }

    public void onClickFontAdd() {
        if (mSbFont.getProgress() < 80) {
            mSbFont.setProgress(mSbFont.getProgress() + 1);
        }
    }

    public void onClickBoldTitle() {
        mIvTitleFont.setImageResource(R.drawable.icon_font_n);
        mIvTitleAlign.setImageResource(R.drawable.icon_align_n);
        mIvTitleBold.setImageResource(R.drawable.icon_bold_p);
        mIvTitleBubble.setImageResource(R.drawable.icon_bubble_n);
        mLlFont.setVisibility(View.GONE);
        mLlAlign.setVisibility(View.GONE);
        mLlBold.setVisibility(View.VISIBLE);
        mRvBubble.setVisibility(View.GONE);
        mLineTitleFont.setVisibility(View.GONE);
        mLineTitleAlign.setVisibility(View.GONE);
        mLineTitleBold.setVisibility(View.VISIBLE);
    }

    public void onClickBold() {
        if (mIvBold.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.icon_bold_n).getConstantState()) {
            mIvBold.setImageResource(R.drawable.icon_bold_p);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.FONT_BOLD, true));
        } else {
            mIvBold.setImageResource(R.drawable.icon_bold_n);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.FONT_BOLD, false));
        }
    }

    public void onClickItalic() {
        if (mIvItalic.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.icon_italic_n).getConstantState()) {
            mIvItalic.setImageResource(R.drawable.icon_italic_p);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.FONT_ITALIC, true));

        } else {
            mIvItalic.setImageResource(R.drawable.icon_italic_n);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.FONT_ITALIC, false));

        }
    }

    public void onClickUnderline() {
        if (mIvUnderline.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.icon_underline_n).getConstantState()) {
            mIvUnderline.setImageResource(R.drawable.icon_underline_p);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.FONT_UNDERLINE, true));
        } else {
            mIvUnderline.setImageResource(R.drawable.icon_underline_n);
            EventBus.getDefault().post(new EventBusEntity(EventConstant.FONT_UNDERLINE, false));
        }
    }

    public void onClickAlignTitle() {
        mIvTitleFont.setImageResource(R.drawable.icon_font_n);
        mIvTitleAlign.setImageResource(R.drawable.icon_align_p);
        mIvTitleBold.setImageResource(R.drawable.icon_bold_n);
        mIvTitleBubble.setImageResource(R.drawable.icon_bubble_n);
        mLlFont.setVisibility(View.GONE);
        mLlAlign.setVisibility(View.VISIBLE);
        mLlBold.setVisibility(View.GONE);
        mRvBubble.setVisibility(View.GONE);
        mLineTitleFont.setVisibility(View.GONE);
        mLineTitleAlign.setVisibility(View.VISIBLE);
        mLineTitleBold.setVisibility(View.GONE);
    }

    public void onClickAlignLeft() {
        mIvAlignLeft.setImageResource(R.drawable.icon_align_left_p);
        mIvAlignCenter.setImageResource(R.drawable.icon_align_center_n);
        mIvAlignRight.setImageResource(R.drawable.icon_align_right_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.FONT_GRAVITY, Gravity.LEFT));
    }

    public void onClickAlignCenter() {
        mIvAlignLeft.setImageResource(R.drawable.icon_align_left_n);
        mIvAlignCenter.setImageResource(R.drawable.icon_align_center_p);
        mIvAlignRight.setImageResource(R.drawable.icon_align_center_n);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.FONT_GRAVITY, Gravity.CENTER));
    }

    public void onClickAlignRight() {
        mIvAlignLeft.setImageResource(R.drawable.icon_align_left_n);
        mIvAlignCenter.setImageResource(R.drawable.icon_align_center_n);
        mIvAlignRight.setImageResource(R.drawable.icon_align_right_p);
        EventBus.getDefault().post(new EventBusEntity(EventConstant.FONT_GRAVITY, Gravity.RIGHT));
    }

    public void onClickBubbleTitle() {
        mIvTitleFont.setImageResource(R.drawable.icon_font_n);
        mIvTitleAlign.setImageResource(R.drawable.icon_align_n);
        mIvTitleBold.setImageResource(R.drawable.icon_bold_n);
        mIvTitleBubble.setImageResource(R.drawable.icon_bubble_p);
        mLlFont.setVisibility(View.GONE);
        mLlAlign.setVisibility(View.GONE);
        mLlBold.setVisibility(View.GONE);
        mRvBubble.setVisibility(View.VISIBLE);
    }

}