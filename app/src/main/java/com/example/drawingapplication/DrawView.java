package com.example.drawingapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class DrawView extends View {

    private DrawPoints points = new DrawPoints();
    private DrawPoints temporaryPoints = new DrawPoints();
    public int strokeWidth = 20;
    public int baseStrokeWidth = 20;
    public float maxVariableBrushSize = 50f;
    private int maxDistanceMovementForLongPress = 18;
    private int minLongPressDurationInMS = 500;
    public boolean colorCycle = false;
    private boolean movedLongDistance = false;
    private CustomColor customColor = new CustomColor();

    private boolean longPress = false;

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
        temporaryPoints.draw(canvas);
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

                PointF point = new PointF();

                point.x = event.getX(pointerIndex);
                point.y = event.getY(pointerIndex);

                if (colorCycle) {
                    temporaryPoints.addPoint(point, strokeWidth);
                } else {
                    temporaryPoints.addPoint(point, strokeWidth, customColor);
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
                        temporaryPoints.addPoint(point, strokeWidth);
                    } else {
                        temporaryPoints.addPoint(point, strokeWidth, customColor);
                    }

                    if (temporaryPoints.strokeMaxDistance() > maxDistanceMovementForLongPress) {
                        longPress = false;
                        movedLongDistance = true;
                    }
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {

                if (colorCycle == false
                        && longPress == false
                        && movedLongDistance == false
                        && maskedAction == MotionEvent.ACTION_UP
                        && event.getEventTime() - event.getDownTime() > minLongPressDurationInMS
                        && temporaryPoints.strokeMaxDistance() < maxDistanceMovementForLongPress) {
                    longPress = true;
                }

                if (longPress) {
                    customColor = new CustomColor();
                    temporaryPoints.useColorForAllPoints(customColor);
                    longPress = false;
                }

                movedLongDistance = false;
                points.addPoints(temporaryPoints);
                temporaryPoints.clearStoredPoints();
                break;
            }
        }

        invalidate();

        return true;
    }


    public void brushSize(int progress) {
        float additionalValue = maxVariableBrushSize * (progress / 100f);
        strokeWidth = baseStrokeWidth + (int)additionalValue ;
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

    public void clearView() {
        temporaryPoints.clearStoredPoints();
        points.clearStoredPoints();
        invalidate();
    }
}
