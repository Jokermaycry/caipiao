package com.xiaopo.flying.graffiti;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GraffitiView extends View {
    private Paint paint;
    private Path path;
    private float downX, downY;
    private float tempX, tempY;
    private int paintWidth = 10;
    private int paintColor = Color.BLACK;
    private List<DrawPath> drawPathList;
    private List<DrawPath> savePathList;
    private boolean graffitiable = false;

    public GraffitiView(Context context) {
        this(context, null);
    }

    public GraffitiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraffitiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawPathList = new ArrayList<>();
        savePathList = new ArrayList<>();
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(paintWidth);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void initPaint(float strokeWidth, int color) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawPathList != null && !drawPathList.isEmpty()) {
            for (DrawPath drawPath : drawPathList) {
                if (drawPath.path != null) {
                    canvas.drawPath(drawPath.path, drawPath.paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!graffitiable) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                path = new Path();//每次手指下去都是一条新的路径
                path.moveTo(downX, downY);
                DrawPath drawPath = new DrawPath();
                drawPath.paint = paint;
                drawPath.path = path;
                drawPathList.add(drawPath);
                drawPath.points.add(new PathPoint(downX, downY));
                invalidate();
                tempX = downX;
                tempY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                path.quadTo(tempX, tempY, moveX, moveY);
                drawPathList.get(drawPathList.size() - 1).points.add(new PathPoint(moveX, moveY));
                invalidate();
                tempX = moveX;
                tempY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                initPaint(paint.getStrokeWidth(), paint.getColor());//每次手指抬起都要重置下画笔,不然画笔会保存了之前的设置什么画笔的属性会引起bug
                break;
        }
        return true;
    }

    /**
     * 撤销功能
     */
    public void undo() {
        if (drawPathList != null && drawPathList.size() >= 1) {
            savePathList.add(drawPathList.get(drawPathList.size() - 1));
            drawPathList.remove(drawPathList.size() - 1);
            invalidate();
        }
    }

    /**
     * 反撤销功能
     */
    public void reundo() {
        if (savePathList != null && !savePathList.isEmpty()) {
            drawPathList.add(savePathList.get(savePathList.size() - 1));
            savePathList.remove(savePathList.size() - 1);
            invalidate();
        }
    }

    /**
     * 改变画笔颜色
     *
     * @param color
     */
    public void resetPaintColor(int color) {
        paintColor = color;
        paint.setColor(paintColor);
    }

    /**
     * 放大就是改变画笔的宽度
     */
    public void resetPaintWidth() {
        paintWidth += 2;
        paint.setStrokeWidth(paintWidth);
    }

    public void setPaintWidth(int width) {
        paintWidth = width;
        paint.setStrokeWidth(paintWidth);
        paint.setColor(paintColor);
    }

    public void setPaintColor(int color) {
        paintColor = getResources().getColor(color);
        paint.setStrokeWidth(paintWidth);
        paint.setColor(paintColor);
    }

    /**
     * 橡皮擦功能 把画笔的颜色和view的背景颜色一样就ok,然后把画笔的宽度变大了,擦除的时候显得擦除范围大点
     */
    public void eraser() {
        paint.setColor(Color.WHITE);//这是view背景的颜色
        paint.setStrokeWidth(paintWidth + 6);
    }

    public void eraser(int width) {
        paint.setColor(Color.WHITE);//这是view背景的颜色
        paint.setStrokeWidth(width);
    }

    public void noEraser() {
        paint.setColor(paintColor);
        paint.setStrokeWidth(paintWidth);
    }

    public boolean isGraffitiable() {
        return graffitiable;
    }

    public void setGraffitiable(boolean graffitiable) {
        this.graffitiable = graffitiable;
    }

    public List<DrawPath> getDrawPathList() {
        return drawPathList;
    }

    public void setDrawPathList(List<DrawPath> drawPathList) {
        this.drawPathList = drawPathList;
    }

    public void drawPath(List<PathDraft> pathDrafts) {
        for (PathDraft draft :
                pathDrafts) {
            DrawPath drawPath = new DrawPath();
            drawPathList.add(drawPath);
            savePathList.add(drawPath);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(draft.getPaintWidth());
            paint.setColor(draft.getPaintColor());
            paint.setStyle(Paint.Style.STROKE);
            Path path = new Path();
            for (int i = 0; i < draft.getPoints().size(); i++) {
                PathPoint point = draft.getPoints().get(i);
                if (i == 0) {
                    path.moveTo(point.x, point.y);
                } else {
                    PathPoint tempPoint = draft.getPoints().get(i - 1);
                    path.quadTo(tempPoint.x, tempPoint.y, point.x, point.y);
                }
            }
            drawPath.path = path;
            drawPath.paint = paint;
            drawPath.points = draft.getPoints();
        }
        invalidate();
    }
}
