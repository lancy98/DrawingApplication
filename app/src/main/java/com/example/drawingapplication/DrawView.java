package com.example.drawingapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawView extends View implements View.OnTouchListener {

    ArrayList<DrawPoint> points = new ArrayList<DrawPoint>();
    public int strokeWidth = 40;

    public DrawView(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(DrawPoint drawPoint: points) {
            drawPoint.draw(canvas);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        PointF point = new PointF(x, y);
        DrawPoint drawPoint = new DrawPoint(point, strokeWidth);
        points.add(drawPoint);
        invalidate();
        return true;
    }
}
