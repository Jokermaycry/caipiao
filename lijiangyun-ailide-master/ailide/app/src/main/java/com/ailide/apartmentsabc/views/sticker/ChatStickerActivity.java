package com.ailide.apartmentsabc.views.sticker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
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
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ailide.apartmentsabc.R;
import com.ailide.apartmentsabc.eventbus.EventBusEntity;
import com.ailide.apartmentsabc.eventbus.EventConstant;
import com.ailide.apartmentsabc.eventbus.SendEditEvent;
import com.ailide.apartmentsabc.framework.util.KeyBoardUtils;
import com.ailide.apartmentsabc.model.Bubble;
import com.ailide.apartmentsabc.model.DraftSticker;
import com.ailide.apartmentsabc.model.DrawableDraftSticker;
import com.ailide.apartmentsabc.model.PictureBean;
import com.ailide.apartmentsabc.model.StickerDraft;
import com.ailide.apartmentsabc.model.TextDraftSticker;
import com.ailide.apartmentsabc.model.Theme;
import com.ailide.apartmentsabc.model.Ttf;
import com.ailide.apartmentsabc.tools.Contants;
import com.ailide.apartmentsabc.tools.ToastUtil;
import com.ailide.apartmentsabc.tools.Urls;
import com.ailide.apartmentsabc.tools.shareprefrence.SPUtil;
import com.ailide.apartmentsabc.utils.FileUtil;
import com.ailide.apartmentsabc.utils.WordAndPicture;
import com.ailide.apartmentsabc.views.base.BaseActivity;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.xiaopo.flying.exsticker.ExSticker;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.RotateIconEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import org.greenrobot.eventbus.EventBus;
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

public class ChatStickerActivity extends BaseActivity implements ExSticker.OnBubbleClickLisener, ExSticker.OnStickerClickLisener {
    public static final int PERM_RQST_CODE = 110;

    @BindView(R.id.ll_main)
    LinearLayout mLlMain;
    @BindView(R.id.exsticker)
    ExSticker mExSticker;
    @BindView(R.id.ll_text)
    LinearLayout mLlText;
    @BindView(R.id.ll_pic)
    LinearLayout mLlPic;
    @BindView(R.id.ll_emoji)
    LinearLayout mLlEmoji;
    @BindView(R.id.ll_theme)
    LinearLayout mLlTheme;
    @BindView(R.id.ll_qr_code)
    LinearLayout mLlQrCode;
    @BindView(R.id.ll_graffiti)
    LinearLayout mLlGraffiti;
    @BindView(R.id.tv_text)
    TextView mTvText;
    @BindView(R.id.tv_pic)
    TextView mTvPic;
    @BindView(R.id.tv_emoji)
    TextView mTvEmoji;
    @BindView(R.id.tv_theme)
    TextView mTvTheme;
    @BindView(R.id.tv_qr_code)
    TextView mTvQrCode;
    @BindView(R.id.tv_graffiti)
    TextView mTvGraffiti;
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
    private TextToolFragment textToolFragment;
    private PicToolFragment picToolFragment;
    private EmojiToolFragment emojiToolFragment;
    private ThemeToolFragment themeToolFragment;
    private QrCodeToolFragment qrCodeToolFragment;
    private GraffitiToolFragment graffitiToolFragment;
    private Theme mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_chat_sticker);
        ButterKnife.bind(this);
        mBottomLinearouts = new ArrayList<>();
        mBottomLinearouts.add(mLlText);
        mBottomLinearouts.add(mLlPic);
        mBottomLinearouts.add(mLlEmoji);
        mBottomLinearouts.add(mLlTheme);
        mBottomLinearouts.add(mLlQrCode);
        mBottomLinearouts.add(mLlGraffiti);

        mBottomTexts = new ArrayList<>();
        mBottomTexts.add(mTvText);
        mBottomTexts.add(mTvPic);
        mBottomTexts.add(mTvEmoji);
        mBottomTexts.add(mTvTheme);
        mBottomTexts.add(mTvQrCode);
        mBottomTexts.add(mTvGraffiti);

        mToolsFragment = new ArrayList<>();
        textToolFragment = new TextToolFragment();
        mToolsFragment.add(textToolFragment);
        picToolFragment = new PicToolFragment();
        mToolsFragment.add(picToolFragment);
        emojiToolFragment = new EmojiToolFragment();
        mToolsFragment.add(emojiToolFragment);
        themeToolFragment = new ThemeToolFragment();
        mToolsFragment.add(themeToolFragment);
        qrCodeToolFragment = new QrCodeToolFragment();
        mToolsFragment.add(qrCodeToolFragment);
        graffitiToolFragment = new GraffitiToolFragment();
