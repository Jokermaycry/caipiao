package com.xiaopo.flying.graffiti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PathDraft implements Serializable {

    private List<PathPoint> points;
    private int paintColor;
    private float paintWidth;

    public PathDraft(DrawPath path) {
        points = new ArrayList<>();
        points.addAll(path.points);
        paintColor = path.paint.getColor();
        paintWidth = path.paint.getStrokeWidth();
    }

    public List<PathPoint> getPoints() {
        return points;
    }

    public void setPoints(List<PathPoint> points) {
        this.points = points;
    }

    public int getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

    public float getPaintWidth() {
        return paintWidth;
    }

    public void setPaintWidth(float paintWidth) {
        this.paintWidth = paintWidth;
    }
}
