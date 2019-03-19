package com.example.drawingapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Random;

public class DrawPoint {
    public PointF point;
    public int radius;

    public Paint paint;

    public DrawPoint(PointF point, int radius) {
        this.point = point;
        this.radius = radius;
        RandomColor randomColor = new RandomColor();
        paint = randomColor.paint;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(point.x, point.y, radius, paint);
    }
}

class RandomColor {
    Paint paint;
    RandomColor() {
        paint = new Paint();
        Random random = new Random();
        paint.setColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
    }
}
