package com.ailide.apartmentsabc.model;

import com.xiaopo.flying.graffiti.PathDraft;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StickerDraft implements Serializable{

    private static final long serialVersionUID = 1L;
    private long time;
    private String editContent;
    private int textSize;
    private boolean textBold;
    private float textItalic;
    private boolean textUnderline;
    private int textGravity;
    private String textTypefacePath;
    private String textTypefaceUrl;
    private String textTypefaceId;
    private List<DraftSticker> draftStickers;
    private List<PathDraft> pathDrafts;
    private boolean select;
    private Theme theme;

    public StickerDraft() {
        time = System.currentTimeMillis();
        draftStickers = new ArrayList<>();
        pathDrafts = new ArrayList<>();
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEditContent() {
        return editContent;
    }

    public void setEditContent(String editContent) {
        this.editContent = editContent;
    }

    public List<PathDraft> getPathDrafts() {
        return pathDrafts;
    }

    public void setPathDrafts(List<PathDraft> pathDrafts) {
        this.pathDrafts = pathDrafts;
    }

    public List<DraftSticker> getDraftStickers() {
        return draftStickers;
    }

    public void setDraftStickers(List<DraftSticker> draftStickers) {
        this.draftStickers = draftStickers;
    }
}
