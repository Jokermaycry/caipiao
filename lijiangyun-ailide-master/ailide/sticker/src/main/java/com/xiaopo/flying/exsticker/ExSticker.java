package com.xiaopo.flying.exsticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.xiaopo.flying.graffiti.DrawPath;
import com.xiaopo.flying.graffiti.GraffitiView;
import com.xiaopo.flying.graffiti.PathDraft;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.R;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerUtils;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.util.BitmapUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExSticker extends FrameLayout {

    public int headDp;
    public int tailDp;
    private ScrollView mScrollView;
    private StickerView mStickerView;
    private ImageView mIvTheme;
    private FrameLayout mFlCanvas;
    private EditText mEtContent;
    private GraffitiView mGraffitiView;
    private FrameLayout mFlSticker;
    private OnBubbleClickLisener onBubbleClickLisener;
    private OnStickerClickLisener onStickerClickLisener;
    private int scrollHeight;
    private String themePath;
    private String typefacePath;
    private String typefaceUrl;
    private String typefaceId;
    private Context mContext;
    private long mLastTime;
    private long mCurTime;

    public ExSticker(Context context) {
        this(context, null);
    }

    public ExSticker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExSticker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public OnStickerClickLisener getOnStickerClickLisener() {
        return onStickerClickLisener;
    }

    public void setOnStickerClickLisener(OnStickerClickLisener onStickerClickLisener) {
        this.onStickerClickLisener = onStickerClickLisener;
    }

    public OnBubbleClickLisener getOnBubbleClickLisener() {
        return onBubbleClickLisener;
    }

    public void setOnBubbleClickLisener(OnBubbleClickLisener onBubbleClickLisener) {
        this.onBubbleClickLisener = onBubbleClickLisener;
    }

    public void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_exsticker, this, true);
        mScrollView = findViewById(R.id.scrollView);
        mStickerView = findViewById(R.id.sticker_view);
        mIvTheme = findViewById(R.id.iv_theme);
        mFlCanvas = findViewById(R.id.fl_canvas);
        mEtContent = findViewById(R.id.et_content);
        mGraffitiView = findViewById(R.id.graffiti_view);
        mFlSticker = findViewById(R.id.fl_sticker);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                mScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                scrollHeight = mScrollView.getMeasuredHeight();
                calculateHeight();
            }
        });
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        calculateHeight();
                    }
                }, 100);
            }
        });
        mEtContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mGraffitiView.isGraffitiable()) {
                    mGraffitiView.onTouchEvent(event);
                }
                return mGraffitiView.isGraffitiable();
            }
        });
        mStickerView.setConstrained(false);
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = MotionEventCompat.getActionMasked(motionEvent);
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mLastTime = mCurTime;
                        mCurTime = System.currentTimeMillis();
                        if (mCurTime - mLastTime < 500) {
                            if (onStickerClickLisener != null) {
                                onStickerClickLisener.onClick();
                            }
                        }
                        break;
                }
                //选中贴纸时，scrollview不能滑动
                //false可以滑动, true不能滑动
                if (onStickerClickLisener != null) {
                    onStickerClickLisener.onTouch();
                }
                return mGraffitiView.isGraffitiable() || mStickerView.isSelect();
            }
        });
        mStickerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //scrollview滑动嵌套，防止滑动贴纸时，scrollview联动
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mScrollView.requestDisallowInterceptTouchEvent(false);
                } else if (mStickerView.isSelect()) {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                } else if (mGraffitiView.isGraffitiable()) {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                } else if (!mStickerView.isSelect()) {
                    mScrollView.requestDisallowInterceptTouchEvent(false);
                } else if (!mGraffitiView.isGraffitiable()) {
                    mScrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                }
              /*  if (onStickerClickLisener != null) {
                    onStickerClickLisener.onTouch();
                }*/
                return false;
            }
        });
        mStickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                if (onStickerClickLisener != null) {
                    onStickerClickLisener.onStickerClick();
                }
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                if (onStickerClickLisener != null) {
                    onStickerClickLisener.onStickerClick();
                }
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                calculateHeight();
                if (onStickerClickLisener != null) {
                    onStickerClickLisener.onStickerClick();
                    onStickerClickLisener.onStickerDelete();
                }
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                calculateHeight();
            }

            @Override
            public void onStickerDrag(@NonNull Sticker sticker) {
                calculateHeight(true);
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                calculateHeight();
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                if (sticker instanceof TextSticker) {
                    if (onBubbleClickLisener != null) {
                        onBubbleClickLisener.onBubbleClick(sticker);
                    }
                }
            }

            @Override
            public void update() {
                calculateHeight();
            }

            @Override
            public void onStickerCancel() {
                if (onBubbleClickLisener != null) {
                    onBubbleClickLisener.onBubbleCancel();
                }
                if (onStickerClickLisener != null) {
                    onStickerClickLisener.onStickerClick();
                }
            }
        });
    }

    public void calculateHeight() {
        calculateHeight(false);
    }

    public void calculateHeight(boolean move) {
//        int lastHeight = mScrollView.getMeasuredHeight();
        int etContentHeight = mEtContent.getHeight();
        int lastHeight = etContentHeight;
        int stickerHeight = (int) mStickerView.getLastLocationY(lastHeight);
        int bgHeight = stickerHeight;
        if (bgHeight < etContentHeight) {
            bgHeight = etContentHeight;
        }
        if (move) {
            if (bgHeight > scrollHeight) {
                return;
            }
        }
        if (lastHeight < stickerHeight) {
            lastHeight = stickerHeight;
        }

        if (TextUtils.isEmpty(themePath)) {
            if (lastHeight < scrollHeight) {
                lastHeight = scrollHeight - 50;
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mStickerView.getLayoutParams();
            lastHeight = lastHeight - mFlSticker.getPaddingTop() - mFlSticker.getPaddingBottom();
            params.height = lastHeight + 50;
            mStickerView.setLayoutParams(params);
        } else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mStickerView.getLayoutParams();
            lastHeight = lastHeight - mFlSticker.getPaddingTop() - mFlSticker.getPaddingBottom();
            if (getStickers().size() > 0) {
                params.height = lastHeight + 50;
            } else {
                params.height = lastHeight;
            }
            mStickerView.setLayoutParams(params);
            FrameLayout.LayoutParams themeParams = (FrameLayout.LayoutParams) mIvTheme.getLayoutParams();
            if (getStickers().size() > 0) {
                themeParams.height = bgHeight + tailDp + headDp + 50;
            } else {
                themeParams.height = bgHeight + tailDp + headDp;
            }
            mIvTheme.setLayoutParams(themeParams);
        }
    }

    public void setIcons(@NonNull List<BitmapStickerIcon> icons) {
        mStickerView.setIcons(icons);
    }

    public StickerView getStickerView() {
        return mStickerView;
    }

    public FrameLayout getFlSticker() {
        return mFlSticker;
    }

    public void save(@NonNull final File file) {
        try {
            int etContentHeight = mEtContent.getHeight();
            int lastHeight = etContentHeight;
            int stickerHeight = (int) mStickerView.getLastLocationY(lastHeight);
            int bgHeight = stickerHeight;
            if (bgHeight < etContentHeight) {
                bgHeight = etContentHeight;
            }
            if (lastHeight < stickerHeight) {
                lastHeight = stickerHeight;
            }

            if (TextUtils.isEmpty(themePath)) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mStickerView.getLayoutParams();
                lastHeight = lastHeight - mFlSticker.getPaddingTop() - mFlSticker.getPaddingBottom();
                params.height = lastHeight;
//                params.height = lastHeight+50 ;
                mStickerView.setLayoutParams(params);
                final int finalLastHeight = lastHeight;
                mStickerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mStickerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        StickerUtils.saveImageToGallery(file, BitmapUtil.createBitmap(mFlSticker));
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mStickerView.getLayoutParams();
                        if (getStickers().size() > 0) {
                            params.height = finalLastHeight + 50;
                        } else {
                            params.height = finalLastHeight;
                        }
                        mStickerView.setLayoutParams(params);
                    }
                });
            } else {
                StickerUtils.saveImageToGallery(file, BitmapUtil.createBitmap(mFlCanvas));
             /*   FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mStickerView.getLayoutParams();
                lastHeight = lastHeight - mFlSticker.getPaddingTop() - mFlSticker.getPaddingBottom();
                params.height = lastHeight + 50;
                mStickerView.setLayoutParams(params);
                FrameLayout.LayoutParams themeParams = (FrameLayout.LayoutParams) mIvTheme.getLayoutParams();
                FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) mFlSticker.getLayoutParams();
//            themeParams.height = bgHeight + lp2.bottomMargin*2;
                themeParams.height = bgHeight + tailDp * 2;
                mIvTheme.setLayoutParams(themeParams);
                mIvTheme.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mStickerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        StickerUtils.saveImageToGallery(file, BitmapUtil.createBitmap(mFlCanvas));
                    }
                });*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFontSize(int size) {
        mEtContent.setTextSize(size);
        calculateHeight();
    }

    public void setTypeface(Typeface ttf) {
        mEtContent.setTypeface(ttf);
        calculateHeight();
    }

    public void setNoTypeface() {
        mEtContent.setTypeface(null);
        calculateHeight();
    }

    public void setFontItalic(boolean italic) {
        mEtContent.getPaint().setTextSkewX(italic ? -0.5f : 0f);
        mEtContent.setText(mEtContent.getText());
        mEtContent.setSelection(mEtContent.getText().length());
        calculateHeight();
    }

    public void setFontUnderline(boolean underline) {
        mEtContent.getPaint().setUnderlineText(underline);
        mEtContent.setText(mEtContent.getText());
        mEtContent.setSelection(mEtContent.getText().length());
        calculateHeight();
    }

    public void setFontBold(boolean bold) {
        mEtContent.getPaint().setFakeBoldText(bold);
        mEtContent.setText(mEtContent.getText());
        mEtContent.setSelection(mEtContent.getText().length());
        calculateHeight();
    }

    public void setFontGravity(int gravity) {
        mEtContent.setGravity(gravity);
        calculateHeight();
    }

    public String getEditContent() {
        return mEtContent.getText().toString();
    }

    public void setEditContent(String content) {
        mEtContent.setText(content);
        calculateHeight();
    }

    public List<DrawPath> getGraffitiPath() {
        return mGraffitiView.getDrawPathList();
    }

    public void drawGraffitiPath(List<PathDraft> pathDrafts) {
        mGraffitiView.drawPath(pathDrafts);
    }

    @NonNull
    public void addSticker(@NonNull Sticker sticker) {
        mStickerView.addSticker(sticker);
        calculateHeight();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void addDraftSticker(Sticker sticker) {
        mStickerView.addDraftSticker(sticker);
        calculateHeight();
    }

    public void addSticker(@NonNull final Sticker sticker,
                           final @Sticker.Position int position) {
        mStickerView.addSticker(sticker, position);
        calculateHeight();
    }

    public boolean replace(@Nullable Sticker sticker) {
        return mStickerView.replace(sticker);
    }

    @NonNull
    public StickerView setLocked(boolean locked) {
        return mStickerView.setLocked(locked);
    }

    public boolean isLocked() {
        return mStickerView.isLocked();
    }

    public boolean removeCurrentSticker() {
        return mStickerView.removeCurrentSticker();
    }

    public void removeAllStickers() {
        mStickerView.removeAllStickers();
    }

    public void moveUpSticker() {
        Sticker sticker = mStickerView.getCurrentSticker();
        if (sticker != null) {
            List<Sticker> list = mStickerView.getStickers();
            int index = list.indexOf(sticker);
            if (index != list.size() - 1) {
                list.remove(sticker);
                list.add(index + 1, sticker);
                mStickerView.invalidate();
            }
        }
    }

    public boolean canMoveUp() {
        if (!mStickerView.isSelect()) {
            return false;
        }
        Sticker sticker = mStickerView.getCurrentSticker();
        if (sticker != null) {
            List<Sticker> list = mStickerView.getStickers();
            int index = list.indexOf(sticker);
            if (index < list.size() - 1) {
                return true;
            }
        }
        return false;
    }

    public boolean canMoveDown() {
        if (!mStickerView.isSelect()) {
            return false;
        }
        Sticker sticker = mStickerView.getCurrentSticker();
        if (sticker != null) {
            List<Sticker> list = mStickerView.getStickers();
            int index = list.indexOf(sticker);
            if (index > 0) {
                return true;
            }
        }
        return false;
    }

    public void moveDownSticker() {
        Sticker sticker = mStickerView.getCurrentSticker();
        if (sticker != null) {
            List<Sticker> list = mStickerView.getStickers();
            int index = list.indexOf(sticker);
            if (index != 0) {
                list.remove(sticker);
                list.add(index - 1, sticker);
                mStickerView.invalidate();
            }
        }
    }

    public boolean isGraffitiable() {
        return mGraffitiView.isGraffitiable();
    }

    public void setGraffitiable(boolean graffitiable) {
        mGraffitiView.setGraffitiable(graffitiable);
    }

    public void setPaintWidth(int width) {
        mGraffitiView.setPaintWidth(width);
        mGraffitiView.setGraffitiable(true);
        mStickerView.setLocked(true);
    }

    public void setPaintColor(int color) {
        mGraffitiView.setPaintColor(color);
        mGraffitiView.setGraffitiable(true);
        mStickerView.setLocked(true);
    }

    public void graffitiDo() {
        mGraffitiView.reundo();
    }

    public void graffitiUndo() {
        mGraffitiView.undo();
    }

    public void graffitiEraser(int width) {
        mGraffitiView.eraser(width);
        mGraffitiView.setGraffitiable(true);
        mStickerView.setLocked(true);
    }

    public void graffitiNoEraser() {
        mGraffitiView.noEraser();
        mGraffitiView.setGraffitiable(true);
        mStickerView.setLocked(true);
    }

    public ArrayList<Sticker> getStickers() {
        return mStickerView.getStickers();
    }

    public Sticker getCurrentSticker() {
        return mStickerView.getCurrentSticker();
    }

    public void setSelect(boolean select) {
        mStickerView.setSelect(select);
    }

    public void setCursorVisible(boolean show) {
        if (show) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEtContent.setCursorVisible(true);
                }
            }, 100);
        } else {
            mEtContent.setCursorVisible(show);
        }
    }

    public EditText getEdittext() {
        return mEtContent;
    }

    public void refresh() {
        mStickerView.invalidate();
    }

    public void changeNoTheme() {
        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) mFlSticker.getLayoutParams();
        lp2.setMargins(0, 0, 0, 0);
        mFlSticker.setLayoutParams(lp2);
//        mFlSticker.setPadding(0,0,0,0);
        this.headDp = 0;
        this.tailDp = 0;
        mIvTheme.setImageDrawable(null);
        calculateHeight();
    }

    public void changeTheme(String path, int head, int middle, int tail) {
        themePath = path;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            Toast.makeText(mContext, "主题图片为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mIvTheme.setImageBitmap(bitmap);

        byte[] chunk = bitmap.getNinePatchChunk();
        if (NinePatch.isNinePatchChunk(chunk)) {
            NinePatchDrawable patchy = new NinePatchDrawable(bitmap, chunk, new Rect(), null);
            mIvTheme.setImageDrawable(patchy);
        } else {
            Toast.makeText(mContext, "主题不是.9图片", Toast.LENGTH_SHORT).show();
            return;
        }
//        float headDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, head, getResources().getDisplayMetrics());
//        float middleDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, middle, getResources().getDisplayMetrics());
//        float tailDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tail, getResources().getDisplayMetrics());
        float raid = bitmap.getWidth() * 1f / getWidth();
//        float headDp = head / raid;
//        float tailDp = tail / raid;
        float headDp = head;
        float tailDp = tail;
        float middleDp = middle / raid;

        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) mFlSticker.getLayoutParams();
        this.headDp = (int) headDp;
        this.tailDp = (int) tailDp;
        lp2.setMargins((int) middleDp, (int) headDp, (int) middleDp, 0);
        mFlSticker.setLayoutParams(lp2);
//        mFlSticker.setPadding((int)middleDp, (int)headDp, (int)middleDp, (int)tailDp);
        calculateHeight();
    }

    public String getTypefacePath() {
        return typefacePath;
    }

    public void setTypefacePath(String typefacePath) {
        this.typefacePath = typefacePath;
    }

    public String getTypefaceUrl() {
        return typefaceUrl;
    }

    public void setTypefaceUrl(String typefaceUrl) {
        this.typefaceUrl = typefaceUrl;
    }

    public String getTypefaceId() {
        return typefaceId;
    }

    public void setTypefaceId(String typefaceId) {
        this.typefaceId = typefaceId;
    }

    public int getStickerViewMargin() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mStickerView.getLayoutParams();
        return params.leftMargin;
    }

    public interface OnBubbleClickLisener {
        void onBubbleClick(Sticker sticker);

        void onBubbleCancel();
    }

    public interface OnStickerClickLisener {
        void onStickerClick();

        void onStickerDelete();

        void onTouch();

        void onClick();
    }
}