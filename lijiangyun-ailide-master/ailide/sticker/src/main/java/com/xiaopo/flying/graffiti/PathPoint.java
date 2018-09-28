package com.xiaopo.flying.graffiti;

import java.io.Serializable;

public class PathPoint implements Serializable{

    float x;
    float y;

    public PathPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
