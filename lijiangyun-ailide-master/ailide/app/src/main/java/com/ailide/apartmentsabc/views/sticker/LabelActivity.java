package com.ailide.apartmentsabc.views.sticker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.framework.util.CommonFunction;
import com.ailide.apartmentsabc.framework.util.KeyBoardUtils;
import com.ailide.apartmentsabc.model.Bubble;
import com.ailide.apartmentsabc.model.DraftSticker;
import com.ailide.apartmentsabc.model.DrawableDraftSticker;
import com.ailide.apartmentsabc.model.LabelDraft;
import com.ailide.apartmentsabc.model.LabelSize;
import com.ailide.apartmentsabc.model.TextDraftSticker;
import com.ailide.apartmentsabc.model.Theme;
import com.ailide.apartmentsabc.model.Ttf;
import com.ailide.apartmentsabc.tools.Contants;
import com.ailide.apartmentsabc.tools.ToastUtil;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.utils.FileUtil;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.ailide.apartmentsabc.views.main.fragment.PrintFragment;
import com.ailide.apartmentsabc.views.mine.MyEquitmentActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.xiaopo.flying.exsticker.ExNoScrollSticker;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.RotateIconEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ailide.apartmentsabc.framework.util.KeyBoardUtils.hideInputForce;
import static com.ailide.apartmentsabc.tools.Contants.PATH_STICKER_BUBBLE;
import static com.ailide.apartmentsabc.tools.Contants.PATH_STICKER_EMOJI;
import static com.ailide.apartmentsabc.tools.Contants.PATH_STICKER_THEME;
import static com.ailide.apartmentsabc.tools.Contants.PATH_STICKER_TTF_FILE;

