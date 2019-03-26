package com.example.drawingapplication;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class DrawPoint {
    public PointF point;
    public int radius;

    public Paint paint;

    public DrawPoint(PointF point, int radius) {
        this.point = point;
        this.radius = radius;
        CustomColor randomColor = new CustomColor();
        paint = randomColor.paint;
    }

    public DrawPoint(PointF point, int radius, Paint paint) {
        this.point = point;
        this.radius = radius;
        this.paint = paint;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(point.x, point.y, radius, paint);
    }
}
