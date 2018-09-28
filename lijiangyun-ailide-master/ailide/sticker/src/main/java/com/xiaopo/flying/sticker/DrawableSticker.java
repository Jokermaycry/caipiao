package com.xiaopo.flying.sticker;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * @author wupanjie
 */
public class DrawableSticker extends Sticker {

    private Drawable drawable;
    private Rect realBounds;

    public DrawableSticker(Drawable drawable) {
        this.drawable = drawable;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
    }

    public DrawableSticker(Drawable drawable, float width) {
        this.drawable = drawable;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
        float ratio = width / getWidth();
        Matrix matrix = getMatrix();
        matrix.setScale(ratio, ratio);
//        matrix.postTranslate(-(getWidth()*ratio/2),0);
        setMatrix(matrix);
    }

    public DrawableSticker(String path) {
        this(Drawable.createFromPath(path));
        setPath(path);
    }

    public DrawableSticker(String path, float width) {
        this(path);
        float ratio = width / getWidth();
        Matrix matrix = getMatrix();
        matrix.setScale(ratio, ratio);
//        matrix.postTranslate(-(getWidth()*ratio/2),0);
        setMatrix(matrix);
    }

    public DrawableSticker(int height, Drawable drawable) {
        this(drawable);
        float ratio = height / 2.0f / getHeight();
        Matrix matrix = getMatrix();
        matrix.setScale(ratio, ratio);
        setMatrix(matrix);
    }

    public DrawableSticker(int height, String path) {
        this(path);
        float ratio = height / 2.0f / getHeight();
        Matrix matrix = getMatrix();
        matrix.setScale(ratio, ratio);
        setMatrix(matrix);
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public DrawableSticker setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());
        drawable.setBounds(realBounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    @NonNull
    @Override
    public DrawableSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        drawable.setAlpha(alpha);
        return this;
    }

    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    @Override
    public void release() {
        super.release();
        if (drawable != null) {
            drawable = null;
        }
    }
}
