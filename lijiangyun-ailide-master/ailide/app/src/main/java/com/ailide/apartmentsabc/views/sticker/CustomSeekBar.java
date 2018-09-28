package com.ailide.apartmentsabc.views.sticker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.ailide.apartmentsabc.R;

public class CustomSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    String temp_str = "0";
    private Resources res;
    private Paint paint;
    private Drawable mThumb;

    public CustomSeekBar(Context context) {
        this(context, null);
    }

    @SuppressWarnings("deprecation")
    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(getResources().getColor(R.color.text_gray));
        res = context.getResources();
        paint.setTextSize(30);
    }

    @Override
    public void setThumb(Drawable thumb) {
        // TODO Auto-generated method stub
        super.setThumb(thumb);
        this.mThumb = thumb;
    }

    public Drawable getSeekBarThumb() {
        return mThumb;
    }

    //设置thumb的偏移数值
    @Override
    public void setThumbOffset(int thumbOffset) {
        // TODO Auto-generated method stub
        super.setThumbOffset(thumbOffset / 3);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.save();
        Rect rect = getSeekBarThumb().getBounds();
        float fontwidth = paint.measureText(temp_str);
        canvas.drawText(temp_str, rect.left - paint.ascent() + rect.width() - (paint.descent() - paint.ascent()) / 2.0F-4, rect.top - paint.ascent() + (rect.height() - (paint.descent() - paint.ascent())) / 2.0F, paint);
//        canvas.drawText(temp_str, (rect.left + rect.right) / 2 + fontwidth+(paint.descent() - paint.ascent())/2, rect.top - paint.ascent() + (rect.height() - (paint.descent() - paint.ascent())) / 2.0F, paint);

        canvas.restore();
    }

    public void setValue(String value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        temp_str = sb.toString();
        invalidate();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setOnSeekBarChangeListener(final OnSeekBarChangeListener l) {
        super.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (l != null) {
                    l.onProgressChanged(seekBar, progress, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (l != null) {
                    l.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (l != null) {
                    l.onStopTrackingTouch(seekBar);
                }
            }
        });
    }
}
