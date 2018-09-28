package com.ailide.apartmentsabc.model;

import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;

import com.ailide.apartmentsabc.utils.FileUtil;
import com.ailide.apartmentsabc.views.MyApplication;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerUtils;
import com.xiaopo.flying.sticker.TextSticker;

import java.io.File;
import java.io.Serializable;

public class TextDraftSticker extends DraftSticker implements Serializable {

    private String editContent;
    private int textSize;
    private boolean textBold;
    private float textItalic;
    private boolean textUnderline;
    private int textGravity;
    private String textTypefacePath;
    private String textTypefaceUrl;
    private String textTypefaceId;
    private String bubbleId;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    public TextDraftSticker(StickerDraft draft, Sticker sticker) {
        super(draft, sticker);
        TextSticker textSticker = (TextSticker) sticker;
        setEditContent(textSticker.getText());
        setTextSize(textSticker.getTextSize());
        setTextBold(textSticker.isFontBold());
        setTextItalic(textSticker.getFontItalic());
        setTextUnderline(textSticker.isFontUnderline());
        setTextGravity(textSticker.getTextAlign());
        setTextTypefacePath(textSticker.getTextTypefacePath());
        setTextTypefaceUrl(textSticker.getTextTypefaceUrl());
        setTextTypefaceId(textSticker.getTextTypefaceId());
        setBubbleId(textSticker.getBubbleId());
        setPaddingLeft(textSticker.getPaddingLeft());
        setPaddingTop(textSticker.getPaddingTop());
        setPaddingRight(textSticker.getPaddingRight());
        setPaddingBottom(textSticker.getPaddingBottom());
    }

    public TextDraftSticker(LabelDraft draft, Sticker sticker) {
        super(draft, sticker);
        TextSticker textSticker = (TextSticker) sticker;
        setEditContent(textSticker.getText());
        setTextSize(textSticker.getTextSize());
        setTextBold(textSticker.isFontBold());
        setTextItalic(textSticker.getFontItalic());
        setTextUnderline(textSticker.isFontUnderline());
        setTextGravity(textSticker.getTextAlign());
        setTextTypefacePath(textSticker.getTextTypefacePath());
        setTextTypefaceUrl(textSticker.getTextTypefaceUrl());
        setTextTypefaceId(textSticker.getTextTypefaceId());
        setBubbleId(textSticker.getBubbleId());
        setPaddingLeft(textSticker.getPaddingLeft());
        setPaddingTop(textSticker.getPaddingTop());
        setPaddingRight(textSticker.getPaddingRight());
        setPaddingBottom(textSticker.getPaddingBottom());
    }

    public String getEditContent() {
        return editContent;
    }

    public void setEditContent(String editContent) {
        this.editContent = editContent;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public boolean isTextBold() {
        return textBold;
    }

    public void setTextBold(boolean textBold) {
        this.textBold = textBold;
    }

    public float getTextItalic() {
        return textItalic;
    }

    public void setTextItalic(float textItalic) {
        this.textItalic = textItalic;
    }

    public boolean isTextUnderline() {
        return textUnderline;
    }

    public void setTextUnderline(boolean textUnderline) {
        this.textUnderline = textUnderline;
    }

    public int getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
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

    public String getBubbleId() {
        return bubbleId;
    }

    public void setBubbleId(String bubbleId) {
        this.bubbleId = bubbleId;
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
}