public class LabelActivity extends BaseActivity implements ExNoScrollSticker.OnBubbleClickLisener, ExNoScrollSticker.OnStickerClickLisener {
    public static final int PERM_RQST_CODE = 110;

    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.exsticker)
    ExNoScrollSticker mExSticker;
    @BindView(R.id.ll_text)
    LinearLayout mLlText;
    @BindView(R.id.ll_emoji)
    LinearLayout mLlEmoji;
    @BindView(R.id.ll_template)
    LinearLayout mLlTemplate;
    @BindView(R.id.ll_qr_code)
    LinearLayout mLlQrCode;
    @BindView(R.id.tv_text)
    TextView mTvText;
    @BindView(R.id.tv_emoji)
    TextView mTvEmoji;
    @BindView(R.id.tv_template)
    TextView mTvTemplate;
    @BindView(R.id.tv_qr_code)
    TextView mTvQrCode;
    @BindView(R.id.vp_tool)
    ContentViewPager mVpTool;
    @BindView(R.id.ll_bubble_sure)
    LinearLayout mLlBubbleSure;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.et_bubble_content)
    EditText mEtBubbleContent;
    @BindView(R.id.tv_bubble_sure)
    TextView mTvBubbleSure;
    @BindView(R.id.tv_size)
    TextView mTvSize;
    @BindView(R.id.ll_size)
    LinearLayout mLlSize;
    @BindView(R.id.iv_move_up)
    ImageView mIvMoveUp;
    @BindView(R.id.tv_move_up)
    TextView mTvMoveUp;
    @BindView(R.id.iv_move_down)
    ImageView mIvMoveDown;
    @BindView(R.id.tv_move_down)
    TextView mTvMoveDown;

    private List<LinearLayout> mBottomLinearouts;
    private List<TextView> mBottomTexts;
    private List<Fragment> mToolsFragment;
    private PagerAdapter mToolsAdapter;
    private LabelTextToolFragment textToolFragment;
    private EmojiToolFragment emojiToolFragment;
    private TemplateToolFragment templateToolFragment;
    private QrCodeToolFragment qrCodeToolFragment;
    private PopupWindow mPopupWindow;
    private List<LabelSize> sizes;
    private int nowSize;
    private double width = 0;
    private PopupWindow popupWindow;

    private double mScaleMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_label);
        ButterKnife.bind(this);

        sizes = new ArrayList<>();
        sizes.add(new LabelSize(15, "15MM标签"));
        sizes.add(new LabelSize(25, "25MM标签"));
        sizes.add(new LabelSize(30, "30MM标签"));
        sizes.add(new LabelSize(40, "40MM标签"));
        sizes.add(new LabelSize(50, "50MM标签"));
        sizes.add(new LabelSize(0, "硬件智能识别"));

        mBottomLinearouts = new ArrayList<>();
        mBottomLinearouts.add(mLlText);
        mBottomLinearouts.add(mLlEmoji);
        mBottomLinearouts.add(mLlTemplate);
        mBottomLinearouts.add(mLlQrCode);

        mBottomTexts = new ArrayList<>();
        mBottomTexts.add(mTvText);
        mBottomTexts.add(mTvEmoji);
        mBottomTexts.add(mTvTemplate);
        mBottomTexts.add(mTvQrCode);

        mToolsFragment = new ArrayList<>();
        textToolFragment = new LabelTextToolFragment();
        mToolsFragment.add(textToolFragment);
        emojiToolFragment = new EmojiToolFragment();
        mToolsFragment.add(emojiToolFragment);
        templateToolFragment = new TemplateToolFragment();
        mToolsFragment.add(templateToolFragment);
        qrCodeToolFragment = new QrCodeToolFragment();
        mToolsFragment.add(qrCodeToolFragment);

      /*  mToolsAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mToolsFragment.get(position);
            }

            @Override
            public int getCount() {
                return mToolsFragment.size();
            }
        };
        mVpTool.setAdapter(mToolsAdapter);
        mVpTool.setNoScroll(true);
        mVpTool.setOffscreenPageLimit(mToolsFragment.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mVpTool.setVisibility(View.GONE);
            }
        }, 100);*/

    /*    BitmapStickerIcon deleteIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this,
                        R.drawable.icon_sticker_delete),
                        BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        deleteIcon.setIconRadius(10f);

        BitmapStickerIcon zoomIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this,
                        R.drawable.icon_sticker_zoom),
                        BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
        zoomIcon.setIconRadius(10f);

        BitmapStickerIcon rotateIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this,
                        R.drawable.icon_sticker_rotate),
                        BitmapStickerIcon.RIGHT_TOP);
        rotateIcon.setIconEvent(new RotateIconEvent());
        rotateIcon.setIconRadius(10f);
        mExSticker.setIcons(Arrays.asList(deleteIcon, zoomIcon, rotateIcon));*/
        mExSticker.setOnBubbleClickLisener(this);
        mExSticker.setOnStickerClickLisener(this);

    /*    //default icon layout
        //stickerView.configDefaultIcons();*/

 /*       stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);*/


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_RQST_CODE);
        }
        mExSticker.setBackground(getResources().getDrawable(R.drawable.bg_label));
        nowSize = (int) SPUtil.get(this, "labelSize", 15);
        switch (nowSize) {
            case 15:
                setLabelSize(sizes.get(0));
                break;
            case 25:
                setLabelSize(sizes.get(1));
                break;
            case 30:
                setLabelSize(sizes.get(2));
                break;
            case 40:
                setLabelSize(sizes.get(3));
                break;
            case 50:
                setLabelSize(sizes.get(4));
                break;
            default:
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mExSticker.getLayoutParams();
                params.height = 0;
                mExSticker.setLayoutParams(params);
                mTvSize.setText(sizes.get(sizes.size() - 1).getName());
                if (PrintFragment.printPP == null || !PrintFragment.printPP.isConnected()) {
                    SPUtil.put(LabelActivity.this, "labelSize", 0);
                    showPopViewTwo();
                    return;
                }
                showLoading("连接中...");
                PrintFragment.printPP.enablePrinter();
                PrintFragment.printPP.printerPosition();
                PrintFragment.printPP.enablePrinter();
                PrintFragment.printPP.printerPosition();
                PrintFragment.printPP.stopPrintJob();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();
                        //获取的高度
                        int height = PrintFragment.printPP.getLabelHeight();
                        sizes.get(sizes.size() - 1).setSize(height);
                        setLabelSize(sizes.get(sizes.size() - 1));
                    }
                }, 3000);
                break;
        }

        mExSticker.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    public void setLabelSize(final LabelSize size) {
        if (width == 0) {
            mExSticker.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mExSticker.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    width = mExSticker.getWidth();
                    mScaleMargin = width / 384 * 8;
                    notifyLabelView(size);
                }
            });
        } else {
            notifyLabelView(size);
        }
    }

    public void notifyLabelView(LabelSize size) {
        SPUtil.put(LabelActivity.this, "labelSize", size.getSize());
        nowSize = size.getSize();
        mTvSize.setText(size.getName());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mExSticker.getLayoutParams();
        params.height = (int) mScaleMargin * size.getSize();
        mExSticker.setLayoutParams(params);
        mExSticker.setViewHeight((int) mScaleMargin * size.getSize() - (int) (mScaleMargin) * 1);
        mExSticker.setPadding((int) (0.5 * mScaleMargin), (int) (0.5 * mScaleMargin), (int) (0.5 * mScaleMargin), (int) (0.5 * mScaleMargin));
        setRadius(size.getSize());
    }

    public void setRadius(float value) {
        BitmapStickerIcon deleteIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this,
                        R.drawable.icon_sticker_delete),
                        BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());
//        deleteIcon.setIconRadius(value);

        BitmapStickerIcon zoomIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this,
                        R.drawable.icon_sticker_zoom),
                        BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
//        zoomIcon.setIconRadius(value);

        BitmapStickerIcon rotateIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this,
                        R.drawable.icon_sticker_rotate),
                        BitmapStickerIcon.RIGHT_TOP);
        rotateIcon.setIconEvent(new RotateIconEvent());
//        rotateIcon.setIconRadius(value);
        mExSticker.setIcons(Arrays.asList(deleteIcon, zoomIcon, rotateIcon));
    }

    @OnClick({R.id.ll_main, R.id.iv_back, R.id.ll_move_up, R.id.ll_move_down, R.id.ll_draft, R.id.ll_save, R.id.ll_print,
            R.id.ll_text, R.id.ll_emoji, R.id.ll_template, R.id.ll_qr_code,
            R.id.tv_bubble_sure, R.id.ll_size})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main:
                hideVpTool();
                mLlBubbleSure.setVisibility(View.GONE);
                mExSticker.setSelect(false);
                mExSticker.invalidate();
                break;
            case R.id.iv_back:
                showSaveDraftDialog();