//        mToolsFragment.add(graffitiToolFragment);

       /* mToolsAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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

        //currently you can config your own icons and icon event
        //the event you can custom
        BitmapStickerIcon deleteIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this,
                        R.drawable.icon_sticker_delete),
                        BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this,
                        R.drawable.icon_sticker_zoom),
                        BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon rotateIcon =
                new BitmapStickerIcon(ContextCompat.getDrawable(this,
                        R.drawable.icon_sticker_rotate),
                        BitmapStickerIcon.RIGHT_TOP);
        rotateIcon.setIconEvent(new RotateIconEvent());
        mExSticker.setIcons(Arrays.asList(deleteIcon, zoomIcon, rotateIcon));
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

        mExSticker.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    @OnClick({R.id.iv_back, R.id.ll_move_up, R.id.ll_move_down, R.id.ll_send,
            R.id.ll_text, R.id.ll_pic, R.id.ll_emoji, R.id.ll_theme, R.id.ll_qr_code, R.id.ll_graffiti,
            R.id.tv_bubble_sure})
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.ll_send:
                File file = FileUtil.getStickerFile();
                mExSticker.setSelect(false);
                mExSticker.setCursorVisible(false);
                mExSticker.refresh();
                mExSticker.save(file);
                mExSticker.setCursorVisible(true);
                SendEditEvent event = new SendEditEvent();
                event.setImg(file.getAbsolutePath());
                EventBus.getDefault().post(event);
                finish();
                break;
            case R.id.ll_text:
                if (mVpTool.getVisibility() == View.VISIBLE && mVpTool.getCurrentItem() == 0) {
                    hideVpTool();
                }else {
                    setBottomTextColor(0);
                    textToolFragment.init();
                }
                break;
            case R.id.ll_pic:
                if (mVpTool.getVisibility() == View.VISIBLE && mVpTool.getCurrentItem() == 1) {
                    hideVpTool();
                }else {
                    setBottomTextColor(1);
                    picToolFragment.init();
                }
                break;
            case R.id.ll_emoji:
                if (mVpTool.getVisibility() == View.VISIBLE && mVpTool.getCurrentItem() == 2) {
                    hideVpTool();
                }else {
                    setBottomTextColor(2);
                    emojiToolFragment.init();
                }
                break;
            case R.id.ll_theme:
                if (mVpTool.getVisibility() == View.VISIBLE && mVpTool.getCurrentItem() == 3) {
                    hideVpTool();
                }else {
                    setBottomTextColor(3);
                    themeToolFragment.init();
                }
                break;
            case R.id.ll_qr_code:
                if (mVpTool.getVisibility() == View.VISIBLE && mVpTool.getCurrentItem() == 4) {
                    hideVpTool();
                }else {
                    setBottomTextColor(4);
                    qrCodeToolFragment.onClickQrCode();
                }
                break;
            case R.id.ll_graffiti:
//                setBottomTextColor(5);
                hideVpTool();
//                graffitiToolFragment.init();
                startActivityForResult(new Intent(this, GraffitiActivity.class), 11);
                break;
            case R.id.tv_bubble_sure:
                onBubbleSure();
                break;
        }
    }

    public void setBottomTextColor(final int position) {
        if(mToolsAdapter == null){
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
       /*     if (position != 5) {
                mExSticker.setGraffitiable(false);
            } else {
                mExSticker.setLocked(false);
            }*/
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
                StickerDraft draft = new StickerDraft();
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
                if (mTheme != null) {
                    draft.setTheme(mTheme);
                }
                List<StickerDraft> drafts = (List<StickerDraft>) SPUtil.readObject(Contants.SP_STICKER_DRAFTS);
                if (drafts == null) {
                    drafts = new ArrayList<>();
                }
                drafts.add(0, draft);
                SPUtil.saveObject(Contants.SP_STICKER_DRAFTS, drafts);
                return draft.getTime();
            }

            @Override
            protected void onPostExecute(Long time) {
                File file = FileUtil.getStickerDraftFile(time);
                mExSticker.save(file);
                dismissLoading();
                mExSticker.setCursorVisible(true);
                ToastUtil.toast("保存成功");
                finish();
            }
        }.execute();
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
//            expandVpTool();
            hideVpTool();
        } else {
            showSaveDraftDialog();
//            finish();
        }
    }

    public void showSaveDraftDialog() {
        if (mExSticker.getStickers().size() == 0 && TextUtils.isEmpty(mExSticker.getEditContent())) {
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
        mVpTool.setVisibility(View.GONE);
        for (int i = 0; i < mBottomTexts.size(); i++) {
            mBottomTexts.get(i).setTextColor(getResources().getColor(R.color.text_gray));
            mBottomLinearouts.get(i).setBackgroundColor(getResources().getColor(R.color.bg_white));
        }
        mExSticker.setGraffitiable(false);
        mExSticker.setLocked(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEntity event) {
        switch (event.getId()) {
            case EventConstant.CHANGE_FONT:
                if (mExSticker.getCurrentSticker() != null && mExSticker.getCurrentSticker() instanceof TextSticker) {
                    TextSticker textSticker = (TextSticker) mExSticker.getCurrentSticker();
                    textSticker.setMaxTextSize((Integer) event.getData()*getResources().getDisplayMetrics().density);
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
                        mExSticker.addSticker(new DrawableSticker(new BitmapDrawable((Bitmap) event.getData())));
                    }
                    hideVpTool();
                } else {
                    finish();
                }
                break;
            case EventConstant.BAR_CODE:
                mExSticker.addSticker(new DrawableSticker(new BitmapDrawable((Bitmap) event.getData())));
                hideVpTool();
                break;
            case EventConstant.TAKE_PHOTO:
//                mExSticker.addSticker(new DrawableSticker((String) event.getData(),getResources().getDisplayMetrics().widthPixels- 70*getResources().getDisplayMetrics().density));
                mExSticker.addSticker(new DrawableSticker((String) event.getData(), mExSticker.getFlSticker().getWidth()));
                hideVpTool();
                break;
            case EventConstant.MY_MATERIAL:
//                mExSticker.addSticker(new DrawableSticker(new BitmapDrawable((Bitmap) event.getData())));
                mExSticker.addSticker(new DrawableSticker(new BitmapDrawable((Bitmap) event.getData()), mExSticker.getFlSticker().getWidth()));
                hideVpTool();
                break;
            case EventConstant.PICTURE_LIST:
//                mExSticker.addSticker(new DrawableSticker((String) event.getData(),getResources().getDisplayMetrics().widthPixels- 70*getResources().getDisplayMetrics().density));
                mExSticker.addSticker(new DrawableSticker((String) event.getData(), mExSticker.getFlSticker().getWidth()));
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
                break;
            case EventConstant.ADD_GRAFFITI:
                mExSticker.addSticker(new DrawableSticker((String) event.getData()));
                hideVpTool();
                break;
            case EventConstant.SHOW_DRAFT:
                showDraft((StickerDraft) event.getData());
                break;
        }
    }

    public void AddEmojiDrawable(String name) {
        if (mVpTool.getVisibility() == View.VISIBLE) {
            if (!TextUtils.isEmpty(name)) {
                Resources resources = getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                int width = dm.widthPixels;
                DrawableSticker sticker = new DrawableSticker(PATH_STICKER_EMOJI + "/" + name, width/ 2.0f);
                mExSticker.addSticker(sticker);
            }
            hideVpTool();
        } else {
            finish();
        }
    }

    public void changeTheme(final Theme theme) {
        if (theme == null) {
            mTheme = null;
            mExSticker.changeNoTheme();
        } else {
            mTheme = theme;
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
//        Drawable bubble = ContextCompat.getDrawable(this, R.drawable.icon_bubble);
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        Drawable bubbleDrawable = Drawable.createFromPath(PATH_STICKER_BUBBLE + "/" + bubble.getId());
        Bitmap bitmap = BitmapFactory.decodeFile(PATH_STICKER_BUBBLE + "/" + bubble.getId());
        if(TextUtils.isEmpty(bubble.getImage_url())){
            mExSticker.addSticker(new TextSticker(getApplicationContext())
                    .setText("双击编辑")
                    .setMaxTextSize(16* dm.density)
                    .setScreenWidth((int) (250 * dm.density))
                    .init()
                    .resizeText());
        }else {
            mExSticker.addSticker(new TextSticker(getApplicationContext())
                    .setBubbleId(bubble.getId() + "")
                    .setBubblePath(PATH_STICKER_BUBBLE + "/" + bubble.getId())
                    .setDrawable(bubbleDrawable)
                    .setMaxTextSize(16* dm.density)
                    .setText("双击编辑")
                    .setScreenWidth((int) (250 * dm.density))
                    .setSize(bitmap.getWidth(), bitmap.getHeight())
                    .setPadding(bubble.getAdd_x_px(), bubble.getAdd_y_px(), bubble.getReduce_x_px(), bubble.getReduce_y_px())
                    .init()
                    .resizeText());
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

    public void showDraft(final StickerDraft draft) {
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
                TextSticker textSticker = new TextSticker(getApplicationContext())
//                .setBitmap(bitmap)
                        .setBubbleId(sticker.getBubbleId())
                        .setBubblePath(PATH_STICKER_BUBBLE + "/" + sticker.getBubbleId())
                        .setDrawable(bubbleDrawable)
                        .setText(sticker.getEditContent())
                        .setMaxTextSize(sticker.getTextSize())
                        .setScreenWidth((int) (250 * dm.density))
//                .setScreenWidth(width)
                        .setSize(bitmap.getWidth(), bitmap.getHeight())
                        .setPadding(sticker.getPaddingLeft(), sticker.getPaddingTop(), sticker.getPaddingRight(), sticker.getPaddingBottom())
                        .resizeText();
                textSticker.getMatrix().setValues(stickerDraft.getMartixValues());
                mExSticker.addDraftSticker(textSticker);
            }

        }
//        mExSticker.drawGraffitiPath(draft.getPathDrafts());
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