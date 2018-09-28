package com.ailide.apartmentsabc.model;

import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;

import com.ailide.apartmentsabc.utils.FileUtil;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerUtils;

import java.io.File;
import java.io.Serializable;

public class DraftSticker implements Serializable {

    private String path;
    private float[] martixValues;

    public DraftSticker(StickerDraft draft, Sticker sticker) {
        martixValues = new float[9];
        sticker.getMatrix().getValues(martixValues);
        if (TextUtils.isEmpty(sticker.getPath())) {
            File file = FileUtil.getStickerDraftStickerFile(draft.getTime(), System.currentTimeMillis());
            if (sticker.getDrawable() instanceof BitmapDrawable) {
                StickerUtils.saveImageToGallery(file, ((BitmapDrawable) sticker.getDrawable()).getBitmap());
                path = file.getAbsolutePath();
            }
        } else {
            path = sticker.getPath();
        }
    }

    public DraftSticker(LabelDraft draft, Sticker sticker) {
        martixValues = new float[9];
        sticker.getMatrix().getValues(martixValues);
        if (TextUtils.isEmpty(sticker.getPath())) {
            File file = FileUtil.getStickerDraftStickerFile(draft.getTime(), System.currentTimeMillis());
            if (sticker.getDrawable() instanceof BitmapDrawable) {
                StickerUtils.saveImageToGallery(file, ((BitmapDrawable) sticker.getDrawable()).getBitmap());
                path = file.getAbsolutePath();
            }
        } else {
            path = sticker.getPath();
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float[] getMartixValues() {
        return martixValues;
    }

    public void setMartixValues(float[] martixValues) {
        this.martixValues = martixValues;
    }
}
