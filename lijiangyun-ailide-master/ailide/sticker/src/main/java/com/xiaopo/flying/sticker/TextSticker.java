package com.xiaopo.flying.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * Customize your sticker with text and image background.
 * You can place some text into a given region, however,
 * you can also add a plain text sticker. To support text
 * auto resizing , I take most of the code from AutoResizeTextView.
 * See https://adilatwork.blogspot.com/2014/08/android-textview-which-resizes-its-text.html
 * Notice: It's not efficient to add long text due to too much of
 * StaticLayout object allocation.
 * Created by liutao on 30/11/2016.
 */

public class TextSticker extends Sticker {

    /**
     * Our ellipsis string.
     */
    private static final String mEllipsis = "\u2026";

    private final Context context;
    private final Rect realBounds;
    private final Rect textRect;
    private final TextPaint textPaint;
    public int drawableLeft;
    public int drawableRight;
    public int drawableTop;
    public int drawableBottom;
    private Drawable drawable;
    private StaticLayout staticLayout;
    private Layout.Alignment alignment;
    private String text = "";
    private String drawText;
    private int viewWidth;
    private int viewHeight;
    private String textTypefacePath;
    private String textTypefaceUrl;
    private String textTypefaceId;
    private String bubbleId;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private float ratio;
    private boolean firstText = true;
    private float nowscale;

    private int textWidth;
    private int textHeight;
    private int scaleTextWidth;
    /**
     * Upper bounds for text size.
     * This acts as a starting point for resizing.
     */
    private float maxTextSizePixels;

    /**
     * Lower bounds for text size.
     */
    private float minTextSizePixels;

    /**
     * Line spacing multiplier.
     */
    private float lineSpacingMultiplier = 1.0f;

    /**
     * Additional line spacing.
     */
    private float lineSpacingExtra = 0.0f;

    public TextSticker(@NonNull Context context) {
        this(context, null);
    }

    public TextSticker(@NonNull Context context, @Nullable Drawable drawable) {
        this.context = context;
        this.drawable = drawable;
        realBounds = new Rect(0, 0, 0, 0);
        textRect = new Rect(0, 0, 0, 0);

        if (drawable != null) {
//            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_background);
            realBounds.set(0, 0, getWidth(), getHeight());
            textRect.set(0, 0, getWidth(), getHeight());
        } else {
            textRect.set(0, 0, 0, 0);
        }
        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        minTextSizePixels = 6;
        maxTextSizePixels = 32;
        alignment = Layout.Alignment.ALIGN_CENTER;
        textPaint.setTextSize(maxTextSizePixels);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Matrix matrix = getMatrix();
        if (drawable != null) {
            canvas.save();
            canvas.concat(matrix);
            drawable.setBounds(realBounds);
            drawable.draw(canvas);
        }
        canvas.restore();
        canvas.save();
        canvas.concat(matrix);
        if (drawable != null) {
            if (staticLayout != null) {
                if (textRect.width() == getWidth()) {
                    int dy = getHeight() / 2 - staticLayout.getHeight() / 2;
                    // center vertical
                    canvas.translate(0, dy);
                } else {
                    int dx = textRect.left;
                    int dy = textRect.top + textRect.height() / 2 - staticLayout.getHeight() / 2;
                    canvas.translate(dx, dy);
                }
                staticLayout.draw(canvas);
            }
        } else {
            if (staticLayout != null) {
                if (textRect.width() == getWidth()) {
                    int dy = getHeight() / 2 - staticLayout.getHeight() / 2;
                    // center vertical
                    canvas.translate(0, dy);
                } else {
                    int dx = textRect.left;
                    int dy = textRect.top + textRect.height() / 2 - staticLayout.getHeight() / 2;
                    canvas.translate(dx, dy);
                }
                staticLayout.draw(canvas);
            }
        }
        canvas.restore();
    }

    @Override
    public int getWidth() {
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return scaleTextWidth;
    }

