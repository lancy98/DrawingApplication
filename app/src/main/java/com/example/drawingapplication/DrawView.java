package com.example.drawingapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View implements View.OnTouchListener {

    float x;
    float y;

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

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(x, y, 40, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();
        invalidate();
        return true;
    }
}