//                finish();
                break;
            case R.id.ll_move_up:
                mExSticker.moveUpSticker();
                onStickerClick();
                break;
            case R.id.ll_move_down:
                mExSticker.moveDownSticker();
                onStickerClick();
                break;
            case R.id.ll_draft:
                jumpToOtherActivity(new Intent(this, LabelScrapPaperActivity.class), false);
                break;
            case R.id.ll_save:
                saveToDraftNoFinish();
                break;
            case R.id.ll_print:
                if (PrintFragment.printPP != null && PrintFragment.printPP.isConnected()) {
                    print();
                } else {
                    showPopViewTwo();
                }
                break;
            case R.id.ll_text:
                if (mVpTool.getVisibility() == View.VISIBLE && mVpTool.getCurrentItem() == 0) {
                    hideVpTool();
                } else {
                    setBottomTextColor(0);
                    textToolFragment.init();
                }
                break;
            case R.id.ll_emoji:
                if (mVpTool.getVisibility() == View.VISIBLE && mVpTool.getCurrentItem() == 1) {
                    hideVpTool();
                } else {
                    setBottomTextColor(1);
                    emojiToolFragment.init();
                }
                break;
            case R.id.ll_template:
                if (mVpTool.getVisibility() == View.VISIBLE && mVpTool.getCurrentItem() == 2) {
                    hideVpTool();
                } else {
                    setBottomTextColor(2);
                    templateToolFragment.init();
                }
                break;
            case R.id.ll_qr_code:
                if (mVpTool.getVisibility() == View.VISIBLE && mVpTool.getCurrentItem() == 3) {
                    hideVpTool();
                } else {
                    setBottomTextColor(3);
                    qrCodeToolFragment.onClickQrCode();
                }
                break;
            case R.id.tv_bubble_sure:
                onBubbleSure();
                break;
            case R.id.ll_size:
                showPopView();
                break;
        }
    }

    private void showPopView() {
        View layout = getLayoutInflater().inflate(R.layout.pop_label_select_size, null);
        mPopupWindow = CommonFunction.getInstance().InitBottomPopupWindow(this, layout, mTvSize, true);
        RecyclerView selectRV = layout.findViewById(R.id.appointment_main_select_recycle_view);
        selectRV.setLayoutManager(new LinearLayoutManager(this));
        selectRV.setNestedScrollingEnabled(false);
        selectRV.setItemAnimator(new DefaultItemAnimator());
        LabelSizeAdapter sizeAdapter = new LabelSizeAdapter(sizes);
        selectRV.setAdapter(sizeAdapter);
        sizeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPopupWindow.dismiss();
                if (position != sizes.size() - 1) {
                    setLabelSize(sizes.get(position));
                } else {
                    mTvSize.setText(sizes.get(sizes.size() - 1).getName());
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mExSticker.getLayoutParams();
                    params.height = 0;
                    mExSticker.setLayoutParams(params);
                    if (PrintFragment.printPP == null || !PrintFragment.printPP.isConnected()) {
                        SPUtil.put(LabelActivity.this, "labelSize", 0);
                        toast("未连接设备");
                        return;
                    }
                    showLoading("连接中...");
                    PrintFragment.printPP.enablePrinter();
                    PrintFragment.printPP.printerPosition();
                    PrintFragment.printPP.stopPrintJob();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoading();
                            //获取的高度
                            int height = PrintFragment.printPP.getLabelHeight();
                            sizes.get(sizes.size() - 1).setSize(height);
                            setLabelSize(sizes.get(sizes.size() - 1));
                        }
                    }, 3000);
                }
            }
        });
    }

    private void showPopViewTwo() {
        View layout = getLayoutInflater().inflate(R.layout.pop_add_equitment, null);
        popupWindow = CommonFunction.getInstance().InitPopupWindow(this, layout, mExSticker, 0, 0, 1, 0.5f, true);
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
                jumpToOtherActivity(new Intent(LabelActivity.this, MyEquitmentActivity.class), false);
            }
        });
        pop_equitment_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void setBottomTextColor(final int position) {
        if (mToolsAdapter == null) {
            mToolsAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return mToolsFragment.get(position);
                }

                @Override
                public int getCount() {
                    return mToolsFragment.size();
                }
            };
            mVpTool.setAdapter(mToolsAdapter);
            mVpTool.setNoScroll(true);
            mVpTool.setOffscreenPageLimit(mToolsFragment.size());
        }
        for (int i = 0; i < mBottomTexts.size(); i++) {
            if (i == position) {
                mBottomTexts.get(i).setTextColor(getResources().getColor(R.color.text_black));
                mBottomLinearouts.get(i).setBackgroundColor(getResources().getColor(R.color.bg_gray));
            } else {
                mBottomTexts.get(i).setTextColor(getResources().getColor(R.color.text_gray));
                mBottomLinearouts.get(i).setBackgroundColor(getResources().getColor(R.color.bg_white));
            }
        }
        if (position != 5) {
            mVpTool.setVisibility(View.VISIBLE);
            mVpTool.setCurrentItem(position, false);
            mVpTool.requestLayout();
        }
        if (position != 5) {
            mExSticker.setGraffitiable(false);
        }
    }

    public void saveToDraft() {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                mExSticker.setCursorVisible(false);
                mExSticker.setSelect(false);
                mExSticker.refresh();
                showLoading("请稍后");
            }

            @Override
            protected String doInBackground(String... strings) {
                LabelDraft draft = new LabelDraft(nowSize);
                for (Sticker sticker :
                        mExSticker.getStickers()) {
                    if (sticker instanceof DrawableSticker) {
                        DrawableDraftSticker stickerDraft = new DrawableDraftSticker(draft, sticker);
                        draft.getDraftStickers().add(stickerDraft);
                    } else if (sticker instanceof TextSticker) {
                        TextDraftSticker stickerDraft = new TextDraftSticker(draft, sticker);
                        draft.getDraftStickers().add(stickerDraft);
                    }
                }
          /*      for (DrawPath path :
                        mExSticker.getGraffitiPath()) {
                    PathDraft pathDraft = new PathDraft(path);
                    draft.getPathDrafts().add(pathDraft);
                }*/
                draft.setEditContent(mExSticker.getEditContent());
                draft.setTextSize((int) (mExSticker.getEdittext().getTextSize() / getResources().getDisplayMetrics().scaledDensity));
                draft.setTextTypefacePath(mExSticker.getTypefacePath());
                draft.setTextTypefaceUrl(mExSticker.getTypefaceUrl());
                draft.setTextTypefaceId(mExSticker.getTypefaceId());
                draft.setTextBold(mExSticker.getEdittext().getPaint().isFakeBoldText());
                draft.setTextItalic(mExSticker.getEdittext().getPaint().getTextSkewX());
                draft.setTextUnderline(mExSticker.getEdittext().getPaint().isUnderlineText());
                draft.setTextGravity(mExSticker.getEdittext().getGravity());
                List<LabelDraft> drafts = (List<LabelDraft>) SPUtil.readObject(Contants.SP_LABEL_DRAFTS);
                if (drafts == null) {
                    drafts = new ArrayList<>();
                }
                drafts.add(0, draft);
                SPUtil.saveObject(Contants.SP_LABEL_DRAFTS, drafts);
                File file = FileUtil.getLabelDraftFile(draft.getTime());
                mExSticker.save(file);
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                dismissLoading();
                mExSticker.setCursorVisible(true);
                ToastUtil.toast("保存成功");
                finish();
            }
        }.execute();
    }

    public void saveToDraftNoFinish() {
        new AsyncTask<String, String, Long>() {
            @Override
            protected void onPreExecute() {
                mExSticker.setCursorVisible(false);
                mExSticker.setSelect(false);
                mExSticker.refresh();
                showLoading("请稍后");
            }

            @Override
            protected Long doInBackground(String... strings) {
                LabelDraft draft = new LabelDraft(nowSize);
                for (Sticker sticker :
                        mExSticker.getStickers()) {
                    if (sticker instanceof DrawableSticker) {
                        DrawableDraftSticker stickerDraft = new DrawableDraftSticker(draft, sticker);
                        draft.getDraftStickers().add(stickerDraft);
                    } else if (sticker instanceof TextSticker) {
                        TextDraftSticker stickerDraft = new TextDraftSticker(draft, sticker);
                        draft.getDraftStickers().add(stickerDraft);
                    }
                }
          /*      for (DrawPath path :
                        mExSticker.getGraffitiPath()) {
                    PathDraft pathDraft = new PathDraft(path);
                    draft.getPathDrafts().add(pathDraft);
                }*/
                draft.setEditContent(mExSticker.getEditContent());
                draft.setTextSize((int) (mExSticker.getEdittext().getTextSize() / getResources().getDisplayMetrics().scaledDensity));
                draft.setTextTypefacePath(mExSticker.getTypefacePath());
                draft.setTextTypefaceUrl(mExSticker.getTypefaceUrl());
                draft.setTextTypefaceId(mExSticker.getTypefaceId());
                draft.setTextBold(mExSticker.getEdittext().getPaint().isFakeBoldText());
                draft.setTextItalic(mExSticker.getEdittext().getPaint().getTextSkewX());
                draft.setTextUnderline(mExSticker.getEdittext().getPaint().isUnderlineText());
                draft.setTextGravity(mExSticker.getEdittext().getGravity());
                List<LabelDraft> drafts = (List<LabelDraft>) SPUtil.readObject(Contants.SP_LABEL_DRAFTS);
                if (drafts == null) {
                    drafts = new ArrayList<>();
                }
                drafts.add(0, draft);
                SPUtil.saveObject(Contants.SP_LABEL_DRAFTS, drafts);
                return draft.getTime();
            }

            @Override
            protected void onPostExecute(Long time) {
                File file = FileUtil.getLabelDraftFile(time);
                mExSticker.save(file);
                dismissLoading();
                mExSticker.setCursorVisible(true);
                ToastUtil.toast("保存成功");
            }
        }.execute();
    }

    public void save() {
        File file = FileUtil.getLabelFile();
        mExSticker.setSelect(false);
        mExSticker.setCursorVisible(false);
        mExSticker.refresh();
        mExSticker.save(file);
        mExSticker.setCursorVisible(true);
        ToastUtil.toast("保存成功");
        finish();
    }

    public void print() {
        mExSticker.setSelect(false);
        mExSticker.setCursorVisible(false);
        mExSticker.refresh();
        File file = FileUtil.getLabelFile();
        mExSticker.save(file);
        mExSticker.setCursorVisible(true);
        Intent intent = new Intent(this, LabelPrePrintActivity.class);
        intent.putExtra("size", nowSize);
        intent.putExtra("path", file.getAbsolutePath());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }

    @Override
    public void onBackPressed() {
        if (mVpTool.getVisibility() == View.VISIBLE) {
            hideVpTool();
//            expandVpTool();
        } else {
            showSaveDraftDialog();
//            finish();
        }
    }

    public void showSaveDraftDialog() {
        if (nowSize == 0 || mExSticker.getStickers().size() == 0 && TextUtils.isEmpty(mExSticker.getEditContent())) {
            finish();
        } else {
            new MaterialDialog.Builder(this)
                    .content(R.string.save_to_draft)
                    .positiveText(R.string.confirm)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            saveToDraft();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    public void hideVpTool() {
        if (mToolsAdapter != null) {
            mVpTool.setVisibility(View.GONE);
            for (int i = 0; i < mBottomTexts.size(); i++) {
                mBottomTexts.get(i).setTextColor(getResources().getColor(R.color.text_gray));
                mBottomLinearouts.get(i).setBackgroundColor(getResources().getColor(R.color.bg_white));
            }
            mExSticker.setGraffitiable(false);
            mExSticker.setLocked(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEntity event) {
        switch (event.getId()) {
            case EventConstant.HIDE_TOOL:
                hideVpTool();
                break;
            case EventConstant.CHANGE_FONT:
                if (mExSticker.getCurrentSticker() != null && mExSticker.getCurrentSticker() instanceof TextSticker) {
                    TextSticker textSticker = (TextSticker) mExSticker.getCurrentSticker();
                    textSticker.setMaxTextSize((Integer) event.getData() * getResources().getDisplayMetrics().density);
                    textSticker.resizeText();
                    mExSticker.refresh();
                } else {
                    mExSticker.setFontSize((Integer) event.getData());
                }
                break;
            case EventConstant.FONT_BOLD:
                if (mExSticker.getCurrentSticker() != null && mExSticker.getCurrentSticker() instanceof TextSticker) {
                    TextSticker textSticker = (TextSticker) mExSticker.getCurrentSticker();
                    textSticker.setFontBold((Boolean) event.getData());
                    textSticker.resizeText();
                    mExSticker.refresh();
                } else {
                    mExSticker.setFontBold((Boolean) event.getData());
                }
                break;
            case EventConstant.FONT_ITALIC:
                if (mExSticker.getCurrentSticker() != null && mExSticker.getCurrentSticker() instanceof TextSticker) {
                    TextSticker textSticker = (TextSticker) mExSticker.getCurrentSticker();
                    textSticker.setFontItalic((Boolean) event.getData());
                    textSticker.resizeText();
                    mExSticker.refresh();
                } else {
                    mExSticker.setFontItalic((Boolean) event.getData());
                }
                break;
            case EventConstant.FONT_UNDERLINE:
                if (mExSticker.getCurrentSticker() != null && mExSticker.getCurrentSticker() instanceof TextSticker) {
                    TextSticker textSticker = (TextSticker) mExSticker.getCurrentSticker();
                    textSticker.setFontUnderline((Boolean) event.getData());
                    textSticker.resizeText();
                    mExSticker.refresh();
                } else {
                    mExSticker.setFontUnderline((Boolean) event.getData());
                }
                break;
            case EventConstant.FONT_GRAVITY:
                if (mExSticker.getCurrentSticker() != null && mExSticker.getCurrentSticker() instanceof TextSticker) {
                    TextSticker textSticker = (TextSticker) mExSticker.getCurrentSticker();
                    switch ((Integer) event.getData()) {
                        case 3:
                            textSticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                            break;
                        case 17:
                            textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                            break;
                        case 5:
                            textSticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                            break;
                    }
                    textSticker.resizeText();
                    mExSticker.refresh();
                } else {
                    mExSticker.setFontGravity((Integer) event.getData());
                }
                break;
            case EventConstant.QR_CODE:
                if (mVpTool.getVisibility() == View.VISIBLE) {
                    if (event.getData() != null) {
                        int height = (int) mScaleMargin * nowSize - mExSticker.getPaddingBottom() - mExSticker.getPaddingTop();
                        mExSticker.addStickerWithHeight(new DrawableSticker(height, new BitmapDrawable((Bitmap) event.getData())), height);
                    }
                    hideVpTool();
                } else {
                    finish();
                }
                break;
            case EventConstant.BAR_CODE:
                int height = (int) mScaleMargin * nowSize - mExSticker.getPaddingBottom() - mExSticker.getPaddingTop();
                mExSticker.addStickerWithHeight(new DrawableSticker(height, new BitmapDrawable((Bitmap) event.getData())), height);
                hideVpTool();
                break;
            case EventConstant.TAKE_PHOTO:
                mExSticker.addSticker(new DrawableSticker((String) event.getData(), getResources().getDisplayMetrics().widthPixels / 2.0f));
                hideVpTool();
                break;
            case EventConstant.PICTURE_LIST:
                mExSticker.addSticker(new DrawableSticker((String) event.getData(), getResources().getDisplayMetrics().widthPixels / 2.0f));
                hideVpTool();
                break;
            case EventConstant.PAINT_WIDTH:
                mExSticker.setPaintWidth((Integer) event.getData());
                break;
            case EventConstant.PAINT_COLOR:
                mExSticker.setPaintColor((Integer) event.getData());
                break;
            case EventConstant.UNDO:
                mExSticker.graffitiUndo();
                break;
            case EventConstant.DO:
                mExSticker.graffitiDo();
                break;
            case EventConstant.ERASER:
                mExSticker.graffitiEraser((Integer) event.getData());
                break;
            case EventConstant.NO_ERASER:
                mExSticker.graffitiNoEraser();
                break;
            case EventConstant.TYPEFACE:
                final Ttf ttf = (Ttf) event.getData();
                final String ttfFilePath = PATH_STICKER_TTF_FILE + "/" + ttf.getId();
                if (mExSticker.getCurrentSticker() != null && mExSticker.getCurrentSticker() instanceof TextSticker) {
                    final TextSticker textSticker = (TextSticker) mExSticker.getCurrentSticker();
                    if (new File(ttfFilePath).exists()) {
                        Typeface tf = Typeface.createFromFile(ttfFilePath);
                        textSticker.setTextTypefacePath(ttfFilePath);
                        textSticker.setTextTypefaceUrl(ttf.getFile_ttf());
                        textSticker.setTextTypefaceId(ttf.getId() + "");
                        textSticker.setTypeface(tf);
                        textSticker.resizeText();
                        mExSticker.refresh();
                    } else {
                        showLoading("请稍后。。。");
                        OkGo.<File>post(Urls.BASE_IMG + ttf.getFile_ttf())
                                .tag(this)
                                .execute(new FileCallback(PATH_STICKER_TTF_FILE, ttf.getId() + "") {
                                    @Override
                                    public void onSuccess(Response<File> response) {
                                        Typeface tf = Typeface.createFromFile(ttfFilePath);
                                        textSticker.setTextTypefacePath(ttfFilePath);
                                        textSticker.setTextTypefaceUrl(ttf.getFile_ttf());
                                        textSticker.setTextTypefaceId(ttf.getId() + "");
                                        textSticker.setTypeface(tf);
                                        textSticker.resizeText();
                                        mExSticker.refresh();
                                        dismissLoading();
                                    }
                                });
                    }
                } else {
                    if (new File(ttfFilePath).exists()) {
                        Typeface tf = Typeface.createFromFile(ttfFilePath);
                        mExSticker.setTypefacePath(ttfFilePath);
                        mExSticker.setTypefaceUrl(ttf.getFile_ttf());
                        mExSticker.setTypefaceId(ttf.getId() + "");
                        mExSticker.setTypeface(tf);
                    } else {
                        showLoading("请稍后。。。");
                        OkGo.<File>post(Urls.BASE_IMG + ttf.getFile_ttf())
                                .tag(this)
                                .execute(new FileCallback(PATH_STICKER_TTF_FILE, ttf.getId() + "") {
                                    @Override
                                    public void onSuccess(Response<File> response) {
                                        Typeface tf = Typeface.createFromFile(ttfFilePath);
                                        mExSticker.setTypefacePath(ttfFilePath);
                                        mExSticker.setTypefaceUrl(ttf.getFile_ttf());
                                        mExSticker.setTypefaceId(ttf.getId() + "");
                                        mExSticker.setTypeface(tf);
                                        dismissLoading();
                                    }
                                });
                    }
                }
                break;
            case EventConstant.NO_TYPEFACE:
                if (mExSticker.getCurrentSticker() != null && mExSticker.getCurrentSticker() instanceof TextSticker) {
                    TextSticker textSticker = (TextSticker) mExSticker.getCurrentSticker();
                    textSticker.setTextTypefacePath("");
                    textSticker.setTypeface(null);
                    textSticker.resizeText();
                    mExSticker.refresh();
                } else {
                    mExSticker.setTypefacePath("");
                    mExSticker.setNoTypeface();
                }
                break;
            case EventConstant.ADD_EMOJI:
                AddEmojiDrawable((String) event.getData());
                break;
            case EventConstant.CHANGE_THEME:
                changeTheme((Theme) event.getData());
//                mExSticker.changeTheme((String) event.getMaterialClass());
                break;
            case EventConstant.ADD_BUBBLE:
                addBubble((Bubble) event.getData());
                hideVpTool();
                break;
            case EventConstant.ADD_GRAFFITI:
                mExSticker.addSticker(new DrawableSticker((String) event.getData()));
                hideVpTool();
                break;
            case EventConstant.SHOW_DRAFT:
                showDraft((LabelDraft) event.getData());
                for (LabelSize size :
                        sizes) {
                    if (size.getSize() == ((LabelDraft) event.getData()).getSize()) {
                        setLabelSize(size);
                        break;
                    }
                }
                break;
        }
    }

    public void AddEmojiDrawable(String name) {
        if (mVpTool.getVisibility() == View.VISIBLE) {
            if (!TextUtils.isEmpty(name)) {
                int height = (int) mScaleMargin * nowSize - mExSticker.getPaddingBottom() - mExSticker.getPaddingTop();
                DrawableSticker sticker = new DrawableSticker(height, PATH_STICKER_EMOJI + "/" + name);
                mExSticker.addStickerWithHeight(sticker, height);

//                mExSticker.addSticker(new DrawableSticker(PATH_STICKER_EMOJI + "/" + name, getResources().getDisplayMetrics().widthPixels - 70*getResources().getDisplayMetrics().density));

            }
            hideVpTool();
        } else {
            finish();
        }
    }

    public void changeTheme(final Theme theme) {
        if (theme == null) {
            mExSticker.changeNoTheme();
        } else {
            final String path = PATH_STICKER_THEME + "/" + theme.getNineName();
            if (new File(path).exists()) {
                mExSticker.changeTheme(path, theme.getHead_px(), theme.getMiddle_px(), theme.getTail_px());
            } else {
                OkGo.<File>post(Urls.BASE_IMG + theme.getAn_image())
                        .tag(this)
                        .execute(new FileCallback(PATH_STICKER_THEME, theme.getNineName()) {
                            @Override
                            public void onSuccess(Response<File> response) {
                                mExSticker.changeTheme(path, theme.getHead_px(), theme.getMiddle_px(), theme.getTail_px());
                            }
                        });
            }
        }
        hideVpTool();
    }

    public void addBubble(Bubble bubble) {
        Drawable bubbleDrawable = Drawable.createFromPath(PATH_STICKER_BUBBLE + "/" + bubble.getId());
        Bitmap bitmap = BitmapFactory.decodeFile(PATH_STICKER_BUBBLE + "/" + bubble.getId());
        int height = (int) mScaleMargin * nowSize - (int) (0.5 * mScaleMargin) * 2;
        if (TextUtils.isEmpty(bubble.getImage_url())) {
   /*         mExSticker.addStickerWithHeight(new TextSticker(getApplicationContext())
                    .setText("双击编辑内容")
                    .setMaxTextSize(16 * getResources().getDisplayMetrics().density)
//                    .setScreenHeight(height)
                    .setScreenWidth((int)(getResources().getDisplayMetrics().widthPixels - 70*getResources().getDisplayMetrics().density))
                    .resizeText(), height);*/
            mExSticker.addStickerWithHeight(new TextSticker(getApplicationContext())
                    .setText("双击编辑")
                    .setMaxTextSize(15 * getResources().getDisplayMetrics().density)
//                    .setScreenHeight(height)
                    .setScreenWidth((int) width - 5)
                    .init()
                    .resizeText(), height);
        } else {
            mExSticker.addStickerWithHeight(new TextSticker(getApplicationContext())
                    .setBubbleId(bubble.getId() + "")
                    .setBubblePath(PATH_STICKER_BUBBLE + "/" + bubble.getId())
                    .setDrawable(bubbleDrawable)
                    .setScreenWidth( mExSticker.getStickerView().getWidth())
//                    .setScreenWidth((int) width - (int) (nowSize * 2 * getResources().getDisplayMetrics().density))
                    .setText("双击编辑")
                    .setMaxTextSize(15 * getResources().getDisplayMetrics().density)
                    .setSize(bitmap.getWidth(), bitmap.getHeight())
                    .setPadding(bubble.getAdd_x_px(), bubble.getAdd_y_px(), bubble.getReduce_x_px(), bubble.getReduce_y_px())
                    .init()
                    .resizeText(), height);

//            mExSticker.addSticker(new DrawableSticker((String) event.getData(), mExSticker.getStickerView().getWidth()));

        }
    }

    @Override
    public void onBubbleClick(Sticker sticker) {
        mEtBubbleContent.setText("");
        mLlBubbleSure.setVisibility(View.VISIBLE);
        mLlBottom.setVisibility(View.GONE);
        if (((TextSticker) sticker).isFirstText()) {
            mEtBubbleContent.setHint("双击编辑");
        } else {
            mEtBubbleContent.setText(((TextSticker) sticker).getText());
            mEtBubbleContent.setSelection(((TextSticker) sticker).getText().length());
        }
        hideVpTool();
        KeyBoardUtils.showInput(this, mEtBubbleContent);
    }

    @Override
    public void onBubbleCancel() {
        mLlBubbleSure.setVisibility(View.GONE);
        mLlBottom.setVisibility(View.VISIBLE);
    }

    public void onBubbleSure() {
        TextSticker sticker = (TextSticker) mExSticker.getCurrentSticker();
        if (TextUtils.isEmpty(mEtBubbleContent.getText().toString())) {
            sticker.setText("双击编辑");
            sticker.setFirstText(true);
        } else {
            sticker.setText(mEtBubbleContent.getText().toString());
        }
        sticker.resizeText();
        mExSticker.refresh();
        mLlBubbleSure.setVisibility(View.GONE);
        mLlBottom.setVisibility(View.VISIBLE);
    }

    public void showDraft(final LabelDraft draft) {
        mExSticker.setEditContent(draft.getEditContent());
        mExSticker.setFontSize(draft.getTextSize());
        mExSticker.setFontBold(draft.isTextBold());
        mExSticker.setFontItalic(draft.getTextItalic() != 0f);
        mExSticker.setFontUnderline(draft.isTextUnderline());
        mExSticker.setFontGravity(draft.getTextGravity());

        if (!TextUtils.isEmpty(draft.getTextTypefacePath())) {
            if (new File(draft.getTextTypefacePath()).exists()) {
                Typeface tf = Typeface.createFromFile(draft.getTextTypefacePath());
                mExSticker.setTypefacePath(draft.getTextTypefacePath());
                mExSticker.setTypefaceId(draft.getTextTypefaceId());
                mExSticker.setTypefaceUrl(draft.getTextTypefaceUrl());
                mExSticker.setTypeface(tf);
            } else {
                showLoading("请稍后。。。");
                OkGo.<File>post(Urls.BASE_IMG + draft.getTextTypefaceUrl())
                        .tag(this)
                        .execute(new FileCallback(PATH_STICKER_TTF_FILE, draft.getTextTypefaceId()) {
                            @Override
                            public void onSuccess(Response<File> response) {
                                Typeface tf = Typeface.createFromFile(draft.getTextTypefacePath());
                                mExSticker.setTypefacePath(draft.getTextTypefacePath());
                                mExSticker.setTypefaceId(draft.getTextTypefaceId());
                                mExSticker.setTypefaceUrl(draft.getTextTypefaceUrl());
                                mExSticker.setTypeface(tf);
                                dismissLoading();
                            }
                        });
            }
        }
        mExSticker.getStickers().clear();
        for (DraftSticker stickerDraft :
                draft.getDraftStickers()) {
            if (stickerDraft instanceof DrawableDraftSticker) {
                if (!TextUtils.isEmpty(stickerDraft.getPath()) && new File(stickerDraft.getPath()).exists()) {
                    DrawableSticker sticker = new DrawableSticker(stickerDraft.getPath());
                    sticker.getMatrix().setValues(stickerDraft.getMartixValues());
                    mExSticker.addDraftSticker(sticker);
                }
            } else if (stickerDraft instanceof TextDraftSticker) {
                TextDraftSticker sticker = (TextDraftSticker) stickerDraft;
                Resources resources = getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                int width = dm.widthPixels;
                Drawable bubbleDrawable = Drawable.createFromPath(PATH_STICKER_BUBBLE + "/" + sticker.getBubbleId());
                Bitmap bitmap = BitmapFactory.decodeFile(PATH_STICKER_BUBBLE + "/" + sticker.getBubbleId());
                TextSticker textSticker;
                int height = (int) mScaleMargin * nowSize - (int) (2.5 * mScaleMargin) * 2;
                if (TextUtils.isEmpty(sticker.getPath())) {
                    textSticker = new TextSticker(getApplicationContext())
                            .setText(sticker.getEditContent())
                            .setMaxTextSize(sticker.getTextSize())
//                            .setScreenHeight(height)
                            .resizeText();
                    textSticker.getMatrix().setValues(stickerDraft.getMartixValues());
                } else {
                    textSticker = new TextSticker(getApplicationContext())
                            .setBubbleId(sticker.getBubbleId())
                            .setBubblePath(PATH_STICKER_BUBBLE + "/" + sticker.getBubbleId())
                            .setDrawable(bubbleDrawable)
                            .setText(sticker.getEditContent())
                            .setMaxTextSize(sticker.getTextSize())
//                            .setScreenHeight(height)
                            .setSize(bitmap.getWidth(), bitmap.getHeight())
                            .setPadding(sticker.getPaddingLeft(), sticker.getPaddingTop(), sticker.getPaddingRight(), sticker.getPaddingBottom())
                            .resizeText();
                    textSticker.getMatrix().setValues(stickerDraft.getMartixValues());
                }

    /*            textSticker= new TextSticker(getApplicationContext())
                        .setBubbleId(sticker.getBubbleId())
                        .setBubblePath(PATH_STICKER_BUBBLE + "/" + sticker.getBubbleId())
                        .setDrawable(bubbleDrawable)
                        .setText(sticker.getEditContent())
                        .setMaxTextSize(sticker.getTextSize())
                        .setScreenWidth((int) (500 * dm.density))
                        .setSize(bitmap.getWidth(), bitmap.getHeight())
                        .setPadding(sticker.getPaddingLeft(), sticker.getPaddingTop(), sticker.getPaddingRight(), sticker.getPaddingBottom())
                        .resizeText();
                textSticker.getMatrix().setValues(stickerDraft.getMartixValues());*/
//                mExSticker.addStickerWithHeight(textSticker,height);
                mExSticker.addDraftSticker(textSticker);
            }

        }
//        mExSticker.drawGraffitiPath(draft.getPathDrafts());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null)
            mPopupWindow.dismiss();
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    @Override
    public void onStickerClick() {
        if (mExSticker.canMoveUp()) {
            mIvMoveUp.setImageResource(R.drawable.icon_move_up_p);
            mTvMoveUp.setTextColor(getResources().getColor(R.color.text_orange));
        } else {
            mIvMoveUp.setImageResource(R.drawable.icon_move_up_n);
            mTvMoveUp.setTextColor(getResources().getColor(R.color.text_gray));
        }
        if (mExSticker.canMoveDown()) {
            mIvMoveDown.setImageResource(R.drawable.icon_move_down_p);
            mTvMoveDown.setTextColor(getResources().getColor(R.color.text_orange));
        } else {
            mIvMoveDown.setImageResource(R.drawable.icon_move_down_n);
            mTvMoveDown.setTextColor(getResources().getColor(R.color.text_gray));
        }
    }

    @Override
    public void onStickerDelete() {
        mLlBubbleSure.setVisibility(View.GONE);
        mLlBottom.setVisibility(View.VISIBLE);
        hideInputForce(this);
    }

    @Override
    public void onTouch() {
        hideVpTool();
    }

    @Override
    public void onClick() {
        KeyBoardUtils.showInput(this, mExSticker.getEdittext());
    }
}