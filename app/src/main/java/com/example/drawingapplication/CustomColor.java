package com.example.drawingapplication;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class CustomColor {
    Paint paint;

    public CustomColor() {
        setUpPaint();
    }

    public void changeColor() {
        setUpPaint();
    }

    private void setUpPaint() {
        paint = new Paint();
        Random random = new Random();
        paint.setColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
    }
}
