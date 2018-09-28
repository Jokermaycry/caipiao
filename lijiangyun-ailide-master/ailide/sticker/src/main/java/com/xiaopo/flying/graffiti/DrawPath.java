package com.xiaopo.flying.graffiti;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装一条路径
 * Created by Adminis on 2017/5/1.
 */
public class DrawPath {
    public Path path;
    public Paint paint;
    public List<PathPoint> points;

    public DrawPath() {
        points = new ArrayList<>();
    }
}