    @Override
    public int getHeight() {
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        StaticLayout staticLayout1 =
                new StaticLayout("啊", textPaint, scaleTextWidth, alignment, lineSpacingMultiplier,
                        lineSpacingExtra, true);
        staticLayout =
                new StaticLayout(this.drawText, textPaint, scaleTextWidth, alignment, lineSpacingMultiplier,
                        lineSpacingExtra, true);
        Rect rect = new Rect();
        textPaint.getTextBounds("阿萨德", 0, "阿萨德".length(), rect);
        textHeight = ((staticLayout1.getHeight()) * staticLayout.getLineCount());
        return textHeight;
    }

    @Override
    public void release() {
        super.release();
        if (drawable != null) {
            drawable = null;
        }
    }

    @NonNull
    @Override
    public TextSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        textPaint.setAlpha(alpha);
        return this;
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public TextSticker setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        realBounds.set(0, 0, getWidth(), getHeight());
        textRect.set(0, 0, getWidth(), getHeight());
        return this;
    }

    public TextSticker setBubblePath(String path) {
        setPath(path);
        return this;
    }

    public TextSticker setBitmap(Bitmap bitmap) {
        return setDrawable(new BitmapDrawable(bitmap));
    }

    public TextSticker setScreenWidth(int width) {
        if (drawable != null) {
            ratio = width * 1f / getWidth();
            Matrix matrix = getMatrix();
            matrix.setScale(ratio, ratio);
            setMatrix(matrix);
        }
        return this;
    }

    public TextSticker setScreenHeight(int height) {
        if (drawable != null) {
            ratio = height / 2.0f / getHeight();
            Matrix matrix = getMatrix();
            matrix.setScale(ratio, ratio);
            setMatrix(matrix);
        }
        return this;
    }

    public TextSticker setSize(int width, int height) {
        this.viewWidth = width;
        this.viewHeight = height;
        return this;
    }

    public TextSticker setPadding(int left, int top, int right, int bottom) {
        float widthRatio = viewWidth * 1f / getWidth();
        float heightRatio = viewHeight * 1f / getHeight();
        drawableLeft = (int) (left / widthRatio);
        drawableTop = (int) (top / heightRatio);
        drawableRight = textRect.width() - (int) (right / widthRatio);
        drawableBottom = textRect.height() - (int) (bottom / heightRatio);
        textRect.set(drawableLeft, drawableTop, drawableRight, drawableBottom);
        setPaddingLeft(left);
        setPaddingTop(top);
        setPaddingRight(right);
        setPaddingBottom(bottom);
        return this;
    }

    public TextSticker init() {
        this.firstText = true;
        return this;
    }

    public boolean isFirstText() {
        return firstText;
    }

    public void setFirstText(boolean firstText) {
        this.firstText = firstText;
    }

    @NonNull
    public TextSticker setDrawable(@NonNull Drawable drawable, @Nullable Rect region) {
        this.drawable = drawable;
        realBounds.set(0, 0, getWidth(), getHeight());
        if (region == null) {
            textRect.set(0, 0, getWidth(), getHeight());
        } else {
            textRect.set(region.left, region.top, region.right, region.bottom);
        }
        return this;
    }

    @NonNull
    public TextSticker setTypeface(@Nullable Typeface typeface) {
        textPaint.setTypeface(typeface);
        return this;
    }

    @NonNull
    public TextSticker setTextColor(@ColorInt int color) {
        textPaint.setColor(color);
        return this;
    }

    public boolean isFontBold() {
        return textPaint.isFakeBoldText();
    }

    public TextSticker setFontBold(boolean bold) {
        textPaint.setFakeBoldText(bold);
        return this;
    }

    public float getFontItalic() {
        return textPaint.getTextSkewX();
    }

    public TextSticker setFontItalic(boolean italic) {
        textPaint.setTextSkewX(italic ? -0.5f : 0f);
        return this;
    }

    public boolean isFontUnderline() {
        return textPaint.isUnderlineText();
    }

    public TextSticker setFontUnderline(boolean underline) {
        textPaint.setUnderlineText(underline);
        return this;
    }

    @NonNull
    public TextSticker setTextAlign(@NonNull Layout.Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public int getTextAlign() {
        if (Layout.Alignment.ALIGN_OPPOSITE == alignment) {
            return 2;
        } else if (Layout.Alignment.ALIGN_CENTER == alignment) {
            return 1;
        } else {
            return 0;
        }
    }

    public TextSticker setTextAlign(@NonNull int type) {
        switch (type) {
            case 0:
                this.alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case 1:
                this.alignment = Layout.Alignment.ALIGN_CENTER;
                break;
            case 2:
                this.alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
        }
        return this;
    }

    @NonNull
    public TextSticker setMaxTextSize(float size) {
        if (ratio != 0) {
            size = size / ratio;
        }
        textPaint.setTextSize(size);
//        textPaint.setTextSize(convertSpToPx(size));
        maxTextSizePixels = textPaint.getTextSize();
        return this;
    }

    /**
     * Sets the lower text size limit
     *
     * @param minTextSizeScaledPixels the minimum size to use for text in this view,
     *                                in scaled pixels.
     */
    @NonNull
    public TextSticker setMinTextSize(float minTextSizeScaledPixels) {
        minTextSizePixels = convertSpToPx(minTextSizeScaledPixels);
        return this;
    }

    @NonNull
    public TextSticker setLineSpacing(float add, float multiplier) {
        lineSpacingMultiplier = multiplier;
        lineSpacingExtra = add;
        return this;
    }

    @Nullable
    public String getText() {
        return text;
    }

    @NonNull
    public TextSticker setText(@Nullable String text) {
        this.firstText = false;
        this.text = text;
        this.drawText = text;
        if(textWidth == 0){
            float width = 0f;
            for (String str :
                    drawText.split("\n")) {
                float strWidth = textPaint.measureText(str, 0, str.length()) + 100;
                if (width < strWidth) {
                    width = strWidth;
                }
            }
            textWidth = (int) width + 100;
            scaleTextWidth = textWidth;
            StaticLayout staticLayout1 =
                    new StaticLayout("啊", textPaint, scaleTextWidth, alignment, lineSpacingMultiplier,
                            lineSpacingExtra, true);
            staticLayout =
                    new StaticLayout(this.drawText, textPaint, scaleTextWidth, alignment, lineSpacingMultiplier,
                            lineSpacingExtra, true);
            Rect rect = new Rect();
            textPaint.getTextBounds("阿萨德", 0, "阿萨德".length(), rect);
            textHeight = ((staticLayout1.getHeight()) * staticLayout.getLineCount());
        }
        return this;
    }

    public TextSticker setDrawText(@Nullable String text) {
        this.drawText = text;
        return this;
    }

    /**
     * Resize this view's text size with respect to its width and height
     * (minus padding). You should always call this method after the initialization.
     */
    @NonNull
    public TextSticker resizeText() {
        if (drawable == null) {
            StaticLayout staticLayout1 =
                    new StaticLayout("啊", textPaint, scaleTextWidth, alignment, lineSpacingMultiplier,
                            lineSpacingExtra, true);
            staticLayout =
                    new StaticLayout(this.drawText, textPaint, scaleTextWidth, alignment, lineSpacingMultiplier,
                            lineSpacingExtra, true);
            Rect rect = new Rect();
            textPaint.getTextBounds("阿萨德", 0, "阿萨德".length(), rect);
            textHeight = ((staticLayout1.getHeight()) * staticLayout.getLineCount());
            textRect.set(0, 0, scaleTextWidth, textHeight);
        } else {
            textRect.set(drawableLeft, drawableTop, drawableRight, drawableBottom);
        }
        final int availableHeightPixels = textRect.height();
        final int availableWidthPixels = textRect.width();
        final CharSequence text = getText();
        drawText = text.toString();
        // Safety check
        // (Do not resize if the view does not have dimensions or if there is no text)
        if (text == null
                || text.length() <= 0
                || availableHeightPixels <= 0
                || availableWidthPixels <= 0
                || maxTextSizePixels <= 0) {
            return this;
        }
        float targetTextSizePixels = maxTextSizePixels;

        if (drawable != null) {
            int targetTextHeightPixels =
                    getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels);

            if (targetTextHeightPixels > availableHeightPixels) {
                // Make a copy of the original TextPaint object for measuring
                TextPaint textPaintCopy = new TextPaint(textPaint);
                textPaintCopy.setTextSize(targetTextSizePixels);

                // Measure using a StaticLayout instance
                StaticLayout staticLayout =
                        new StaticLayout(text, textPaintCopy, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                                lineSpacingMultiplier, lineSpacingExtra, false);
                // Check that we have a least one line of rendered text
                if (staticLayout.getLineCount() > 0) {
                    // Since the line at the specific vertical position would be cut off,
                    // we must trim up to the previous line and add an ellipsis
                    int lastLine = staticLayout.getLineForVertical(availableHeightPixels);

                    if (lastLine >= 0) {
                        int startOffset = staticLayout.getLineStart(lastLine);
                        int endOffset = staticLayout.getLineEnd(lastLine);
                        float lineWidthPixels = staticLayout.getLineWidth(lastLine);
                        float ellipseWidth = textPaintCopy.measureText(mEllipsis);

                        while (availableWidthPixels < lineWidthPixels + ellipseWidth) {
                            endOffset--;
                            lineWidthPixels =
                                    textPaintCopy.measureText(text.subSequence(startOffset, endOffset + 1).toString());
                        }
                        if (endOffset < 0) {
                            endOffset = 0;
                        }
                        setDrawText(text.subSequence(0, endOffset) + mEllipsis);
                    }
                }
            }
        }
        textPaint.setTextSize(targetTextSizePixels);
        staticLayout =
                new StaticLayout(this.drawText, textPaint, textRect.width(), alignment, lineSpacingMultiplier,
                        lineSpacingExtra, true);
        return this;
    }

    /**
     * @return lower text size limit, in pixels.
     */
    public float getMinTextSizePixels() {
        return minTextSizePixels;
    }

    /**
     * Sets the text size of a clone of the view's {@link TextPaint} object
     * and uses a {@link StaticLayout} instance to measure the height of the text.
     *
     * @return the height of the text when placed in a view
     * with the specified width
     * and when the text has the specified size.
     */
    protected int getTextHeightPixels(@NonNull CharSequence source, int availableWidthPixels,
                                      float textSizePixels) {
        textPaint.setTextSize(textSizePixels);
        // It's not efficient to create a StaticLayout instance
        // every time when measuring, we can use StaticLayout.Builder
        // since api 23.
        StaticLayout staticLayout =
                new StaticLayout(source, textPaint, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                        lineSpacingMultiplier, lineSpacingExtra, true);
        return staticLayout.getHeight();
    }

    /**
     * @return the number of pixels which scaledPixels corresponds to on the device.
     */
    private float convertSpToPx(float scaledPixels) {
        return scaledPixels * context.getResources().getDisplayMetrics().scaledDensity;
    }

    public int getTextSize() {
        return (int) textPaint.getTextSize();
//        return (int) (textPaint.getTextSize() / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public String getTextTypefacePath() {
        return textTypefacePath;
    }

    public void setTextTypefacePath(String textTypefacePath) {
        this.textTypefacePath = textTypefacePath;
    }

    public String getTextTypefaceUrl() {
        return textTypefaceUrl;
    }

    public void setTextTypefaceUrl(String textTypefaceUrl) {
        this.textTypefaceUrl = textTypefaceUrl;
    }

    public String getTextTypefaceId() {
        return textTypefaceId;
    }

    public void setTextTypefaceId(String textTypefaceId) {
        this.textTypefaceId = textTypefaceId;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public String getBubbleId() {
        return bubbleId;
    }

    public TextSticker setBubbleId(String bubbleId) {
        this.bubbleId = bubbleId;
        return this;
    }

    public float getScale() {
        return nowscale;
    }

    public void setScale(float scale) {
        float strWidth = textPaint.measureText("啊", 0, "啊".length())+10;
        if(strWidth<textWidth * scale){
            nowscale = scale;
            scaleTextWidth = (int) (textWidth * nowscale);
            resizeText();
        }
    }

    public void scaleFinish() {
        if (nowscale != 0) {
            nowscale = 0;
            textWidth = scaleTextWidth;
        }
    }
}
