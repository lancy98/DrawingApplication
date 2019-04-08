package com.example.drawingapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


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
    private TOUCH_STATE currentTouchState;
    private int firstFingerPointerIndex;
    private Timer timer;

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

        event.getDownTime();

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

                if (maskedAction == MotionEvent.ACTION_DOWN) {
                    firstFingerPointerIndex = pointerIndex;
                    currentTouchState = TOUCH_STATE.DOWN;

                    // Start a timer.
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {

                            if (movedLongDistance == false
                                    && colorCycle == false
                                    && currentTouchState != TOUCH_STATE.NONE) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        customColor = new CustomColor();
                                        temporaryPoints.useColorForAllPoints(customColor);
                                        invalidate();
                                    }
                                });
                            }

                            timer.cancel();
                            timer = null;
                        };
                    };

                    timer = new Timer();
                    timer.schedule(task, minLongPressDurationInMS);
                }

                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                //FIXME: "Exception was thrown: pointerIndex out of range" is thrown when event.getX and event.getY is called.
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int pointerID = event.getPointerId(i);

                    PointF point = new PointF();

                    try {
                        point.x = event.getX(pointerID);
                        point.y = event.getY(pointerID);

                        if (colorCycle) {
                            temporaryPoints.addPoint(point, strokeWidth);
                        } else {
                            temporaryPoints.addPoint(point, strokeWidth, customColor);
                        }

                        // Calculate the distance moved.
                        if (temporaryPoints.strokeMaxDistance() > maxDistanceMovementForLongPress) {
                            movedLongDistance = true;
                        }

                        // changing the current state to moved.
                        if (firstFingerPointerIndex == pointerID) {
                            currentTouchState = TOUCH_STATE.MOVE;
                        }
                    } catch (Exception e) {
                        Log.d("DrawingApplication", "Exception was thrown: " + e.getLocalizedMessage());
                    }
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {

                currentTouchState = TOUCH_STATE.NONE;
                movedLongDistance = false;

                points.addPoints(temporaryPoints);
                temporaryPoints.clearStoredPoints();

                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

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
