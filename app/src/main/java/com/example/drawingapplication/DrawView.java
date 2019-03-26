package com.example.drawingapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class DrawView extends View {

    private DrawPoints points = new DrawPoints();
    public int strokeWidth = 40;
    public boolean colorCycle = false;
    private CustomColor customColor = new CustomColor();

    private boolean longPress = false;
    private float initialXPoint = 0f;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        points.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // get pointer index from the event object
                int pointerIndex = event.getActionIndex();
                // get pointer ID
                int pointerId = event.getPointerId(pointerIndex);

                PointF point = new PointF();

                point.x = event.getX(pointerIndex);
                point.y = event.getY(pointerIndex);

                if (colorCycle) {
                    points.addPoint(point, strokeWidth);
                } else {
                    points.addPoint(point, strokeWidth, customColor);
                }

                if (maskedAction == MotionEvent.ACTION_DOWN) {
                    longPress = false;
                    initialXPoint = event.getX();
                }

                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    int pointerID = event.getPointerId(i);

                    PointF point = new PointF();

                    point.x = event.getX(pointerID);
                    point.y = event.getY(pointerID);

                    if (colorCycle) {
                        points.addPoint(point, strokeWidth);
                    } else {
                        points.addPoint(point, strokeWidth, customColor);
                    }
                }

                if (event.getEventTime() - event.getDownTime() > 500 &&
                        Math.abs(event.getX() - initialXPoint) < 5) {
                    longPress = true;
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {

                if (!longPress &&
                        event.getEventTime() - event.getDownTime() > 500 &&
                        Math.abs(event.getX() - initialXPoint) < 5) {
                    longPress = true;
                }

                if (longPress) {
                    customColor = new CustomColor();
                    longPress = false;
                }
                break;
            }
        }

        invalidate();

        return true;
    }


    public void brushSize(int progress) {
        int baseValue = 20;
        // 20 plus max 50; brush size between 20 and 70.
        float additionalValue = 50f * (progress / 100f);
        strokeWidth = baseValue + (int)additionalValue ;
        invalidate();
    }

    public CAPACITY undo() {
        CAPACITY capacity = points.undo();

        if (capacity == CAPACITY.SPACE_LEFT) {
            invalidate();
        }

        return capacity;
    }

    public boolean redo() {
        boolean success = points.redo();

        if (success) {
            invalidate();
        }

        return success;
    }
}
